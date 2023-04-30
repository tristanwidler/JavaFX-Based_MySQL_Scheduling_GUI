package models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**This class acts as a shell for all Country information gathered from the database.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-14
 */
public class Country {

    /**The country's id number*/
    private String countryID;
    /**The country's name*/
    private String country;
    /**The creation date of the record*/
    private String createDateTime;
    /**The name of the user who created the record*/
    private String createdBy;
    /**The date of the records most recent update*/
    private Timestamp lastUpdate;
    /**The name of the user who most recently updated the record*/
    private String lastUpdateBy;

    /** This constructor assigns the objects variables to their respective passed values.
     * @param countryID The country's id number
     * @param country The country's name
     * @param createDate The creation date of the record
     * @param createTime The creation time of the record
     * @param createdBy The name of the user who created the record
     * @param lastUpdate The date of the records most recent update
     * @param lastUpdateBy The name of the user who most recently updated the record
     */
    public Country(String countryID, String country, LocalDate createDate, LocalTime createTime, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.countryID = countryID;
        this.country = country;
        this.createDateTime = createDate + " " + createTime;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    /** This constructor assigns the objects variables to null.*/
    public Country ()
    {
        this.countryID = null;
        this.country = null;
        this.createDateTime = null;
        this.createdBy = null;
        this.lastUpdate = null;
        this.lastUpdateBy = null;
    }

    /** This method retrieves the country id.
     * @return the country id.
     */
    public String getCountryID() {
        return countryID;
    }

    /** This method sets the country id.
     * @param countryID  the country id.
     */
    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    /** This method retrieves the country name.
     * @return the country name.
     */
    public String getCountry() {
        return country;
    }

    /** This method sets the country name.
     * @param country  the country name.
     */
    public void setCountry(String country) {
        this.country = country;
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
    public String getCreatedBy() {
        return createdBy;
    }

    /** This method sets the name of the user who created the record.
     * @param createdBy  the name of the user who created the record.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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






}
