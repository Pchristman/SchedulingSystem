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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.database.DBAppointments;
import sample.database.DBConnection;
import sample.model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentScreenController implements Initializable {

    @FXML
    private Label title;

    @FXML
    private Label appointmentTitle;

    @FXML
    private Button homeButton;

    @FXML
    private TableView<Appointments> aptTV;

    @FXML
    private TableColumn<Appointments, Integer> idTC;

    @FXML
    private TableColumn<Appointments, String> titleTC;

    @FXML
    private TableColumn<Appointments, String> descriptionTC;

    @FXML
    private TableColumn<Appointments, String> locationTC;

    @FXML
    private TableColumn<Appointments, String> typeTC;

    @FXML
    private TableColumn<Appointments, String> contactTC;

    @FXML
    private TableColumn<Appointments, String> startDateTimeTC;

    @FXML
    private TableColumn<Appointments, String> endDateTimeTC;

    @FXML
    private TableColumn<Appointments, Integer> customerIdTC;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private RadioButton allRB;

    @FXML
    private ToggleGroup radioToggleGroup;

    @FXML
    private RadioButton weekRB;

    @FXML
    private RadioButton monthRB;

    static Appointments modifyAppointment;


    /**
     * This returns the appointment being passed into the UpdateAppointment screen
     * @return
     */
    public static Appointments getModifyAppointment() {
        return modifyAppointment;
    }

    /**
     * This opens the AddAppointment Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void addAppointment(ActionEvent event) throws IOException {
        Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
        screen.close();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../views/AddAppointmentScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add Appointment Screen");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This deletes an appointment from the database. It first checks that something is selected and if not, displays an
     * error message. If something is selected it cancels the appointment and displays a message of the ID and Type of
     * the appointment cancelled.
     * @param event
     */
    @FXML
    void deleteAppointment(ActionEvent event) {
        Appointments appointments = aptTV.getSelectionModel().getSelectedItem();
        if (appointments == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected");
            alert.setContentText("You have to select an appointment in order to delete it.");
            alert.showAndWait();
        } else {
            try {
                int appointmentID = appointments.getAppointmentId();
                String sql = "DELETE FROM appointments WHERE Appointment_ID = " + appointmentID;
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ps.execute();
                updateAppointmentTV();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmed Delete");
                alert.setHeaderText("Appointment Canceled");
                alert.setContentText("Appointment ID: " + appointmentID + " of Type: " + appointments.getType() +
                                    " has been canceled.");
                alert.showAndWait();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * This returns to the Main Screen of the application
     * @param event
     * @throws IOException
     */
    @FXML
    void goHome(ActionEvent event) throws IOException {
        Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
        screen.close();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../views/MainScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Database Scheduling System");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This switches the display to show All appointments
     * @param event
     */
    @FXML
    void setAllRadio(ActionEvent event) {
        setRadioFilters(true, true);

    }

    /**
     * This switches the display to only show appointments in the current month.
     * @param event
     */
    @FXML
    void setMonthRadio(ActionEvent event) {
        setRadioFilters(false, true);
    }

    /**
     * This switches the display to only show appointments in the current week
     * @param event
     */
    @FXML
    void setWeekRadio(ActionEvent event) {
        setRadioFilters(false, false);
    }

    /**
     * This initializes the radio buttons and sets their default values.
     */
    private void initializeRadios() {
        radioToggleGroup = new ToggleGroup();
        allRB.setToggleGroup(radioToggleGroup);
        monthRB.setToggleGroup(radioToggleGroup);
        weekRB.setToggleGroup(radioToggleGroup);
        allRB.setSelected(true);
        monthRB.setSelected(false);
        weekRB.setSelected(false);
    }

    /**
     * This first attches a reference to the appointment selected and passed to modifyAppointment. IF there is no value selected
     * an error message is displayed. The screen for Update Appointment is opened if an object is selected.
     * @param event
     * @throws IOException
     */
    @FXML
    void updateAppointment(ActionEvent event) throws IOException {
        modifyAppointment = aptTV.getSelectionModel().getSelectedItem();

        if (modifyAppointment != null) {
            Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            screen.close();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../views/UpdateAppointmentsScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Update Appointment Screen");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("You do not have an appointment selected.");
            alert.setContentText("Press OK to continue");
            alert.showAndWait();
        }

    }

    /**
     * This sets the appointment Table View to display all appointments.
     */
    public void updateAppointmentTV() {
        aptTV.setItems(DBAppointments.getAllAppointments());
    }

    /**
     * This function is to create a filtered list based on the radio button selected. By passing two boolean values I can configure
     * the settings of the function to be able to have a different result for all three radio buttons.
     * FIRST LAMBDA: This filters the rows to be able to show whether a true variable for month has been passed or not
     * and displays the appropriate information. I found it was easier to provide a dynamic Filtered List using a lambda.
     * @param all
     * @param month
     */
    public void setRadioFilters(boolean all, boolean month) {
        if (all) {
            updateAppointmentTV();
        } else {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime end = month ? now.plusMonths(1) : now.plusWeeks(1);
            FilteredList<Appointments> filteredData = new FilteredList<>(DBAppointments.getAllAppointments());
            filteredData.setPredicate(row -> {
                LocalDateTime rowDate = row.getStartTime();
                return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(end);
            });
            aptTV.setItems(filteredData);
        }
    }

    /**
     * This initializes all the columns in the TV and the TV itself, as well as the radio buttons.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idTC.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTC.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTC.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactTC.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        startDateTimeTC.setCellValueFactory(new PropertyValueFactory<>("startTimeFormat"));
        endDateTimeTC.setCellValueFactory(new PropertyValueFactory<>("endTimeFormat"));
        customerIdTC.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        updateAppointmentTV();

        initializeRadios();
    }
}
