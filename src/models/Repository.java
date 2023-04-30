package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**This class acts as a database with custom methods for the program.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-13
 */
public abstract class Repository {

    /**The list of all Customer Records in the database.*/
    private static ObservableList<Customer> customerRecords = FXCollections.observableArrayList();
    /**The list of all Customer Appointments in the database.*/
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    /**The list of all Country Records in the database.*/
    private static ObservableList<Country> countryRecords = FXCollections.observableArrayList();
    /**The list of all First Level Divisions in the database.*/
    private static ObservableList<FirstLvlDiv> firstLvlDivisions = FXCollections.observableArrayList();
    /**The list of all Contact Records in the database.*/
    private static ObservableList<Contact> contacts = FXCollections.observableArrayList();
    /**The Contact Result Set from the database.*/
    private static ResultSet contactRS;
    /**The Customer Result Set from the database.*/
    private static ResultSet customerRS;
    /**The Appointment Result Set from the database.*/
    private static ResultSet appointmentsRS;
    /**The Country Result Set from the database.*/
    private static ResultSet countryRS;
    /**The First Level Div Result Set from the database.*/
    private static ResultSet firstLvlDivRS;
    /**The ID of the currently logged in user.*/
    private static int currentUserID;
    /**The username of the currently logged in user.*/
    private static String currentUsername;
    /**The Zone ID users system.*/
    private static ZoneId usersZoneID;
    /**The language translation resource bundle*/
    private static ResourceBundle rb = ResourceBundle.getBundle("lang/Nat", Locale.getDefault());

    /** This method gets the users Zone_ID.
     * @return the ZoneID
     */
    public static ZoneId getUsersZoneID() {
        return usersZoneID;
    }

    /** This method sets the Zone_ID.
     * @param usersZoneID the Zone ID to set
     */
    public static void setUsersZoneID(ZoneId usersZoneID) {
        Repository.usersZoneID = usersZoneID;
    }

