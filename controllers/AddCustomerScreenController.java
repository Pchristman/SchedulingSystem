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
import javafx.stage.Stage;
import sample.database.DBConnection;
import sample.database.DBCountries;
import sample.database.DBDivisions;
import sample.model.Countries;
import sample.model.Customers;
import sample.model.Divisions;
import sample.model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddCustomerScreenController implements Initializable {

    @FXML
    private Label title;

    @FXML
    private Label addCustomerLabel;

    @FXML
    private Label customerNameLabel;

    @FXML
    private TextField customerNameText;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField customerAddressText;

    @FXML
    private Label zipLabel;

    @FXML
    private TextField zipText;

    @FXML
    private Label phoneLabel;

    @FXML
    private TextField phoneText;

    @FXML
    private Label divisionLabel;

    @FXML
    private ComboBox<String> divisionCombo;

    @FXML
    private Label countryLabel;

    @FXML
    private ComboBox<String> countryCombo;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    private ObservableList<String> divisionNames = FXCollections.observableArrayList();
    private ObservableList<String> countryNames = FXCollections.observableArrayList();

    static Users currentUser = LoginScreenController.getCurrentUser();

    /**
     * This cancels out of the AddCustomer Screen and returns to the base Customer Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void cancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Add Customer");
        alert.setHeaderText("Are you sure you wish to cancel?");
        alert.setContentText("Press OK to cancel. \nPress cancel to return to the Add Customer screen");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            screen.close();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../views/CustomerScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Customer Screen");
            stage.setScene(scene);
            stage.show();
        } else {
            alert.close();
        }

    }

    /**
     * This pulls the information from the combo boxes and text fields and first checks that all fields are valid
     * and then performs an insert to the mySql database.
     * @param event
     * @throws IOException
     */
    @FXML
    void save(ActionEvent event) throws IOException {
        String customerName = customerNameText.getText();
        String customerAddress = customerAddressText.getText();
        String zip = zipText.getText();
        String phone = phoneText.getText();
        String division =  divisionCombo.getValue();
        int divisionId = getDivisionId(DBDivisions.getAllDivisions(), division);
        String country = countryCombo.getValue();
        int countryId = getCountriesId(DBCountries.getAllCountries(), country);

        String errorMessage = "";
        errorMessage = isCustomerValid(customerName, customerAddress, zip, phone, divisionId, countryId, errorMessage);
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Error adding customer");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        } else {
            try {
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID, " +
                            "Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ps.setString(1, customerName);
                ps.setString(2, customerAddress);
                ps.setString(3, zip);
                ps.setString(4, phone);
                ps.setInt(5, divisionId);
                ps.setTimestamp(6, now);
                ps.setString(7, currentUser.getUserName());
                ps.setTimestamp(8, now);
                ps.setString(9, currentUser.getUserName());

                ps.executeUpdate();

                Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
                screen.close();
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("../views/CustomerScreen.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Customer Screen");
                stage.setScene(scene);
                stage.show();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }

    /**
     * This converts the division name in the combo boxes into the division ID for inserting to the database.
     * @param divisionList
     * @param name
     * @return
     */
    int getDivisionId(ObservableList<Divisions> divisionList, String name) {
        for (Divisions d : divisionList) {
            if (d.getDivision().equals(name)) {
                return d.getDivisionID();
            }
        }
        return -1;
    }

    /**
     * This converts the country name from the combo boxes to a country id for inserting into the database.
     * @param countryList
     * @param name
     * @return
     */
    int getCountriesId(ObservableList<Countries> countryList, String name) {
        for (Countries c : countryList) {
            if (c.getCountryName().equals(name)) {
                return c.getCountryId();
            }
        }
        return -1;
    }

    /**
     * This functions makes sure that the supplied division is within the country provided. It is validated in the isCustomerValid() function
     * and displays an error message if not.
     * @param countryId
     * @param divisionId
     * @return
     */
    public static boolean validateDivAndCountry(int countryId, int divisionId) {
        for (Divisions d : DBDivisions.getAllDivisions()) {
            if (d.getDivisionID() == divisionId && d.getCountryId() == countryId) {
                return true;
            }
        }
        return false;
    }

    /**
     * This checks the information supplied is valid. It is ran in the save() function and displays an error message if not.
     * @param name
     * @param address
     * @param zip
     * @param phone
     * @param divisionId
     * @param countryId
     * @param errorMessage
     * @return
     */
    public static String isCustomerValid(String name, String address, String zip, String phone, int divisionId,
                                         int countryId, String errorMessage) {
        if (name.equals("")) {
            errorMessage = errorMessage + "The customer needs a name.\n";
        }
        if (address.equals("")) {
            errorMessage = errorMessage + "The customer needs an address.\n";
        }
        if (zip.equals("")) {
            errorMessage = errorMessage + "The customer needs a postal code.\n";
        }
        if (phone.equals("")) {
            errorMessage = errorMessage + "The customer needs a phone number.\n";
        }
        if (!(validateDivAndCountry(countryId, divisionId))) {
            errorMessage = errorMessage + "The division supplied is not in the correct country";
        }
        return errorMessage;
    }

    /**
     * This converts the list of divisions into a list of division names for inserting into the combo boxes.
     * @param divisionList
     * @return
     */
    public ObservableList<String> convertDivision(ObservableList<Divisions> divisionList) {
        for (Divisions d : divisionList) {
            divisionNames.add(d.getDivision());
        }
        return divisionNames;
    }

    /**
     * This converts the list of countries into a list of country names for inserting into the combo boxes.
     * @param countryList
     * @return
     */
    public ObservableList<String> convertCountries(ObservableList<Countries> countryList) {
        for (Countries c : countryList) {
            countryNames.add(c.getCountryName());
        }
        return countryNames;
    }

    /**
     * This initializes the combo boxes and sets their default value.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        divisionCombo.setItems(convertDivision(DBDivisions.getAllDivisions()));
        divisionCombo.setValue("Select a State/Region");

        countryCombo.setItems(convertCountries(DBCountries.getAllCountries()));
        countryCombo.setValue("Select a country");
    }
}
