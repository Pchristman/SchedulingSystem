package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.DBConnection;
import sample.database.DBContacts;
import sample.database.DBCustomers;
import sample.database.DBUsers;
import sample.model.Contacts;
import sample.model.Customers;
import sample.model.TimeFuncs;
import sample.model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddAppointmentScreenController implements Initializable {

    @FXML
    private AnchorPane titleTextField;

    @FXML
    private Label title;

    @FXML
    private Label addAppointmentTitle;

    @FXML
    private Label appointmentTitleLabel;

    @FXML
    private TextField titleText;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextField descriptionText;

    @FXML
    private Label locationLabel;

    @FXML
    private TextField locationText;

    @FXML
    private Label typeLabel;

    @FXML
    private TextField typeText;

    @FXML
    private Label customerLabel;

    @FXML
    private ComboBox<String> customerCombo;

    @FXML
    private Label contactLabel;

    @FXML
    private ComboBox<String> contactCombo;

    @FXML
    private Label userLabel;

    @FXML
    private ComboBox<String> userCombo;

    @FXML
    private Label startDateTimeLabel;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<String> startHourCombo;

    @FXML
    private ComboBox<String> startMinuteCombo;

    @FXML
    private Label endDateTimeLabel;

    @FXML
    private ComboBox<String> endHourCombo;

    @FXML
    private ComboBox<String> endMinuteCombo;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    private ObservableList<String> contactNames = FXCollections.observableArrayList();
    private ObservableList<String> userNames = FXCollections.observableArrayList();
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    static ObservableList<String> startHours = FXCollections.observableArrayList();
    static ObservableList<String> endHours = FXCollections.observableArrayList();
    private ObservableList<String> minutes = FXCollections.observableArrayList();

    static Users currentUser = LoginScreenController.getCurrentUser();


    /**
     *
     * @param event
     * @throws IOException
     * This cancels out of the AddAppointment Screen and goes back to the base Appointment Screen.
     */
    @FXML
    void cancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Add Customer");
        alert.setHeaderText("Are you sure you wish to cancel?");
        alert.setContentText("Press OK to cancel. \nPress cancel to return to the Add Appointment screen");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            screen.close();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../views/AppointmentScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Appointment Screen");
            stage.setScene(scene);
            stage.show();
        } else {
            alert.close();
        }

    }

    /**
     *
     * @param event
     * @throws SQLException
     * This method pulls the info out of the text boxes/date picker/combo boxes and first checks that the information
     * supplied is valid. Then performs an insert to the appointments table within the mySql database. In addition
     * it checks that the appointment scheduled doesn't conflict with the customers prior appointments.
     */
    @FXML
    void save(ActionEvent event) throws SQLException {
        String title = titleText.getText();
        String description = descriptionText.getText();
        String location = locationText.getText();
        String type = typeText.getText();
        int customerID = getCustomerID(customerCombo.getValue());
        int contactID = getContactID(contactCombo.getValue());
        int userID = getUserID(userCombo.getValue());
        LocalDate date = startDatePicker.getValue();
        String startHour = startHourCombo.getValue();
        String startMinutes = startMinuteCombo.getValue();
        String endHour = endHourCombo.getValue();
        String endMinutes = endMinuteCombo.getValue();

        String errorMessage = "";
        errorMessage = isAppointmentValid(description, location, type, title, userID, customerID, contactID, date, startHour, endHour, startMinutes, endMinutes, errorMessage);
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error adding appointment");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        } else {
            LocalTime startHourMin = LocalTime.of(Integer.parseInt(startHour), Integer.parseInt(startMinutes));
            LocalTime endHourMin = LocalTime.of(Integer.parseInt(endHour), Integer.parseInt(endMinutes));
            LocalDateTime startTime = LocalDateTime.of(date, startHourMin);
            Timestamp startTimeT = Timestamp.valueOf(startTime);
            LocalDateTime endTime = LocalDateTime.of(date, endHourMin);
            Timestamp endTimeT = Timestamp.valueOf(endTime);
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());

            if (!TimeFuncs.checkConflict(startTimeT, endTimeT, customerID)) {
                try {
                    String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID, " +
                                "Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                    ps.setString(1, title);
                    ps.setString(2, description);
                    ps.setString(3, location);
                    ps.setString(4, type);
                    ps.setTimestamp(5, startTimeT);
                    ps.setTimestamp(6, endTimeT);
                    ps.setInt(7, customerID);
                    ps.setInt(8, userID);
                    ps.setInt(9, contactID);
                    ps.setTimestamp(10, now);
                    ps.setString(11, currentUser.getUserName());
                    ps.setTimestamp(12, now);
                    ps.setString(13, currentUser.getUserName());
                    ps.executeUpdate();

                    Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    screen.close();
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("../views/AppointmentScreen.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Appointment Screen");
                    stage.setScene(scene);
                    stage.show();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Error adding appointment");
                alert.setContentText("This customer already has an appointment at this time");
                alert.showAndWait();
            }

        }
    }

    /**
     *
     * @return
     * This converts the list of Contacts into a list of just the contact names for loading into the combo boxes
     */
    public ObservableList<String> convertContact() {
        for (Contacts c : DBContacts.getAllContacts()) {
            contactNames.add(c.getContactName());
        }
        return contactNames;
    }

    /**
     * This converts the list of Users into a list of just the usernames for loading into the combo boxes
     * @return
     */
    public ObservableList<String> convertUser() {
        for (Users u : DBUsers.getAllUsers()) {
            userNames.add(u.getUserName());
        }
        return userNames;
    }

    /**
     * This converts the list of Customers into a list of just the customer names for loading into the combo boxes
     * @return
     */
    public ObservableList<String> convertCustomer() {
        for (Customers c : DBCustomers.getAllCustomers()) {
            customerNames.add(c.getCustomerName());
        }
        return customerNames;
    }

    /**
     * This matches a contact name from the combo box to a contact ID for loading into the database
     * @param name
     * @return
     */
    public int getContactID(String name) {
        for (Contacts c : DBContacts.getAllContacts()) {
            if (c.getContactName().equals(name)) {
                return c.getContactID();
            }
        }
        return -1;
    }

    /**
     * This matches a username from the combo box to a user ID for loading into the database
     * @param name
     * @return
     */
    public int getUserID(String name) {
        for (Users u : DBUsers.getAllUsers()) {
            if (u.getUserName().equals(name)) {
                return u.getUserID();
            }
        }
        return -1;
    }

    /**
     * This matches a customer name from the combo box to a Customer ID for loading into the database
     * @param name
     * @return
     */
    public int getCustomerID(String name) {
        for (Customers c : DBCustomers.getAllCustomers()) {
            if (c.getCustomerName().equals(name)) {
                return c.getCustomerID();
            }
        }
        return -1;
    }

    /**
     * This initializes the times and sets the business hours equal to 8 AM - 10 PM EST but has it display as whatever the
     * equivalent times would be on the computer running the application. In addition it initializes the hours and minute
     * combo boxes
     */
    private void initializeTimes() {
        ZoneId estTimeZone = ZoneId.of("America/New_York");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime startTime = LocalDateTime.parse("2021-06-15 08:00", formatter);
        LocalDateTime endTime = LocalDateTime.parse("2021-06-15 22:00", formatter);

        ZonedDateTime estStart = ZonedDateTime.of(startTime, estTimeZone);
        ZonedDateTime estEnd = ZonedDateTime.of(endTime, estTimeZone);
        ZonedDateTime localStart = estStart.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime localEnd = estEnd.withZoneSameInstant(ZoneId.systemDefault());

        for (int i = localStart.getHour(); i <= localEnd.getHour(); i++) {
            String hour = i < 10 ? "0" + i : Integer.toString(i);
            startHours.add(hour);
            endHours.add(hour);
        }

        minutes.addAll("00", "15" ,"30", "45");

        startHourCombo.setItems(startHours);
        startMinuteCombo.setItems(minutes);
        endHourCombo.setItems(endHours);
        endMinuteCombo.setItems(minutes);

    }

    /**
     * This function checks that all fields are valid from the information pulled from the form. If any part is found invalid
     * that information is added to an errorMessage which will be displayed by the save() function with the appropriate
     * errors.
     * @param description
     * @param location
     * @param type
     * @param title
     * @param userId
     * @param customerId
     * @param contactId
     * @param date
     * @param startHours
     * @param endHour
     * @param startMinutes
     * @param endMinutes
     * @param errorMessage
     * @return
     */
    static String isAppointmentValid(String description, String location, String type, String title, int userId, int customerId, int contactId,
                                   LocalDate date, String startHours, String endHour, String startMinutes, String endMinutes, String errorMessage) {


        if (title.equals("")) {
            errorMessage = errorMessage + "The appointment needs a title.\n";
        } if (description.equals("")) {
            errorMessage = errorMessage + "The appointment needs a description.\n";
        } if (location.equals(" ")) {
            errorMessage = errorMessage + "The appointment needs a location.\n";
        } if (type.equals("")) {
            errorMessage = errorMessage + "The appointment needs a type.\n";
        } if (userId == -1) {
            errorMessage = errorMessage + "The appointment needs a user assigned.\n";
        } if (customerId == -1) {
            errorMessage = errorMessage + "The appointment needs a customer assigned.\n";
        } if (contactId == -1) {
            errorMessage = errorMessage + "The appointment needs a contact assigned.\n";
        } if (date == null) {
            errorMessage = errorMessage + "You need to select a date.\n";
        } if (startHours== null) {
            errorMessage = errorMessage + "You need to select a starting hour.\n";
        } if (startMinutes == null) {
            errorMessage = errorMessage + "You need to select a starting minute.\n";
        } if (endHour == null) {
            errorMessage = errorMessage + "You need to a select an ending hour.\n";
        } if (endMinutes == null) {
            errorMessage = errorMessage + "You need to select an ending minute.\n";
        } if (startHours.equals(endHours.get(endHours.size() - 1))) {
            errorMessage = errorMessage + "An appointment cannot start at closing time.\n";
        } if (endHour.equals(endHours.get(endHours.size() - 1)) && Integer.parseInt(endMinutes) > 0) {
            errorMessage = errorMessage + "An appointment cannot go past closing time.";
        }
        return errorMessage;
    }


    /**
     * Initializes the window and sets the combo boxes and times.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCombo.setItems(convertContact());
        contactCombo.setValue("Select a contact");

        userCombo.setItems(convertUser());
        userCombo.setValue("Select a user");

        customerCombo.setItems(convertCustomer());
        customerCombo.setValue("Select a customer");

        initializeTimes();





    }
}
