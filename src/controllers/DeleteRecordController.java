package controllers;

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
import models.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**This class is the controller of DeleteRecordMenu.fxml.
 * The class displays the data of the Customer Record to be deleted as well as a table view with all related Appointments.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-13
 */
public class DeleteRecordController implements Initializable {

    /**Used to select the Country of the Customer*/
    @FXML
    public ComboBox countryComB;
    /**Used to select the First Level Division of the Customer*/
    @FXML
    public ComboBox fLDComB;
    /**Used to navigate the program*/
    @FXML
    public ComboBox functionComB;
    /**The phone number of the Customer*/
    @FXML
    public TextField phoneTxt;
    /**The name of the Customer*/
    @FXML
    public TextField nameTxt;
    /**The ID of the Customer*/
    @FXML
    public TextField custIDTxt;
    /**The postal code of the Customer*/
    @FXML
    public TextField postalTxt;
    /**The address of the Customer*/
    @FXML
    public TextField addressTxt;
    /**The appointment table*/
    @FXML
    public TableView <Appointment> custApptTableView;
    /**The column of the custApptTableView that displays the IDs of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> apptIDCol;
    /**The column of the custApptTableView that displays the Titles of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> titleCol;
    /**The column of the custApptTableView that displays the Locations of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> locationCol;
    /**The column of the custApptTableView that displays the Descriptions of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> descCol;
    /**The column of the custApptTableView that displays the Start_Dates of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> startDateCol;
    /**The column of the custApptTableView that displays the End_Dates of the Appointments it contains.*/
    @FXML
    public TableColumn <Appointment, String> endDateCol;
    /**The language translation resource bundle*/
    private ResourceBundle rb = ResourceBundle.getBundle("lang/Nat", Locale.getDefault());

    /**This method is called when the controller is loaded.
     * The method is used to populate the country and function combination boxes, Appointment table view, and text fields based on the original Customer Record.*/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functionComB.getItems().add(rb.getString("records"));
        functionComB.getItems().add(rb.getString("scheduling"));
        functionComB.getItems().add(rb.getString("reports"));
        functionComB.getItems().add(rb.getString("mainMenu"));

        Repository.updateRepoRecords();
        ObservableList<Country> countries = Repository.getAllCountries();

        //Populate the country combo box
        for (int i = 0; i< countries.size(); i++)
        {
            countryComB.getItems().add(countries.get(i).getCountry());
        }

        //Populate text fields
        custIDTxt.setText(RecordController.selectedCustomer.getCustID().toString());
        nameTxt.setText(RecordController.selectedCustomer.getCustName());
        phoneTxt.setText(RecordController.selectedCustomer.getPhone());
        addressTxt.setText(RecordController.selectedCustomer.getAddress());
        postalTxt.setText(RecordController.selectedCustomer.getPostalCode());

        //Populate combination boxes
        countryComB.setValue(Repository.lookupCountryOfDiv(RecordController.selectedCustomer.getDivisionID()).getCountry());
        populateFLDComB();
        fLDComB.setValue(Repository.lookupFLDByID(RecordController.selectedCustomer.getDivisionID()).getDiv());

        //Populate appointments tableview
        ObservableList<Appointment> apptsOfCustList = Repository.getAppointmentsOfCustomer(RecordController.selectedCustomer);
        fillApptTableView(apptsOfCustList);
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

    /** This method is used by other methods within the class.
     * This method will populate the First Level Division combination box based on the start year selected.
     */
    public void populateFLDComB ()
    {
        //Gathers selection from the country combo box
        String selectedCountryStr = countryComB.getSelectionModel().getSelectedItem().toString();

        //Gathers the country object from the repository based on the user selection
        Country selectedCountry = Repository.lookupCountry(selectedCountryStr);

        //Gathers all divisions of the selected country
        ObservableList<FirstLvlDiv> selectedCountryDivs = Repository.lookupFLDOfCountry(selectedCountry.getCountryID());

        //Fills the division combo box for further filtering if desired
        for (int i = 0; i < selectedCountryDivs.size(); i++) {
            fLDComB.getItems().add(selectedCountryDivs.get(i).getDiv());
        }
    }

