package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.entity.Session;
import com.github.JuanManuel.model.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView homeImage;
    @FXML
    private Button cartButton;
    @FXML
    private ImageView centrosImage;
    @FXML
    private ImageView ramosImage;
    @FXML
    private ImageView productosImage;
    @FXML
    private ImageView personalizarImage;

    /**
     * This method is called when the view is opened.
     * It could be used for additional setup when this screen is shown, but is empty in this case.
     *
     * @param input The input passed when opening the view
     * @throws Exception If there is an error
     */
    @Override
    public void onOpen(Object input) throws Exception {}

    /**
     * This method is called when the view is closed.
     * It could be used to handle any cleanup, but is empty here.
     *
     * @param output The output data when closing the view
     */
    @Override
    public void onClose(Object output) {}

    /**
     * This method initializes the view and is called automatically after the FXML is loaded.
     * It can be used for additional setup if necessary, but is empty in this case.
     *
     * @param url The URL for the FXML resource
     * @param resourceBundle The resource bundle for localization (not used here)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * This method is called when the logout button is clicked.
     * It logs the user out by invalidating the session and navigates back to the login screen.
     *
     * @param actionEvent The action event triggered by the button click
     */
    @FXML
    public void logout(ActionEvent actionEvent) {
        try {
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is triggered when the user clicks the home image.
     * Currently does nothing, but can be expanded if needed.
     *
     * @param mouseEvent The mouse event triggered by the click
     */
    @FXML
    public void goToHome(MouseEvent mouseEvent) { }

    /**
     * This method is called when the user clicks the "Ramos" image or button.
     * It navigates to the "Ramos" scene.
     *
     * @param mouseEvent The mouse event triggered by the click
     */
    @FXML
    public void goToRamos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.RAMOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when the user clicks the "Centros" image or button.
     * It navigates to the "Centros" scene.
     *
     * @param mouseEvent The mouse event triggered by the click
     */
    @FXML
    public void goToCentros(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.CENTROS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when the user clicks the "Productos" image or button.
     * It navigates to the "Productos" scene.
     *
     * @param mouseEvent The mouse event triggered by the click
     */
    @FXML
    public void goToProductos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.PRODUCTOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when the user clicks the "Personalizar" image or button.
     * It navigates to the "Personalizar" scene.
     *
     * @param mouseEvent The mouse event triggered by the click
     */
    @FXML
    public void goToPersonalizar(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.PERSONALIZAR, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when the shopping cart button is clicked.
     * It navigates to the "Cart" scene.
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
}
