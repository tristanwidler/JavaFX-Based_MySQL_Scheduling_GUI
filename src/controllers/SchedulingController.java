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
import javafx.stage.Stage;
import models.Appointment;
import models.DBQuery;
import models.JDBC;
import models.Repository;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**This class is the controller of SchedulingMenu.fxml.
 * The class displays a TableView and various buttons to modify and interact with the information inside the tables.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-13
 */
public class SchedulingController implements Initializable {

    /**The column of the apptTableView that displays the Customer_IDs of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> custIDCol;
    /**The column of the apptTableView that displays the User_IDs of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> userIDCol;
    /**The column of the apptTableView that displays the End_Dates of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> endCol;
    /**The column of the apptTableView that displays the Start_Dates of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> startCol;
    /**The column of the apptTableView that displays the Types of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> typeCol;
    /**The column of the apptTableView that displays the Contact_IDs of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> contactCol;
    /**The column of the apptTableView that displays the Locations of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> locationCol;
    /**The column of the apptTableView that displays the Descriptions of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> descripCol;
    /**The column of the apptTableView that displays the Titles of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> titleCol;
    /**The column of the apptTableView that displays the IDs of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> apptIDCol;
    /**The appointment table*/
    @FXML
    public TableView <Appointment> apptTableView;
    /**Used to navigate the program*/
    @FXML
    public ComboBox functionComB;
    /**Displays the number of appointments currently in the table*/
    @FXML
    public Label apptCounterLbl;
    /**Selected to sort by the next week or month*/
    @FXML
    public Button nextDateClusterButton;
    /**Selected to sort by the next week or month*/
    @FXML
    public Button prevDateClusterButton;
    /**Selected if sorting by month is desired*/
    @FXML
    public RadioButton monthRB;
    /**Selected if sorting by week is desired*/
    @FXML
    public RadioButton weekRB;
    /**Displays start of sorted date range*/
    @FXML
    public TextField startDateTxt;
    /**Displays end of sorted date range*/
    @FXML
    public TextField endDateTxt;
    /**Selected if no sorting is desired*/
    @FXML
    public RadioButton noneRB;
    /**Toggles the radio buttons*/
    @FXML
    public ToggleGroup Sorting;
    /**Displays the current timezone*/
    @FXML
    public Label timeZoneLbl;
    /**The language translation resource bundle*/
    private ResourceBundle rb = ResourceBundle.getBundle("lang/Nat", Locale.getDefault());
    /**The current Appointment selected by the user*/
    public static Appointment selectedAppointment;

    /**This method is called when the controller is loaded.
     * The methods is used to populate the function combination box, and fill the page with all necessary info from the repository.*/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functionComB.getItems().add(rb.getString("records"));
        functionComB.getItems().add(rb.getString("scheduling"));
        functionComB.getItems().add(rb.getString("reports"));
        functionComB.getItems().add(rb.getString("mainMenu"));

        timeZoneLbl.setText(rb.getString("timeDisplayIn") + Repository.getUsersZoneID() + rb.getString("apostTime"));

        fillApptTableView(Repository.getAllAppointments());
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

