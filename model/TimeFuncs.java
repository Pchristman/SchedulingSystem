package sample.model;

import sample.database.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

public class TimeFuncs {


    /**
     * Checks that there is no conflicts when scheduling a new appointment for a customer.
     * @param start
     * @param end
     * @param customerId
     * @return
     * @throws SQLException
     */
    public static boolean checkConflict(Timestamp start, Timestamp end, int customerId) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE (? BETWEEN Start AND End OR ? BETWEEN Start AND End OR (? < Start AND ? > End)) " +
                "AND Customer_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setTimestamp(1, start);
        ps.setTimestamp(2, end);
        ps.setTimestamp(3, start);
        ps.setTimestamp(4, end);
        ps.setInt(5, customerId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return true;
        }

        return false;
    }

    /**
     * Checks that there is no conflict when updating an appointment for a customer.
     * @param start
     * @param end
     * @param appointmentId
     * @param customerID
     * @return
     * @throws SQLException
     */
    public static boolean checkConflict(Timestamp start, Timestamp end, int appointmentId, int customerID) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE (? BETWEEN Start AND End OR ? BETWEEN Start AND End OR (? < Start AND ? > End)) " +
                "AND Appointment_ID != ? AND Customer_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setTimestamp(1, start);
        ps.setTimestamp(2, end);
        ps.setTimestamp(3, start);
        ps.setTimestamp(4, end);
        ps.setInt(5, appointmentId);
        ps.setInt(6, customerID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return true;
        }

        return false;
    }


    }







