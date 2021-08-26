package sample.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointments {
    private int appointmentId;
    private String description;
    private String location;
    private String type;
    private String title;
    private LocalDateTime startTime;
    private String startTimeFormat;
    private LocalDateTime endTime;
    private String endTimeFormat;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactName;
    private String customerName;
    private String userName;

    /**
     * Constructor for Appointment objects
     * @param appointmentId
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startTime
     * @param endTime
     * @param customerId
     * @param userId
     * @param contactId
     * @param contactName
     * @param customerName
     * @param userName
     */
    public Appointments(int appointmentId, String title, String description, String location, String type, LocalDateTime startTime,
                        LocalDateTime endTime, int customerId, int userId, int contactId, String contactName, String customerName,
                        String userName) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.startTimeFormat = formatter.format(this.startTime);
        this.endTime = endTime;
        this.endTimeFormat = this.endTime.format(formatter);
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
        this.customerName = customerName;
        this.userName = userName;

    }

    /**
     * Returns the appointment id
     * @return
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * returns the customer id
     * @return
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * returns the user id
     * @return
     */
    public int getUserId() {
        return userId;
    }

    /**
     * returns the contact id
     * @return
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * returns the description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the location
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the Type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the start time
     * @return
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * returns the end time
     * @return
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * returns the title of the appointment
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the contact name
     * @return
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Returns the customer name
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Returns the username
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns a formatted version of the start time
     * @return
     */
    public String getStartTimeFormat() {
        return this.startTimeFormat;
    }

    /**
     * Returns a formatted version of the end time
     * @return
     */
    public String getEndTimeFormat() {
        return endTimeFormat;
    }
}

