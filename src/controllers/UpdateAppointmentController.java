package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**This class is the controller of UpdateAppointmentMenu.fxml.
 * The class displays two TableViews and various buttons to modify an existing Appointment.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-13
 */
public class UpdateAppointmentController implements Initializable {

    /**The column of the customerTableView that displays the Customer_IDs of the Customers it contains.*/
    @FXML
    public TableColumn <Customer, Integer> custIDCol;
    /**The column of the customerTableView that displays the Customer_Names of the Customers it contains.*/
    @FXML
    public TableColumn <Customer, String> custNameCol;
    /**The Customer table*/
    @FXML
    public TableView <Customer> customerTableView;
    /**The column of the contactTableView that displays the Contact_Names of the Contacts it contains.*/
    @FXML
    public TableColumn <Contact, String>contactNameCol;
    /**The column of the contactTableView that displays the Contact_IDs of the Contacts it contains.*/
    @FXML
    public TableColumn <Contact, Integer> contactIdCol;
    /**The Contact table*/
    @FXML
    public TableView <Contact> contactTableView;
    /**Used to navigate the program*/
    @FXML
    public ComboBox functionComB;
    /**The Type of the Appointment*/
    @FXML
    public TextField typeTxt;
    /**The Location of the Appointment*/
    @FXML
    public TextField locationTxt;
    /**The Description of the Appointment*/
    @FXML
    public TextField descripTxt;
    /**The Title of the Appointment*/
    @FXML
    public TextField titleTxt;
    /**The Appointment_ID of the Appointment*/
    @FXML
    public TextField apptIDTxt;
    /**The Contacts that can be selected for the Appointment*/
    @FXML
    public ComboBox contactComB;
    /**Used as a search bar for the customer table view*/
    @FXML
    public TextField contactSearchBar;
    /**Used to select a customer*/
    @FXML
    public ComboBox custComB;
    /**Used as a search bar for the Customer table view*/
    @FXML
    public TextField custSearchBar;
    /**Used to select the year of the start of the appointment*/
    @FXML
    public ComboBox startYearComB;
    /**Used to select the month of the start of the appointment*/
    @FXML
    public ComboBox startMonthComB;
    /**Used to select the day of the start of the appointment*/
    @FXML
    public ComboBox startDayComB;
    /**Used to select the time of the start of the appointment*/
    @FXML
    public ComboBox startTimeComB;
    /**Used to display the year of the end of the appointment*/
    @FXML
    public ComboBox endYearComB;
    /**Used to display the month of the end of the appointment*/
    @FXML
    public ComboBox endMonthComB;
    /**Used to display the day of the end of the appointment*/
    @FXML
    public ComboBox endDayComB;
    /**Used to select the time of the end of the appointment*/
    @FXML
    public ComboBox endTimeComB;
    /**The language translation resource bundle*/
    private ResourceBundle rb = ResourceBundle.getBundle("lang/Nat", Locale.getDefault());

    /**Temporary storage of searched Customer results. Used in combination with the custSearchBar.*/
    public static final ObservableList<Customer> searchedCustomers = FXCollections.observableArrayList();
    /**Temporary storage of searched Contact results. Used in combination with the contactSearchBar.*/
    public static final ObservableList<Contact> searchedContacts = FXCollections.observableArrayList();
    /**Allows the class to highlight an item in the customerTableView.*/
    public static TableView.TableViewSelectionModel<Customer> customerSelectionModel;
    /**Allows the class to highlight an item in the contactTableView.*/
    public static TableView.TableViewSelectionModel<Contact> contactSelectionModel;

    /**This method is called when the controller is loaded.
     * The method is used to populate the text fields, combination boxes, and tableviews with information from the original Appointment.*/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functionComB.getItems().add(rb.getString("records"));
        functionComB.getItems().add(rb.getString("scheduling"));
        functionComB.getItems().add(rb.getString("reports"));
        functionComB.getItems().add(rb.getString("mainMenu"));

        LocalDateTime comBFiller = LocalDateTime.now();

        //Populates the Customer combo box
        for (Customer temp: Repository.getAllCustomers())
            custComB.getItems().add(temp.getCustName());

        //Populates the Contact combo box
        for (Contact temp : Repository.getAllContacts())
            contactComB.getItems().add(temp.getContactName());

