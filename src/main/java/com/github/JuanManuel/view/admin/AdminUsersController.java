package com.github.JuanManuel.view.admin;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.userDAO;
import com.github.JuanManuel.model.entity.Session;
import com.github.JuanManuel.model.entity.User;
import com.github.JuanManuel.model.utils.HashPass;
import com.github.JuanManuel.view.Alerta;
import com.github.JuanManuel.view.Controller;
import com.github.JuanManuel.view.Scenes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminUsersController extends Controller implements Initializable {
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> phoneColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, Boolean> adminColumn;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private CheckBox adminCheck;
    @FXML
    private Button insertButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button findButton;

    /**
     * Inicializa el controlador configurando las columnas de la tabla,
     * recuperando la lista de usuarios desde la base de datos, y definiendo
     * eventos para la selección de filas en la tabla.
     *
     * @param url URL de ubicación usada para resolver recursos relacionados.
     * @param resourceBundle Conjunto de recursos utilizados por la vista.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        adminColumn.setCellValueFactory(cellData -> cellData.getValue().adminProperty());
        adminColumn.setCellFactory(tc -> new CheckBoxTableCell<>());

        List<User> usuarios = userDAO.build().findAll();
        mostrarUsuarios(usuarios);
        usersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                User selectedUser = usersTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    setUser(selectedUser);
                }
            }
        });
    }

    /**
     * Configura los campos del formulario con los datos del usuario seleccionado.
     *
     * @param u Usuario a configurar en el formulario.
     */
    public void setUser(User u) {
        phoneField.setText(u.getPhone());
        nameField.setText(u.getName());
        passwordField.setText(u.getPassword());
        adminCheck.setSelected(u.isAdmin());
    }

    /**
     * Muestra una lista de usuarios en la tabla.
     *
     * @param users Lista de usuarios a mostrar.
     */
    public void mostrarUsuarios(List<User> users) {
        ObservableList<User> usersObservableList = FXCollections.observableArrayList(users);
        usersTable.setItems(usersObservableList);
        usersTable.refresh();
    }

    /**
     * Cambia la escena a la pantalla principal del administrador.
     *
     * @param actionEvent el evento de acción disparado al llamar a este método.
     */
    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINHOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cierra la sesión del usuario actual y redirige a la pantalla de inicio de sesión.
     *
     * @param actionEvent el evento de acción disparado al llamar a este método.
     */
    public void logout(ActionEvent actionEvent) {
        try {
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Método ejecutado al abrir la vista o inicializarla.
     * Puede recibir datos de entrada para configurar la vista.
     *
     * @param input un objeto con los datos de entrada para inicializar la vista.
     * @throws Exception si ocurre algún error durante la inicialización.
     */
    @Override
    public void onOpen(Object input) throws Exception {

    }

    /**
     * Método ejecutado al cerrar la vista.
     * Puede manejar la salida o datos generados antes de cerrar.
     *
     * @param output un objeto con los datos de salida o resultado de la vista.
     */
    @Override
    public void onClose(Object output) {

    }

    /**
     * Valida los campos del formulario para asegurarse de que los datos son correctos.
     *
     * @return true si los campos son válidos, false en caso contrario.
     */
    public boolean validarFields() {
        boolean result = false;
        if (phoneField.getText().trim().equals("")) {
            Alerta.showAlert("ERROR", "Telefono vacío", "Tienes que introducir un numero de telefono");
            return result;
        }
        if (nameField.getText().trim().equals("")) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("CONFIRMATION");
            alerta.setHeaderText("Nombre vacío");
            alerta.setContentText("No has puesto un nombre.\n¿Estas seguro?");
            Optional<ButtonType> result1 = alerta.showAndWait();

            if (result1.isPresent() && result1.get() == ButtonType.CANCEL) {
                return result;
            }
        }

        if (passwordField.getText().trim().equals("")) {
            Alert alerta2 = new Alert(Alert.AlertType.CONFIRMATION);
            alerta2.setTitle("CONFIRMATION");
            alerta2.setHeaderText("Contraseña vacía");
            alerta2.setContentText("No has puesto una contraseña, todo el mundo podrá acceder a tu cuenta.\n¿Estas seguro?");
            Optional<ButtonType> result2 = alerta2.showAndWait();

            if (result2.isPresent() && result2.get() == ButtonType.CANCEL) {
                return result;
            }
        }
        User u = userDAO.build().findByPK(new User(phoneField.getText().trim()));
        if (u != null) {
            Alerta.showAlert("ERROR", "Telefono en uso", "Ya existe un usuario usando ese numero de telefono");
            return result;
        }
        result = true;
        return result;
    }

    /**
     * Inserta un nuevo usuario en la base de datos y actualiza la tabla.
     *
     * @param actionEvent Evento que dispara la acción.
     */
    public void insert(ActionEvent actionEvent) {
        try {
            if (!validarFields()) {
                return;
            }
            User u = new User();
            String telefono = phoneField.getText().trim();
            String nombre = nameField.getText();
            String password = HashPass.hashPassword(passwordField.getText().trim());
            Boolean isAdmin = adminCheck.isSelected();
            u = new User(telefono, nombre, password, isAdmin);
            userDAO.build().insertUser(u);

            Alerta.showAlert("INFORMATION", "Usuario insertado", "El usuario ha sido insertado en la base de datos exitosamente");


            List<User> usuarios = userDAO.build().findAll();
            mostrarUsuarios(usuarios);

            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Usuario NO insertado", "El usuario NO ha sido insertado en la base de datos exitosamente.\nRevisa que el numero de telefono no este en uso");
            throw new RuntimeException(e);
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    public void clearFields() {
        phoneField.setText("");
        nameField.setText("");
        passwordField.setText("");
        adminCheck.setSelected(false);
    }

    /**
     * Actualiza un usuario existente en la base de datos.
     *
     * @param actionEvent Evento que dispara la acción.
     */
    public void update(ActionEvent actionEvent) {
        try {
            String telefono = phoneField.getText().trim();
            String nombre = nameField.getText();
            if (passwordField.getText().trim().equals("")) {
                Alert alerta2 = new Alert(Alert.AlertType.CONFIRMATION);
                alerta2.setTitle("CONFIRMATION");
                alerta2.setHeaderText("Contraseña vacía");
                alerta2.setContentText("No has puesto una contraseña, todo el mundo podrá acceder a tu cuenta.\n¿Estas seguro?");
                Optional<ButtonType> result2 = alerta2.showAndWait();

                if (result2.isPresent() && result2.get() == ButtonType.CANCEL) {
                    return;
                }
            }
            String password = HashPass.hashPassword(passwordField.getText().trim());
            Boolean isAdmin = adminCheck.isSelected();

            User u = new User(telefono, nombre, password, isAdmin);
            userDAO.build().updateUser(u);

            Alerta.showAlert("INFORMATION", "Usuario actualizado", "El usuario ha sido actualizado en la base de datos exitosamente");

            List<User> usuarios = userDAO.build().findAll();
            mostrarUsuarios(usuarios);

            clearFields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param actionEvent Evento que dispara la acción.
     */
    public void delete(ActionEvent actionEvent) {
        try {
            User u = new User();
            String telefono = phoneField.getText().trim();
            String nombre = nameField.getText();
            String password = HashPass.hashPassword(passwordField.getText().trim());
            Boolean isAdmin = adminCheck.isSelected();
            u = new User(telefono, nombre, password, isAdmin);
            User usr = userDAO.build().delete(u);
            if (usr == null) {
                Alerta.showAlert("ERROR", "Usuario no encontrado", "El usuario con ese numero de telefono no existe en nuestra base de datos");
            }
            Alerta.showAlert("INFORMATION", "Usuario eliminado", "El usuario ha sido eliminado de la base de datos exitosamente");

            List<User> usuarios = userDAO.build().findAll();
            mostrarUsuarios(usuarios);

            clearFields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Busca usuarios en la base de datos según el criterio seleccionado.
     *
     * @param actionEvent Evento que dispara la acción.
     */
    public void find(ActionEvent actionEvent) {
        try {
            List<User> usrList = new ArrayList<>();

            switch (selectAlert()) {
                case 1:
                    usrList.add(userDAO.build().findByPK(new User(phoneField.getText().trim())));
                    break;
                case 2:
                    usrList = userDAO.build().findByName(nameField.getText());
                    break;
                case 3:
                    usrList = userDAO.build().findAdmins(adminCheck.isSelected());
                    break;
                case 4:
                    usrList = userDAO.build().findAll();
                    break;
                default:
                    return;
            }
            mostrarUsuarios(usrList);

            clearFields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Muestra un cuadro de diálogo para seleccionar el criterio de búsqueda.
     *
     * @return Código del criterio seleccionado: 1 para teléfono, 2 para nombre, 3 para admin, 4 para todos, 0 para cancelar.
     */
    private int selectAlert() {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Elegir campo");
        al.setHeaderText("Elige el campo por el que quieres buscar");
        ButtonType phone = new ButtonType("Telefono");
        ButtonType name = new ButtonType("Nombre");
        ButtonType admin = new ButtonType("Admin");
        ButtonType all = new ButtonType("All");
        ButtonType cancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        al.getButtonTypes().setAll(phone, name, admin, all, cancel);

        Optional<ButtonType> result = al.showAndWait();
        if (result.get() == phone) {
            return 1;
        } else if (result.get() == name) {
            return 2;
        } else if (result.get() == admin) {
            return 3;
        } else if (result.get() == all) {
            return 4;
        } else {
            return 0;
        }

    }

}
