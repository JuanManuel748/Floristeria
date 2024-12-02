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
    @FXML
    public Button purchaseButton;
    @FXML
    public Label totalText;
    @FXML
    public DatePicker dateSelecter;
    @FXML
    public TableView detailsTable;
    @FXML
    public TableColumn<Detalles, String> nameColumn;
    @FXML
    public TableColumn<Detalles, Integer> quantityColumn;
    @FXML
    public TableColumn<Detalles, Double> subtotalColumn;
    @FXML
    public Button productButton;
    @FXML
    public Spinner quantitySpinner;

    private double total;
    private Producto pro;

    /**
     * Initializes the view by setting up the necessary variables and configurations.
     *
     * @param input the input object passed when opening the view.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        mostrarDetalles();
    }

    /**
     * Closes the view by setting up the necessary variables and configurations.
     *
     * @param output the output object passed when opening other view.
     */
    @Override
    public void onClose(Object output) {}

    /**
     * Initializes the controller and sets up the DatePicker, Spinner, and event handlers.
     *
     * @param url the URL used to load the FXML file.
     * @param resourceBundle the resource bundle used for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateSelecter.setValue(LocalDate.now());
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        quantitySpinner.getValueFactory().setConverter(new IntegerStringConverter());
        quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateQuantity();
        });
        detailsTable.setOnMouseClicked(event -> handleTableSelection(event));
    }

    private void updateQuantity() {
        if (pro != null) {
            try {
                int cantidad = (int) quantitySpinner.getValue();
                Session.getInstance().changeQuantity(pro, cantidad);
                mostrarDetalles();
                detailsTable.refresh();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Navigates to the home screen.
     *
     * @param actionEvent the event triggered when the user clicks the "Home" button.
     */
    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.HOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the details of the products in the shopping cart.
     * It retrieves the cart details from the current session and updates the table and total.
     */
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
        detailsTable.refresh();
    }

    /**
     * Sets the selected product details in the quantity spinner.
     *
     * @param selected the selected Detalles object from the table.
     */
    private void setProducto(Detalles selected) {
        pro = selected.getPro();
        quantitySpinner.getValueFactory().setValue(selected.getCantidad());  // Establece la cantidad del producto
    }

    /**
     * Handles the table row selection event. If a product is double-clicked, it is selected and set in the quantity spinner.
     *
     * @param event the mouse event triggered when a table row is clicked.
     */
    private void handleTableSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Detalles selected = (Detalles) detailsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setProducto(selected);
            } else {
                Alerta.showAlert("ERROR", "Ningún producto seleccionado", "Haz clic en un producto para seleccionarlo.");
            }
        }
    }

    /**
     * Finalizes the purchase process. It checks the selected delivery date and confirms the order if valid.
     *
     * @param actionEvent the event triggered when the user clicks the "Purchase" button.
     */
    public void purchase(ActionEvent actionEvent) {
        LocalDate now = LocalDate.now();
        LocalDate delivery = dateSelecter.getValue();


        if (delivery == null) {
            Alerta.showAlert("ERROR", "Error en la fecha de entrega", "La fecha de entrega es nula");
            return;
        }
        if (now.isAfter(delivery)) {
            Alerta.showAlert("ERROR", "Error en la fecha de entrega", "La fecha de entrega no puede ser anterior a la fecha actual");
            return;
        }


        if (ChronoUnit.DAYS.between(now, delivery) < 2) {
            Alerta.showAlert("INFORMATION", "Fecha demasiado temprana", "La fecha de entrega debe ser como mínimo 2 días después del día que se realiza el pedido");
            return;
        }


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
        Session.getInstance().madePed(dateNow, dateDelivery, total);

        Alerta.showAlert("INFORMATION", "Pedido realizado", "Pedido realizado con éxito");
        try {
            App.currentController.changeScene(Scenes.HOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Navigates to the product details screen.
     *
     * @param actionEvent the event triggered when the user clicks the "See Product" button.
     */
    public void seeProduct(ActionEvent actionEvent) {
        try {
            if (pro != null) {
                pro = productoDAO.build().findByPK(pro);
                DetailsController.setProducto(pro);
                App.currentController.changeScene(Scenes.DETAILS, null);
            } else {
                Alerta.showAlert("INFORMATION", "Falta producto", "Debes seleccionar un producto de la tabla");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
