package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/** Main is a subclass of Application and prepares the program to display information in a Main Menu GUI.
 *
 * <p>+++JAVADOC LOCATION*** ...\Widler_Tristan_C195_Submission\Javadoc</p>
 * <p>+++MySQL Connector 8.0.27 LOCATION*** ...\Widler_Tristan_C195_Submission\mysql-connector-java-8.0.27</p>
 *
 * @author Tristan Widler, Stu ID(001213512)
 * @version 1.0
 * @since 2021-12-14
 */
public class Main extends Application {

    /**The language translation resource bundle*/
    private ResourceBundle rb = ResourceBundle.getBundle("lang/Nat", Locale.getDefault());

    /** This method loads and displays the LogInMenu.fxml file and default Style Sheet.
     * @param primaryStage default stage
     * @throws IOException required by the FXMLLoader.load() function
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/LogInMenu.fxml"), rb);
        primaryStage.setTitle(rb.getString("programTitle"));
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("/views/MainStyleSheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Runs at the start of the program.
     * @param args part of the default main()
     */
    public static void main(String[] args) {
        launch(args);
    }
}