        Repository.updateRepoRecords();

        customerSelectionModel = customerTableView.getSelectionModel();
        contactSelectionModel = contactTableView.getSelectionModel();

        try
        {
            fillCustTableView(Repository.getAllCustomers());
            fillConTableView(Repository.getAllContacts());
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        //Populates the start and end year combo boxes
        for (int i=0; i<=100 ; i++) {
            startYearComB.getItems().add(comBFiller.getYear());
            endYearComB.getItems().add(comBFiller.getYear());
            comBFiller = comBFiller.plusYears(1);
        }

        //Pre-populates all fields with original values
        startMonthComB.setDisable(false);
        startDayComB.setDisable(false);
        startTimeComB.setDisable(false);
        endTimeComB.setDisable(false);

        //Populates customer and contact comboB
        custComB.setValue(Repository.lookupCust(Integer.parseInt(SchedulingController.selectedAppointment.getCustID())).getCustName());
        contactComB.setValue(Repository.lookupContact(Integer.parseInt(SchedulingController.selectedAppointment.getContactID())).getContactName());

        //Populates text fields
        apptIDTxt.setText(SchedulingController.selectedAppointment.getApptID());
        titleTxt.setText(SchedulingController.selectedAppointment.getTitle());
        descripTxt.setText(SchedulingController.selectedAppointment.getDescription());
        locationTxt.setText(SchedulingController.selectedAppointment.getLocation());
        typeTxt.setText(SchedulingController.selectedAppointment.getType());

        //Populates time combo boxes
        String [] startDTSeparation = SchedulingController.selectedAppointment.getStart().split("\\s");
        String [] sDSeparation = startDTSeparation[0].split("-");
        String [] sTSeparation = startDTSeparation[1].split(":");
        LocalDate startLD = LocalDate.of(Integer.parseInt(sDSeparation[0]), Integer.parseInt(sDSeparation[1]), Integer.parseInt(sDSeparation[2]));
        LocalTime startLT = LocalTime.of(Integer.parseInt(sTSeparation[0]), Integer.parseInt(sTSeparation[1]));

        String[] endDTSeparation = SchedulingController.selectedAppointment.getEnd().split("\\s");
        String [] eTSeparation = endDTSeparation[1].split(":");
        LocalTime endLT = LocalTime.of(Integer.parseInt(eTSeparation[0]),Integer.parseInt(eTSeparation[1]));

        startYearComB.setValue(startLD.getYear());
        popStartMonthComB();
        startMonthComB.setValue(startLD.getMonth());
        popStartDayComB();
        startDayComB.setValue(startLD.getDayOfMonth());
        popStartTimeComB();
        startTimeComB.setValue(startLT.toString());
        popEndTimeComB();
        endTimeComB.setValue(endLT.toString());
    }

