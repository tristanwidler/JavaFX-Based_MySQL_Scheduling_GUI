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
import models.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**This class is the controller of RecordMenu.fxml.
 * The class displays a TableView and various buttons to modify and interact with the information inside the tables.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-13
 */
public class RecordController implements Initializable {

    /**The Customer Record table*/
    @FXML
    public TableView <Customer> recordTableView;
    /**The column of the recordTableView that displays the Customer_IDs of the Customers it contains.*/
    @FXML
    public TableColumn <Customer, Integer>custIDCol;
    /**The column of the recordTableView that displays the Customer_Names of the Customers it contains.*/
    @FXML
    public TableColumn <Customer, String>custNameCol;
    /**The column of the recordTableView that displays the Address of the Customers it contains.*/
    @FXML
    public TableColumn <Customer, String>addressCol;
    /**The column of the recordTableView that displays the Postal_Codes of the Customers it contains.*/
    @FXML
    public TableColumn <Customer, String>postalCodeCol;
    /**The column of the recordTableView that displays the Creation Date and Times of the Customers it contains.*/
    @FXML
    public TableColumn <Customer, String>createDateCol;
    /**The column of the recordTableView that displays the creators of the Customers it contains.*/
    @FXML
    public TableColumn <Customer, String>createByCol;
    /**The column of the recordTableView that displays the Date and Times of the Customers most recent update.*/
    @FXML
    public TableColumn <Customer, Timestamp>lastUpCol;
    /**The column of the recordTableView that displays the Users who most recently updated the Customers it contains.*/
    @FXML
    public TableColumn <Customer, String>lastUpByCol;
    /**The column of the recordTableView that displays the Division_IDs of the Customers it contains.*/
    @FXML
    public TableColumn <Customer, String>divIDCol;
    /**Used to select the Country to sort by*/
    @FXML
    public ComboBox countryComB;
    /**Used to select the First Level Division to sort by*/
    @FXML
    public ComboBox fLDComB;
    /**Used to navigate the program*/
    @FXML
    public ComboBox functionComB;
    /**Displays the number of Customers currently in the table*/
    @FXML
    public Label recordsReturnedLbl;
    /**The language translation resource bundle*/
    private ResourceBundle rb = ResourceBundle.getBundle("lang/Nat", Locale.getDefault());
    /**The current Appointment selected by the user*/
    public static Customer selectedCustomer;

    /**This method is called when the controller is loaded.
     * The methods is used to populate the function and country combination boxes, and fill the customer record table view.*/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functionComB.getItems().add(rb.getString("records"));
        functionComB.getItems().add(rb.getString("scheduling"));
        functionComB.getItems().add(rb.getString("reports"));
        functionComB.getItems().add(rb.getString("mainMenu"));

        Repository.updateRepoRecords();
        ObservableList <Country> countries = Repository.getAllCountries();

        //Populate the country combo box
        countryComB.getItems().add(rb.getString("noSelection"));
        for (int i = 0; i< countries.size(); i++)
        {
            countryComB.getItems().add(countries.get(i).getCountry());
        }

