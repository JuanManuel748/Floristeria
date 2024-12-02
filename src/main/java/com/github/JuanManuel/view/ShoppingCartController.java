package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.entity.Detalles;
import com.github.JuanManuel.model.entity.Pedido;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.entity.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class ShoppingCartController extends Controller implements Initializable {

    // Elementos de la interfaz gráfica
    @FXML
    public Button purchaseButton;  // Botón para realizar la compra
    @FXML
    public Label totalText;  // Etiqueta que muestra el total de la compra
    @FXML
    public DatePicker dateSelecter;  // Selector de fecha para elegir la fecha de entrega
    @FXML
    public TableView detailsTable;  // Tabla que muestra los detalles de los productos en el carrito
    @FXML
    public TableColumn<Detalles, String> nameColumn;  // Columna para mostrar el nombre del producto
    @FXML
    public TableColumn<Detalles, Integer> quantityColumn;  // Columna para mostrar la cantidad de producto
    @FXML
    public TableColumn<Detalles, Double> subtotalColumn;  // Columna para mostrar el subtotal del producto
    @FXML
    public Button productButton;  // Botón para ver más detalles de un producto
    @FXML
    public Spinner quantitySpinner;  // Selector para modificar la cantidad de un producto

    private double total;  // Variable que almacena el total de la compra
    private Producto pro;  // Producto seleccionado

    // Método llamado cuando se abre la vista
    @Override
    public void onOpen(Object input) throws Exception {
        mostrarDetalles();  // Muestra los detalles del carrito de compras
    }

    // Método llamado al cerrar la vista (no se utiliza aquí)
    @Override
    public void onClose(Object output) {

    }

    // Método de inicialización de la vista
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateSelecter.setValue(LocalDate.now());  // Inicializa el selector de fecha con la fecha actual
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));  // Configura el spinner de cantidad
        quantitySpinner.getValueFactory().setConverter(new IntegerStringConverter());  // Convierte los valores del spinner en enteros
        detailsTable.setOnMouseClicked(event -> handleTableSelection(event));  // Maneja la selección de productos en la tabla
    }

    // Método para redirigir al usuario a la pantalla de inicio
    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.HOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);  // Maneja cualquier error al cambiar de escena
        }
    }

    // Método que muestra los detalles del carrito de compras en la tabla
    public void mostrarDetalles() {
        // Obtiene los detalles del carrito de compras desde la sesión actual
        List<Detalles> detaller = Session.getInstance().getDetalles();

        ObservableList<Detalles> detallesObservableList = FXCollections.observableArrayList(detaller);  // Convierte la lista a una lista observable

        // Configura las columnas de la tabla
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPro().getNombre()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        detailsTable.setItems(detallesObservableList);  // Asocia los datos a la tabla

        total = 0;  // Inicializa el total
        // Recorre los detalles del carrito y calcula el total
        List<Detalles> detallesList = Session.getInstance().getDetalles();
        for(Detalles d : detallesList) {
            total += d.getSubtotal();
        }

        totalText.setText("TOTAL: " + total + "€");  // Actualiza el total en la interfaz
    }

    // Establece el producto seleccionado en el spinner de cantidad
    private void setProducto(Detalles selected) {
        pro = selected.getPro();
        quantitySpinner.getValueFactory().setValue(selected.getCantidad());  // Establece la cantidad del producto
    }

    // Maneja la selección de productos en la tabla (doble clic)
    private void handleTableSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Detalles selected = (Detalles) detailsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setProducto(selected);  // Establece el producto seleccionado
            } else {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Haz clic en un producto para seleccionarlo.");
            }
        }
    }

    // Método que maneja la acción de realizar la compra
    public void purchase(ActionEvent actionEvent) {
        LocalDate now = LocalDate.now();  // Fecha actual
        LocalDate delivery = dateSelecter.getValue();  // Fecha seleccionada para la entrega

        // Verifica si la fecha de entrega es válida
        if (delivery == null) {
            Alerta.showAlert("ERROR", "Error en la fecha de entrega", "La fecha de entrega es nula");
            return;
        }
        if (now.isAfter(delivery)) {
            Alerta.showAlert("ERROR", "Error en la fecha de entrega", "La fecha de entrega no puede ser anterior a la fecha actual");
            return;
        }

        // Verifica que la fecha de entrega esté al menos 2 días después de la fecha actual
        if (ChronoUnit.DAYS.between(now, delivery) < 2) {
            Alerta.showAlert("INFORMATION", "Fecha demasiado temprana", "La fecha de entrega debe ser como mínimo 2 días después del día que se realiza el pedido");
            return;
        }

        // Registra el pedido en la sesión
        String dateNow ="";
        if (now.getDayOfMonth() < 10) {
            dateNow = "0" + now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear();
        } else {
            dateNow = now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear();
        }
        String dateDelivery = "";
        if (delivery.getDayOfMonth() < 10) {
            dateDelivery = "0" + delivery.getDayOfMonth() + "/" + delivery.getMonthValue() + "/" + delivery.getYear();
        } else {
            dateDelivery = delivery.getDayOfMonth() + "/" + delivery.getMonthValue() + "/" + delivery.getYear();
        }
        Session.getInstance().madePed(dateNow, dateDelivery, total);  // Realiza el pedido

        Alerta.showAlert("INFORMATION", "Pedido realizado", "Pedido realizado con éxito");
    }

    // Método que maneja la visualización de más detalles de un producto seleccionado
    public void seeProduct(ActionEvent actionEvent) {
        try {
            if (pro != null) {
                pro = productoDAO.build().findByPK(pro);  // Obtiene el producto completo desde la base de datos
                DetailsController.setProducto(pro);  // Establece el producto en el controlador de detalles
                App.currentController.changeScene(Scenes.DETAILS, null);  // Cambia a la pantalla de detalles del producto
            } else {
                Alerta.showAlert("INFORMATION", "Falta producto", "Debes seleccionar un producto de la tabla");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);  // Maneja cualquier error al ver los detalles del producto
        }
    }
}
