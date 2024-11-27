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


    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

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

    @FXML
    public void goToHome(MouseEvent mouseEvent) {
    }

    @FXML
    public void goToRamos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.RAMOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToCentros(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.CENTROS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToProductos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.PRODUCTOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToPersonalizar(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.PERSONALIZAR, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
