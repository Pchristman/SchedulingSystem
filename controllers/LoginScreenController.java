package sample.controllers;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.database.DBAppointments;
import sample.database.DBConnection;
import sample.database.DBUsers;
import sample.model.Appointments;
import sample.model.Users;

import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginScreenController implements Initializable {

    @FXML
    private Label pwLabel;

    @FXML
    private Label title;

    @FXML
    private Label userLabel;

    @FXML
    private TextField userText;

    @FXML
    private PasswordField pwField;

    @FXML
    private Button loginBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private Label locationLabel;

    static Users currentUser;

    ResourceBundle language = ResourceBundle.getBundle("locale");

    /**
     * This exits out the application
     * @param event
     */
    @FXML
    void exit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(language.getString("EXIT"));
        alert.setHeaderText(language.getString("EXITALERT"));
        alert.setContentText(language.getString("EXITCONTENT"));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage screen = (Stage) ((Node)event.getSource()).getScene().getWindow();
            DBConnection.closeConnection();
            screen.close();
        } else {
            alert.close();
        }
    }

    /**
     * This validates the text supplied in the two text fields and if the information provided matches a user in the database
     * it assigns that user to currentUser. It logs into login_acitivity.txt whether it is a successful or failed login.
     * It addition, it provides a pop up if the user has any appointments within 15 minutes of logging in.
     * @param event
     * @throws IOException
     */
    @FXML
    void login(ActionEvent event) throws IOException {
        String username = userText.getText();
        String password = pwField.getText();
        if (validateLogin(username, password)) {
            logActivity("User: " + currentUser.getUserName() + " Successful login.\n");
            String upcomingMessage = upcomingAppointments();
            if (upcomingMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(language.getString("UPCOMING"));
                alert.setHeaderText(language.getString("YOURUPCOMING"));
                alert.setContentText(upcomingMessage);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(language.getString("UPCOMING"));
                alert.setHeaderText(language.getString("YOURUPCOMING"));
                alert.setContentText(language.getString("NOUPCOMING"));
                alert.showAndWait();
            }
            Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            screen.close();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../views/MainScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        } else {
            logActivity("User: " + username + " Failed login attempt.\n");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(language.getString("ERROR"));
            alert.setHeaderText(language.getString("LOGINATTEMPT"));
            alert.setContentText(language.getString("LOGINCONTENT"));
            alert.showAndWait();
        }

    }

    /**
     * This functions writes to login_activity.txt when there is a successful or failed log in
     * @param activity
     * @throws FileNotFoundException
     */
    private void logActivity(String activity) throws FileNotFoundException {
        File file = new File("login_activity.txt");
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(localDateTime);
        printWriter.append(formattedDate + " " + activity);
        printWriter.close();
    }

    /**
     * This provides translations based on the computer's default language and changes the text accordingly. Currently supported
     * in English and French
     */
    private void translateLabels() {
        pwLabel.setText(language.getString("PASSWORD"));
        userLabel.setText(language.getString("USERNAME"));
        title.setText(language.getString("TITLE"));
        loginBtn.setText(language.getString("LOGIN"));
        exitBtn.setText(language.getString("EXIT"));
        locationLabel.setText(TimeZone.getTimeZone(ZoneId.systemDefault()).getDisplayName());

    }

    /**
     * This checks the username and password provided match in the database.
     * @param username
     * @param password
     * @return
     */
    private boolean validateLogin(String username, String password) {
        for (Users u : DBUsers.getAllUsers()) {
            if (username.equals(u.getUserName()) && password.equals(u.getPassword())) {
                currentUser = u;
                return true;
            }
        }
        return false;
    }

    /**
     * This goes through the upcoming Appointments assigned to a user and adds to a message the appointment id and
     * start time of the appointment if its within 15 minutes. This function contains two different Lambdas.
     * THIRD LAMBDA - The first lambda like the other lambdas used throughout the program creates a filtered list of appointments
     * if their start Time is in the next 15 minutes.
     * FOURTH LAMBDA - The second lambda goes through each appointment in the filtered list and appends the various values
     * a String Builder object. This allows us to used a dynamically built list with a custom error message tailored
     * to each individually case.
     * @return
     */
    private String upcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        StringBuilder upcomingMessage = new StringBuilder();
        FilteredList<Appointments> appointments = new FilteredList<>(DBAppointments.getAllAppointments());

        // THIRD LAMBDA
        appointments.setPredicate(row -> {
            LocalDateTime upcoming = row.getStartTime();
            return upcoming.isAfter(now.minusMinutes(1)) && upcoming.isBefore(now.plusMinutes(15));
        });

        // FOURTH LAMBDA
        if (!appointments.isEmpty()) {
            appointments.forEach(appointment -> {
                upcomingMessage.append(language.getString("APPOINTMENTID"));
                upcomingMessage.append(": ");
                upcomingMessage.append(appointment.getAppointmentId());
                upcomingMessage.append(" ");
                upcomingMessage.append(language.getString("AT"));
                upcomingMessage.append(" ");
                upcomingMessage.append(appointment.getStartTimeFormat());
                upcomingMessage.append("\n");

            });
        }

        return upcomingMessage.toString();
//        String upcomingMessage = "";
//        for (Appointments a : DBAppointments.getAllAppointments()) {
//            LocalDateTime appointStartTime = a.getStartTime();
//            if (a.getUserId() == currentUser.getUserID()) {
//                if (appointStartTime.isAfter(now.minusMinutes(1)) && appointStartTime.isBefore(now.plusMinutes(15))) {
//                    upcomingMessage = upcomingMessage + language.getString("APPOINTMENTID") + ": " + a.getAppointmentId() + " " +
//                            language.getString("AT") + " " + a.getStartTimeFormat() + "\n";
//                }
//            }
//        }
//        return upcomingMessage;
    }

    /**
     * Translates the labels upon initialization
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         translateLabels();
    }

    /**
     * Returns the currentUser for use on other screens.
     * @return
     */
    public static Users getCurrentUser() {
        return currentUser;
    }
}
