package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.centroDAO;
import com.github.JuanManuel.model.DAO.ramoDAO;
import com.github.JuanManuel.model.entity.Centro;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.entity.Ramo;
import javafx.event.ActionEvent;
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

public class CentrosController extends Controller implements Initializable {
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
        initializeSpinner(minSpinner, 0, 999, 0);
        initializeSpinner(maxSpinner, 1, 1000, 100);
        List<Centro> pro = centroDAO.build().findByRange(minSpinner.getValue(), maxSpinner.getValue());
        mostrarProductos(pro);
    }

    private void initializeSpinner(Spinner<Integer> Spinner, int min, int max, int value) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value);
        Spinner.setValueFactory(valueFactory);
        Spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProductos();
        });
    }

    public void filtrarProductos() {
        searchField.clear();
        List<Centro> centros = new ArrayList<>();
        int min = minSpinner.getValue();
        int max = maxSpinner.getValue();
        centros = centroDAO.build().findByRange(min, max);
        mostrarProductos(centros);
    }

    public void buscarProductos(MouseEvent mouseEvent) {
        String field = searchField.getText().trim();
        List<Centro> centros = centroDAO.build().findByName(field);
        mostrarProductos(centros);
    }

    public void mostrarProductos(List<Centro> centros) {
        boxGrid.getChildren().clear();
        final int MAX_COLUMNS = 3;
        int columns = 0;
        int rows = 1;

        try {
            for (Producto pr : centros) {
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
}
