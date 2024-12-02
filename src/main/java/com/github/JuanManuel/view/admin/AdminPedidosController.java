package com.github.JuanManuel.view.admin;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.florDAO;
import com.github.JuanManuel.model.DAO.pedidoDAO;
import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.DAO.userDAO;
import com.github.JuanManuel.model.entity.*;
import com.github.JuanManuel.view.Alerta;
import com.github.JuanManuel.view.Controller;
import com.github.JuanManuel.view.Scenes;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;

public class AdminPedidosController extends Controller implements Initializable {
    @FXML
    public TableView<Pedido> pedsTable;
    @FXML
    public TableColumn<Pedido, Integer> idColumn;
    @FXML
    public TableColumn<Pedido, String> purcDateColumn;
    @FXML
    public TableColumn<Pedido, String> deliDateColumn;
    @FXML
    public TableColumn<Pedido, Double> priceColumn;
    @FXML
    public TableColumn<Pedido, String> estaColumn;
    @FXML
    public TableColumn<Pedido, String> usrColumn;
    @FXML
    public TextField idField;
    @FXML
    public ComboBox<User> usrChoice;
    @FXML
    public DatePicker purDateSelecter;
    @FXML
    public DatePicker deliDateSelecter;
    @FXML
    public Spinner priceField;
    @FXML
    public ComboBox estateChoice;
    @FXML
    public Button insertButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button findButton;
    @FXML
    public TableView<Map.Entry<String, Integer>> statsTable;
    @FXML
    public TableColumn<Map.Entry<String, Integer>, String> monthColumn;
    @FXML
    public TableColumn<Map.Entry<String, Integer>, Integer> numColumn;


    private static Pedido p;
    private static Detalles d;
    private static List<User> userList = userDAO.build().findAll();

    /**
     * Inicializa las columnas y elementos de la interfaz de la vista de administración de pedidos.
     * Establece el comportamiento de la tabla, las listas desplegables, y los eventos de selección.
     *
     * @param url La URL de la vista FXML.
     * @param resourceBundle El recurso que contiene las configuraciones de idioma.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        p = new Pedido();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        purcDateColumn.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        deliDateColumn.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        estaColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        usrColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getName() + " - " + cellData.getValue().getUser().getPhone()));
        priceField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.00, Double.MAX_VALUE, 0.00, 0.10));
        priceField.getValueFactory().setConverter(new DoubleStringConverter());
        estateChoice.setItems(observableArrayList("PENDIENTE", "PAGADO", "ENTREGADO"));
        estateChoice.setValue("PENDIENTE");
        ObservableList<User> userObservableList = observableArrayList(userDAO.build().findAll());
        usrChoice.setItems(userObservableList);

        pedsTable.setOnMouseClicked(event -> handleTableSelection(event));

        monthColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey()));
        numColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        inicializaComboBox(usrChoice, userList);
        mostrarPedidos(pedidoDAO.build().findAll());

    }

    /**
     * Establece los valores del formulario de pedido basado en el pedido seleccionado.
     *
     * @param selected El pedido seleccionado.
     */
    private void setPedido(Pedido selected) {
        try {
            p = selected;
            idField.setText(String.valueOf(selected.getIdPedido()));
            usrChoice.setValue(selected.getUser());
            String purchaseDateST = selected.getFechaPedido();
            String deliDateST = selected.getFechaEntrega();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate purDate = LocalDate.parse(purchaseDateST, formatter);
            LocalDate deliDate = LocalDate.parse(deliDateST, formatter);
            deliDateSelecter.setValue(deliDate);
            purDateSelecter.setValue(purDate);
            priceField.getValueFactory().setValue(selected.getTotal());
            estateChoice.setValue(selected.getEstado());
            Session.getInstance().setPedido(selected);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Limpia los campos del formulario y establece valores predeterminados.
     */
    public void clearFields() {
        idField.setText(String.valueOf(Session.getInstance().searchID()));
        inicializaComboBox(usrChoice, userList);
        purDateSelecter.setValue(LocalDate.now());
        deliDateSelecter.setValue(LocalDate.now().plusDays(7));
        priceField.getValueFactory().setValue(0.00);
        estateChoice.setValue("PENDIENTE");
    }

    /**
     * Inicializa el ComboBox de usuarios con una lista de usuarios y establece su formato de visualización.
     *
     * @param c El ComboBox a inicializar.
     * @param list La lista de usuarios para llenar el ComboBox.
     */
    public void inicializaComboBox(ComboBox<User> c, List<User> list) {
        c.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                if (user != null) {
                    return user.getName() + " - " + user.getPhone();
                }
                return "";
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });

        c.getItems().setAll(list);
    }

