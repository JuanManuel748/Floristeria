package com.github.JuanManuel.view;

import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.entity.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void onOpen(Object input) throws Exception {
    }

    @Override
    public void onClose(Object output) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setProducto(Producto p) {
        producto = p;
        // Convertir la imagen y configurar controles
        ByteArrayInputStream bait = new ByteArrayInputStream(p.getImg());
        Image img = new Image(bait);
        productImageView.setImage(img);
        productNameLabel.setText(p.getNombre());
        productPriceLabel.setText(p.getPrecio() + "€");
    }


    public String inputCantidad() {
        TextInputDialog tid = new TextInputDialog();
        tid.setTitle("TITUTLO");
        tid.setHeaderText("CABEZA");
        tid.setContentText("CONTENIDO");
        Optional<String> opt = tid.showAndWait();
        String result = opt.get();
        return result;
    }

    public void selectProduc(javafx.event.ActionEvent actionEvent) {
        int cantidad = -1;
        while(cantidad == -1) {
            try {
                cantidad = Integer.parseInt(inputCantidad());
            } catch (NumberFormatException e) {
                cantidad = -1;
                throw new RuntimeException(e);
            }
        }
        Session.getInstance().addDetalle(producto, cantidad);
        thisVBox.setStyle("-fx-background-color: #008080;");
    }
}
