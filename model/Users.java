package sample.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Users {

    private int userID;
    private String userName;
    private String password;

    /**
     * Returns user id
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     * returns username
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Constructor for Users objects
     * @param userID
     * @param userName
     * @param password
     */
    public Users(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }
}