    /** This method updates the repository.
     *  The methods purpose is to update the repository with the latest information from the database.
     */
    public static void updateRepoRecords ()
    {
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        //Clear existing record data
        customerRecords.clear();
        allAppointments.clear();
        countryRecords.clear();
        firstLvlDivisions.clear();
        contacts.clear();

        //Populate all customer records
        try {
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM Customers");
            preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            Repository.setCustomerRS(resultSet);

            while (resultSet.next()) {
                Customer tempCustomer = new Customer();

                tempCustomer.setCustID(resultSet.getInt(1));
                tempCustomer.setCustName(resultSet.getString(2));
                tempCustomer.setAddress(resultSet.getString(3));
                tempCustomer.setPostalCode(resultSet.getString(4));
                tempCustomer.setPhone(resultSet.getString(5));
                tempCustomer.setCreateDateTime(resultSet.getDate(6).toLocalDate(), resultSet.getTime(6).toLocalTime());
                tempCustomer.setCreatedBy(resultSet.getString(7));
                tempCustomer.setLastUpdate(resultSet.getTimestamp(8));
                tempCustomer.setLastUpdateBy(resultSet.getString(9));
                tempCustomer.setDivisionID(resultSet.getString(10));

                Repository.addCustomer(tempCustomer);
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        //Populate all appointments
        try {
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM Appointments");
            preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            Repository.setAppointmentsRS(resultSet);

            while (resultSet.next()) {
                Appointment tempAppt = new Appointment();
                ZonedDateTime tempZDT;
                LocalDateTime tempDT;
                ZoneId utcZID= ZoneId.of("UTC");

                tempAppt.setApptID(resultSet.getString(1));
                tempAppt.setTitle(resultSet.getString(2));
                tempAppt.setDescription(resultSet.getString(3));
                tempAppt.setLocation(resultSet.getString(4));
                tempAppt.setType(resultSet.getString(5));

                tempDT = LocalDateTime.of(resultSet.getDate(6).toLocalDate(),resultSet.getTime(6).toLocalTime());
                tempZDT = ZonedDateTime.ofInstant(ZonedDateTime.of(tempDT, utcZID).toInstant(), usersZoneID);
                tempAppt.setStart(tempZDT.toLocalDate(), tempZDT.toLocalTime());

                tempDT = LocalDateTime.of(resultSet.getDate(7).toLocalDate(), resultSet.getTime(7).toLocalTime());
                tempZDT = ZonedDateTime.ofInstant(ZonedDateTime.of(tempDT, utcZID).toInstant(), usersZoneID);
                tempAppt.setEnd(tempZDT.toLocalDate(), tempZDT.toLocalTime());

                tempDT = LocalDateTime.of(resultSet.getDate(8).toLocalDate(), resultSet.getTime(8).toLocalTime());
                tempZDT = ZonedDateTime.ofInstant(ZonedDateTime.of(tempDT, utcZID).toInstant(), usersZoneID);
                tempAppt.setCreateDate(tempZDT.toLocalDate(), tempZDT.toLocalTime());

                tempAppt.setCreatedBy(resultSet.getString(9));

                tempDT = resultSet.getTimestamp(10).toLocalDateTime();
                tempZDT = ZonedDateTime.ofInstant(ZonedDateTime.of(tempDT, utcZID).toInstant(), usersZoneID);
                tempAppt.setLastUpdate(tempZDT.toLocalDateTime());

                tempAppt.setLastUpdateBy(resultSet.getString(11));
                tempAppt.setCustID(resultSet.getString(12));
                tempAppt.setUserID(resultSet.getString(13));
                tempAppt.setContactID(resultSet.getString(14));

                Repository.addAppointment(tempAppt);
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        //Populate all countries
        try {
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM countries");
            preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            Repository.setCountryRS(resultSet);

            while (resultSet.next()) {
                Country tempCountry = new Country();

                tempCountry.setCountryID(resultSet.getString(1));
                tempCountry.setCountry(resultSet.getString(2));
                tempCountry.setCreateDateTime(resultSet.getDate(3).toLocalDate(), resultSet.getTime(3).toLocalTime());
                tempCountry.setCreatedBy(resultSet.getString(4));
                tempCountry.setLastUpdate(resultSet.getTimestamp(5));
                tempCountry.setLastUpdateBy(resultSet.getString(6));

                countryRecords.add(tempCountry);
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        //Populate all First Level Divisions
        try {
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM first_level_divisions");
            preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            Repository.setFirstLvlDivRS(resultSet);

            while (resultSet.next()) {
                FirstLvlDiv tempFLD = new FirstLvlDiv();

                tempFLD.setDivID(resultSet.getString(1));
                tempFLD.setDiv(resultSet.getString(2));
                tempFLD.setCreateDateTime(resultSet.getDate(3).toLocalDate(), resultSet.getTime(3).toLocalTime());
                tempFLD.setCreateBy(resultSet.getString(4));
                tempFLD.setLastUpdate(resultSet.getTimestamp(5));
                tempFLD.setLastUpdateBy(resultSet.getString(6));
                tempFLD.setCountryID(resultSet.getString(7));

                Repository.addFLD(tempFLD);
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        //Populate all contacts
        try {
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM contacts");
            preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            Repository.setContactRS(resultSet);

            while (resultSet.next()) {
                Contact tempContact = new Contact();

                tempContact.setContactID(Integer.parseInt(resultSet.getString(1)));
                tempContact.setContactName(resultSet.getString(2));
                tempContact.setEmail(resultSet.getString(3));

                Repository.addContact(tempContact);
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }


    }

    /** This method checks for appointment availability.
     * The method checks the start and end dates for availability for the customer and contact that are passed in.
     * @param startTime the start time of the Appointment
     * @param endTime the end time of the Appointment
     * @param apptDate the Appointment date
     * @param zonedStartDT the zoned Appointment start date
     * @param zonedEndDT the zoned Appointment end date
     * @param customerID the Customer ID in question
     * @param contactID the Contact ID in question
     * @return whether the appointment time slot is available.
     */
    public static Boolean apptIsAvailable (ZonedDateTime zonedStartDT, ZonedDateTime zonedEndDT, LocalDate apptDate, LocalTime startTime, LocalTime endTime, String customerID, String contactID)
    {
        try
        {
            ResultSet custApptsRS;
            ResultSet contApptsRS;


            // Gather all appointments tied to the customer
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM appointments WHERE Customer_ID = ?");
            PreparedStatement custPS = DBQuery.getPreparedStatement();
            custPS.setString(1, customerID);
            custPS.execute();
            custApptsRS = custPS.getResultSet();

            //Gather all appointments tied to the contact
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM appointments WHERE Contact_ID = ?");
            PreparedStatement contPS = DBQuery.getPreparedStatement();
            contPS.setString(1, contactID);
            contPS.execute();
            contApptsRS = contPS.getResultSet();

            LocalDate existingStartD;
            LocalTime existingStartT;
            LocalTime existingEndT;
            ZoneId estZoneID = ZoneId.of("EST5EDT");
            ZonedDateTime eastSTStartAppt = ZonedDateTime.ofInstant(zonedStartDT.toInstant(), estZoneID);
            ZonedDateTime eastSTEndAppt = ZonedDateTime.ofInstant(zonedEndDT.toInstant(), estZoneID);
            LocalTime earliestAppt = LocalTime.of(8,0);
            LocalTime lastestAppt = LocalTime.of(22,0);


            //Test for the time being within business hours.
            if (eastSTStartAppt.toLocalTime().isBefore(earliestAppt) || eastSTStartAppt.toLocalTime().isAfter(lastestAppt) || eastSTEndAppt.toLocalTime().isAfter(lastestAppt) || eastSTEndAppt.toLocalTime().isBefore(earliestAppt))
            {
                JOptionPane.showMessageDialog(null, rb.getString("selectedApptNotInBuinessHour"));
                return false;
            }

            //Test for customer appointment availability
            while (custApptsRS.next())
            {
                existingStartD = custApptsRS.getDate(6).toLocalDate();
                existingStartT = custApptsRS.getTime(6).toLocalTime();
                existingEndT = custApptsRS.getTime(7).toLocalTime();

                //Tests for equal existing and proposed appointment dates
                if (apptDate.isEqual(existingStartD))
                {
                    /*
                    Entered if the proposed start is before an existing appointment's end time and after that
                    appointments start time. It is also entered if the proposed start is equal to the existing start time
                     */
                    if ( (startTime.isBefore(existingEndT)) &&
                            (startTime.isAfter(existingStartT)) ||
                            startTime.equals(existingStartT))
                    {
                        JOptionPane.showMessageDialog(null, rb.getString("custBookedInSlot"));
                        return false;
                    }
                    if ( (endTime.isBefore(existingEndT)) &&
                            (endTime.isAfter(existingStartT)) ||
                            endTime.equals(existingStartT))
                    {
                        JOptionPane.showMessageDialog(null, rb.getString("custBookedInSlot"));
                        return false;
                    }
                }

            }

            //Test for contact appointment availability
            while (contApptsRS.next())
            {
                existingStartD = contApptsRS.getDate(6).toLocalDate();
                existingStartT = contApptsRS.getTime(6).toLocalTime();
                existingEndT = contApptsRS.getTime(7).toLocalTime();

                //Tests for equal existing and proposed appointment dates
                if (apptDate.isEqual(existingStartD))
                {
                    /*
                    Entered if the proposed start is before an existing appointment's end time and after that
                    appointments start time. It is also entered if the proposed start is equal to the existing start time
                     */
                    if ( (startTime.isBefore(existingEndT)) &&
                            (startTime.isAfter(existingStartT)) ||
                            startTime.equals(existingStartT))
                    {
                        JOptionPane.showMessageDialog(null, rb.getString("custBookedInSlot"));
                        return false;
                    }
                    if ( (endTime.isBefore(existingEndT)) &&
                            (endTime.isAfter(existingStartT)) ||
                            endTime.equals(existingEndT))
                    {
                        JOptionPane.showMessageDialog(null, rb.getString("custBookedInSlot"));
                        return false;
                    }
                }

            }
            return true;
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, rb.getString("errorCheckForAvailAppt"));
            return false;
        }
    }

    /** This method checks for appointment availability.
     * The method checks the start and end dates for availability for the customer and contact that are passed in and ignores the original appointment during the comparison.
     * @param startTime the start time of the Appointment
     * @param endTime the end time of the Appointment
     * @param apptDate the Appointment date
     * @param zonedStartDT the zoned Appointment start date
     * @param zonedEndDT the zoned Appointment end date
     * @param customerID the Customer ID in question
     * @param contactID the Contact ID in question
     * @param origApptID the Appointment ID of the original Appointment
     * @return whether the appointment time slot is available.
     */
    public static Boolean apptIsUpdateable (ZonedDateTime zonedStartDT, ZonedDateTime zonedEndDT, LocalDate apptDate, LocalTime startTime, LocalTime endTime, String customerID, String contactID, String origApptID)
    {
        try
        {
            ResultSet custApptsRS;
            ResultSet contApptsRS;


            // Gather all appointments tied to the customer
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM appointments WHERE Customer_ID = ?");
            PreparedStatement custPS = DBQuery.getPreparedStatement();
            custPS.setString(1, customerID);
            custPS.execute();
            custApptsRS = custPS.getResultSet();

            //Gather all appointments tied to the contact
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM appointments WHERE Contact_ID = ?");
            PreparedStatement contPS = DBQuery.getPreparedStatement();
            contPS.setString(1, contactID);
            contPS.execute();
            contApptsRS = contPS.getResultSet();

            LocalDate existingStartD;
            LocalTime existingStartT;
            LocalTime existingEndT;
            ZoneId estZoneID = ZoneId.of("EST5EDT");
            ZonedDateTime eastSTStartAppt = ZonedDateTime.ofInstant(zonedStartDT.toInstant(), estZoneID);
            ZonedDateTime eastSTEndAppt = ZonedDateTime.ofInstant(zonedEndDT.toInstant(), estZoneID);
            LocalTime earliestAppt = LocalTime.of(8,0);
            LocalTime lastestAppt = LocalTime.of(22,0);


            //Test for the time being within business hours.
            if (eastSTStartAppt.toLocalTime().isBefore(earliestAppt) || eastSTStartAppt.toLocalTime().isAfter(lastestAppt) || eastSTEndAppt.toLocalTime().isAfter(lastestAppt) || eastSTEndAppt.toLocalTime().isBefore(earliestAppt))
            {
                JOptionPane.showMessageDialog(null, rb.getString("selectedApptNotInBuinessHour"));
                return false;
            }

            //Test for customer appointment availability
            while (custApptsRS.next())
            {
                if (!(custApptsRS.getString(1).equals(origApptID))) {
                    existingStartD = custApptsRS.getDate(6).toLocalDate();
                    existingStartT = custApptsRS.getTime(6).toLocalTime();
                    existingEndT = custApptsRS.getTime(7).toLocalTime();

                    //Tests for equal existing and proposed appointment dates
                    if (apptDate.isEqual(existingStartD)) {
                    /*
                    Entered if the proposed start is before an existing appointment's end time and after that
                    appointments start time. It is also entered if the proposed start is equal to the existing start time
                     */
                        if ((startTime.isBefore(existingEndT)) &&
                                (startTime.isAfter(existingStartT)) ||
                                startTime.equals(existingStartT)) {
                            JOptionPane.showMessageDialog(null, rb.getString("custBookedInSlot"));
                            return false;
                        }
                        if ((endTime.isBefore(existingEndT)) &&
                                (endTime.isAfter(existingStartT)) ||
                                endTime.equals(existingStartT)) {
                            JOptionPane.showMessageDialog(null, rb.getString("custBookedInSlot"));
                            return false;
                        }
                    }
                }
            }

            //Test for contact appointment availability
            while (contApptsRS.next())
            {
                if (!(contApptsRS.getString(1).equals(origApptID))) {
                    existingStartD = contApptsRS.getDate(6).toLocalDate();
                    existingStartT = contApptsRS.getTime(6).toLocalTime();
                    existingEndT = contApptsRS.getTime(7).toLocalTime();

                    //Tests for equal existing and proposed appointment dates
                    if (apptDate.isEqual(existingStartD)) {
                    /*
                    Entered if the proposed start is before an existing appointment's end time and after that
                    appointments start time. It is also entered if the proposed start is equal to the existing start time
                     */
                        if ((startTime.isBefore(existingEndT)) &&
                                (startTime.isAfter(existingStartT)) ||
                                startTime.equals(existingStartT)) {
                            JOptionPane.showMessageDialog(null, rb.getString("contBookedInSlot"));
                            return false;
                        }
                        if ((endTime.isBefore(existingEndT)) &&
                                (endTime.isAfter(existingStartT)) ||
                                endTime.equals(existingEndT)) {
                            JOptionPane.showMessageDialog(null, rb.getString("contBookedInSlot"));
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, rb.getString("errorCheckForAvailAppt"));
            return false;
        }
    }

    /** This method gathers the appointments of a customer.
     * The method loops through all appointments and returns those that are of the Customer passed in.
     * @param selectedCust the Customer who's Appointments are the be gathered for
     * @return all appointments related to the Customer passed in.
     */
    public static ObservableList<Appointment> getAppointmentsOfCustomer (Customer selectedCust)
    {
        ObservableList<Appointment> custAppts = FXCollections.observableArrayList();

        for (Appointment temp : allAppointments)
        {
            if (temp.getCustID().equals(selectedCust.getCustID().toString()))
            {
                custAppts.add(temp);
            }
        }

        return custAppts;
    }

    /** This method gathers the appointments of a Contact.
     * The method loops through all appointments and returns those that are of the Contact passed in.
     * @param selectedContact the Contact who's Appointments are the be gathered for
     * @return all appointments related to the Contact passed in.
     */
    public static ObservableList <Appointment> getApptsOfContact (Contact selectedContact)
    {
        ObservableList<Appointment> contactAppts = FXCollections.observableArrayList();

        for (Appointment temp : allAppointments)
        {
            if (temp.getContactID().equals(selectedContact.getContactID().toString()))
            {
                contactAppts.add(temp);
            }
        }

        return contactAppts;
    }

    /** This method retrieves a customer.
     * The method loops through all Customer Records and returns the record that has a matching customer ID.
     * @param custID the Customer ID  to search for
     * @return the Customer with the same Customer ID that was passed in.
     */
    public static Customer lookupCust (int custID)
    {
        for (Customer temp : customerRecords){
            if (Integer.toString(temp.getCustID()).contains(Integer.toString(custID)))
            {
                return temp;
            }
        }
        return null;
    }

    /** This method retrieves a Contact.
     * The method loops through all Contact Records and returns the record that has a matching Contact ID.
     * @param conID the Contact ID  to search for
     * @return the Contact with the same Contact ID that was passed in.
     */
    public static Contact lookupContact (int conID)
    {
        for (Contact temp : contacts){
            if (Integer.toString(temp.getContactID()).contains(Integer.toString(conID)))
            {
                return temp;
            }
        }
        return null;
    }

    /** This method retrieves an Appointment.
     * The method loops through all Appointment and returns the record that has a matching Appointment ID.
     * @param apptID the Appointment ID  to search for
     * @return the Appointment with the same Appointment ID that was passed in.
     */
    public static Appointment lookupAppointment (String apptID)
    {
        for (Appointment temp : allAppointments){
            if (temp.getApptID().contains(apptID))
            {
                return temp;
            }
        }
        return null;
    }

    /** This method retrieves a Country via name.
     * The method loops through all Country and returns the record that has a matching Country name.
     * @param countryName the Country name  to search for
     * @return the Country with the same Country name that was passed in.
     */
    public static Country lookupCountry (String countryName)
    {
        for (Country temp : countryRecords){
            if (temp.getCountry().contains(countryName))
            {
                return temp;
            }
        }
        return null;
    }

    /** This method retrieves a Country via FLD.
     * The method loops through all Country and returns the record that encases the passed in Division ID.
     * @param divID the Division ID to search for
     * @return the Country that encases the passed in Division ID.
     */
    public static Country lookupCountryOfDiv (String divID)
    {
        for (Country temp : countryRecords)
        {
            String fLDCountryID = lookupFLDByID(divID).getCountryID();
            String countryID = temp.getCountryID();
            if(countryID.equals(fLDCountryID))
            {
                return temp;
            }
        }
        return null;
    }

    /** This method retrieves all Customers via a country division list.
     * The method loops through all Customers and passed in Divisions to find the matching records.
     * @param countryDivisionList the Division list to compare to.
     * @return the Customers that are within the Divisions passed in.
     */
    public static ObservableList <Customer> customersInCountry (ObservableList <FirstLvlDiv> countryDivisionList)
    {
        ObservableList <Customer> customersInCountry = FXCollections.observableArrayList();
        for (Customer temp : customerRecords)
        {
            for (FirstLvlDiv tempDiv : countryDivisionList)
            {
                if (temp.getDivisionID().equals(tempDiv.getDivID()))
                {
                    customersInCountry.add(temp);
                }
            }
        }

        return customersInCountry;
    }

    /** This method retrieves a FLD via Division ID.
     * The method loops through all Division's and returns the Division that matches the passed in ID.
     * @param divisionID the Division ID to search for
     * @return the Division that matches the passed in Division ID.
     */
    public static FirstLvlDiv lookupFLDByID (String divisionID)
    {
        for (FirstLvlDiv temp : firstLvlDivisions){
            if (temp.getDivID().contains(divisionID))
            {
                return temp;
            }
        }
        return null;
    }

    /** This method retrieves a FLD via Division Name.
     * The method loops through all Division's and returns the Division that matches the passed in Division name.
     * @param divisionName the Division Name to search for
     * @return the Division that matches the passed in Division Name.
     */
    public static FirstLvlDiv lookupFLDByName (String divisionName)
    {
        for (FirstLvlDiv temp : firstLvlDivisions){
            if (temp.getDiv().contains(divisionName))
            {
                return temp;
            }
        }
        return null;
    }

    /** This method retrieves all FLDs via a Country ID.
     * The method loops through all FLDs to find the matching records to the passed in ID.
     * @param countryID the Country ID to compare to.
     * @return the FLDs that are within the Country ID passed in.
     */
    public static ObservableList <FirstLvlDiv> lookupFLDOfCountry (String countryID)
    {
        ObservableList<FirstLvlDiv> tempList = FXCollections.observableArrayList();
        for (FirstLvlDiv temp : firstLvlDivisions){
            if (temp.getCountryID().contains(countryID))
            {
                tempList.add(temp);
            }
        }
        return tempList;
    }

    /** This method retrieves all Customer via a Customer Name.
     * The method loops through all Customers to find the matching records to the passed in name.
     * @param custName the Customer Name to compare to.
     * @return the Customers that have the same Customer Name passed in.
     */
    public static ObservableList <Customer> lookupCust (String custName)
    {
        ObservableList<Customer> tempList = FXCollections.observableArrayList();
        for (Customer temp : customerRecords) {
            if (temp.getCustName().contains(custName)) {
                tempList.add(temp);
            }
        }
        return tempList;
    }

    /** This method retrieves all Contacts via a Contact Name.
     * The method loops through all Contacts to find the matching records to the passed in name.
     * @param conName the Contact Name to compare to.
     * @return the Contacts that have the same Contact Name passed in.
     */
    public static ObservableList <Contact> lookupContact(String conName)
    {
        ObservableList<Contact> tempList = FXCollections.observableArrayList();
        for (Contact temp : contacts) {
            if (temp.getContactName().contains(conName)) {
                tempList.add(temp);
            }
        }
        return tempList;
    }

    /** This method retrieves all Contacts.
     * @return all Contacts.
     */
    public static ObservableList <Contact> getAllContacts ()
    {
        return contacts;
    }

    /** This method adds a new Contact.
     * @param contact the Contact to add
     */
    public static void addContact (Contact contact)
    {
        contacts.add(contact);
    }

    /** This method retrieves the Contact Result Set.
     * @return the Contact Result Set.
     */
    public static ResultSet getContactRS ()
    {
        return contactRS;
    }

    /** This method sets the Contact Result Set.
     * @param rs the Contact Result Set.
     */
    public static void setContactRS (ResultSet rs)
    {
        contactRS = rs;
    }

    /** This method retrieves all FLDs.
     * @return all FLDs.
     */
    public static ObservableList <FirstLvlDiv> getAllFLDs ()
    {
        return firstLvlDivisions;
    }

    /** This method adds a new FLD.
     * @param FLD the FLD to add
     */
    public static void addFLD (FirstLvlDiv FLD)
    {
        firstLvlDivisions.add(FLD);
    }

    /** This method retrieves the FLD Result Set.
     * @return the FLD Result Set.
     */
    public static ResultSet getFirstLvlDivRS() {
        return firstLvlDivRS;
    }

    /** This method sets the FLD Result Set.
     * @param firstLvlDivRS the FLD Result Set.
     */
    public static void setFirstLvlDivRS(ResultSet firstLvlDivRS) {
        Repository.firstLvlDivRS = firstLvlDivRS;
    }

    /** This method retrieves the Country Result Set.
     * @return the Country Result Set.
     */
    public static ResultSet getCountryRS() {
        return countryRS;
    }

    /** This method sets the Country Result Set.
     * @param countryRS the Country Result Set.
     */
    public static void setCountryRS(ResultSet countryRS) {
        Repository.countryRS = countryRS;
    }

    /** This method retrieves all Countries.
     * @return all Countries.
     */
    public static ObservableList<Country> getAllCountries() {
        return countryRecords;
    }

    /** This method adds a new Country.
     * @param country the country to add
     */
    public static void addCountryRecord(Country country) {
        countryRecords.add(country);
    }

    /** This method adds a new Customer.
     * @param newCust the Customer to add
     */
    public static void addCustomer (Customer newCust)
    {
        customerRecords.add(newCust);
    }

    /** This method adds a new Appointment.
     * @param appt the Appointment to add
     */
    public static void addAppointment (Appointment appt)
    {
        allAppointments.add(appt);
    }

    /** This method retrieves all Customers.
     * @return all Customers.
     */
    public static ObservableList<Customer> getAllCustomers ()
    {
        return customerRecords;
    }

    /** This method retrieves all Appointments.
     * @return all Appointments.
     */
    public static ObservableList<Appointment> getAllAppointments()
    {
        return allAppointments;
    }

    /** This method retrieves the Customer Result Set.
     * @return the Customer Result Set.
     */
    public static ResultSet getCustomerRS() {
        return customerRS;
    }

    /** This method sets the Customer Result Set.
     * @param customerRS the Customer Result Set.
     */
    public static void setCustomerRS(ResultSet customerRS) {
        Repository.customerRS = customerRS;
    }

    /** This method retrieves the Appointment Result Set.
     * @return the Appointment Result Set.
     */
    public static ResultSet getAppointmentsRS() {
        return appointmentsRS;
    }

    /** This method sets the Appointment Result Set.
     * @param appointmentsRS the Appointment Result Set.
     */
    public static void setAppointmentsRS(ResultSet appointmentsRS) {
        Repository.appointmentsRS = appointmentsRS;
    }

    /** This method retrieves the current User ID.
     * @return the Users ID.
     */
    public static int getCurrentUserID() {
        return currentUserID;
    }

    /** This method sets the current User ID.
     * @param currentUserID the Users ID.
     */
    public static void setCurrentUserID(int currentUserID) {
        Repository.currentUserID = currentUserID;
    }

    /** This method gets the current Username.
     * @return  the Users Username.
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /** This method sets the current Users Name.
     * @param currentUsername the Users Name.
     */
    public static void setCurrentUsername(String currentUsername) {
        Repository.currentUsername = currentUsername;
    }

}
