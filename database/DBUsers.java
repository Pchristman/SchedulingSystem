package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers {

    /**
     * Returns a list of all the users in the database
     * @return
     */
    public static ObservableList<Users> getAllUsers() {

        ObservableList<Users> userList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int userID = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");

                Users user = new Users(userID, userName, password);
                userList.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userList;
    }
}
