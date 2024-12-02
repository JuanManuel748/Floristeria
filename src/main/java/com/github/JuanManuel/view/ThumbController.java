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
    private VBox thisVBox;
    @FXML
    private ImageView productImageView;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;

    private Producto producto;

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
    public void onClose(Object output) {}

    /**
     * Initializes the controller by setting up any necessary configurations.
     * This method is required as part of the Initializable interface but is empty here.
     *
     * @param url the URL used to load the FXML file.
     * @param resourceBundle the resource bundle used for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Sets the product details to the labels and image view in the thumbnail.
     * This method is called to display the product information in the thumbnail UI component.
     *
     * @param p the product to display in the thumbnail.
     */
    public void setProducto(Producto p) {
        producto = p;

        ByteArrayInputStream bait = new ByteArrayInputStream(p.getImg());
        Image img = new Image(bait);
        productImageView.setImage(img);
        productNameLabel.setText(p.getNombre());
        productPriceLabel.setText(p.getPrecio() + "€");
    }

    /**
     * Prompts the user to input the quantity of the product they want to purchase.
     * If the input is invalid (non-numeric or less than 1), an alert will be shown.
     *
     * @return the valid quantity input by the user. If the input is invalid, it returns 0.
     */
    public int inputCantidad() {
        int result = 0;
        try {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Cantidad");
            tid.setHeaderText("Inserte la cantidad del producto");
            Optional<String> opt = tid.showAndWait();
            String st = opt.get();
            result = Integer.parseInt(st);
            if (result < 1) {
                Alerta.showAlert("ERROR", "Error en la cantidad", "No puedes comprar 0 o menos de 0 productos");
                return 0;
            }
            thisVBox.setStyle("-fx-background-color: #008080;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Handles the product selection event when the user clicks on the thumbnail.
     * It prompts the user to enter the desired quantity, and if valid, it adds the product to the cart.
     *
     * @param actionEvent the event triggered when the user selects the product.
     */
    public void selectProduc(javafx.event.ActionEvent actionEvent) {
        int cantidad = -1;

        while (cantidad == 0 || cantidad < 0) {
            try {
                cantidad = inputCantidad();
                if (cantidad > 0) {
                    Session.getInstance().addDetalle(producto, cantidad);
                }
            } catch (NumberFormatException e) {
                cantidad = -1;
                throw new RuntimeException(e);
            }
        }
    }
}
