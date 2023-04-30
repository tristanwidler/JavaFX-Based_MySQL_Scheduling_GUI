package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**This class is the controller of AddRecordMenu.fxml.
 * The class displays various buttons and text boxes which allow a user to create a new Customer Record.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-13
 */
public class AddRecordController implements Initializable {

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
    /**The language translation resource bundle*/
    private ResourceBundle rb = ResourceBundle.getBundle("lang/Nat", Locale.getDefault());

    /**This method is called when the controller is loaded.
     * The method is used to populate the country and function combination boxes.*/
    public void initialize(URL url, ResourceBundle resourceBundle) {

        functionComB.getItems().add(rb.getString("records"));
        functionComB.getItems().add(rb.getString("scheduling"));
        functionComB.getItems().add(rb.getString("reports"));
        functionComB.getItems().add(rb.getString("mainMenu"));

        Repository.updateRepoRecords();
        ObservableList <Country> countries = Repository.getAllCountries();

        //Populate the country combo box
        for (int i = 0; i< countries.size(); i++)
        {
            countryComB.getItems().add(countries.get(i).getCountry());
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

    /** This method is called when on option is selected from the country combination box.
     * The method populates the First Level Division combination box based on the users selection.
     * @param event is not used
     */
    public void onActionCountryCombB(ActionEvent event) {
        fLDComB.setDisable(false);
        fLDComB.getItems().clear();
        addressTxt.setDisable(true);
        postalTxt.setDisable(true);

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

    /** This method is called when on option is selected from the FLD combination box.
     * The method enables the address and postal code text fields.
     * @param event is not used
     */
    public void onActionFLDComB(ActionEvent event) {
        addressTxt.setDisable(false);
        postalTxt.setDisable(false);
    }

    /** This method is called when the "Cancel" button is clicked.
     * This method sends the user to RecordMenu.fxml without adding the Customer Record.
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionCancelButton(ActionEvent event) throws IOException {
        Stage stage = (Stage)(((Button)event.getSource()).getScene().getWindow());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/RecordMenu.fxml")),rb);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** This method is called when the "Save" button is clicked.
     * This method checks that all necessary values have been entered by the user and that they are valid.
     * The method will return to the Record Menu after Inserting the Customer record into the database.
     * <p>RUNTIME ERROR: Should the Insert into the database fail, an error message will appear.</p>
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    public void onActionSaveButton(ActionEvent event) throws IOException {
        if (validInput()) {

            int userChoice =
                    JOptionPane.showConfirmDialog(null,
                            rb.getString("confirmAddCustRec")
                            ,rb.getString("confirmAddCustTitle"), 0);

            if (userChoice == 0) {
                try {
                    //Example SQL:
                    //INSERT INTO customers VALUES(1, 'Daddy Warbucks', '1919 Boardwalk', '01291', '869-908-1875', NOW(), 'script', NOW(), 'script', 29);
                    DBQuery.setPreparedStatement(JDBC.connection, "INSERT INTO customers VALUES(null, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)");
                    PreparedStatement ps = DBQuery.getPreparedStatement();

                    ps.setString(1, nameTxt.getText());
                    ps.setString(2, addressTxt.getText());
                    ps.setString(3, postalTxt.getText());
                    ps.setString(4, phoneTxt.getText());
                    ps.setString(5, Repository.getCurrentUsername());
                    ps.setString(6, Repository.getCurrentUsername());
                    ps.setString(7, Repository.lookupFLDByName(fLDComB.getValue().toString()).getDivID());

                    ps.execute();

                    Stage stage = (Stage) (((Button) event.getSource()).getScene().getWindow());
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/RecordMenu.fxml")),rb);
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null, rb.getString("insertFail"));
                }
            }
        }
    }

    /** This method is used by other methods within the class.
     * This method will check for blank fields and for input that can not be uploaded to the database.
     * <p>RUNTIME ERROR: Any invalid input will show the user an error message related to the issue.
     * @return the result of the logic and validity tests.</p>
     */
    public boolean validInput ()
    {
        Boolean isValid = true;

        if (nameTxt.getText().isBlank()) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("mustAddName"));
        }
        if (phoneTxt.getText().isBlank()) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("mustAddPhone"));
        }
        if (addressTxt.getText().isBlank()) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("mustAddAddress"));
        }
        if (postalTxt.getText().isBlank()) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("mustAddPostal"));
        }
        if (countryComB.getValue() == null) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("mustSelectCountry"));
        }
        if (fLDComB.getValue() == null) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("mustSelectDivision"));
        }

        if (nameTxt.getText().length() > 50) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("nameCharLimit"));
        }
        if (phoneTxt.getText().length() > 50) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("phoneCharLimit"));
        }
        if (addressTxt.getText().length() > 100) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("addressCharLimit"));
        }
        if (postalTxt.getText().length() > 50) {
            isValid = false;
            JOptionPane.showMessageDialog(null, rb.getString("postalCharLimit"));
        }

        return isValid;
    }
}
