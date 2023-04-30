package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.*;
import java.io.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.*;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**This class is the controller of LogInMenu.fxml.
 * This class serves as a log-in form for the user.
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-13
 */
public class LogInController implements Initializable {
    /**The username of the user.*/
    public TextField usernameTxt;
    /**The password of the user.*/
    public PasswordField passTxt;
    /**The time zone of the users computer.*/
    public Label timeZoneLbl;
    /**The file used for Log-in records.*/
    private File file = new File("src/files/logInRecord.txt");
    /**Used to write information to a text file*/
    private PrintWriter pw;
    /**Used to write information to a text file*/
    private FileWriter fw;
    /**The language translation resource bundle*/
    private ResourceBundle rb = ResourceBundle.getBundle("lang/Nat", Locale.getDefault());

    /**This method is called when the Log-in menu opens.
     * The method is used to create a data logging file and initiate a connection to the SQL database.*/
    public void initialize (URL url, ResourceBundle resourceBundle)
    {
        try {
            //Creates a logInRecord.txt file if it doesn't already exist
            if (!file.exists())
            {
                file.createNewFile();
            }

            //Opens connection to the database
            JDBC.openConnection();

            //Gathers and sets Time Zone Information
            Repository.setUsersZoneID(ZoneId.systemDefault());
            ZoneId myZoneID = Repository.getUsersZoneID();
            timeZoneLbl.setText(myZoneID.toString());
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, rb.getString("initializationError"));
            System.exit(0);
        }
    }

    /**This method is called when the help button is clicked.
     * The method is used to provide IT contact information to the user.
     * @param event is not used*/
    public void onActionNeedHelpButton(ActionEvent event) {
        JOptionPane.showMessageDialog(null,rb.getString("logInHelpTxt"));
    }

    /**This method is called when the "Log-In" button is clicked.
     * This method checks the username and password combination entered by the user against the database.
     * <p>LAMBDA: This method incorporates two lambda expressions. <br>AppointmentInterface apptsInFifteen returns
     * any appointments within the next fifteen minutes. This was created using a lambda expression as the method is only
     * required once in the program and does not to be referenced more than once within this method.
     * <br>StringInterface structuredOutput returns a structured string of the appointments returned in apptInFifteen.
     * This was created using a lambda expression for the same reason as apptInFifteen.</p>
     * <p>RUNTIME ERROR: If the method can't query the database or if the password or username is incorrect, an error message will appear.</p>
     * @param event high-level event used to set the Stage
     * @throws IOException required by the FXMLLoader.load() and the prepared statements
     */
    public void onActionLogInButton(ActionEvent event) throws IOException {
        String enteredUsername, enteredPassword;
        ResultSet rs;
        LocalDateTime loginTimestamp = LocalDateTime.now(Repository.getUsersZoneID());
        fw = new FileWriter(file,true);
        pw = new PrintWriter(fw);

        try {
            DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM users WHERE User_Name = ?");
            PreparedStatement ps = DBQuery.getPreparedStatement();
            enteredUsername = usernameTxt.getText();
            enteredPassword = passTxt.getText();

            ps.setString(1, enteredUsername);
            ps.execute();
            rs = ps.getResultSet();

            //Entered if there is at least one username that matches the users entered value
            if (rs.next())
            {
                DBQuery.setPreparedStatement(JDBC.connection, "SELECT * FROM users WHERE User_Name = ? AND Password = ?");
                ps = DBQuery.getPreparedStatement();
                ps.setString(1,enteredUsername);
                ps.setString(2,enteredPassword);
                ps.execute();
                rs = ps.getResultSet();

                //Entered if there is at least one username and password record that matches the users entered values
                if (rs.next())
                {
                    //Sets values in repository for later use in the program
                    Repository.setCurrentUserID(rs.getInt(1));
                    Repository.setCurrentUsername(rs.getString("User_Name"));
                    Repository.updateRepoRecords();

                    //Lambda expression which returns any appointments within fifteen minutes of the current time.
                    AppointmentInterface apptsInFifteen=()->{
                        ObservableList<Appointment> apptsScheduledSoon = FXCollections.observableArrayList();
                        LocalDateTime currentDT = LocalDateTime.now();
                        String [] startDTSeparation;
                        String [] sDSeparation;
                        String [] sTSeparation;
                        LocalDate startLD;
                        LocalTime startLT;


                        for (Appointment temp: Repository.getAllAppointments())
                        {
                            startDTSeparation = temp.getStart().split("\\s");
                            sDSeparation = startDTSeparation[0].split("-");
                            sTSeparation = startDTSeparation[1].split(":");
                            startLD = LocalDate.of(Integer.parseInt(sDSeparation[0]), Integer.parseInt(sDSeparation[1]), Integer.parseInt(sDSeparation[2]));
                            startLT = LocalTime.of(Integer.parseInt(sTSeparation[0]), Integer.parseInt(sTSeparation[1]));

                            if (currentDT.toLocalDate().isEqual(startLD))
                            {
                                if(startLT.isAfter(currentDT.toLocalTime()) && startLT.minusMinutes(15).isBefore(currentDT.toLocalTime()))
                                {
                                    apptsScheduledSoon.add(temp);
                                }
                            }
                        }

                        return apptsScheduledSoon;
                    };

                    ObservableList<Appointment> nearbyAppts = apptsInFifteen.gatherAppts();

                    if (nearbyAppts.size() == 0)
                    {
                        JOptionPane.showMessageDialog(null, rb.getString("noApptsInFifteen"));
                    }
                    else
                    {
                        StringInterface structuredOutput= apptList ->{
                            String outputMessage = apptList.size() + " " + rb.getString("apptsInFifteenDetails") + "\n";
                            String [] startDTSeparation;
                            String [] sDSeparation;
                            String [] sTSeparation;
                            LocalDate startLD;
                            LocalTime startLT;

                            for(Appointment temp : apptList)
                            {
                                //Separate current appointment values and append appointment_id, date, and time to the output string
                                startDTSeparation = temp.getStart().split("\\s");
                                sDSeparation = startDTSeparation[0].split("-");
                                sTSeparation = startDTSeparation[1].split(":");
                                startLD = LocalDate.of(Integer.parseInt(sDSeparation[0]), Integer.parseInt(sDSeparation[1]), Integer.parseInt(sDSeparation[2]));
                                startLT = LocalTime.of(Integer.parseInt(sTSeparation[0]), Integer.parseInt(sTSeparation[1]));

                                outputMessage = outputMessage +
                                        rb.getString("apptID")+": " + temp.getApptID() + ", " +
                                        rb.getString("date")+": " + startLD.toString() + ", " +
                                        rb.getString("time")+": " + startLT.toString() + ".\n";

                            }
                            return outputMessage;
                        };

                        JOptionPane.showMessageDialog(null, structuredOutput.structureString(nearbyAppts));
                    }

                    pw.println("Successful/No_Issues" + "--" + loginTimestamp.toString() + "--" + "TimeZone:" + Repository.getUsersZoneID().toString());
                    pw.flush();
                    pw.close();
                    Stage stage = (Stage) (((Button) event.getSource()).getScene().getWindow());
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/MainMenu.fxml")), rb);
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                else
                {
                    pw.println("Unsuccessful/Incorrect_Password" + "--" + loginTimestamp.toString() + "--" + "TimeZone:" + Repository.getUsersZoneID().toString());
                    pw.flush();
                    pw.close();
                    JOptionPane.showMessageDialog(null, rb.getString("passIncorrectMsg"));
                }
            }
            else
            {
                pw.println("Unsuccessful/Incorrect_Username"+ "--" + loginTimestamp.toString() + "--" + "TimeZone:" + Repository.getUsersZoneID().toString());
                pw.flush();
                pw.close();
                JOptionPane.showMessageDialog(null, rb.getString("userIncorrectMsg"));

            }
        }
        catch (Exception e)
        {
            pw.println("Unsuccessful/Script_Execution_Error"+ "--" + loginTimestamp.toString() + "--" + "TimeZone:" + Repository.getUsersZoneID().toString());
            pw.flush();
            pw.close();
            JOptionPane.showMessageDialog(null, rb.getString("scriptExecError"));
        }

    }

    //Non-lambda/Interface based code for checking if there is an appointment within fifteen minutes.
    /*
    private ObservableList<Appointment> apptsWithinFifteenMin ()
    {
        ObservableList<Appointment> apptsScheduledSoon = FXCollections.observableArrayList();
        LocalDateTime currentDT = LocalDateTime.now();
        String [] startDTSeparation;
        String [] sDSeparation;
        String [] sTSeparation;
        LocalDate startLD;
        LocalTime startLT;


        for (Appointment temp: Repository.getAllAppointments())
        {
            startDTSeparation = temp.getStart().split("\\s");
            sDSeparation = startDTSeparation[0].split("-");
            sTSeparation = startDTSeparation[1].split(":");
            startLD = LocalDate.of(Integer.parseInt(sDSeparation[0]), Integer.parseInt(sDSeparation[1]), Integer.parseInt(sDSeparation[2]));
            startLT = LocalTime.of(Integer.parseInt(sTSeparation[0]), Integer.parseInt(sTSeparation[1]));

            if (currentDT.toLocalDate().isEqual(startLD))
            {
                if(startLT.isAfter(currentDT.toLocalTime()) && startLT.minusMinutes(15).isBefore(currentDT.toLocalTime()))
                {
                    apptsScheduledSoon.add(temp);
                }
            }
        }

        return apptsScheduledSoon;
    }

    private String outputStructuredApptMsg (ObservableList<Appointment> apptList)
    {
        String outputMessage = apptList.size() + " " + rb.getString("apptsInFifteenDetails") + "\n";
        String [] startDTSeparation;
        String [] sDSeparation;
        String [] sTSeparation;
        LocalDate startLD;
        LocalTime startLT;
        DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm");

        for(Appointment temp : apptList)
        {
            //Separate current appointment values and append appointment_id, date, and time to the output string
            startDTSeparation = temp.getStart().split("\\s");
            sDSeparation = startDTSeparation[0].split("-");
            sTSeparation = startDTSeparation[1].split(":");
            startLD = LocalDate.of(Integer.parseInt(sDSeparation[0]), Integer.parseInt(sDSeparation[1]), Integer.parseInt(sDSeparation[2]));
            startLT = LocalTime.of(Integer.parseInt(sTSeparation[0]), Integer.parseInt(sTSeparation[1]));

            outputMessage = outputMessage +
                    rb.getString("apptID")+": " + temp.getApptID() + ", " +
                    rb.getString("date")+": " + startLD.toString() + ", " +
                    rb.getString("time")+": " + startLT.toString() + ".\n";

        }
        return outputMessage;
    }

     */

}
