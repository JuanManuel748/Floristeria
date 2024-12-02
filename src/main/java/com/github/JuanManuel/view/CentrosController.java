package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.centroDAO;
import com.github.JuanManuel.model.entity.Centro;
import com.github.JuanManuel.model.entity.Producto;
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

/**
 * The CentrosController class manages the view for displaying and filtering "Centros" (centers).
 * It provides functionality for filtering by name or a range of values, displaying results in a grid layout,
 * and navigating to other scenes in the application.
 */
public class CentrosController extends Controller implements Initializable {

    // Grid layout for displaying the list of Centros
    @FXML
    public GridPane boxGrid;

    // Text field for searching Centros by name
    @FXML
    public TextField searchField;

    // Spinners for filtering Centros by a range of values
    @FXML
    public Spinner<Integer> minSpinner;
    @FXML
    public Spinner<Integer> maxSpinner;

    /**
     * Called when the controller is opened. Currently, no specific actions are performed.
     *
     * @param input Data passed to the controller (not used in this case).
     * @throws Exception if an error occurs during opening.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // No specific implementation
    }

    /**
     * Called when the controller is closed. Currently, no specific actions are performed.
     *
     * @param output Data passed when closing the controller (not used in this case).
     */
    @Override
    public void onClose(Object output) {
        // No specific implementation
    }

    /**
     * Initializes the controller when it is loaded by the JavaFX framework.
     * Sets up the spinners and populates the grid with an initial list of Centros.
     *
     * @param url Not used, typically contains the location of the FXML file.
     * @param resourceBundle Not used, typically contains resources for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the spinners with default values and listeners for filtering
        initializeSpinner(minSpinner, 0, 999, 0);
        initializeSpinner(maxSpinner, 1, 1000, 100);

        // Fetch and display an initial list of Centros based on spinner values
        List<Centro> pro = centroDAO.build().findByRange(minSpinner.getValue(), maxSpinner.getValue());
        mostrarProductos(pro);
    }

    /**
     * Configures a Spinner with a specified range and initial value.
     * Adds a listener to update the displayed Centros whenever the Spinner value changes.
     *
     * @param spinner The Spinner to initialize.
     * @param min The minimum value for the Spinner.
     * @param max The maximum value for the Spinner.
     * @param value The initial value for the Spinner.
     */
    private void initializeSpinner(Spinner<Integer> spinner, int min, int max, int value) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value);
        spinner.setValueFactory(valueFactory);

        // Add a listener to filter Centros whenever the Spinner value changes
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProductos();
        });
    }

    /**
     * Filters the displayed Centros based on the values in the Spinners.
     * Clears the search field and fetches the list of Centros within the specified range.
     */
    public void filtrarProductos() {
        searchField.clear(); // Clear the search field
        List<Centro> centros = centroDAO.build().findByRange(minSpinner.getValue(), maxSpinner.getValue());
        mostrarProductos(centros); // Display the filtered list of Centros
    }

    /**
     * Searches for Centros by name based on the value in the search field.
     *
     * @param mouseEvent The mouse event that triggered the search.
     */
    public void buscarProductos(MouseEvent mouseEvent) {
        String field = searchField.getText().trim(); // Get the search term
        List<Centro> centros = centroDAO.build().findByName(field); // Fetch Centros matching the name
        mostrarProductos(centros); // Display the search results
    }

    /**
     * Displays a list of Centros in the GridPane layout.
     * Each Centro is represented as a VBox loaded from an FXML template.
     *
     * @param centros The list of Centros to display.
     */
    public void mostrarProductos(List<Centro> centros) {
        boxGrid.getChildren().clear(); // Clear the existing grid content
        final int MAX_COLUMNS = 3; // Maximum number of columns in the grid
        int columns = 0; // Current column index
        int rows = 1; // Current row index

        try {
            for (Producto pr : centros) {
                // Load the FXML layout for a single Centro thumbnail
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("thumb.fxml"));
                VBox box = loader.load(); // Load the VBox layout
                VBox.setMargin(box, new Insets(20, 20, 20, 20)); // Add margin around each VBox

                // Configure the thumbnail controller with the Centro data
                ThumbController thumbController = loader.getController();
                thumbController.setProducto(pr);

                // Add the VBox to the grid
                boxGrid.add(box, columns++, rows);

                // Move to the next row if the maximum number of columns is reached
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
     * Navigates to the HOME scene when triggered.
     *
     * @param actionEvent The event that triggered the navigation.
     */
    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.HOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Navigates to the CART scene when triggered.
     *
     * @param actionEvent The event that triggered the navigation.
     */
    public void goToCart(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.CARRITO, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
