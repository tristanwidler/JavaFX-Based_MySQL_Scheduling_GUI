package models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**This class acts as a shell for all First Level Division information gathered from the database.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-14
 */
public class FirstLvlDiv {

    /**The division id number*/
    private String divID;
    /**The division id name*/
    private String div;
    /**The creation date of the record*/
    private String createDateTime;
    /**The name of the user who created the record*/
    private String createBy;
    /**The date of the records most recent update*/
    private Timestamp lastUpdate;
    /**The name of the user who most recently updated the record*/
    private String lastUpdateBy;
    /**The related country's id number*/
    private String countryID;

    /** This constructor assigns the objects variables to their respective passed values.
     * @param countryID The country's id number
     * @param div The country's name
     * @param createDate The creation date of the record
     * @param createTime The creation time of the record
     * @param createBy The name of the user who created the record
     * @param lastUpdate The date of the records most recent update
     * @param lastUpdateBy The name of the user who most recently updated the record
     * @param divID The division id number
     */
    public FirstLvlDiv(String divID, String div, LocalDate createDate, LocalTime createTime, String createBy, Timestamp lastUpdate, String lastUpdateBy, String countryID) {
        this.divID = divID;
        this.div = div;
        this.createDateTime = createDate + " " + createTime;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.countryID = countryID;
    }

    /** This constructor assigns the objects variables to null.*/
    public FirstLvlDiv() {
    }

    /** This method retrieves the division id.
     * @return the division id.
     */
    public String getDivID() {
        return divID;
    }

    /** This method sets the division id.
     * @param divID  the division id.
     */
    public void setDivID(String divID) {
        this.divID = divID;
    }

    /** This method retrieves the division name.
     * @return the division name.
     */
    public String getDiv() {
        return div;
    }

    /** This method sets the division name.
     * @param div the division name.
     */
    public void setDiv(String div) {
        this.div = div;
    }

    /** This method retrieves the creation date of the record.
     * @return the creation date of the record.
     */
    public String getCreateDateTime() {
        return createDateTime;
    }

    /** This method sets the creation date and time of the record.
     * @param createDate the creation date of the record.
     * @param createTime the creation time of the record.
     */
    public void setCreateDateTime(LocalDate createDate, LocalTime createTime) {
        this.createDateTime = createDate + " " + createTime;
    }

    /** This method retrieves the name of the user who created the record.
     * @return the name of the user who created the record.
     */
    public String getCreateBy() {
        return createBy;
    }

    /** This method sets the name of the user who created the record.
     * @param createBy  the name of the user who created the record.
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /** This method retrieves the date of the records most recent update.
     * @return the date of the records most recent update.
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /** This method sets the date of the records most recent update.
     * @param lastUpdate  the date of the records most recent update.
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** This method retrieves the name of the user who most recently updated the record.
     * @return the name of the user who most recently updated the record.
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /** This method sets the name of the user who most recently updated the record.
     * @param lastUpdateBy  the name of the user who most recently updated the record.
     */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /** This method retrieves the related country's id number.
     * @return the related country's id number.
     */
    public String getCountryID() {
        return countryID;
    }

    /** This method sets the related country's id number.
     * @param countryID  the related country's id number.
     */
    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }
}
