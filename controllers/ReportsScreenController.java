package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.database.DBAppointments;
import sample.database.DBContacts;
import sample.database.DBCountryReport;
import sample.database.DBTypeMonthReport;
import sample.model.Appointments;
import sample.model.Contacts;
import sample.model.CountryReport;
import sample.model.TypeMonthReport;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsScreenController implements Initializable{

    @FXML
    private Label title;

    @FXML
    private Label reportLabel;

    @FXML
    private Button homeButton;

    @FXML
    private Tab contactTab;

    @FXML
    private TableView<Appointments> contactTV;

    @FXML
    private TableColumn<Appointments, Integer> appointmentIdTC;

    @FXML
    private TableColumn<Appointments, String> appointmentTitleTC;

    @FXML
    private TableColumn<Appointments, String> appointmentDescripTC;

    @FXML
    private TableColumn<Appointments, String> appointmentTypeTC;

    @FXML
    private TableColumn<Appointments, String> startTC;

    @FXML
    private TableColumn<Appointments, String> endTC;

    @FXML
    private TableColumn<Appointments, Integer> customerIdTC;

    @FXML
    private ComboBox<String> contactCombo;

    @FXML
    private Tab typeMonthTab;

    @FXML
    private TableView<TypeMonthReport> typeTV;

    @FXML
    private TableColumn<TypeMonthReport, Integer> typeTotalTC;

    @FXML
    private TableColumn<TypeMonthReport, String> typeTC;

    @FXML
    private TableColumn<TypeMonthReport, String> monthTC;

    @FXML
    private Tab countryTab;

    @FXML
    private TableView<CountryReport> countryTV;

    @FXML
    private TableColumn<CountryReport, Integer> countryTotalTC;

    @FXML
    private TableColumn<CountryReport, String> countryTC;

    public ObservableList<String> contactNames = FXCollections.observableArrayList();

    /**
     * Returns to the Main Screen
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
     * This sets the contact TV equal to the applicable appointments for that Contact, or shows All if all is selected.
     * SECOND LAMBDA: This returns the rows appointments only for a contact that is selected or All if all is selected.
     * I found it was easier to dynamically filter the lists using the lambda.
     * @param event
     */
    @FXML
    void toggleContact(ActionEvent event) {
        String contact = contactCombo.getSelectionModel().getSelectedItem();
        FilteredList<Appointments> filteredList = new FilteredList<>(DBAppointments.getAllAppointments());
        filteredList.setPredicate(row -> {
            int contactID = row.getContactId();
            return  contact.equals(DBContacts.getAllContacts().get(contactID - 1).getContactName()) || contact.equals("All");
        });
        if (contact.equals("All")) {
            updateContactTV();
        } else {
            contactTV.setItems(filteredList);
        }


    }

    /**
     * This sets the Type and Month TV
     */
    void updateTypeMonthTV() {
        typeTV.setItems(DBTypeMonthReport.getTypeMonthReports());
    }

    /**
     * This sets the Country TV
     */
    void updateCountryTV() {
        countryTV.setItems(DBCountryReport.getAllCountryReports());
    }

    /**
     * This converts the list of contacts into a list of contact names for the combo boxes.
     * @return
     */
    public ObservableList<String> convertContacts() {
        for (Contacts c : DBContacts.getAllContacts()) {
            contactNames.add(c.getContactName());
        }
        contactNames.add("All");
        return contactNames;
    }

    /**
     * This sets the contact TV with all appointments by default
     */
    void updateContactTV() {
        contactTV.setItems(DBAppointments.getAllAppointments());
    }


    /**
     * This initializes all three TVs and all the columns. It also initializes the combo box and sets the default value.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeTotalTC.setCellValueFactory(new PropertyValueFactory<>("count"));
        typeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        monthTC.setCellValueFactory(new PropertyValueFactory<>("month"));
        updateTypeMonthTV();

        countryTotalTC.setCellValueFactory(new PropertyValueFactory<>("total"));
        countryTC.setCellValueFactory(new PropertyValueFactory<>("location"));
        updateCountryTV();

        appointmentIdTC.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentDescripTC.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentTitleTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        startTC.setCellValueFactory(new PropertyValueFactory<>("startTimeFormat"));
        endTC.setCellValueFactory(new PropertyValueFactory<>("endTimeFormat"));
        appointmentTypeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerIdTC.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        updateContactTV();
        contactCombo.setItems(convertContacts());
        contactCombo.setValue("All");

    }
}
