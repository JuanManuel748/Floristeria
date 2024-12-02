package com.github.JuanManuel.view;

import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.entity.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ThumbController extends Controller implements Initializable {
    @FXML
    private VBox thisVBox;  // Contenedor para la miniatura del producto
    @FXML
    private ImageView productImageView;  // Vista para mostrar la imagen del producto
    @FXML
    private Label productNameLabel;  // Etiqueta para el nombre del producto
    @FXML
    private Label productPriceLabel;  // Etiqueta para el precio del producto

    private Producto producto;  // Objeto Producto asociado con la miniatura

    @Override
    public void onOpen(Object input) throws Exception {
        // No se utiliza en este caso
    }

    @Override
    public void onClose(Object output) {
        // No se utiliza en este caso
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización de la vista, aunque en este caso está vacía
    }

    // Método para configurar el producto en la miniatura
    public void setProducto(Producto p) {
        producto = p;
        // Convertir la imagen del producto (almacenada como array de bytes) en una imagen de JavaFX
        ByteArrayInputStream bait = new ByteArrayInputStream(p.getImg());
        Image img = new Image(bait);
        productImageView.setImage(img);  // Establece la imagen en el ImageView
        productNameLabel.setText(p.getNombre());  // Establece el nombre del producto en el Label
        productPriceLabel.setText(p.getPrecio() + "€");  // Establece el precio del producto en el Label
    }

    // Método para obtener la cantidad de productos que el usuario desea comprar
    public int inputCantidad() {
        int result = 0;
        try {
            // Muestra un cuadro de diálogo de texto para ingresar la cantidad
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Cantidad");
            tid.setHeaderText("Inserte la cantidad del producto");
            Optional<String> opt = tid.showAndWait();
            String st = opt.get();  // Obtiene el valor ingresado por el usuario
            result = Integer.parseInt(st);  // Convierte la cadena a un número entero

            // Valida que la cantidad sea positiva
            if (result < 1) {
                Alerta.showAlert("ERROR", "Error en la cantidad", "No puedes comprar 0 o menos de 0 productos");
                return 0;  // Si la cantidad es 0 o menor, muestra un error y retorna 0
            }

            // Cambia el color de fondo del VBox a verde cuando se ingresa una cantidad válida
            thisVBox.setStyle("-fx-background-color: #008080;");
        } catch (Exception e) {
            e.printStackTrace();  // Si hay un error al convertir la cantidad, lo imprime
        }
        return result;  // Retorna la cantidad ingresada
    }

    // Método para manejar la acción de seleccionar el producto
    public void selectProduc(javafx.event.ActionEvent actionEvent) {
        int cantidad = -1;

        // El ciclo sigue pidiendo una cantidad válida hasta que la cantidad ingresada sea positiva
        while (cantidad == 0 || cantidad < 0) {
            try {
                cantidad = inputCantidad();  // Pide la cantidad
                if (cantidad > 0) {
                    // Añade el producto a los detalles del carrito en la sesión actual
                    Session.getInstance().addDetalle(producto, cantidad);
                }
            } catch (NumberFormatException e) {
                cantidad = -1;  // Si ocurre un error al ingresar la cantidad, vuelve a pedirla
                throw new RuntimeException(e);
            }
        }
    }
}
