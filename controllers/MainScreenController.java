package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.database.DBConnection;

import java.io.IOException;


public class MainScreenController  {

    @FXML
    private Label title;

    @FXML
    private Button appointmentButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button exitButton;


    /**
     *
     * @param event
     * @throws IOException
     * Opens the  AppointmentScreen screen
     */
    @FXML
    void AppointmentsScreen(ActionEvent event) throws IOException {
        Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
        screen.close();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../views/AppointmentScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Appointment Screen");
        stage.setScene(scene);
        stage.show();

    }

    /**
     *
     * @param event
     * @throws IOException
     * Opens the CustomerScreen screen
     */
    @FXML
    void CustomerScreen(ActionEvent event) throws IOException {
        Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
        screen.close();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../views/CustomerScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Customer Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event
     * @throws IOException
     * Opens the ReportsScreen screen
     */
    @FXML
    void ReportsScreen(ActionEvent event) throws IOException {
        Stage screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
        screen.close();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../views/ReportsScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Reports Screen");
        stage.setScene(scene);
        stage.show();

    }

    /**
     *
     * @param event
     * Exits the program
     */
    @FXML
    void exit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Database Scheduling System");
        alert.setHeaderText("Are you sure you wish to exit?");
        alert.setContentText("Press OK to exit the program.\nPress Cancel to return to the program");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage screen = (Stage) ((Node)event.getSource()).getScene().getWindow();
            DBConnection.closeConnection();
            screen.close();
        } else {
            alert.close();
        }

    }


}

