package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Countries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBCountries {

    /**
     * Returns a list of all the countries from the database
     * @return
     */
    public static ObservableList<Countries> getAllCountries() {

        ObservableList<Countries> countryList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int countryID = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");
                Countries country = new Countries(countryID, countryName);
                countryList.add(country);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return countryList;
    }

    public static void checkDateConversion() {
        String sql = "SELECT Create_Date FROM countries";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("Create_Date");
                System.out.println("Create Date: " + timestamp.toLocalDateTime().toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