    /** This method is called when the "Cancel" button is clicked.
     * This method sends the user to SchedulingMenu.fxml without updating the Appointment.
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionCancelButton(ActionEvent event) throws IOException {
        Stage stage = (Stage)(((Button)event.getSource()).getScene().getWindow());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/SchedulingMenu.fxml")),rb);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** This method is called when the "Update Appointment" button is clicked.
     * This method checks that all necessary values have been entered by the user and that they are valid.
     * The method also check the time slot selected to confirm neither the contact or customer selected are already booked.
     * <p>RUNTIME ERROR: Should the Update into the database fail, an error message will appear.</p>
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionUpdateButton(ActionEvent event) throws IOException {

        //Confirm all fields have a valid value entered or selected
        if (isValidInput())
        {
            //Confirm the appointment time for the selected customer and contact is available
            LocalDate apptDate = LocalDate.of(
                    Integer.parseInt(startYearComB.getValue().toString()),
                    Month.valueOf(startMonthComB.getValue().toString()),
                    Integer.parseInt(startDayComB.getValue().toString()));

            String[] parsedStartTime = startTimeComB.getValue().toString().split(":");
            String[] parsedEndTime = endTimeComB.getValue().toString().split(":");

            String custID = (Repository.lookupCust(custComB.getValue().toString())).get(0).getCustID().toString();
            String contID = (Repository.lookupContact(contactComB.getValue().toString())).get(0).getContactID().toString();
            String apptID = apptIDTxt.getText();

            ZoneId utcZoneID = ZoneId.of("UTC");
            ZoneId estZoneID = ZoneId.of("EST5EDT");

            LocalDateTime startDateTime = LocalDateTime.of(
                    Integer.parseInt(startYearComB.getValue().toString()),
                    Month.valueOf(startMonthComB.getValue().toString()),
                    Integer.parseInt(startDayComB.getValue().toString()),
                    Integer.parseInt(parsedStartTime[0]), Integer.parseInt(parsedStartTime [1]));

            LocalDateTime endDateTime = LocalDateTime.of(
                    Integer.parseInt(startYearComB.getValue().toString()),
                    Month.valueOf(startMonthComB.getValue().toString()),
                    Integer.parseInt(startDayComB.getValue().toString()),
                    Integer.parseInt(parsedEndTime[0]), Integer.parseInt(parsedEndTime [1]));



            ZonedDateTime utcStartDT = ZonedDateTime.ofInstant(ZonedDateTime.of(startDateTime, Repository.getUsersZoneID()).toInstant(),utcZoneID);
            ZonedDateTime utcEndDT = ZonedDateTime.ofInstant(ZonedDateTime.of(endDateTime, Repository.getUsersZoneID()).toInstant(), utcZoneID);

            ZonedDateTime estEndDT = ZonedDateTime.ofInstant(ZonedDateTime.of(endDateTime, Repository.getUsersZoneID()).toInstant(), estZoneID);
            ZonedDateTime estStartDT = ZonedDateTime.ofInstant(ZonedDateTime.of(startDateTime, Repository.getUsersZoneID()).toInstant(),estZoneID);

            LocalTime startTimeUTC = utcStartDT.toLocalTime();
            LocalTime endTimeUTC = utcEndDT.toLocalTime();

            int userChoice =
                    JOptionPane.showConfirmDialog(null, rb.getString("confirmAddApptToDatabase")+"\n"
                        + rb.getString("apptInLocal") +" "+ startDateTime.toLocalTime().toString() +" "+ rb.getString("to") +" "+ endDateTime.toLocalTime().toString() + "\n"
                        + rb.getString("apptInEST") + " " + estStartDT.toLocalTime().toString() +" "+ rb.getString("to") +" "+ estEndDT.toLocalTime().toString() + "\n"
                        + rb.getString("apptInUTC") + " " + utcStartDT.toLocalTime().toString() +" "+ rb.getString("to") +" "+ utcEndDT.toLocalTime().toString() + "\n"
                        , rb.getString("addApptQTitle"), 0);


            if (userChoice == 0) {
                if (Repository.apptIsUpdateable(utcStartDT, utcEndDT, apptDate, startTimeUTC, endTimeUTC, custID, contID, apptID)) {
                    try {
                        // Example insert statement:
                        // INSERT INTO appointments VALUES(null,?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?)"
                        // Example Update statement:
                        // UPDATE appointments SET Title = "Test Update" WHERE Appointment_ID = 1;
                        DBQuery.setPreparedStatement(JDBC.connection, "UPDATE appointments SET " +
                                "Title=?, " + "Description=?, " + "Location=?, " + "Type=?, " + "Start=?, " +
                                "End=?, " + "Create_Date=?, " + "Created_By=?, " + "Last_Update=NOW(), " + "Last_Updated_By=?, " +
                                "Customer_ID=?, " + "User_ID=?, " + "Contact_ID=? " + "WHERE Appointment_ID =?;"
                                );

                        /* Prepared Statement Key:
                        1:Title 2:Description 3:Location 4:Type 5:Start 6:End 7:Create_Date
                        8:Created_By 9:Last_Updated_By 10:Customer_ID 11:User_ID 12:Contact_ID
                        */

                        Appointment origAppt =  Repository.lookupAppointment(apptIDTxt.getText());
                        PreparedStatement ps = DBQuery.getPreparedStatement();
                        ZonedDateTime temp;

                        ps.setString(1, titleTxt.getText());
                        ps.setString(2, descripTxt.getText());
                        ps.setString(3, locationTxt.getText());
                        ps.setString(4, typeTxt.getText());

                        temp = ZonedDateTime.ofInstant(ZonedDateTime.of(startDateTime, Repository.getUsersZoneID()).toInstant(), utcZoneID);
                        ps.setString(5, temp.toLocalDate().toString() + " " + temp.toLocalTime().toString());

