package sample.model;

public class Contacts {
    private int contactID;
    private String contactName;
    private String email;

    /**
     * Constructor for Contact objects
     * @param contactID
     * @param contactName
     * @param email
     */
    public Contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * Returns the contact id
     * @return
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Returns the contact name
     * @return
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Returns the contact email
     * @return
     */
    public String getEmail() {
        return email;
    }
}
