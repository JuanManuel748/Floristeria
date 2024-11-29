package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonalizarController extends Controller implements Initializable {
    @FXML
    public Button exitButton;
    @FXML
    public Button cartButton;
    @FXML
    public ChoiceBox typeChoice;
    @FXML
    public ChoiceBox flPRChoice;
    @FXML
    public ChoiceBox flSecunChoice3;
    @FXML
    public ChoiceBox flSecunChoice1;
    @FXML
    public ChoiceBox flSecunChoice2;
    @FXML
    public ChoiceBox sizeChoice;
    @FXML
    public Pane variablePane;
    @FXML
    public Button buyButton;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    public void buy(ActionEvent actionEvent) {
    }
}
