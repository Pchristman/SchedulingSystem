package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Divisions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDivisions {

    /**
     * Returns a list of all the divisions from the database
     * @return
     */
    public static ObservableList<Divisions> getAllDivisions() {

        ObservableList<Divisions> divisionList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM first_level_divisions";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int divisionID = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryID = resultSet.getInt("COUNTRY_ID");


                Divisions divisions = new Divisions(divisionID, divisionName, countryID);
                divisionList.add(divisions);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return divisionList;
    }
}
