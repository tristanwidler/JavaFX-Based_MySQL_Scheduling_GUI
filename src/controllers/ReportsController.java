package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Appointment;
import models.Contact;
import models.Repository;
import javax.swing.*;
import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

/**This class is the controller of ReportsMenu.fxml.
 * The class displays multiple report methods which have search criteria that can be modified by the user.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-13
 */
public class ReportsController implements Initializable {

    /**Used to navigate the program*/
    @FXML
    public ComboBox functionComB;
    /**The Contact to be sorted by*/
    @FXML
    public ComboBox contactComB;
    /**The Month to be sorted by*/
    @FXML
    public ComboBox apptMonthComB;
    /**The Type to be sorted by*/
    @FXML
    public ComboBox apptTypeComB;
    /**Displays the number of appointments matching the search criteria*/
    @FXML
    public TextField numApptTxt;
    /**The appointment table*/
    @FXML
    public TableView <Appointment> contactScheduleTableView;
    /**The column of the contactScheduleTableView that displays the IDs of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> apptIDCol;
    /**The column of the contactScheduleTableView that displays the Titles of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> titleCol;
    /**The column of the contactScheduleTableView that displays the Types of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> typeCol;
    /**The column of the contactScheduleTableView that displays the Descriptions of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> descripCol;
    /**The column of the contactScheduleTableView that displays the Start_Dates of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> startCol;
    /**The column of the contactScheduleTableView that displays the End_Dates of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> endCol;
    /**The column of the contactScheduleTableView that displays the Customer_IDs of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> custIDCol;
    /**Displays the number of log-in attempts matching the search criteria*/
    @FXML
    public TextField logInAmountTxt;
    /**The log-in type to be sorted by*/
    @FXML
    public ComboBox logInTypeComB;
    /**Holds the lines of the desired log-in results*/
    private ObservableList<String> loginAttemptLines = FXCollections.observableArrayList();
    /**The language translation resource bundle*/
    private ResourceBundle rb = ResourceBundle.getBundle("lang/Nat", Locale.getDefault());

    /**This method is called when the controller is loaded.
     * The methods is used to populate all information necessary for the user to generate the desired reports.*/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functionComB.getItems().add(rb.getString("records"));
        functionComB.getItems().add(rb.getString("scheduling"));
        functionComB.getItems().add(rb.getString("reports"));
        functionComB.getItems().add(rb.getString("mainMenu"));

        Repository.updateRepoRecords();

        //Populate the types combination box
        if (Repository.getAllAppointments().size() > 0) {
            apptTypeComB.getItems().add(rb.getString("all"));
            for (Appointment temp : Repository.getAllAppointments()) {
                if (notTypeDuplicate (temp.getType()))
                {
                    apptTypeComB.getItems().add(temp.getType());
                }
            }
            //Populate the months combination box
            Month fillerMonth = Month.JANUARY;

            apptMonthComB.getItems().add(rb.getString("all"));
            for (int i = 0; i < 12; i++)
            {
                apptMonthComB.getItems().add(fillerMonth.toString());
                fillerMonth = fillerMonth.plus(1);
            }

            apptTypeComB.setValue(rb.getString("all"));
            apptMonthComB.setValue(rb.getString("all"));
            numApptTxt.setText(calculateNumOfAppointments().toString());
        }
        else
        {
            apptTypeComB.setPromptText(rb.getString("noCustAppts"));
            apptMonthComB.setPromptText(rb.getString("noCustAppts"));
            apptTypeComB.setDisable(true);
            apptMonthComB.setDisable(true);
            numApptTxt.setText("0");
        }

        //Populate contacts combination box
        if (Repository.getAllContacts().size() > 0) {
            contactComB.getItems().add(rb.getString("all"));
            for (Contact temp : Repository.getAllContacts()) {
                contactComB.getItems().add(temp.getContactID().toString() + "-" + temp.getContactName());
            }
            contactComB.setValue(rb.getString("all"));
            fillScheduleTableView(Repository.getAllAppointments());
        }
        else
        {
            contactScheduleTableView.setDisable(true);
            contactComB.setDisable(true);
            contactComB.setPromptText(rb.getString("noContInDB"));
        }

