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

/**
 * This controller manages the home screen of the application.
 * It handles user actions such as logging out, navigating to different product categories, and accessing the shopping cart.
 */
public class HomeController extends Controller implements Initializable {

    // UI elements bound to the FXML
    @FXML
    private AnchorPane anchorPane;       // The root layout pane for the home screen
    @FXML
    private Button exitButton;           // Button for exiting/logging out
    @FXML
    private ImageView homeImage;         // Image for the home screen
    @FXML
    private Button cartButton;           // Button for navigating to the shopping cart
    @FXML
    private ImageView centrosImage;      // Image for the centros section
    @FXML
    private ImageView ramosImage;        // Image for the ramos section
    @FXML
    private ImageView productosImage;    // Image for the productos section
    @FXML
    private ImageView personalizarImage; // Image for the personalizar section

    /**
     * This method is called when the view is opened.
     * It could be used for additional setup when this screen is shown, but is empty in this case.
     *
     * @param input The input passed when opening the view
     * @throws Exception If there is an error
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // No specific behavior needed for this context
    }

    /**
     * This method is called when the view is closed.
     * It could be used to handle any cleanup, but is empty here.
     *
     * @param output The output data when closing the view
     */
    @Override
    public void onClose(Object output) {
        // No specific behavior needed for this context
    }

    /**
     * This method initializes the view and is called automatically after the FXML is loaded.
     * It can be used for additional setup if necessary, but is empty in this case.
     *
     * @param url The URL for the FXML resource
     * @param resourceBundle The resource bundle for localization (not used here)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization logic (currently empty)
    }

    /**
     * This method is called when the logout button is clicked.
     * It logs the user out by invalidating the session and navigates back to the login screen.
     *
     * @param actionEvent The action event triggered by the button click
     */
    @FXML
    public void logout(ActionEvent actionEvent) {
        try {
            // Get the current user from the session and log them out
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);

            // Change the scene to the login screen
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
    public void goToHome(MouseEvent mouseEvent) {
        // No specific behavior here
    }

    /**
     * This method is called when the user clicks the "Ramos" image or button.
     * It navigates to the "Ramos" scene.
     *
     * @param mouseEvent The mouse event triggered by the click
     */
    @FXML
    public void goToRamos(MouseEvent mouseEvent) {
        try {
            // Change the scene to the "Ramos" screen
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
            // Change the scene to the "Centros" screen
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
            // Change the scene to the "Productos" screen
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
            // Change the scene to the "Personalizar" screen
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
            // Change the scene to the "Cart" screen
            App.currentController.changeScene(Scenes.CARRITO, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
