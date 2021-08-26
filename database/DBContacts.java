package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContacts {

    /**
     * Returns a list of all the contacts from the database
     * @return
     */
    public static ObservableList<Contacts> getAllContacts() {

        ObservableList<Contacts> contactList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int contactID = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");

                Contacts contacts = new Contacts(contactID, contactName, email);
                contactList.add(contacts);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contactList;
    }
}
