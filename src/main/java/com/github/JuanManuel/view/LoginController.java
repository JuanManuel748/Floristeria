package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.userDAO;
import com.github.JuanManuel.model.entity.Session;
import com.github.JuanManuel.model.entity.User;
import com.github.JuanManuel.model.utils.HashPass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The LoginController class manages the login functionality for the application.
 * It initializes the login view, handles user interactions like logging in and
 * navigating to the registration screen, and interacts with the DAO layer to verify credentials.
 */
public class LoginController extends Controller implements Initializable {

    // UI components
    @FXML
    private AnchorPane anchorPane; // Root container for the login view
    @FXML
    private Button loginButton; // Button to initiate login
    @FXML
    private Button goToRegisterButton; // Button to navigate to the registration screen
    @FXML
    private TextField phoneField; // Input field for the user's phone number
    @FXML
    private TextField passwordField; // Input field for the user's password

    /**
     * Method called when the controller's view is opened.
     * Currently, this method has no implemented functionality.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // No specific functionality for now
    }

    /**
     * Method called when the controller's view is closed.
     * Currently, this method has no implemented functionality.
     */
    @Override
    public void onClose(Object output) {
        // No specific functionality for now
    }

    /**
     * Initializes the login view. Sets the stage dimensions and centers the window on the screen.
     * Logs a message if the stage is unavailable.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = (Stage) App.getPrimaryStage();
        if (stage != null) {
            stage.setWidth(820); // Set window width
            stage.setHeight(640); // Set window height
            stage.centerOnScreen(); // Center the window on the screen
        } else {
            System.out.println("Stage no disponible."); // Log error if stage is null
        }
    }

    /**
     * Handles the login action. Validates the user's phone number and password,
     * checks the credentials against the database, and manages the user session.
     *
     * @param actionEvent the event triggered by pressing the login button.
     */
    @FXML
    public void onLogin(ActionEvent actionEvent) {
        try {
            String phonenumber = phoneField.getText().trim(); // Get phone number input
            String hashedPassword = HashPass.hashPassword(passwordField.getText().trim()); // Hash the entered password

            // Validate input fields
            if (phonenumber.isEmpty() || hashedPassword.isEmpty()) {
                Alerta.showAlert("ERROR", "Campos vacíos", "El numero de telefono y la contraseña no pueden estar vacíos");
            } else {
                // Initialize login status
                boolean logged = false;

                // Retrieve user data from the database
                User currentUser = userDAO.build().findByPK(new User(phonenumber));

                if (currentUser != null) {
                    // Check if the hashed password matches the stored password
                    if (hashedPassword.equals(currentUser.getPassword())) {
                        // Log in the user and show a welcome message
                        Session.getInstance().logIn(currentUser);
                        Alerta.showAlert("INFORMATION", "", "Bienvenido, " + currentUser.getName());

                        // Navigate to the appropriate home screen based on user role
                        if (currentUser.isAdmin()) {
                            App.currentController.changeScene(Scenes.ADMINHOME, null);
                        } else {
                            App.currentController.changeScene(Scenes.HOME, null);
                        }
                    } else {
                        // Handle incorrect password
                        Alerta.showAlert("ERROR", "Contraseña incorrecta", "La contraseña de esa cuenta no coincide con la contraseña introducida");
                    }
                } else {
                    // Handle account not found
                    Alerta.showAlert("ERROR", "Cuenta no encontrada", "La cuenta con ese numero de telefono no existe en nuestra base de datos");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Show alert for unexpected errors
            Alerta.showAlert("ERROR", "ERROR AL LOGEAR", "Hubo un error al encontrar tu cuenta");
        }
    }

    /**
     * Handles the action to navigate to the registration screen.
     *
     * @param actionEvent the event triggered by pressing the "Go to Register" button.
     */
    public void goToRegister(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.REGISTER, null); // Change scene to registration view
        } catch (Exception e) {
            throw new RuntimeException(e); // Handle exceptions by throwing a runtime exception
        }
    }
}
