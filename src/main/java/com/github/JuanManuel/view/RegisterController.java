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

    // Elementos de la interfaz gráfica vinculados con el archivo FXML
    @FXML
    private AnchorPane anchorPane;  // Contenedor principal de la vista
    @FXML
    private Button registerButton;  // Botón para registrar el usuario
    @FXML
    private Button loginButton;  // Botón para ir a la pantalla de login
    @FXML
    private TextField nameField;  // Campo para ingresar el nombre del usuario
    @FXML
    private TextField phoneField;  // Campo para ingresar el teléfono del usuario
    @FXML
    private TextField passwordField;  // Campo para ingresar la contraseña
    @FXML
    private TextField repassField;  // Campo para repetir la contraseña

    // Método llamado al abrir la vista (no realiza ninguna acción en este caso)
    @Override
    public void onOpen(Object input) throws Exception {

    }

    // Método llamado al cerrar la vista (no realiza ninguna acción en este caso)
    @Override
    public void onClose(Object output) {

    }

    // Método de inicialización de la vista (no realiza ninguna acción adicional)
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Método que maneja el registro del usuario cuando se hace clic en el botón de registro
    @FXML
    public void register(ActionEvent actionEvent) {
        // Obtiene los datos ingresados por el usuario
        String phone = phoneField.getText().trim();
        String name = nameField.getText();
        String password = passwordField.getText().trim();
        String repass = repassField.getText().trim();

        // Valida los datos ingresados por el usuario
        if (!validate(phone, name, password, repass)) {
            return;  // Si la validación falla, termina la ejecución
        }

        // Verifica si ya existe un usuario con ese número de teléfono
        User temp = new User(phone);
        User exists = userDAO.build().findByPK(temp);
        if (exists != null) {
            // Si ya existe, muestra un mensaje de error
            Alerta.showAlert("ERROR", "Usuario existente", "Ya existe un usuario con ese numero de telefono");
            return;
        }

        // Si el usuario no existe, crea un nuevo usuario
        User u = new User(phone, name, HashPass.hashPassword(password));
        if (u != null) {
            // Muestra un mensaje de bienvenida al nuevo usuario
            Alerta.showAlert("INFORMATION","", "Bienvenido, " + u.getName());
        }

        // Guarda el nuevo usuario en la base de datos
        userDAO.build().save(u);

        // Cambia la vista a la pantalla de login
        try {
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);  // Maneja cualquier error al cambiar de escena
        }
    }

    // Método que redirige al usuario a la pantalla de login
    @FXML
    public void goToLogin(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.LOGIN, null);  // Cambia la escena a la pantalla de login
        } catch (Exception e) {
            throw new RuntimeException(e);  // Maneja cualquier error al cambiar de escena
        }
    }

    // Método que valida los datos ingresados por el usuario en el formulario
    private boolean validate(String phone, String name, String pass, String repass) {
        int numResult = 0;  // Contador para los resultados de validación
        boolean result = false;  // Variable que almacena el resultado final de la validación

        // Verifica que ninguno de los campos esté vacío
        if (phone.isEmpty() || name.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
            Alerta.showAlert("INFORMATION", "Rellene todos los campos", "Ha dejado algunos campos vacíos, rellenelos para poder registrarse");
            return false;  // Si hay campos vacíos, la validación falla
        } else {
            numResult++;  // Si todos los campos están llenos, incrementa el contador
        }

        // Verifica que el número de teléfono tenga el formato correcto (9 dígitos)
        String patron = "^\\d{9}$";  // Expresión regular para validar el formato del teléfono
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            numResult++;  // Si el teléfono es válido, incrementa el contador
        } else {
            // Si el formato del teléfono es incorrecto, muestra un mensaje de error
            Alerta.showAlert("ERROR", "Formato telefono incorrecto", "El telefono debe ser de 9 caracteres");
        }

        // Verifica que las contraseñas coincidan
        if(pass.equals(repass)) {
            numResult++;  // Si las contraseñas coinciden, incrementa el contador
        } else {
            // Si las contraseñas no coinciden, muestra un mensaje de error
            System.out.println("Conraseña: " + pass);
            System.out.println("Repass: " + repass);
            Alerta.showAlert("ERROR", "No coinciden las contraseñas", "Las contraseñas no coinciden");
        }

        // Si todas las validaciones son exitosas, devuelve true
        if (numResult == 3) {
            result = true;
        }
        return result;  // Devuelve el resultado final de la validación
    }
}
