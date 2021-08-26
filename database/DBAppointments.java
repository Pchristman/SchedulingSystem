package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DBAppointments {

    /**
     * This returns a list of all the appointments from the database
     * @return
     */
    public static ObservableList<Appointments> getAllAppointments() {

        ObservableList<Appointments> aptList = FXCollections.observableArrayList();
        final ZoneId LOCAL_TIMEZONE = ZoneId.of(String.valueOf(ZoneId.systemDefault()));
        final ZoneId UTC_TIME = ZoneId.of("UTC");

        try {
            String sql = "SELECT * FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                        "INNER JOIN customers ON appointments.Customer_ID = customers.Customer_ID " +
                        "INNER JOIN users ON appointments.User_ID = users.User_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime startTime = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endTime = resultSet.getTimestamp("End").toLocalDateTime();
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String customerName = resultSet.getString("Customer_Name");
                String userName = resultSet.getString("User_Name");

                Appointments appointments = new Appointments(appointmentID, title, description, location, type, startTime,
                                                            endTime, customerID, userID, contactID, contactName, customerName,
                                                            userName);

                aptList.add(appointments);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aptList;
    }
}
