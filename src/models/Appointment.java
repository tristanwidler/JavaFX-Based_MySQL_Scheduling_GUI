package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**This class acts as a shell for all appointment information gathered from the database.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-14
 */
public class Appointment {

    /**The appointments id number*/
    private String apptID;
    /**The appointments title*/
    private String title;
    /**The appointments description*/
    private String description;
    /**The appointments location*/
    private String location;
    /**The appointments type*/
    private String type;
    /**The appointments start date and time*/
    private String start;
    /**The appointments end date and time*/
    private String end;
    /**The creation date of the record*/
    private String createDate;
    /**The name of the user who created the record*/
    private String createdBy;
    /**The date of the records most recent update*/
    private String lastUpdate;
    /**The name of the user who most recently updated the record*/
    private String lastUpdateBy;
    /**The id of the Customer the Appointment is connected to*/
    private String custID;
    /**The id of the User the Appointment was created by*/
    private String userID;
    /**The id of the Contact the Appointment is connected to*/
    private String contactID;

    /** This constructor assigns the objects variables to their respective passed values.
     * @param custID The id of the Customer the Appointment is connected to
     * @param apptID The appointments id number
     * @param title The appointments title
     * @param description The appointments description
     * @param location The appointments location
     * @param type The appointments type
     * @param createDate The creation date of the record
     * @param createTime The creation time of the record
     * @param startDate The start date of the record
     * @param startTime The start time of the record
     * @param endDate The end date of the record
     * @param endTime The end time of the record
     * @param contactID The id of the Contact the Appointment is connected to
     * @param createdBy The name of the user who created the record
     * @param lastUpdate The date of the records most recent update
     * @param lastUpdateBy The name of the user who most recently updated the record
     * @param userID  The id of the User the Appointment was created by
     */
    public Appointment(String apptID, String title, String description, String location, String type, LocalDate startDate, LocalTime startTime, LocalDate endDate,
                       LocalTime endTime, LocalDate createDate, LocalTime createTime, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy, String custID, String userID, String contactID) {
        this.apptID = apptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = startDate.toString() + " " + startTime.toString();
        this.end = endDate.toString() + " " + endTime.toString();
        this.createDate = createDate.toString() + " " + createTime.toString();
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate.toString();
        this.lastUpdateBy = lastUpdateBy;
        this.custID = custID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /** This constructor assigns the objects variables to null.*/
    public Appointment() {
        this.apptID = null;
        this.title = null;
        this.description = null;
        this.location = null;
        this.type = null;
        this.start = null;
        this.end = null;
        this.createDate = null;
        this.createdBy = null;
        this.lastUpdate = null;
        this.lastUpdateBy = null;
        this.custID = null;
        this.userID = null;
        this.contactID = null;
    }

    /** This method retrieves the appointment id.
     * @return the appointment id.
     */
    public String getApptID() {
        return apptID;
    }

    /** This method sets the appointment id.
     * @param apptID  the appointment id.
     */
    public void setApptID(String apptID) {
        this.apptID = apptID;
    }

    /** This method retrieves the appointment title.
     * @return the appointment title.
     */
    public String getTitle() {
        return title;
    }

    /** This method sets the appointment title.
     * @param title  the appointment title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /** This method retrieves the appointment description.
     * @return the appointment description.
     */
    public String getDescription() {
        return description;
    }

    /** This method sets the appointment description.
     * @param description  the appointment description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** This method retrieves the appointment location.
     * @return the appointment location.
     */
    public String getLocation() {
        return location;
    }

    /** This method sets the appointment location.
     * @param location  the appointment location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /** This method retrieves the appointment type.
     * @return the appointment type.
     */
    public String getType() {
        return type;
    }

    /** This method sets the appointment type.
     * @param type the appointment type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /** This method retrieves the appointments start date and time.
     * @return the appointments start date and time.
     */
    public String getStart() {
        return start;
    }

    /** This method retrieves the appointments start date and time.
     * @param date the appointments start date.
     * @param time  the appointments start time.
     */
    public void setStart(LocalDate date, LocalTime time) {
        this.start = date + " " + time;
    }

    /** This method retrieves the appointments end date and time.
     * @return the appointments end date and time.
     */
    public String getEnd() {
        return end;
    }

    /** This method sets the appointments end date and time.
     * @param date the appointments end date.
     * @param time the appointments end time.
     */
    public void setEnd(LocalDate date, LocalTime time) {
        this.end = date + " " + time;
    }

    /** This method retrieves the creation date and time.
     * @return the creation date and time.
     */
    public String getCreateDate() {
        return createDate;
    }

    /** This method sets the creation date and time.
     * @param createTime the creation time.
     * @param createDate the creation date.
     */
    public void setCreateDate(LocalDate createDate, LocalTime createTime) {
        this.createDate = createDate.toString() + " " + createTime.toString();
    }

    /** This method retrieves the creator name.
     * @return the creator name.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** This method sets the name of the user who created the record.
     * @param createdBy the name of the user who created the record.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** This method retrieves the most recent update date and time.
     * @return the most recent update date and time.
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /** This method sets the date of the records most recent update.
     * @param lastUpdate The date of the records most recent update.
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate.toString();
    }

    /** This method retrieves the name of the user who most recently updated the record.
     * @return the name of the user who most recently updated the record.
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /** This method sets the name of the user who most recently updated the record.
     * @param lastUpdateBy the name of the user who most recently updated the record.
     */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /** This method retrieves the id of the Customer the Appointment is connected to.
     * @return the id of the Customer the Appointment is connected to.
     */
    public String getCustID() {
        return custID;
    }

    /** This method sets the id of the Customer the Appointment is connected to.
     * @param custID  the id of the Customer the Appointment is connected to.
     */
    public void setCustID(String custID) {
        this.custID = custID;
    }

    /** This method retrieves the id of the User the Appointment was created by.
     * @return the id of the User the Appointment was created by.
     */
    public String getUserID() {
        return userID;
    }

    /** This method sets the id of the User the Appointment was created by.
     * @param userID  the id of the User the Appointment was created by.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /** This method retrieves the id of the Contact the Appointment is connected to.
     * @return the id of the Contact the Appointment is connected to.
     */
    public String getContactID() {
        return contactID;
    }

    /** This method sets the id of the Contact the Appointment is connected to.
     * @param contactID  the id of the Contact the Appointment is connected to.
     */
    public void setContactID(String contactID) {
        this.contactID = contactID;
    }
}
