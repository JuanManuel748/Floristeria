package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.DAO.centroDAO;
import com.github.JuanManuel.model.DAO.florDAO;
import com.github.JuanManuel.model.DAO.ramoDAO;
import com.github.JuanManuel.model.entity.Centro;
import com.github.JuanManuel.model.entity.Flor;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.entity.Ramo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController extends Controller implements Initializable {
    @FXML
    public ImageView previewImage;
    @FXML
    public Label nameField;
    @FXML
    public Label priceField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public Pane RCpane;
    @FXML
    public Label prField1;
    @FXML
    public Label secunField1;
    @FXML
    public Label secunField2;
    @FXML
    public Label secunField3;
    @FXML
    public Label sizeField;
    @FXML
    public Label phraseField;
    @FXML
    public Pane fPane;
    @FXML
    public Label colorField;
    @FXML
    public Label prField;
    @FXML
    public Label prFLField;
    private static final File imgNull = new File("src/main/resources/com/github/JuanManuel/view/images/noPicture.jpg");
    private static Producto pro;

    @Override
    public void onOpen(Object input) throws Exception {

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

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (pro != null) {
            try {
                byte[] byteImage = pro.getImg();
                Image defaultImage = new Image(imgNull.toURI().toString());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteImage);
                Image image = new Image(byteArrayInputStream);
                if (image != null) {
                    previewImage.setImage(image);
                } else {
                    previewImage.setImage(defaultImage);
                }
                nameField.setText("Nombre: " + pro.getNombre());
                priceField.setText("Precio: " + pro.getPrecio()  + " €");
                descriptionField.setText(pro.getDescripcion());
                System.out.println(pro.getTipo());
                switch (pro.getTipo()) {
                    case "complemento":
                        fPane.setVisible(false);
                        RCpane.setVisible(false);
                        break;
                    case "flor":
                        fPane.setVisible(true);
                        RCpane.setVisible(false);
                        Flor f = florDAO.build().findByPK(new Flor(pro.getIdProducto()));
                        colorField.setText("Color: " + f.getColor());
                        if (f.getTipoFlor()) {
                            prField.setText("Es principal");
                        } else {
                            prField.setText("Es secundaria");
                        }
                        break;
                    case "ramo":
                        fPane.setVisible(false);
                        RCpane.setVisible(true);
                        Ramo r = ramoDAO.build().findByPK(new Ramo(pro.getIdProducto()));
                        if (r == null) {
                            Alerta.showAlert("ERROR", "Producto no encontrado", "No se ha encontrado el producto en la base de datos");
                            return;
                        }
                        prFLField.setText("Principal: " + r.getFlorPr().getNombre());
                        secunField1.setText("Secundaria 1: " + r.getFloresSecun().get(0).getNombre());
                        secunField2.setText("Secundaria 2: " + r.getFloresSecun().get(1).getNombre());
                        secunField3.setText("Secundaria 3: " + r.getFloresSecun().get(2).getNombre());
                        sizeField.setText("Nº flores: " + r.getCantidadFlores());
                        phraseField.setText("Color envoltorio: " + r.getColorEnvol());
                        break;
                    case "centro":
                        fPane.setVisible(false);
                        RCpane.setVisible(true);
                        Centro c = centroDAO.build().findByPK(new Centro(pro.getIdProducto()));
                        if (c == null) {
                            Alerta.showAlert("ERROR", "Producto no encontrado", "No se ha encontrado el producto en la base de datos");
                            return;
                        }
                        prFLField.setText("Principal: " + c.getFlorPr().getNombre());
                        secunField1.setText("Secundaria 1: " + c.getFloresSecun().get(0).getNombre());
                        secunField2.setText("Secundaria 2: " + c.getFloresSecun().get(1).getNombre());
                        secunField3.setText("Secundaria 3: " + c.getFloresSecun().get(2).getNombre());
                        sizeField.setText("Tamaño: " + c.getSize());
                        phraseField.setText("Frase: " + c.getFrase());
                        break;
                    default:
                        fPane.setVisible(false);
                        RCpane.setVisible(false);
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected static void setProducto(Producto p) {
        pro = p;
    }
}
