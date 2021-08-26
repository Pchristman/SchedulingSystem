package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.CountryReport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCountryReport {

    /**
     * Returns a list of the total amount of customers in each country as CountryReport objects.
     * @return
     */
    public static ObservableList<CountryReport> getAllCountryReports() {

        ObservableList<CountryReport> countryReports = FXCollections.observableArrayList();

        try {
            String sql = "SELECT COUNT(Country) as Total, Country FROM customers INNER JOIN first_level_divisions ON " +
                        "customers.Division_ID = first_level_divisions.Division_ID INNER JOIN countries ON countries.Country_ID " +
                        "= first_level_divisions.COUNTRY_ID GROUP BY Country;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int total = resultSet.getInt("Total");
                String country = resultSet.getString("Country");

                CountryReport countryReport = new CountryReport(total, country);

                countryReports.add(countryReport);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countryReports;
    }
}