    /**
     * Valida los campos del formulario de pedidos para asegurar que no haya datos vacíos o incorrectos.
     *
     * @return true si todos los campos son válidos, false en caso contrario.
     */
    public boolean validateFields() {
        boolean result = false;
        try {
            if (idField.getText().equals("")) {
                Alerta.showAlert("ERROR", "ID vacía", "La id esta vacía");
                return result;
            }
            int id = Integer.parseInt(idField.getText());
            if (usrChoice.getValue() == null) {
                Alerta.showAlert("ERROR", "Usuario vacío", "No has seleccionado un usuario");
                return result;
            }
            User u = usrChoice.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (deliDateSelecter.getValue().format(formatter).toString().equals("") || purDateSelecter.getValue().format(formatter).toString().equals("")) {
                Alerta.showAlert("ERROR", "Fecha vacía", "La fecha esta vacía");
                return result;
            }
            LocalDate deli = deliDateSelecter.getValue();
            LocalDate purch = purDateSelecter.getValue();
            if (ChronoUnit.DAYS.between(purch, deli) < 2) {
                Alerta.showAlert("ERROR", "Fecha inválida", "La fecha de entrega debe ser 2 dias despues de la fecha de pedido como mínimo ");
                return result;
            }
            String deliDate = deliDateSelecter.getValue().format(formatter).toString();
            String purDate = purDateSelecter.getValue().format(formatter).toString();
            Double price = (Double) priceField.getValue();
            if (price <= 0) {

                Alerta.showAlert("ERROR", "Precio nulo", "El precio no puede ser menor o igual a 0");
                return result;
            }
            String state = estateChoice.getValue().toString();
            if (state.equals("")) {
                Alerta.showAlert("ERROR", "Estado nulo", "El estado esta vacío");
                return result;
            }

            p.setIdPedido(id);
            p.setUser(u);
            p.setFechaPedido(purDate);
            p.setFechaEntrega(deliDate);
            p.setTotal(price);
            p.setEstado(state);
            result = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Inserta un nuevo pedido en la base de datos si los campos son válidos.
     *
     * @param actionEvent El evento de acción para el botón de insertar.
     */
    public void insert(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            pedidoDAO.build().insertPedido(p);
            Alerta.showAlert("INFORMATION", "Pedido insertado", "El pedido ha sido insertado en la base de datos exitosamente");
            mostrarPedidos(pedidoDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Pedido NO insertado", "Error al insertar el pedido.");
            e.printStackTrace();
        }
    }

    /**
     * Actualiza un pedido en la base de datos si los campos son válidos.
     *
     * @param actionEvent El evento de acción para el botón de actualizar.
     */
    public void update(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            pedidoDAO.build().updatePedido(p);
            Alerta.showAlert("INFORMATION", "Pedido actualizado", "El pedido ha sido actualizado en la base de datos exitosamente");
            mostrarPedidos(pedidoDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Pedido NO actualizado", "Error al actualizar el pedido.");
            e.printStackTrace();
        }
    }

    /**
     * Elimina un pedido de la base de datos y actualiza la tabla de pedidos.
     *
     * @param actionEvent El evento de acción para el botón de eliminar.
     */
    public void delete(ActionEvent actionEvent) {
        try {
            if (!validateFields()) {
                return;
            }
            pedidoDAO.build().delete(p);
            Alerta.showAlert("INFORMATION", "Pedido eliminado", "El pedido ha sido eliminado en la base de datos exitosamente");
            mostrarPedidos(pedidoDAO.build().findAll());
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Pedido NO eliminado", "Error al eliminar el pedido.");
            e.printStackTrace();
        }
    }

    /**
     * Realiza una búsqueda basada en el criterio seleccionado en un cuadro de diálogo.
     * Dependiendo del campo elegido, busca un pedido por ID, usuario, detalles, o devuelve todos los pedidos.
     * Los resultados se muestran mediante `mostrarPedidos`.
     *
     * @param actionEvent el evento de acción disparado al llamar a este método.
     */
    public void find(ActionEvent actionEvent) {
        try {
            List<Pedido> findList = new ArrayList<>();
            switch (selectAlert()) {
                case 1:
                    int id = Integer.parseInt(idField.getText().trim());
                    Pedido entity = pedidoDAO.build().findByPK(new Pedido(id));
                    if (entity != null) {
                        findList.add(entity);
                    }
                    break;
                case 2:
                    User u = new User();
                    u = usrChoice.getValue();
                    findList = pedidoDAO.build().findByUser(u);
                    break;
                case 3:
                    Session.getInstance().setPedido(p);
                    App.currentController.changeScene(Scenes.ADMINDETAILS, null);
                    break;
                case 4:
                    findList = pedidoDAO.build().findAll();
                    break;
                default:
                    return;
            }
            mostrarPedidos(findList);
            clearFields();
        } catch (Exception e) {
            Alerta.showAlert("ERROR", "Error de búsqueda", "Error al buscar productos.");
        }
    }

    /**
     * Muestra un cuadro de diálogo para seleccionar el criterio de búsqueda.
     * Las opciones incluyen buscar por ID, usuario, detalles, o todos los registros.
     *
     * @return un entero que representa la opción seleccionada:
     *         1 para ID, 2 para Usuario, 3 para Detalles, 4 para Todos, 0 para cancelar.
     */
    private int selectAlert() {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Elegir campo");
        al.setHeaderText("Elige el campo por el que quieres buscar");
        ButtonType phone = new ButtonType("ID");
        ButtonType name = new ButtonType("Usuario");
        ButtonType detalles = new ButtonType("Detalles");
        ButtonType all = new ButtonType("All");
        ButtonType cancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        al.getButtonTypes().setAll(phone, name, detalles, all, cancel);

        Optional<ButtonType> result = al.showAndWait();
        if (result.get() == phone) {
            return 1;
        } else if (result.get() == name) {
            return 2;
        } else if (result.get() == detalles) {
            return 3;
        } else if (result.get() == all) {
            return 4;
        }else {
            return 0;
        }

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
     * Maneja la selección de un pedido en la tabla.
     *
     * @param event El evento de clic en la tabla.
     */
    private void handleTableSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Pedido selected = pedsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setPedido(selected);
            } else {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Haz clic en un producto para seleccionarlo.");
            }
        }
    }

    /**
     * Muestra los pedidos en la tabla de la interfaz de usuario.
     *
     * @param array La lista de pedidos a mostrar.
     */
    public void mostrarPedidos(List<Pedido> array) {
        ObservableList<Pedido> pedidosObservableList = observableArrayList(array);
        pedsTable.setItems(pedidosObservableList);
        pedsTable.refresh();

        Map<String, Integer> map = pedidoDAO.build().FindStatsMonth();
        ObservableList<Map.Entry<String, Integer>> statsObservableList = observableArrayList(map.entrySet());
        statsTable.setItems(statsObservableList);
        statsTable.refresh();
    }

}

