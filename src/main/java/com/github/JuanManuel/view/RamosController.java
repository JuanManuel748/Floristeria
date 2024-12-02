package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.ramoDAO;
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

public class RamosController extends Controller implements Initializable {
    @FXML
    public GridPane boxGrid;
    @FXML
    public TextField searchField;
    @FXML
    public Spinner<Integer> minSpinner;
    @FXML
    public Spinner<Integer> maxSpinner;

    /**
     * Initializes the controller. Sets up the product grid, spinners, and initial data.
     *
     * @param url the URL used to load the FXML file.
     * @param resourceBundle the resource bundle used for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Ramo> pro = ramoDAO.build().findByName("");
        mostrarProductos(pro);


        initializeSpinner(minSpinner, 0, 999, 0);
        initializeSpinner(maxSpinner, 1, 1000, 50);
    }

    /**
     * Initializes a spinner with specified minimum, maximum, and default values.
     * Sets an event listener to filter products when the spinner value changes.
     *
     * @param Spinner the spinner to initialize.
     * @param min the minimum value for the spinner.
     * @param max the maximum value for the spinner.
     * @param value the default value for the spinner.
     */
    private void initializeSpinner(Spinner<Integer> Spinner, int min, int max, int value) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value);
        Spinner.setValueFactory(valueFactory);
        Spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProductos();
        });
    }

    /**
     * Searches for products when the user enters text in the search field.
     * The search is based on the product name.
     *
     * @param mouseEvent the event triggered when the user clicks in the search field.
     */
    public void buscarProductos(MouseEvent mouseEvent) {
        String field = searchField.getText().trim();
        List<Ramo> ramos = ramoDAO.build().findByName(field);
        mostrarProductos(ramos);
    }

    /**
     * Displays a list of products in the grid.
     *
     * @param productos the list of products to display.
     */
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

    /**
     * Filters products based on the selected price range in the spinners.
     * Clears the search field and retrieves products within the specified price range.
     */
    public void filtrarProductos() {
        searchField.clear();
        List<Ramo> ramos = new ArrayList<>();


        int min = minSpinner.getValue();
        int max = maxSpinner.getValue();


        ramos = ramoDAO.build().findByRange(min, max);
        mostrarProductos(ramos);
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
     * Navigates to the shopping cart screen.
     *
     * @param actionEvent the event triggered when the user clicks the "Cart" button.
     */
    public void goToCart(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.CARRITO, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Initializes the view by setting up the necessary variables and configurations.
     *
     * @param input the input object passed when opening the view.
     */
    @Override
    public void onOpen(Object input) throws Exception {}

    /**
     * Closes the view by setting up the necessary variables and configurations.
     *
     * @param output the output object passed when opening other view.
     */
    @Override
    public void onClose(Object output) {
    }

}
