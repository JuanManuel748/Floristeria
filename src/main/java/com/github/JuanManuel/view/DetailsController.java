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

/**
 * This class is responsible for handling the details view of a product.
 * It displays product-specific information including images, descriptions, and more.
 */
public class DetailsController extends Controller implements Initializable {

    // UI elements bound to the FXML
    @FXML
    public ImageView previewImage;   // Image view to display the product image
    @FXML
    public Label nameField;          // Label for product name
    @FXML
    public Label priceField;         // Label for product price
    @FXML
    public TextArea descriptionField; // TextArea for product description
    @FXML
    public Pane RCpane;             // Pane for Ramo/Centro-specific details
    @FXML
    public Label prField1;          // Label for primary product information
    @FXML
    public Label secunField1;       // Label for secondary product info (1)
    @FXML
    public Label secunField2;       // Label for secondary product info (2)
    @FXML
    public Label secunField3;       // Label for secondary product info (3)
    @FXML
    public Label sizeField;         // Label for product size (used in Ramo/Centro)
    @FXML
    public Label phraseField;       // Label for product phrase (used in Centro)
    @FXML
    public Pane fPane;              // Pane for Flor-specific details
    @FXML
    public Label colorField;        // Label for flower color
    @FXML
    public Label prField;           // Label for flower type (Principal or Secondary)
    @FXML
    public Label prFLField;         // Label for primary flower name

    // A default image to use when no product image is available
    private static final File imgNull = new File("src/main/resources/com/github/JuanManuel/view/images/noPicture.jpg");

    // Static reference to the current product being displayed
    private static Producto pro;

    /**
     * This method is called when the view is opened.
     * Initializes the UI with the product details if available.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // No specific behavior needed for onOpen in this context
    }

    /**
     * Navigates back to the home screen when the home button is clicked.
     *
     * @param actionEvent The action event triggered by the button click
     */
    public void goToHome(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.HOME, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Navigates to the shopping cart screen when the cart button is clicked.
     *
     * @param actionEvent The action event triggered by the button click
     */
    public void goToCart(ActionEvent actionEvent) {
        try {
            App.currentController.changeScene(Scenes.CARRITO, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when the view is closed.
     *
     * @param output The output to be processed when closing the view
     */
    @Override
    public void onClose(Object output) {
        // No specific behavior needed for onClose in this context
    }

    /**
     * This method is called when the controller is initialized.
     * It sets the product details (name, price, description, etc.) and configures the visibility of UI elements
     * based on the product type (Flor, Ramo, Centro, etc.).
     *
     * @param url The location of the FXML file
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (pro != null) {
            try {
                // Attempt to load the product image
                byte[] byteImage = pro.getImg();
                Image defaultImage = new Image(imgNull.toURI().toString());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteImage);
                Image image = new Image(byteArrayInputStream);

                // If the image is null, use the default "no image" image
                if (image != null) {
                    previewImage.setImage(image);
                } else {
                    previewImage.setImage(defaultImage);
                }

                // Set other product details
                nameField.setText("Nombre: " + pro.getNombre());
                priceField.setText("Precio: " + pro.getPrecio() + " €");
                descriptionField.setText(pro.getDescripcion());

                // Handle product-specific details based on the type of product
                switch (pro.getTipo()) {
                    case "complemento":
                        // Hide product-specific details for complemento type
                        fPane.setVisible(false);
                        RCpane.setVisible(false);
                        break;
                    case "flor":
                        // Show flower-specific details
                        fPane.setVisible(true);
                        RCpane.setVisible(false);
                        Flor f = florDAO.build().findByPK(new Flor(pro.getIdProducto()));
                        colorField.setText("Color: " + f.getColor());
                        prField.setText(f.getTipoFlor() ? "Es principal" : "Es secundaria");
                        break;
                    case "ramo":
                        // Show Ramo-specific details
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
                        // Show Centro-specific details
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
                        // Hide all additional details for unrecognized product types
                        fPane.setVisible(false);
                        RCpane.setVisible(false);
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Static setter to set the current product for display in the details view.
     * This method is used to pass the product to be displayed in the view.
     *
     * @param p The product to be displayed
     */
    protected static void setProducto(Producto p) {
        pro = p;
    }
}
