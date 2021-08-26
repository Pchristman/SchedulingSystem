package sample.model;

public class CountryReport {

    private int total;
    private String country;

    /**
     * Constructor for CountryReport objects
     * @param total
     * @param country
     */
    public CountryReport(int total, String country) {
        this.total = total;
        this.country = country;
    }

    /**
     * Returns the total
     * @return
     */
    public int getTotal() {
        return total;
    }

    /**
     * Returns the country
     * @return
     */
    public String getLocation() {
        return country;
    }
}
