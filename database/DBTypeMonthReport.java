package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.TypeMonthReport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DBTypeMonthReport {

     static HashMap<Integer, String> monthMap = new HashMap<>() {{
        put(1, "January");
        put(2, "February");
        put(3, "March");
        put(4, "April");
        put(5, "May");
        put(6, "June");
        put(7, "July");
        put(8, "August");
        put(9, "September");
        put(10, "October");
        put(11, "November");
        put(12, "December");
    }};

    /**
     * Returns a list of all the types of appointment in each month as TypeMonthReport objects and converts the number
     * return as month in a String value using a hash map.
     * @return
     */
    public static ObservableList<TypeMonthReport> getTypeMonthReports() {
        ObservableList<TypeMonthReport> typeMonthReports = FXCollections.observableArrayList();

        try {
            String sql = "SELECT count(Type) as Total, Type, month(Start) as Month FROM appointments group by Type, Month;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int total = resultSet.getInt("Total");
                String type = resultSet.getString("Type");
                String month = monthMap.get(resultSet.getInt("Month"));

                TypeMonthReport typeMonthReport = new TypeMonthReport(type, total, month);
                typeMonthReports.add(typeMonthReport);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  typeMonthReports;
    }


}
