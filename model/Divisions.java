package sample.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Divisions {

    private int divisionID;
    private String divisionName;
    private int countryId;

    /**
     * Constructor for Division type objects
     * @param divisionID
     * @param divisionName
     * @param countryId
     */
    public Divisions(int divisionID, String divisionName, int countryId) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**
     * Returns division id
     * @return
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Returns division name
     * @return
     */
    public String getDivision() {
        return divisionName;
    }

    /**
     * Returns country id
     * @return
     */
    public int getCountryId() {
        return countryId;
    }
}
