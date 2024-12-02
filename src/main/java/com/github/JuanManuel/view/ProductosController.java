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

    // Elementos de la interfaz gráfica vinculados con el FXML
    @FXML
    private Button cartButton;  // Botón para ir al carrito
    @FXML
    private Button exitButton;  // Botón para salir
    @FXML
    private ChoiceBox FilterBox;  // ChoiceBox para filtrar productos por tipo
    @FXML
    private GridPane boxGrid;  // Contenedor de productos en formato grid
    @FXML
    private TextField searchField;  // Campo de búsqueda de productos

    // Método de inicialización que se ejecuta al cargar la vista
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (boxGrid == null) {
            throw new IllegalStateException("boxGrid no está inicializado. Verifica el archivo FXML.");
        }

        // Se inicializa el filtro con opciones "Todos", "Complemento" y "Flor"
        FilterBox.setItems(observableArrayList("Todos", "Complemento", "Flor"));
        FilterBox.setValue("Todos");  // Establece el valor por defecto como "Todos"
        FilterBox.setOnAction(this::filtrarProductos);  // Asocia la acción para filtrar productos
        List<Producto> productos = productoDAO.build().findByType("complemento");  // Busca productos de tipo "complemento"
        List<Producto> flores = productoDAO.build().findByType("flor");  // Busca productos de tipo "flor"
        productos.addAll(flores);  // Combina ambos tipos de productos
        mostrarProductos(productos);  // Muestra los productos combinados en la interfaz
    }

    // Método que maneja la búsqueda de productos cuando se hace clic en el campo de búsqueda
    public void buscarProductos(MouseEvent mouseEvent) {
        String field = searchField.getText().trim();  // Obtiene el texto del campo de búsqueda
        List<Producto> productos = productoDAO.build().findByComplName(field, "complemento", "flor");  // Busca productos por nombre
        mostrarProductos(productos);  // Muestra los productos encontrados
    }

    // Método que filtra los productos según el tipo seleccionado en el ChoiceBox
    private void filtrarProductos(Event event) {
        searchField.clear();  // Limpia el campo de búsqueda
        List<Producto> productos = new ArrayList<>();  // Lista para almacenar los productos filtrados
        String filtro = (String) FilterBox.getValue();  // Obtiene el tipo de filtro seleccionado

        if (filtro.equalsIgnoreCase("Todos")) {
            // Si el filtro es "Todos", se combinan los productos de tipo "complemento" y "flor"
            productos = productoDAO.build().findByType("complemento");
            List<Producto> flores = productoDAO.build().findByType("flor");
            productos.addAll(flores);
        } else {
            // Si el filtro es "Complemento" o "Flor", se busca por el tipo respectivo
            productos = productoDAO.build().findByType(filtro.toLowerCase());
        }

        mostrarProductos(productos);  // Muestra los productos filtrados
    }

    // Método que muestra los productos en el GridPane
    public void mostrarProductos(List<Producto> productos) {
        boxGrid.getChildren().clear();  // Limpia el GridPane antes de agregar nuevos productos
        final int MAX_COLUMNS = 3;  // Número máximo de columnas en el grid
        int columns = 0;  // Contador de columnas
        int rows = 1;  // Contador de filas

        try {
            // Itera sobre cada producto y lo agrega al GridPane
            for (Producto pr : productos) {
                FXMLLoader loader = new FXMLLoader();  // Carga el archivo FXML de cada miniatura de producto
                loader.setLocation(getClass().getResource("thumb.fxml"));
                VBox box = loader.load();  // Carga el VBox que representa la miniatura del producto
                VBox.setMargin(box, new Insets(20, 20, 20, 20));  // Establece márgenes alrededor de la miniatura

                // Se obtiene el controlador de la miniatura de producto
                ThumbController thumbController = loader.getController();
                thumbController.setProducto(pr);  // Asocia el producto con su miniatura

                boxGrid.add(box, columns++, rows);  // Agrega el VBox al GridPane

                // Si se alcanzan tres columnas, se pasa a una nueva fila
                if (columns == MAX_COLUMNS) {
                    columns = 0;
                    rows++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar el diseño de miniaturas", e);  // Maneja cualquier error de carga
        }
    }

    // Método que redirige a la pantalla de inicio
    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.HOME, null);  // Cambia la escena a la pantalla de inicio
        } catch (Exception e) {
            throw new RuntimeException(e);  // Maneja cualquier error al cambiar de escena
        }
    }

    // Método que redirige al carrito de compras
    public void goToCart(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.CARRITO, null);  // Cambia la escena al carrito de compras
        } catch (Exception e) {
            throw new RuntimeException(e);  // Maneja cualquier error al cambiar de escena
        }
    }

    // Método llamado al abrir la vista (en este caso no realiza ninguna acción)
    @Override
    public void onOpen(Object input) throws Exception {
        // No hace nada al abrir
    }

    // Método llamado al cerrar la vista (en este caso no realiza ninguna acción)
    @Override
    public void onClose(Object output) {
        // No hace nada al cerrar
    }
}
