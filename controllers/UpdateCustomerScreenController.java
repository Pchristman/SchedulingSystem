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

import static sample.controllers.CustomerScreenController.updateCustomer;

public class UpdateCustomerScreenController implements Initializable {

    @FXML
    private Label title;

    @FXML
    private Label updateCustomerLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameText;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressText;

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
     * Returns to the base Customer Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void cancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Update Customer");
        alert.setHeaderText("Are you sure you wish to cancel?");
        alert.setContentText("Press OK to cancel. \nPress cancel to return to the Update Customer screen");
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
     * Checks that the information provided is valid and updates the data in the database using the updateCustomer object
     * passed from the customer screen
     * @param event
     */
    @FXML
    void save(ActionEvent event) {
        Customers customer = updateCustomer;
        int customerId = customer.getCustomerID();

        String name = nameText.getText();
        String address = addressText.getText();
        String zip = zipText.getText();
        String phone = phoneText.getText();
        String divisionName = divisionCombo.getValue();
        int divisionId = getDivisionId(DBDivisions.getAllDivisions(), divisionName);
        String countryName = countryCombo.getValue();
        int countryId = getCountriesId(DBCountries.getAllCountries(), countryName);

        String errorMessage = "";
        errorMessage = isCustomerValid(name, address, zip, phone, divisionId, countryId, errorMessage);
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Error adding customer");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            errorMessage = "";
        } else {
            try {
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                            "Division_ID = ?, Last_Update = ?, Last_Updated_By = ? WHERE Customer_ID = ?";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, address);
                ps.setString(3, zip);
                ps.setString(4, phone);
                ps.setInt(5, divisionId);
                ps.setTimestamp(6, now);
                ps.setString(7, currentUser.getUserName());
                ps.setInt(8, customerId);


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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * Converts from a division name to a division id
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
     * Converts from a country name to a country id
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
     * Converts the list of divisions to a list of division names
     * @param divisionList
     * @return
     */
    public ObservableList<String> convertDivisionToName(ObservableList<Divisions> divisionList) {
        for (Divisions d : divisionList) {
            divisionNames.add(d.getDivision());
        }
        return divisionNames;
    }

    /**
     * converts the list of countries to a list of country names
     * @param countryList
     * @return
     */
    public ObservableList<String> convertCountriesToName(ObservableList<Countries> countryList) {
        for (Countries c : countryList) {
            countryNames.add(c.getCountryName());
        }
        return countryNames;
    }

    /**
     * This validates that the division supplied is within the country supplied.
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
     * This validates all the information provided and returns an error message if not.
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
     * This sets the default information in the form to the applicable info from the passed updateCustomer object.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customers customer = updateCustomer;
        nameText.setText(customer.getCustomerName());
        addressText.setText(customer.getCustomerAddress());
        zipText.setText(customer.getCustomerZip());
        phoneText.setText(customer.getCustomerPhone());

        divisionCombo.setItems(convertDivisionToName(DBDivisions.getAllDivisions()));
        divisionCombo.setValue(customer.getDivisionName());

        countryCombo.setItems(convertCountriesToName(DBCountries.getAllCountries()));
        countryCombo.setValue(customer.getCountryName());

    }
}