package sample.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.database.DBConnection;

public class Main extends Application {

    /**
     * Opens the login screen
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/LoginScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Database Scheduling System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Connects to the database and closes it when exited
     * @param args
     */
    public static void main(String[] args) {
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