    /** This method is called when the "Add" button is clicked.
     * This method sends the user to AddAppointmentMenu.fxml.
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionAddAppt(ActionEvent event) throws IOException {
        Stage stage = (Stage)(((Button)event.getSource()).getScene().getWindow());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AddAppointmentMenu.fxml")),rb);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** This method is called when the "Update" button is clicked.
     * This method sends the user to UpdateAppointmentMenu.fxml.
     * <p>RUNTIME ERROR: The method will display an error if the user has not selected an appointment from the tableview.</p>
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionUpdateAppt(ActionEvent event) throws IOException {
        if (apptTableView.getSelectionModel().getSelectedItem() != null) {
            selectedAppointment = apptTableView.getSelectionModel().getSelectedItem();

            Stage stage = (Stage) (((Button) event.getSource()).getScene().getWindow());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/UpdateAppointmentMenu.fxml")),rb);
            stage.setScene(new Scene(root));
            stage.show();
        }
        else
        {
            JOptionPane.showMessageDialog(null, rb.getString("selectApptToUp"));
        }
    }

    /** This method is called when the "Delete" button is clicked.
     * This method will remove the selected Appointment from the database and update the repository and tableview accordingly.
     * <p>RUNTIME ERROR: The method will display an error if the user has not selected an appointment from the tableview.</p>
     * @param event is not used*/
    public void onActionDeleteAppt(ActionEvent event) {
        if (apptTableView.getSelectionModel().getSelectedItem() != null)
        {
            int userChoice = JOptionPane.showConfirmDialog(null,
                    rb.getString("confirmDelCustappt") + apptTableView.getSelectionModel().getSelectedItem().getApptID() +
                    ", Type: " + apptTableView.getSelectionModel().getSelectedItem().getType(), rb.getString("delCustApptTitle"), 0);
            if (userChoice == 0) {
                try
                {
                    // Attempt to delete the selected appointment
                    DBQuery.setPreparedStatement(JDBC.connection, "DELETE FROM appointments WHERE Appointment_ID = ?");
                    PreparedStatement deletionPS = DBQuery.getPreparedStatement();
                    deletionPS.setString(1, apptTableView.getSelectionModel().getSelectedItem().getApptID());
                    if(deletionPS.execute())
                    {
                        JOptionPane.showMessageDialog(null, rb.getString("delUnsuc"));
                    }
                    Repository.updateRepoRecords();
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null, rb.getString("delScriptError"));
                    Repository.updateRepoRecords();
                }

            }
            else {
                JOptionPane.showMessageDialog(null, rb.getString("delAbort"));
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,rb.getString("selectApptToDel"));
        }
    }

    /** This method is called when the "None" radio button is clicked.
     * This method will show all customer Appointments in the tableview.
     * @param event is not used*/
    public void onActionNoneRB(ActionEvent event) {
        startDateTxt.setText("");
        startDateTxt.setDisable(true);
        endDateTxt.setText("");
        endDateTxt.setDisable(true);
        prevDateClusterButton.setDisable(true);
        nextDateClusterButton.setDisable(true);

        fillApptTableView(Repository.getAllAppointments());
    }

    /** This method is called when the "Week" radio button is clicked.
     * This method will show all customer Appointments, of the date range specified in the text boxes below it, in the tableview.
     * @param event is not used*/
    public void onActionWeekRB(ActionEvent event) {
        endDateTxt.setDisable(false);
        startDateTxt.setDisable(false);
        prevDateClusterButton.setDisable(false);
        nextDateClusterButton.setDisable(false);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(1);

        startDateTxt.setText(startDate.toString());
        endDateTxt.setText(endDate.toString());

        fillSortedApptTableView (startDate, endDate);
    }

    /** This method is called when the "Month" radio button is clicked.
     * This method will show all customer Appointments, of the date range specified in the text boxes below it, in the tableview.
     * @param event is not used*/
    public void onActionMonthRB(ActionEvent event) {
        endDateTxt.setDisable(false);
        startDateTxt.setDisable(false);
        prevDateClusterButton.setDisable(false);
        nextDateClusterButton.setDisable(false);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(1);

        startDateTxt.setText(startDate.toString());
        endDateTxt.setText(endDate.toString());

        fillSortedApptTableView (startDate, endDate);
    }

    /** This method is called when the "Prev" button is clicked.
     * This method will change the date range of Appointments displayed by either minus 1 week or month based on the radio button selected.
     * @param event is not used*/
    public void onActionPrevDate(ActionEvent event) {

        String[] startDateStrList = startDateTxt.getText().toString().split("-");
        String[] endDateStrList = endDateTxt.getText().toString().split("-");

        LocalDate startDate = LocalDate.of(Integer.parseInt(startDateStrList[0]), Integer.parseInt(startDateStrList[1]), Integer.parseInt(startDateStrList[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(endDateStrList[0]), Integer.parseInt(endDateStrList[1]), Integer.parseInt(endDateStrList[2]));

        if (weekRB.isSelected())
        {
            startDate = startDate.minusWeeks(1);
            endDate = endDate.minusWeeks(1);
        }
        else
        {
            startDate = startDate.minusMonths(1);
            endDate = endDate.minusMonths(1);
        }

        startDateTxt.setText(startDate.toString());
        endDateTxt.setText(endDate.toString());

        fillSortedApptTableView (startDate, endDate);
    }

    /** This method is called when the "Next" button is clicked.
     * This method will change the date range of Appointments displayed by either plus 1 week or month based on the radio button selected.
     * @param event is not used*/
    public void onActionNextDate(ActionEvent event) {

        String[] startDateStrList = startDateTxt.getText().toString().split("-");
        String[] endDateStrList = endDateTxt.getText().toString().split("-");

        LocalDate startDate = LocalDate.of(Integer.parseInt(startDateStrList[0]), Integer.parseInt(startDateStrList[1]), Integer.parseInt(startDateStrList[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(endDateStrList[0]), Integer.parseInt(endDateStrList[1]), Integer.parseInt(endDateStrList[2]));

        if (weekRB.isSelected())
        {
            startDate = startDate.plusWeeks(1);
            endDate = endDate.plusWeeks(1);
        }
        else
        {
            startDate = startDate.plusMonths(1);
            endDate = endDate.plusMonths(1);
        }

        startDateTxt.setText(startDate.toString());
        endDateTxt.setText(endDate.toString());

        fillSortedApptTableView (startDate, endDate);
    }

    /** This method is used by other methods within the class.
     * This method will populate the table view with all Appointments passed in as a parameter.
     * @param apptList contains all Appointments to be displayed in the table view*/
    public void fillApptTableView (ObservableList<Appointment> apptList)
    {
        Repository.updateRepoRecords();
        apptTableView.setItems(apptList);

        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descripCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));

        apptCounterLbl.setText(rb.getString("custApptsReturned") + apptList.size());
    }

    /** This method is used by other methods within the class.
     * This method will populate the table view with a Appointments sorted via a date range.
     * @param startDate specifies the starting date of the date range to be sorted by*
     * @param endDate specifies the ending date of the date range to be sorted by*/
    public void fillSortedApptTableView (LocalDate startDate, LocalDate endDate)
    {
        try {
            ObservableList <Appointment> filteredAppts = FXCollections.observableArrayList();

            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM Appointments");
            PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
            preparedStatement.execute();
            ResultSet apptsRS = preparedStatement.getResultSet();
            Repository.setAppointmentsRS(apptsRS);

            while (apptsRS.next())
            {
                if (apptsRS.getDate(6).toLocalDate().isEqual(startDate) ||
                        apptsRS.getDate(6).toLocalDate().isEqual(endDate) ||
                        (apptsRS.getDate(6).toLocalDate().isBefore(endDate)
                                && apptsRS.getDate(6).toLocalDate().isAfter(startDate)))
                {
                    Appointment tempAppt = new Appointment();
                    ZonedDateTime tempZDT;
                    LocalDateTime tempDT;
                    ZoneId utcZID= ZoneId.of("UTC");

                    tempAppt.setApptID(apptsRS.getString(1));
                    tempAppt.setTitle(apptsRS.getString(2));
                    tempAppt.setDescription(apptsRS.getString(3));
                    tempAppt.setLocation(apptsRS.getString(4));
                    tempAppt.setType(apptsRS.getString(5));

                    tempDT = LocalDateTime.of(apptsRS.getDate(6).toLocalDate(),apptsRS.getTime(6).toLocalTime());
                    tempZDT = ZonedDateTime.ofInstant(ZonedDateTime.of(tempDT, utcZID).toInstant(), Repository.getUsersZoneID());
                    tempAppt.setStart(tempZDT.toLocalDate(), tempZDT.toLocalTime());

                    tempDT = LocalDateTime.of(apptsRS.getDate(7).toLocalDate(), apptsRS.getTime(7).toLocalTime());
                    tempZDT = ZonedDateTime.ofInstant(ZonedDateTime.of(tempDT, utcZID).toInstant(), Repository.getUsersZoneID());
                    tempAppt.setEnd(tempZDT.toLocalDate(), tempZDT.toLocalTime());

                    tempDT = LocalDateTime.of(apptsRS.getDate(8).toLocalDate(), apptsRS.getTime(8).toLocalTime());
                    tempZDT = ZonedDateTime.ofInstant(ZonedDateTime.of(tempDT, utcZID).toInstant(), Repository.getUsersZoneID());
                    tempAppt.setCreateDate(tempZDT.toLocalDate(), tempZDT.toLocalTime());

                    tempAppt.setCreatedBy(apptsRS.getString(9));

                    tempDT = apptsRS.getTimestamp(10).toLocalDateTime();
                    tempZDT = ZonedDateTime.ofInstant(ZonedDateTime.of(tempDT, utcZID).toInstant(), Repository.getUsersZoneID());
                    tempAppt.setLastUpdate(tempZDT.toLocalDateTime());

                    tempAppt.setLastUpdateBy(apptsRS.getString(11));
                    tempAppt.setCustID(apptsRS.getString(12));
                    tempAppt.setUserID(apptsRS.getString(13));
                    tempAppt.setContactID(apptsRS.getString(14));

                    filteredAppts.add(tempAppt);
                }
            }
            fillApptTableView(filteredAppts);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, rb.getString("errorWhileFillTable"));
        }
    }
}
