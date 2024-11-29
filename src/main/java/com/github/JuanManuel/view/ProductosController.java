package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.entity.Detalles;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.entity.Session;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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




    public void buscarProductos(MouseEvent mouseEvent) {
        String field = searchField.getText().trim();
        List<Producto> productos = productoDAO.build().findByComplName(field, "complemento", "flor");
        mostrarProductos(productos);

    }

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


    public void goToHome(ActionEvent actionEvent)     {
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

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

}
