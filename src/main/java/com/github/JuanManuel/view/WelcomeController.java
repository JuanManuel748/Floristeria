package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.connection.H2Connection;
import com.github.JuanManuel.model.connection.MySQLConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class WelcomeController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button adminWayButton;
    @FXML
    public CheckBox connectionChoice;

    public static Connection mainCon = null;
    public static boolean isSQL = false;

    /**
     * Called when the view is opened. This method is intended for any setup or initialization
     * operations when the controller is first initialized. In this case, it's empty.
     *
     * @param input the input object passed when opening the view.
     */
    @Override
    public void onOpen(Object input) throws Exception {}

    /**
     * Called when the view is closed. This method handles any cleanup or state persistence
     * when the view is closed. In this case, it's empty.
     *
     * @param output the output object passed when opening another view.
     */
    @Override
    public void onClose(Object output) {}



    /**
     * This method is called when the "adminWayButton" is clicked. It navigates the user to
     * the login screen by changing the current scene to the login screen.
     *
     * @throws Exception if there is an issue while changing the scene.
     */
    public void setAdminWayButton() throws Exception {
        App.currentController.changeScene(Scenes.LOGIN, null);
    }

    /**
     * Initializes the controller. This method is part of the Initializable interface and
     * is called when the controller is initialized.
     *
     * @param location the location used to load the FXML file.
     * @param resources the resources used for localization.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainCon = H2Connection.getTEMPConnection();
        connectionChoice.setOnAction(actionEvent -> updateConnection());
    }

    private void updateConnection() {
        Boolean select = connectionChoice.isSelected();
        if (select) {
            mainCon = MySQLConnection.getConnection();
            isSQL = true;
        } else {
            mainCon = H2Connection.getTEMPConnection();
            isSQL = false;
        }
    }
}
