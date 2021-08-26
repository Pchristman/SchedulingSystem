package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Customers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCustomers {

    /**
     * Returns a list of all customers from the database.
     * @return
     */
    public static ObservableList<Customers> getAllCustomers() {
        ObservableList<Customers> customerList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM customers INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                        "INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {

                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String customerAddress = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                int divisionID = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryID = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");

                Customers customer = new Customers(customerID, customerName, customerAddress, postalCode, phone, divisionID,
                                                divisionName, countryID, countryName);
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }


}
