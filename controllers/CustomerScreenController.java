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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.database.DBAppointments;
import sample.database.DBConnection;
import sample.database.DBCustomers;
import sample.model.Appointments;
import sample.model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerScreenController implements Initializable {

    @FXML
    private Label title;

    @FXML
    private Label customerHeading;

    @FXML
    private TableView<Customers> customerTableView;

    @FXML
    private TableColumn<Customers, Integer> customerIdTC;

    @FXML
    private TableColumn<Customers, String> customerNameTC;

    @FXML
    private TableColumn<Customers, String> customerAddressTC;

    @FXML
    private TableColumn<Customers, String> customerZipTC;

    @FXML
    private TableColumn<Customers, String> customerPhoneTC;

    @FXML
    private TableColumn<Customers, String> divisionNameTC;

    @FXML
    private Button homeButton;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    static Customers updateCustomer;
    static int updateCustomerIndex;

    private ObservableList<Integer> aptCustomerId = FXCollections.observableArrayList();


    /**
     *
     * @param event
     * @throws IOException
     * Opens the AddCustomerScreen screen
     */
    @FXML
    void addCustomer(ActionEvent event) throws IOException {
        Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
        screen.close();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../views/AddCustomerScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add Customer Screen");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * First checks that a customer is selected. If one is it uses the customer id of the object selected to delete the
     * customer from the database.
     * @param event
     */
    @FXML
    void deleteCustomer(ActionEvent event) {
        Customers customer = customerTableView.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("You have to select a customer first.");
            alert.showAndWait();
        } else {
            int customerId = customer.getCustomerID();
            if (validateDelete(customerId)) {
                try {
                    String sql = "DELETE FROM customers WHERE Customer_ID = " + customerId;
                    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                    ps.execute();
                    updateCustomersTV();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Error deleting Customer");
                alert.setContentText("Customer has appointments that must be deleted first");
                alert.showAndWait();
            }
        }

    }

    /**
     *
     * @param event
     * Returns to the main screen
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
     *
     * @param event
     * @throws IOException
     * Opens the UpdateCustomerScreen screen
     */
    @FXML
    void updateCustomer(ActionEvent event) throws IOException {
        updateCustomer = customerTableView.getSelectionModel().getSelectedItem();
        updateCustomerIndex = DBCustomers.getAllCustomers().indexOf(updateCustomer);

        if (updateCustomer != null) {
            Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            screen.close();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../views/UpdateCustomerScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Update Customer Screen");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("You do not have a customer selected");
            alert.setContentText("Press OK to continue");
            alert.showAndWait();
        }

    }

    /**
     * Sets the customer TV with the list of customers
     */
    public void updateCustomersTV() {
        customerTableView.setItems(DBCustomers.getAllCustomers());
    }

    /**
     * This checks that all the customer's appointments have been cancelled prior to the deletion of the customer.
     * @param customerId
     * @return
     */
   public boolean validateDelete(int customerId) {
       for (Appointments a : DBAppointments.getAllAppointments()) {
           if (customerId == a.getCustomerId()) {
               return false;
           }
       }
       return true;
   }

    /**
     * This initializes the columns of the TV and the TV itself.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerIdTC.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameTC.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressTC.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerZipTC.setCellValueFactory(new PropertyValueFactory<>("customerZip"));
        customerPhoneTC.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        divisionNameTC.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        updateCustomersTV();


    }
}
