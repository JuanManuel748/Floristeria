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
    // Elementos de la interfaz gráfica vinculados con el FXML
    @FXML
    public GridPane boxGrid;  // Contenedor de ramos en formato grid
    @FXML
    public TextField searchField;  // Campo de búsqueda de ramos
    @FXML
    public Spinner<Integer> minSpinner;  // Spinner para el valor mínimo del filtro
    @FXML
    public Spinner<Integer> maxSpinner;  // Spinner para el valor máximo del filtro

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

    // Método de inicialización que se ejecuta al cargar la vista
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Obtiene la lista de ramos y los muestra en la interfaz
        List<Ramo> pro = ramoDAO.build().findByName("");  // Busca ramos sin filtro de nombre
        mostrarProductos(pro);  // Muestra los productos (ramos) encontrados

        // Inicializa los spinners con valores predeterminados
        initializeSpinner(minSpinner, 0, 999, 0);  // Spinner para el valor mínimo, entre 0 y 999
        initializeSpinner(maxSpinner, 1, 1000, 50);  // Spinner para el valor máximo, entre 1 y 1000
    }

    // Método para inicializar un Spinner con valores y rango
    private void initializeSpinner(Spinner<Integer> Spinner, int min, int max, int value) {
        // Establece el rango y el valor predeterminado del Spinner
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value);
        Spinner.setValueFactory(valueFactory);

        // Añade un listener al Spinner para que se filtre cuando cambie el valor
        Spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProductos();  // Filtra los productos cada vez que cambia el valor del Spinner
        });
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

    // Método que maneja la búsqueda de ramos cuando se hace clic en el campo de búsqueda
    public void buscarProductos(MouseEvent mouseEvent) {
        String field = searchField.getText().trim();  // Obtiene el texto del campo de búsqueda
        List<Ramo> ramos = ramoDAO.build().findByName(field);  // Busca ramos por nombre
        mostrarProductos(ramos);  // Muestra los ramos encontrados
    }

    // Método que muestra los ramos en el GridPane
    public void mostrarProductos(List<Ramo> productos) {
        boxGrid.getChildren().clear();  // Limpia el GridPane antes de agregar nuevos productos
        final int MAX_COLUMNS = 3;  // Número máximo de columnas en el grid
        int columns = 0;  // Contador de columnas
        int rows = 1;  // Contador de filas

        try {
            // Itera sobre cada ramo y lo agrega al GridPane
            for (Producto pr : productos) {
                FXMLLoader loader = new FXMLLoader();  // Carga el archivo FXML de cada miniatura de ramo
                loader.setLocation(getClass().getResource("thumb.fxml"));
                VBox box = loader.load();  // Carga el VBox que representa la miniatura del ramo
                VBox.setMargin(box, new Insets(20, 20, 20, 20));  // Establece márgenes alrededor de la miniatura

                // Se obtiene el controlador de la miniatura de ramo
                ThumbController thumbController = loader.getController();
                thumbController.setProducto(pr);  // Asocia el ramo con su miniatura

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

    // Método que filtra los productos según los valores de los Spinners
    public void filtrarProductos() {
        searchField.clear();  // Limpia el campo de búsqueda
        List<Ramo> ramos = new ArrayList<>();  // Lista para almacenar los ramos filtrados

        // Obtiene los valores de los Spinners
        int min = minSpinner.getValue();
        int max = maxSpinner.getValue();

        // Busca los ramos dentro del rango de precios seleccionado
        ramos = ramoDAO.build().findByRange(min, max);
        mostrarProductos(ramos);  // Muestra los ramos filtrados
    }
}