        //Log-In report initialization
        try
        {
            File file = new File("src/files/logInRecord.txt");
            Scanner scan = new Scanner(file);

            while (scan.hasNext()){
                loginAttemptLines.add(scan.nextLine());
            }

            if (!(loginAttemptLines.size() > 0))
            {
                logInTypeComB.setPromptText(rb.getString("noLogInRec"));
                logInTypeComB.setDisable(true);
                logInAmountTxt.setPromptText(rb.getString("noLogInRec"));
                logInAmountTxt.setDisable(true);
            }
            else
            {
                logInTypeComB.getItems().add(rb.getString("all"));
                String [] splitLoginInfo;
                for (String temp : loginAttemptLines)
                {
                    splitLoginInfo = temp.split("--");
                    if (notLogInTypeDuplicate(splitLoginInfo[0]))
                    {
                        logInTypeComB.getItems().add(splitLoginInfo[0]);
                    }
                }
                logInTypeComB.setValue(rb.getString("all"));
                logInAmountTxt.setText(calculateNumOfLogInAttempts().toString());
            }

        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }


    }

    /** This method is used by other methods within the class.
     * This method will populate numApptTxt with the number of appointments based on the Type and Month of Appointment selected.
     * @return the number of Appointments matching the selected search criteria
     */
    public Integer calculateNumOfAppointments ()
    {
        Integer numAppointments=0;
        ObservableList<Appointment> allAppts = Repository.getAllAppointments();

        if  (apptTypeComB.getValue().toString().equals(rb.getString("all")))
        {
            if (apptMonthComB.getValue().toString().equals(rb.getString("all")))
            {
                return allAppts.size();
            }
            else
            {
                Month selectedMonth = Month.valueOf(apptMonthComB.getValue().toString());
                String [] startDTSeparation;
                String [] sDSeparation;
                for (Appointment temp : allAppts) {
                    startDTSeparation = temp.getStart().split("\\s");
                    sDSeparation = startDTSeparation[0].split("-");

                    if (Month.of(Integer.parseInt(sDSeparation[1])).toString().equals(selectedMonth.toString()))
                    {
                        numAppointments ++;
                    }
                }
                return numAppointments;
            }
        }
        else
        {
            if (apptMonthComB.getValue().toString().equals(rb.getString("all")))
            {
                for (Appointment temp : allAppts)
                {
                    if (temp.getType().equals(apptTypeComB.getValue().toString()))
                    {
                        numAppointments ++;
                    }
                }
                return numAppointments;
            }
            else
            {
                Month selectedMonth = Month.valueOf(apptMonthComB.getValue().toString());
                String [] startDTSeparation;
                String [] sDSeparation;
                for (Appointment temp : allAppts)
                {
                    if (temp.getType().equals(apptTypeComB.getValue().toString()))
                    {
                        startDTSeparation = temp.getStart().split("\\s");
                        sDSeparation = startDTSeparation[0].split("-");

                        if (Month.of(Integer.parseInt(sDSeparation[1])).toString().equals(selectedMonth.toString()))
                        {
                            numAppointments ++;
                        }
                    }
                }
                return numAppointments;
            }
        }

    }

    /** This method is used by other methods within the class.
     * This method will calculate the number of Log-Ins based on the Type of Log-In attempt selected.
     * @return the number of log-in attempts
     */
    public Integer calculateNumOfLogInAttempts ()
    {
        Integer numLogIns=0;

        if  (logInTypeComB.getValue().toString().equals(rb.getString("all")))
        {
            return loginAttemptLines.size();
        }
        else
        {
            String [] splitLoginInfo;
            for (String temp : loginAttemptLines)
            {
                splitLoginInfo = temp.split("--");
                if (splitLoginInfo[0].equals(logInTypeComB.getValue().toString()))
                {
                    numLogIns++;
                }
            }
            return numLogIns;
        }
    }

    /** This method is used during the initial population of the form.
     * This method will check to make sure the next Appointment type found is not already in the apptTypeComB.
     * @param apptType the Appointment type in question
     * @return whether the Appointment type in question is already in the apptTypeComB
     */
    public Boolean notTypeDuplicate (String apptType)
    {
        if (apptTypeComB.getItems().size() == 0)
        {
            return true;
        }
        else {
            for (Object temp : apptTypeComB.getItems())
            {
                if (temp.toString().equals(apptType))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /** This method is used during the initial population of the form.
     * This method will check to make sure the next Log-In type found is not already in the logInTypeComB.
     * @param logInType the Log-In type in question
     * @return whether the Log-In type in question is already in the logInTypeComB
     */
    public Boolean notLogInTypeDuplicate (String logInType)
    {
        if (logInTypeComB.getItems().size() == 0)
        {
            return true;
        }
        else {
            for (Object temp : logInTypeComB.getItems())
            {
                if (temp.toString().equals(logInType))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /** This method is used by other methods within the class.
     * This method will populate the contactScheduleTableView with all Appointments passed in as a parameter.
     * @param contactAppointments contains all Appointments to be displayed in the table view*/
    public void fillScheduleTableView (ObservableList<Appointment> contactAppointments)
    {
        contactScheduleTableView.setItems(contactAppointments);

        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descripCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
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

    /** This method is called when on option is selected from the Contact combination in the schedule section.
     * The method fills the contactScheduleTableView based on the users selection of the contactComB.
     * @param event is not used
     */
    public void onActionContactComBUpdated(ActionEvent event) {
        if (contactComB.getValue().toString().equals(rb.getString("all")))
        {
            fillScheduleTableView(Repository.getAllAppointments());
        }
        else
        {
            String[] contactIDAndName = contactComB.getValue().toString().split("-");
            fillScheduleTableView(Repository.getApptsOfContact(Repository.lookupContact(Integer.parseInt(contactIDAndName[0]))));
        }
    }

    /** This method is called when on option is selected from the apptTypeComB.
     * The method calls calculateNumOfAppointments() and populates numApptTxt using the returned value.
     * @param event is not used
     */
    public void onActionApptTypeComBUpdated(ActionEvent event) {
        if (apptTypeComB.getValue() != null) {
            numApptTxt.setText(calculateNumOfAppointments().toString());
        }
    }

    /** This method is called when on option is selected from the apptMonthComB.
     * The method calls calculateNumOfAppointments() and populates numApptTxt using the returned value.
     * @param event is not used
     */
    public void onActionApptMonthComBUpdated(ActionEvent event) {
        if (apptMonthComB.getValue() != null) {
            numApptTxt.setText(calculateNumOfAppointments().toString());
        }
    }

    /** This method is called when on option is selected from the logInTypeComB.
     * The method calls calculateNumOfLogInAttempts() and populates logInAmountTxt using the returned value.
     * @param event is not used
     */
    public void onActionLoginComBUpdated(ActionEvent event) {
        logInAmountTxt.setText(calculateNumOfLogInAttempts().toString());
    }
}