        try
        {
            fillRecordTableView(Repository.getAllCustomers());
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
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

    /** This method is called when the "Add" button is clicked.
     * This method sends the user to AddRecordMenu.fxml.
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionAddRecord(ActionEvent event) throws IOException {
        Stage stage = (Stage)(((Button)event.getSource()).getScene().getWindow());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AddRecordMenu.fxml")),rb);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** This method is called when the "Update" button is clicked.
     * This method sends the user to UpdateRecordMenu.fxml.
     * <p>RUNTIME ERROR: The method will display an error if the user has not selected a Customer Record from the tableview.</p>
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionUpdateRecord(ActionEvent event) throws IOException {
        if (recordTableView.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = recordTableView.getSelectionModel().getSelectedItem();

            Stage stage = (Stage) (((Button) event.getSource()).getScene().getWindow());
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/UpdateRecordMenu.fxml")),rb);
            stage.setScene(new Scene(root));
            stage.show();
        }
        else
        {
            JOptionPane.showMessageDialog(null, rb.getString("selectCustToUp"));
        }
    }

    /** This method is called when the "Delete" button is clicked.
     * This method will remove the selected Customer from the database and update the repository and tableview accordingly.
     * <p>RUNTIME ERROR: If there is no customer record selected, an error message will be displayed. <br>
     * If the record does not have any appointments, and there is an error while attempting to delete from the database, a message will be displayed. .</p>
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     * */
    public void onActionDeleteRecord(ActionEvent event) throws IOException {
        if (recordTableView.getSelectionModel().getSelectedItem() != null) {
            ObservableList<Appointment> selectedCustAppointments;
            selectedCustomer = recordTableView.getSelectionModel().getSelectedItem();
            selectedCustAppointments = Repository.getAppointmentsOfCustomer(recordTableView.getSelectionModel().getSelectedItem());

            if (selectedCustAppointments.isEmpty()) {
                int userChoice = JOptionPane.showConfirmDialog(null,
                        rb.getString("selectCustNoAssocAppt")+"\n"+ rb.getString("confirmDeleteCustRec"), rb.getString("deleteCustRecTitle"), 0);

                if (userChoice == 0) {
                    try {
                        // Attempt to delete the selected customer record
                        DBQuery.setPreparedStatement(JDBC.connection, "DELETE FROM customers WHERE Customer_ID = ?");
                        PreparedStatement deletionPS = DBQuery.getPreparedStatement();
                        deletionPS.setString(1, recordTableView.getSelectionModel().getSelectedItem().getCustID().toString());
                        if(deletionPS.execute())
                        {
                            JOptionPane.showMessageDialog(null, rb.getString("deleteUnsuc"));
                        }
                        Repository.updateRepoRecords();
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(null, rb.getString("deleteScriptFail"));
                        Repository.updateRepoRecords();
                    }
                }
            }
            else {
                Stage stage = (Stage) (((Button) event.getSource()).getScene().getWindow());
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/DeleteRecordMenu.fxml")),rb);
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, rb.getString("selectCustToDel"));
        }

    }

    /** This method is called when on option is selected from the Country combination box.
     * The method sorts the customer records in the table view based on the users selection.
     * The method also populates the First Level Division combo box based on the users Country selection.
     * @param event is not used
     */
    public void onActionCounComBUpdated(ActionEvent event) {
        if (countryComB.getValue() == rb.getString("noSelection"))
        {
            fillRecordTableView(Repository.getAllCustomers());
            fLDComB.setDisable(true);
            fLDComB.getItems().clear();
        }
        else {
            fLDComB.setDisable(false);
            fLDComB.getItems().clear();

            //Gathers selection from the country combo box
            String selectedCountryStr = countryComB.getSelectionModel().getSelectedItem().toString();

            //Gathers the country object from the repository based on the user selection
            Country selectedCountry = Repository.lookupCountry(selectedCountryStr);

            //Gathers all divisions of the selected country
            ObservableList<FirstLvlDiv> selectedCountryDivs = Repository.lookupFLDOfCountry(selectedCountry.getCountryID());

            //Fills tableView with all customers in selected country
            fillRecordTableView(sortCustByCoun(selectedCountry.getCountryID()));

            //Fills the division combo box for further filtering if desired
            for (int i = 0; i < selectedCountryDivs.size(); i++) {
                fLDComB.getItems().add(selectedCountryDivs.get(i).getDiv());
            }
        }

    }

    /** This method is called when on option is selected from the First Level Division combination box.
     * The method sorts the customer records in the table view based on the users selection.
     * @param event is not used
     */
    public void onActionFLDComBUpdated(ActionEvent event) {
        if (fLDComB.getValue() != null) {
            String divisionName = fLDComB.getValue().toString();
            FirstLvlDiv temp = Repository.lookupFLDByName(divisionName);
            fillRecordTableView(sortCustByDiv(temp.getDivID()));
        }
    }

    /** This method is used by other methods within the class.
     * This method will populate the table view with all Customers passed in as a parameter.
     * @param custList contains all Customers to be displayed in the table view*/
    public void fillRecordTableView (ObservableList<Customer> custList)
    {
        recordsReturnedLbl.setText(rb.getString("custRecordsReturn") + custList.size());

        recordTableView.setItems(custList);

        custIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDateTime"));
        createByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdateBy"));
        divIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
    }

    /** This method is used by other methods within the class.
     * This method will search through all Customer records to find those that match the passed in division_id.
     * @param divisionID contains all Customers to be displayed in the table view
     * @return the list of Customers matching the passed in division_id
     * */
    public ObservableList<Customer> sortCustByDiv (String divisionID)
    {
        ObservableList<Customer> sortedCustomers = FXCollections.observableArrayList();

        for (Customer temp : Repository.getAllCustomers())
        {
            if (temp.getDivisionID().equals(divisionID))
            {
                sortedCustomers.add(temp);
            }
        }

        return sortedCustomers;
    }

    /** This method is used by other methods within the class.
     * This method will search through all Customer records to find those that match the passed in country_id.
     * @param countryID contains all Customers to be displayed in the table view
     * @return the list of Customers matching the passed in country_id
     * */
    public ObservableList<Customer> sortCustByCoun (String countryID)
    {
        ObservableList<Customer> customersOfCountry = FXCollections.observableArrayList();

        customersOfCountry = Repository.customersInCountry(Repository.lookupFLDOfCountry(countryID));

        return customersOfCountry;
    }
}
