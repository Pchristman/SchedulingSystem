package sample.model;


import javafx.collections.ObservableList;

public class Customers {

    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerZip;
    private String customerPhone;
    private int divisionId;
    private String divisionName;
    private int countryId;
    private String countryName;

    /**
     * Constructor for Customers objects
     * @param customerID
     * @param customerName
     * @param customerAddress
     * @param customerZip
     * @param customerPhone
     * @param divisionId
     * @param divisionName
     * @param countryId
     * @param countryName
     */
    public Customers(int customerID, String customerName, String customerAddress, String customerZip, String customerPhone,
                     int divisionId, String divisionName, int countryId, String countryName) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerZip = customerZip;
        this.customerPhone = customerPhone;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /**
     * Returns customer id
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Returns customer name
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Returns customer Address
     * @return
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Returns customer zip
     * @return
     */
    public String getCustomerZip() {
        return customerZip;
    }

    /**
     * Returns customer phone number
     * @return
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * Returns division id
     * @return
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Returns division name
     * @return
     */
    public String getDivisionName() {
        return divisionName;
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
