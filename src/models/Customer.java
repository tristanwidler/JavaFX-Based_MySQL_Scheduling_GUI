package models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**This class acts as a shell for all customer information gathered from the database.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-14
 */
public class Customer {

    /**The customers id number*/
    private Integer custID;
    /**The customers name*/
    private String custName;
    /**The customers address*/
    private String address;
    /**The customers postal code*/
    private String postalCode;
    /**The customers phone number*/
    private String phone;
    /**The creation date of the record*/
    private String createDateTime;
    /**The name of the user who created the record*/
    private String createdBy;
    /**The date of the records most recent update*/
    private Timestamp lastUpdate;
    /**The name of the user who most recently updated the record*/
    private String lastUpdateBy;
    /**The id of the division the customer resides in*/
    private String divisionID;

    /** This constructor assigns the objects variables to their respective passed values.
     * @param custID The customers id number
     * @param custName The customers name
     * @param address The customers address
     * @param postalCode The customers postal code
     * @param phone The customers phone number
     * @param createDate The creation date of the record
     * @param createdBy The name of the user who created the record
     * @param lastUpdate The date of the records most recent update
     * @param lastUpdateBy The name of the user who most recently updated the record
     * @param divisionID The id of the division the customer resides in
     * @param createTime The creation time of the record
     */
    public Customer(Integer custID, String custName, String address, String postalCode, String phone, LocalDate createDate,
                    LocalTime createTime, String createdBy, Timestamp lastUpdate, String lastUpdateBy, String divisionID)
    {
        this.custID = custID;
        this.custName = custName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone=phone;
        this.createdBy=createdBy;
        this.lastUpdate=lastUpdate;
        this.lastUpdateBy=lastUpdateBy;
        this.divisionID=divisionID;
        this.createDateTime = createDate.toString() + createTime.toString();
    }

    /** This constructor assigns the objects variables to null.*/
    public Customer() {
        custID =null;
        custName = null;
        address = null;
        postalCode = null;
        phone=null;
        createdBy=null;
        lastUpdate=null;
        lastUpdateBy=null;
        divisionID=null;
        createDateTime = null;
    }

    /** This method assigns the objects variables to null.*/
    public void clear()
    {
        custID =null;
        custName = null;
        address = null;
        postalCode = null;
        phone=null;
        createdBy=null;
        lastUpdate=null;
        lastUpdateBy=null;
        divisionID=null;
        createDateTime = null;
    }

    /** This method retrieves the creation date and time.
     * @return the creation date and time.
     */
    public String getCreateDateTime() {
        return createDateTime;
    }

    /** This method sets the creation date and time.
     * @param createDate the creation date.
     * @param createTime the creation time.
     */
    public void setCreateDateTime(LocalDate createDate, LocalTime createTime) {
        this.createDateTime = createDate.toString() + " " + createTime.toString();
    }

    /** This method retrieves the customer id.
     * @return the customer id.
     */
    public Integer getCustID() {
        return custID;
    }

    /** This method sets the customers ID.
     * @param custID the customers ID.
     */
    public void setCustID(Integer custID) {
        this.custID = custID;
    }

    /** This method retrieves the customer name.
     * @return the customer name.
     */
    public String getCustName() {
        return custName;
    }

    /** This method sets the customers name.
     * @param custName the customers name.
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /** This method retrieves the customer address.
     * @return the customer address.
     */
    public String getAddress() {
        return address;
    }

    /** This method sets the customers address.
     * @param address the customers address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /** This method retrieves the customer postal.
     * @return the customer postal.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /** This method sets the customers postal.
     * @param postalCode the customers postal.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** This method retrieves the customer phone.
     * @return the customer phone.
     */
    public String getPhone() {
        return phone;
    }

    /** This method sets the customers phone.
     * @param phone the customers phone.
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /** This method sets the date of the records most recent update.
     * @param lastUpdate The date of the records most recent update.
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** This method retrieves the most recent update user name.
     * @return the most recent update user name.
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

    /** This method retrieves the customers division id.
     * @return the customers division id.
     */
    public String getDivisionID() {
        return divisionID;
    }

    /** This method sets the customers division id.
     * @param divisionID the customers division id.
     */
    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
    }
}