    /** This method is called when the "Delete Appointment" button is clicked.
     * This method confirms the users decision and DELETES the appointment from the database and updates the repository and tableview.
     * <p>RUNTIME ERROR: If the user does not have an appointment selected or if the DELETE from the database fails, an error message will appear.</p>
     * @param event is not used
     */
    public void onActionDeleteAppt(ActionEvent event) {
        if (custApptTableView.getSelectionModel().getSelectedItem() != null) {
            int userChoice = JOptionPane.showConfirmDialog(null,
                    rb.getString("confirmDelCustappt"), rb.getString("delCustApptTitle"), 0);

            if (userChoice == 0) {
                try
                {
                    // Attempt to delete the selected appointment
                    DBQuery.setPreparedStatement(JDBC.connection, "DELETE FROM appointments WHERE Appointment_ID = ?");
                    PreparedStatement deletionPS = DBQuery.getPreparedStatement();
                    deletionPS.setString(1, custApptTableView.getSelectionModel().getSelectedItem().getApptID());
                    if(deletionPS.execute())
                    {
                        JOptionPane.showMessageDialog(null, rb.getString("delUnsuc"));
                    }
                    Repository.updateRepoRecords();

                    fillApptTableView(Repository.getAppointmentsOfCustomer(RecordController.selectedCustomer));

                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null, rb.getString("delScriptError"));
                    Repository.updateRepoRecords();
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, rb.getString("selectApptToDel"));
        }
    }

    /** This method is used by other methods within the class.
     * This method will populate the table view with all Appointments passed in as a parameter.
     * @param apptList contains all Appointments to be displayed in the table view*/
    public void fillApptTableView (ObservableList<Appointment> apptList)
    {
        Repository.updateRepoRecords();
        custApptTableView.setItems(apptList);

        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("end"));
    }

    /** This method is called when the "Cancel" button is clicked.
     * This method sends the user to RecordMenu.fxml without Updating the Customer Record.
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionCancelButton(ActionEvent event) throws IOException {
        Stage stage = (Stage)(((Button)event.getSource()).getScene().getWindow());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/RecordMenu.fxml")),rb);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** This method is called when the "Delete Record" button is clicked.
     * This method confirms the users decision and DELETES the Customer Record from the database and Returns to the Record Menu.
     * <p>RUNTIME ERROR: Should the DELETE from the database fail, an error message will appear.</p>
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionRemoveButton(ActionEvent event) throws IOException {
        if (Repository.getAppointmentsOfCustomer(RecordController.selectedCustomer).isEmpty())
        {
            int userChoice = JOptionPane.showConfirmDialog(null,
                    rb.getString("confirmDelCustRec"), rb.getString("delCustRecTitle"), 0);
            if (userChoice == 0) {
                try {
                    // Attempt to delete the selected customer record
                    DBQuery.setPreparedStatement(JDBC.connection, "DELETE FROM customers WHERE Customer_ID = ?");
                    PreparedStatement deletionPS = DBQuery.getPreparedStatement();
                    deletionPS.setString(1, RecordController.selectedCustomer.getCustID().toString());
                    if (deletionPS.execute()) {
                        JOptionPane.showMessageDialog(null, rb.getString("delUnsuc"));
                    }
                    Repository.updateRepoRecords();

                    Stage stage = (Stage)(((Button)event.getSource()).getScene().getWindow());
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/RecordMenu.fxml")),rb);
                    stage.setScene(new Scene(root));
                    stage.show();

                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, rb.getString("delScriptError"));
                    Repository.updateRepoRecords();
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, rb.getString("delCustApptFirstError"));
        }
    }
}
