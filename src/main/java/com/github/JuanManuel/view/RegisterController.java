package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.userDAO;
import com.github.JuanManuel.model.entity.User;
import com.github.JuanManuel.model.utils.HashPass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField repassField;


    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void register(ActionEvent actionEvent) {
        String phone = phoneField.getText().trim();
        String name = nameField.getText().trim();
        String password = passwordField.getText().trim();
        String repass = repassField.getText().trim();

        if (!validate(phone, name, password, repass)) {
            return;
        }
        User temp = new User(phone);
        User exists = userDAO.build().findByPK(temp);
        if (exists != null) {
            Alerta.showAlert("ERROR", "Usuario existente", "Ya existe un usuario con ese numero de telefono");
            return;
        }

        // TERMINAR
        User u = new User(phone, name, HashPass.hashPassword(password));
        if (u != null) {
            Alerta.showAlert("INFORMATION","", "Bienvenido, " + u.getName());
        }
        userDAO.build().save(u);
        try {
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }





    }

    @FXML
    public void goToLogin(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validate(String phone, String name, String pass, String repass) {
        int numResult = 0;
        boolean result = false;

        if (phone.isEmpty() || name.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
            Alerta.showAlert("INFORMATION", "Rellene todos los campos", "Ha dejado algunos campos vacíos, rellenelos para poder registrarse");
            return false;
        } else {
            numResult++;
        }

        String patron = "^\\d{9}$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            numResult++;
        } else {
            Alerta.showAlert("ERROR", "Formato telefono incorrecto", "El telefono debe ser de 9 caracteres");
        }

        if(pass.equals(repass)) {
            numResult++;
        } else {
            System.out.println("Conraseña: " + pass);
            System.out.println("Repass: " + repass);
            Alerta.showAlert("ERROR", "No coinciden las contraseñas", "Las contraseñas no coinciden");
        }





        if (numResult == 3) {
            result = true;
        }
        return result;
    }
}