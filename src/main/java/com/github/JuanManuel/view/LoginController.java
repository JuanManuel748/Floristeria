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

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button loginButton;
    @FXML
    private Button goToRegisterButton;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField passwordField;

    /**
     * Method called when the controller's view is opened.
     * Currently, this method has no implemented functionality.
     */
    @Override
    public void onOpen(Object input) throws Exception {}

    /**
     * Method called when the controller's view is closed.
     * Currently, this method has no implemented functionality.
     */
    @Override
    public void onClose(Object output) {}

    /**
     * Initializes the login view. Sets the stage dimensions and centers the window on the screen.
     * Logs a message if the stage is unavailable.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = (Stage) App.getPrimaryStage();
        if (stage != null) {
            stage.setWidth(820);
            stage.setHeight(640);
            stage.centerOnScreen();
        } else {
            System.out.println("Stage no disponible.");
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
            String phonenumber = phoneField.getText().trim();
            String hashedPassword = HashPass.hashPassword(passwordField.getText().trim());

            if (phonenumber.isEmpty() || hashedPassword.isEmpty()) {
                Alerta.showAlert("ERROR", "Campos vacíos", "El numero de telefono y la contraseña no pueden estar vacíos");
            } else {
                boolean logged = false;
                User currentUser = userDAO.build().findByPK(new User(phonenumber));

                if (currentUser != null) {
                    if (hashedPassword.equals(currentUser.getPassword())) {
                        Session.getInstance().logIn(currentUser);
                        Alerta.showAlert("INFORMATION", "", "Bienvenido, " + currentUser.getName());
                        if (currentUser.isAdmin()) {
                            App.currentController.changeScene(Scenes.ADMINHOME, null);
                        } else {
                            App.currentController.changeScene(Scenes.HOME, null);
                        }
                    } else {
                        Alerta.showAlert("ERROR", "Contraseña incorrecta", "La contraseña de esa cuenta no coincide con la contraseña introducida");
                    }
                } else {
                    Alerta.showAlert("ERROR", "Cuenta no encontrada", "La cuenta con ese numero de telefono no existe en nuestra base de datos");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            App.currentController.changeScene(Scenes.REGISTER, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
