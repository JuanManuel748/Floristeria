package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.entity.Producto;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

public class ProductosController extends Controller implements Initializable {
    @FXML
    private Button cartButton;
    @FXML
    private Button exitButton;
    @FXML
    private ChoiceBox FilterBox;
    @FXML
    private GridPane boxGrid;
    @FXML
    private TextField searchField;

    /**
     * Initializes the controller by setting up filters and loading products.
     *
     * @param url the URL used to load the FXML file.
     * @param resourceBundle the resource bundle used for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (boxGrid == null) {
            throw new IllegalStateException("boxGrid no está inicializado. Verifica el archivo FXML.");
        }

        FilterBox.setItems(observableArrayList("Todos", "Complemento", "Flor"));
        FilterBox.setValue("Todos");
        FilterBox.setOnAction(this::filtrarProductos);
        List<Producto> productos = productoDAO.build().findByType("complemento");
        List<Producto> flores = productoDAO.build().findByType("flor");
        productos.addAll(flores);
        mostrarProductos(productos);
    }

    /**
     * Searches for products when the user enters text in the search field.
     *
     * @param mouseEvent the event triggered when the user clicks in the search field.
     */
    public void buscarProductos(MouseEvent mouseEvent) {
        String field = searchField.getText().trim();
        List<Producto> productos = productoDAO.build().findByComplName(field, "complemento", "flor");
        mostrarProductos(productos);
    }

    /**
     * Filters the products based on the selected type in the filter dropdown.
     *
     * @param event the event triggered when the value in the filter dropdown is changed.
     */
    private void filtrarProductos(Event event) {
        searchField.clear();
        List<Producto> productos = new ArrayList<>();
        String filtro = (String) FilterBox.getValue();

        if (filtro.equalsIgnoreCase("Todos")) {

            productos = productoDAO.build().findByType("complemento");
            List<Producto> flores = productoDAO.build().findByType("flor");
            productos.addAll(flores);
        } else {

            productos = productoDAO.build().findByType(filtro.toLowerCase());
        }
        mostrarProductos(productos);
    }

    /**
     * Displays the products in the GridPane.
     *
     * @param productos the list of products to be displayed.
     */
    public void mostrarProductos(List<Producto> productos) {
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
    public void onOpen(Object input) throws Exception {
    }

    /**
     * Closes the view by setting up the necessary variables and configurations.
     *
     * @param output the output object passed when opening other view.
     */
    @Override
    public void onClose(Object output) {
    }


}
