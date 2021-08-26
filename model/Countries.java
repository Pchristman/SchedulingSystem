package sample.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Countries {
    private int countryId;
    private String countryName;


    /**
     * Constructor for Countries objects
     * @param countryId
     * @param countryName
     */
    public Countries(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;

    }

    /**
     * Returns country id
     * @return
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Returns country name
     * @return
     */
    public String getCountryName() {
        return countryName;
    }



}