                        temp = ZonedDateTime.ofInstant(ZonedDateTime.of(endDateTime, Repository.getUsersZoneID()).toInstant(), utcZoneID);
                        ps.setString(6, temp.toLocalDate().toString() + " " + temp.toLocalTime().toString());

                        ps.setString(7,origAppt.getCreateDate());
                        ps.setString(8, origAppt.getCreatedBy());
                        ps.setString(9, Repository.getCurrentUsername());
                        ps.setString(10, custID);
                        ps.setString(11, String.valueOf(Repository.getCurrentUserID()));
                        ps.setString(12, contID);
                        ps.setString(13, origAppt.getApptID());

                        ps.execute();

                        Stage stage = (Stage) (((Button) event.getSource()).getScene().getWindow());
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/SchedulingMenu.fxml")),rb);
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, rb.getString("insertFail"));
                    }
                }
            }
        }
    }

    /** This method is called when on option is selected from the combination box at the top of the screen.
     * The method opens a new page respective to the option selected by the user.
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionFuncComBUpdated(ActionEvent event) throws IOException {
        String temp = functionComB.getValue().toString();
        if (temp == rb.getString("records"))
        {
            Stage stage = (Stage)(((ComboBox)event.getSource()).getScene().getWindow());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/RecordMenu.fxml")), rb);
            stage.setScene(new Scene(root));
            stage.show();
        }
        else if (temp == rb.getString("scheduling"))
        {
            Stage stage = (Stage)(((ComboBox)event.getSource()).getScene().getWindow());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/SchedulingMenu.fxml")), rb);
            stage.setScene(new Scene(root));
            stage.show();
        }
        else if (temp == rb.getString("reports"))
        {
            Stage stage = (Stage)(((ComboBox)event.getSource()).getScene().getWindow());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/ReportsMenu.fxml")), rb);
            stage.setScene(new Scene(root));
            stage.show();
        }
        else if (temp == rb.getString("mainMenu"))
        {
            Stage stage = (Stage)(((ComboBox)event.getSource()).getScene().getWindow());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/MainMenu.fxml")), rb);
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    /** This method is called when on option is selected from the start year combination box.
     * The method calls popStartMonthComB() to populate the start month combination box.
     * @param event is not used
     */
    public void onActionStartYearComBUpdated(ActionEvent event) {
        popStartMonthComB();
    }

    /** This method is used by other methods within the class.
     * This method will populate the start month combination box based on the start year selected.
     */
    public void popStartMonthComB ()
    {
        startDayComB.setDisable(true);
        startTimeComB.setDisable(true);
        endTimeComB.setDisable(true);

        startDayComB.getItems().clear();
        startMonthComB.getItems().clear();
        startTimeComB.getItems().clear();

        endYearComB.setPromptText("");
        endMonthComB.setPromptText("");
        endDayComB.setPromptText("");
        endTimeComB.setPromptText("");
        startTimeComB.setPromptText("");


        LocalDate tempDate = LocalDate.now();

        //Checks if the selected year is the current year
        if (Integer.parseInt(startYearComB.getValue().toString()) == tempDate.getYear()) {

            // Fills current and remaining months in the current year in the month combo box
            for (int i = 0; i < 12; i++) {
                startMonthComB.getItems().add(tempDate.getMonth());
                if (tempDate.getMonth() == Month.DECEMBER) {
                    break;
                }
                tempDate = tempDate.plusMonths(1);
            }
        }
        else {
            //Sets the tempDate month to January so the month list order starts at January
            tempDate = LocalDate.of(2021, 1,1);

            // Fills all months in the month combo box
            for (int i = 0; i < 12; i++) {
                startMonthComB.getItems().add(tempDate.getMonth());
                tempDate = tempDate.plusMonths(1);
            }
        }

        startMonthComB.setDisable(false);
    }

    /** This method is called when on option is selected from the start month combination box.
     * The method calls popStartDayComB() to populate the start day combination box.
     * @param event is not used
     */
    public void onActionStartMonthComBUpdated(ActionEvent event) {
        popStartDayComB();
    }

    /** This method is used by other methods within the class.
     * This method will populate the start day combination box based on the start month selected.
     */
    public void popStartDayComB ()
    {
        LocalDate tempDate = LocalDate.now();
        if (startYearComB.getItems().size() >= 1 && startMonthComB.getItems().size() >= 1) {
            startDayComB.setDisable(false);
            startTimeComB.setDisable(true);
            endTimeComB.setDisable(true);
            startDayComB.getItems().clear();
            startTimeComB.setPromptText("");
            endYearComB.setPromptText("");
            endMonthComB.setPromptText("");
            endDayComB.setPromptText("");
            endTimeComB.setPromptText("");

            Month tempMonth = Month.valueOf(startMonthComB.getValue().toString());
            Year tempYear = Year.of(Integer.parseInt(startYearComB.getValue().toString()));
            int monthLength = tempMonth.length(tempYear.isLeap());
            int startingDay;

            if (tempYear.getValue() == LocalDate.now().getYear() && tempMonth == LocalDate.now().getMonth())
            {
                startingDay = tempDate.getDayOfMonth();
            }
            else
            {
                startingDay = 1;
            }

            for (int i = startingDay; i <= monthLength; i++) {
                startDayComB.getItems().add(i);
            }
        }
    }

    /** This method is called when on option is selected from the start day combination box.
     * The method calls popStartTimeComB() to populate the start time combination box.
     * @param event is not used
     */
    public void onActionStartDayComBUpdated(ActionEvent event) {
        popStartTimeComB();
    }

    /** This method is used by other methods within the class.
     * This method will populate the start time combination box based on the start date selected.
     */
    public void popStartTimeComB ()
    {
        Boolean valueEntered = false;
        LocalTime currentTime = LocalTime.now();
        LocalTime tempTimeComBFiller;
        LocalTime latestTimeToFill = LocalTime.of(23,45);
        LocalTime earliestTimeToFill = LocalTime.of(0,0);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm");
        if(startYearComB.getItems().size() >= 1 && startMonthComB.getItems().size() >= 1 && startDayComB.getItems().size() >= 1 ) {
            startTimeComB.setDisable(false);
            endTimeComB.setDisable(true);
            startTimeComB.getItems().clear();
            endYearComB.getItems().clear();
            endMonthComB.getItems().clear();
            endDayComB.getItems().clear();

            startTimeComB.setPromptText("");

            endYearComB.getItems().add(startYearComB.getValue().toString());
            endYearComB.setPromptText(startYearComB.getValue().toString());
            endMonthComB.getItems().add(startMonthComB.getValue().toString());
            endMonthComB.setPromptText(startMonthComB.getValue().toString());
            endDayComB.getItems().add(startDayComB.getValue().toString());
            endDayComB.setPromptText(startDayComB.getValue().toString());

            LocalDate selectedDate = LocalDate.of(
                    Integer.parseInt(startYearComB.getValue().toString()),
                    ((Month) (startMonthComB.getValue())).getValue(),
                    Integer.parseInt(startDayComB.getValue().toString()));

            if (selectedDate.equals(currentDate)) {
                tempTimeComBFiller = currentTime;
                if (tempTimeComBFiller.getMinute() % 15 != 0) {
                    tempTimeComBFiller = tempTimeComBFiller.plusMinutes(15 - (tempTimeComBFiller.getMinute() % 15));
                }
                while (tempTimeComBFiller.isBefore(latestTimeToFill)) {
                    valueEntered = true;
                    startTimeComB.getItems().add(tempTimeComBFiller.format(timeFormatter));
                    if (tempTimeComBFiller.equals(latestTimeToFill.minusMinutes(1)) || tempTimeComBFiller.isAfter(latestTimeToFill)) {
                        break;
                    }
                    tempTimeComBFiller = tempTimeComBFiller.plusMinutes(15);
                }
                if (!valueEntered) {
                    startTimeComB.setPromptText(rb.getString("noTimesAvail"));
                }
            }
            else
            {
                tempTimeComBFiller = earliestTimeToFill;

                for (int i = 0; i < 96; i++)
                {
                    startTimeComB.getItems().add(tempTimeComBFiller.format(timeFormatter));
                    tempTimeComBFiller = tempTimeComBFiller.plusMinutes(15);
                }
            }
        }
    }

    /** This method is called when on option is selected from the start time combination box.
     * The method calls popEndTimeComB() to populate the end time combination box.
     * @param event is not used
     */
    public void onActionStartTimeComBUpdated(ActionEvent event) {
        popEndTimeComB();
    }

    /** This method is used by other methods within the class.
     * This method will populate the end time combination box based on the start time selected.
     */
    public void popEndTimeComB ()
    {
        Boolean valueEntered = false;
        LocalTime currentTime = LocalTime.now();
        LocalTime tempTimeComBFiller;
        LocalTime latestTimeToFill = LocalTime.of(23,46);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm");

        //Entered if a value has been selected in the end year, month, day, and time Combo Boxes
        if(endYearComB.getItems().size() >= 1 && endMonthComB.getItems().size() >= 1 && endDayComB.getItems().size() >=
                1 && startTimeComB.getItems().size() >= 1) {
            endTimeComB.setDisable(false);
            endTimeComB.getItems().clear();
            endTimeComB.setPromptText("");
            LocalTime startTimeValue;

            String[] splitTime = startTimeComB.getValue().toString().split(":");
            startTimeValue = LocalTime.of(Integer.parseInt(splitTime[0]),Integer.parseInt(splitTime[1]));
            if (!startTimeValue.equals(latestTimeToFill.minusMinutes(1))) {
                tempTimeComBFiller = startTimeValue;
                //Adds fifteen minutes to the filler time variable as the end time cannot be the same as the start
                tempTimeComBFiller = tempTimeComBFiller.plusMinutes(15);
                //Advances the Combo Box filler minute value to the nearest multiple of fifteen
                if (tempTimeComBFiller.getMinute() % 15 != 0) {
                    tempTimeComBFiller = tempTimeComBFiller.plusMinutes(15 - (tempTimeComBFiller.getMinute() % 15));
                }
                //Adds available appointment times
                while (tempTimeComBFiller.isBefore(latestTimeToFill)) {
                    valueEntered = true;
                    endTimeComB.getItems().add(tempTimeComBFiller.format(timeFormatter));
                    if (tempTimeComBFiller.equals(latestTimeToFill.minusMinutes(1))) {
                        break;
                    }
                    tempTimeComBFiller = tempTimeComBFiller.plusMinutes(15);
                }
            }

            //Entered when the current time is already past the 21:45 mark
            if (!valueEntered) { endTimeComB.setPromptText(rb.getString("noTimesAvail")); }
        }
    }

    //Customer and Contact TableView Methods:
    /**This method is called after each keystroke of the User in the Contact search bar.
     * The method uses the user input to search through and refine the Contact list displayed in the table view and combination box.
     * @param event is not used
     */
    public void onActionConSearched(KeyEvent event) {
        searchedContacts.clear();
        contactSelectionModel.clearSelection();
        contactComB.getItems().clear();

        boolean isInt;
        int conID=-1;
        try
        {
            conID=Integer.parseInt(contactSearchBar.getText());
            isInt = true;
        }
        catch (Exception e)
        {
            isInt = false;
        }
        if(isInt)
        {
            searchedContacts.add(Repository.lookupContact(conID));
        }
        if(searchedContacts.isEmpty() || (searchedContacts.get(0)==null))
        {
            fillConTableView(Repository.lookupContact(contactSearchBar.getText()));
            for (Contact temp: Repository.lookupContact(contactSearchBar.getText()))
                contactComB.getItems().add(temp.getContactName());
        }
        else
        {
            contactSelectionModel.select(searchedContacts.get(0));
            contactComB.getItems().add(searchedContacts.get(0).getContactName());
        }
    }

    /**This method is called after each keystroke of the User in the Customer search bar.
     * The method uses the user input to search through and refine the Customer list displayed in the table view and combination box.
     * @param event is not used
     */
    public void onActionCustomersSearched(KeyEvent event) {
        searchedCustomers.clear();
        customerSelectionModel.clearSelection();
        custComB.getItems().clear();

        boolean isInt;
        int custID=-1;
        try
        {
            custID=Integer.parseInt(custSearchBar.getText());
            isInt = true;
        }
        catch (Exception e)
        {
            isInt = false;
        }
        if(isInt)
        {
            searchedCustomers.add(Repository.lookupCust(custID));
        }
        if(searchedCustomers.isEmpty() || (searchedCustomers.get(0)==null))
        {
            fillCustTableView(Repository.lookupCust(custSearchBar.getText()));
            for (Customer temp: Repository.lookupCust(custSearchBar.getText()))
                custComB.getItems().add(temp.getCustName());
        }
        else
        {
            customerSelectionModel.select(searchedCustomers.get(0));
            custComB.getItems().add(searchedCustomers.get(0).getCustName());
        }
    }

    /**This method is called when the user clicks a Customer in the table view.
     * The method adds and selects said Customer within the Customer combination box.
     * @param mouseEvent is not used
     */
    public void onActionCustSelected(MouseEvent mouseEvent) {
        if (customerTableView.getSelectionModel().getSelectedItem() != null) {
            custComB.getItems().clear();
            custComB.getItems().add(customerTableView.getSelectionModel().getSelectedItem().getCustName());
            custComB.getSelectionModel().select(0);
        }
    }

    /**This method is called when the user clicks a Contact in the table view.
     * The method adds and selects said Contact within the Contact combination box.
     * @param mouseEvent is not used
     */
    public void onActionContSelected(MouseEvent mouseEvent) {
        if (contactTableView.getSelectionModel().getSelectedItem() != null) {
            contactComB.getItems().clear();
            contactComB.getItems().add(contactTableView.getSelectionModel().getSelectedItem().getContactName());
            contactComB.getSelectionModel().select(0);
        }
    }

    /** This method is used by other methods within the class.
     * This method will populate the Customer table view with all Customers passed in as a parameter.
     * @param custList contains all Customers to be displayed in the table view*/
    public void fillCustTableView (ObservableList<Customer> custList)
    {
        customerTableView.setItems(custList);
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
    }

    /** This method is used by other methods within the class.
     * This method will populate the Contact table view with all Contact passed in as a parameter.
     * @param conList contains all Contact to be displayed in the table view*/
    public void fillConTableView (ObservableList<Contact> conList)
    {
        contactTableView.setItems(conList);
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        contactNameCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
    }

    /** This method is used by other methods within the class.
     * This method will check for input that can not be uploaded to the database.
     * <p>RUNTIME ERROR: Any invalid input will show the user an error message related to the issue.
     * @return the result of the logic and validity tests.</p>
     */
    public Boolean isValidInput ()
    {
        boolean isValid = true;

        if (custComB.getValue() == null) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("mustSelectCustFromDrop"));
        }
        if (contactComB.getValue() == null) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("mustSelectContFromDrop"));
        }
        if (titleTxt.getText().isBlank()) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("mustEnterTitle"));
        }
        if (descripTxt.getText().isBlank()) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("mustEnterDesc"));
        }
        if (locationTxt.getText().isBlank()) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("mustEnterLocation"));
        }
        if (typeTxt.getText().isBlank()) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("mustEnterType"));
        }

        if (titleTxt.getText().length() > 50) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("titleCharLimit"));
        }
        if (descripTxt.getText().length() > 50) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("descCharLimit"));
        }
        if (locationTxt.getText().length() > 50) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("locationCharLimit"));
        }
        if (typeTxt.getText().length() > 50) {
            isValid=false;
            JOptionPane.showMessageDialog(null,rb.getString("typeCharLimit"));
        }

        try
        {
            String[] tempS = startTimeComB.getValue().toString().split(":");
            LocalDateTime tempDT = LocalDateTime.of(
                    Integer.parseInt(startYearComB.getValue().toString()),
                    Month.valueOf(startMonthComB.getValue().toString()),
                    Integer.parseInt(startDayComB.getValue().toString()),
                    Integer.parseInt(tempS[0]), Integer.parseInt(tempS [1]));
        }
        catch (Exception e)
        {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("startDateTimeFailInst"));
        }
        try
        {
            String[] tempS = endTimeComB.getValue().toString().split(":");
            LocalDateTime tempDT = LocalDateTime.of(
                    Integer.parseInt(startYearComB.getValue().toString()),
                    Month.valueOf(startMonthComB.getValue().toString()),
                    Integer.parseInt(startDayComB.getValue().toString()),
                    Integer.parseInt(tempS[0]), Integer.parseInt(tempS [1]));
        }
        catch (Exception e)
        {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("endDateTimeFailInst"));
        }

        return isValid;
    }
}
