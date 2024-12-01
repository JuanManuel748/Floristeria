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


    @Override
    public void onOpen(Object input) throws Exception {
        mostrarDetalles();
    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateSelecter.setValue(LocalDate.now());
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        quantitySpinner.getValueFactory().setConverter(new IntegerStringConverter());
        detailsTable.setOnMouseClicked(event -> handleTableSelection(event));
    }

    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.HOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarDetalles() {
        List<Detalles> detaller = Session.getInstance().getDetalles();

        ObservableList<Detalles> detallesObservableList = FXCollections.observableArrayList(detaller);

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPro().getNombre()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        detailsTable.setItems(detallesObservableList);
        total = 0;
        List<Detalles> detallesList = Session.getInstance().getDetalles();
        for(Detalles d:detallesList) {
            total += d.getSubtotal();
        }

        detailsTable.setOnMouseClicked(event -> handleTableSelection(event));
        totalText.setText("TOTAL: " + total + "€");
    }

    private void setProducto(Detalles selected) {
        pro = selected.getPro();
        quantitySpinner.getValueFactory().setValue(selected.getCantidad());
    }

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

    public void purchase(ActionEvent actionEvent) {

        LocalDate now = LocalDate.now();
        LocalDate delivery = dateSelecter.getValue();
        if(delivery == null) {
            Alerta.showAlert("ERROR", "Error en la fecha de entrega","La fecha de entrega es nula");
            return;
        }
        if(now.isAfter(delivery)){
            Alerta.showAlert("ERROR", "Error en la fecha de entrega","La fecha de entrega no puede ser anterior a la fecha actual");
            return;
        }

        if (ChronoUnit.DAYS.between(now, delivery) < 2) {
            Alerta.showAlert("INFORMATION", "Fecha demasiado temprana","La fecha de entrega debe ser como mínimo 2 días después del día que se realiza el pedido");
            return;
        }

        String dateNow = now.getDayOfMonth()+"/"+now.getMonthValue()+"/"+now.getYear();
        String dateDelivery = delivery.getDayOfMonth()+"/"+delivery.getMonthValue()+"/"+delivery.getYear();
        Session.getInstance().madePed(dateNow, dateDelivery, total);
        Alerta.showAlert("INFORMATION", "Pedido realizado", "Pedido realizado con exito");
    }

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
