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
        ObservableList<User> userObservableList = FXCollections.observableArrayList(userDAO.build().findAll());
        usrChoice.setItems(userObservableList);

        pedsTable.setOnMouseClicked(event -> handleTableSelection(event));

        monthColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey()));
        numColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        inicializaComboBox(usrChoice, userList);
        mostrarPedidos(pedidoDAO.build().findAll());

    }

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void clearFields() {
        idField.setText(String.valueOf(Session.getInstance().searchID()));
        inicializaComboBox(usrChoice, userList);
        deliDateSelecter.setValue(LocalDate.now());
        purDateSelecter.setValue(LocalDate.now().plusDays(7));
        priceField.getValueFactory().setValue(0.00);
        estateChoice.setValue("PENDIENTE");
    }

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
                    AdminDetailsController.setPedido(p);
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


    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINHOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(ActionEvent actionEvent) {
        try {
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

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

    public void mostrarPedidos(List<Pedido> array) {
        ObservableList<Pedido> pedidosObservableList = FXCollections.observableArrayList(array);
        pedsTable.setItems(pedidosObservableList);
        pedsTable.refresh();

        Map<String, Integer> map = pedidoDAO.build().FindStatsMonth();
        ObservableList<Map.Entry<String, Integer>> statsObservableList = FXCollections.observableArrayList(map.entrySet());
        statsTable.setItems(statsObservableList);
        statsTable.refresh();
    }

}

