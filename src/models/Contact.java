package models;

/**This class acts as a shell for all Contact information gathered from the database.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-14
 */
public class Contact {

    /**The contacts id number*/
    private Integer contactID;
    /**The contacts name*/
    private String contactName;
    /**The contacts email*/
    private String email;

    /** This constructor assigns the objects variables to null.*/
    public Contact() {
        contactID = null;
        contactName = null;
        email = null;
    }

    /** This constructor assigns the objects variables to their respective passed values.
     * @param contactID The contacts id number
     * @param contactName The contacts name
     * @param email The contacts email
     */
    public Contact(Integer contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /** This method retrieves the contact id.
     * @return the contact id.
     */
    public Integer getContactID() {
        return contactID;
    }

    /** This method sets the contact ID.
     * @param contactID the contact ID.
     */
    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    /** This method retrieves the contact name.
     * @return the contact name.
     */
    public String getContactName() {
        return contactName;
    }

    /** This method sets the contact name.
     * @param contactName the contact name.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** This method retrieves the contact email.
     * @return the contact email.
     */
    public String getEmail() {
        return email;
    }

    /** This method sets the contact email.
     * @param email the contact email.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
