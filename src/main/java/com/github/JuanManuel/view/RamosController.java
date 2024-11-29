package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.DAO.ramoDAO;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.entity.Ramo;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RamosController extends Controller implements Initializable {
    @FXML
    public GridPane boxGrid;
    @FXML
    public TextField searchField;
    @FXML
    public Spinner<Integer> minSpinner;
    @FXML
    public Spinner<Integer> maxSpinner;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Ramo> pro = ramoDAO.build().findByName("");
        mostrarProductos(pro);
        initializeSpinner(minSpinner, 0, 999, 0);
        initializeSpinner(maxSpinner, 1, 1000, 50);


    }

    private void initializeSpinner(Spinner<Integer> Spinner, int min, int max, int value) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value);
        Spinner.setValueFactory(valueFactory);
        Spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProductos();
        });
    }

    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.HOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void goToCart(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.CARRITO, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void buscarProductos(MouseEvent mouseEvent) {
        String field = searchField.getText().trim();
        List<Ramo> ramos = ramoDAO.build().findByName(field);
        mostrarProductos(ramos);
    }

    public void mostrarProductos(List<Ramo> productos) {
        boxGrid.getChildren().clear();
        final int MAX_COLUMNS = 3;
        int columns = 0;
        int rows = 1;

        try {
            for (Producto pr : productos) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("thumb.fxml"));
                VBox box = loader.load();
                VBox.setMargin(box, new Insets(20, 20, 20, 20));

                ThumbController thumbController = loader.getController();
                thumbController.setProducto(pr);

                boxGrid.add(box, columns++, rows);

                if (columns == MAX_COLUMNS) {
                    columns = 0;
                    rows++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar el diseño de miniaturas", e);
        }
    }

    public void filtrarProductos() {
        searchField.clear();
        List<Ramo> ramos = new ArrayList<>();
        int min = minSpinner.getValue();
        int max = maxSpinner.getValue();
        ramos = ramoDAO.build().findByRange(min, max);
        mostrarProductos(ramos);

    }
}
