package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.entity.Detalles;
import com.github.JuanManuel.model.entity.Pedido;
import com.github.JuanManuel.model.entity.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private double total;


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
    }

    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.HOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarDetalles() {
        // Obtiene la lista de detalles desde la sesión
        List<Detalles> detaller = Session.getInstance().getDetalles();

        // Convierte la lista en una ObservableList para usarla en TableView
        ObservableList<Detalles> detallesObservableList = FXCollections.observableArrayList(detaller);

        // Configura las columnas de la tabla
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPro().getNombre()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Asigna la lista observable a la tabla
        detailsTable.setItems(detallesObservableList);
        total = 0;
        List<Detalles> detallesList = Session.getInstance().getDetalles();
        for(Detalles d:detallesList) {
            total += d.getSubtotal();
        }

        totalText.setText("TOTAL: " + total + "€");
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
}
