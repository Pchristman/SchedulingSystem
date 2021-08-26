package sample.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String softwareName = ":mysql:";
    private static final String hostName = "//wgudb.ucertify.com:3306/";
    private static final String databaseName = "WJ08Bh8";

    private static final String jdbcURL = protocol + softwareName + hostName + databaseName;

    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";

    private static final String userName = "U08Bh8";
    private static final String password = "53689239112";
    private static Connection connection = null;

    /**
     *
     * @return
     * Establishes a connection to the database.
     */

    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            connection = DriverManager.getConnection(jdbcURL, userName,password);

            System.out.println("Connection successfully made.");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns the connection
     * @return
     */
    public static Connection getConnection() {
        return connection;
    }
}
