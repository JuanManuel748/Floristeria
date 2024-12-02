package com.github.JuanManuel.view.admin;

import com.github.JuanManuel.App;
import com.github.JuanManuel.model.entity.Session;
import com.github.JuanManuel.model.entity.User;
import com.github.JuanManuel.view.Alerta;
import com.github.JuanManuel.view.Controller;
import com.github.JuanManuel.view.Scenes;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminHomeController extends Controller implements Initializable {


    public void logout(ActionEvent actionEvent) {
        try {
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void goToUsers(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINUSERS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void goToPedidos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINPEDIDOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void goToComplementos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINPRODUCTOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void goToFlores(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINFLORES, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void goToRamos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINRAMOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void goToCentros(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINCENTROS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Stage stage = (Stage) App.getPrimaryStage();

        if (stage != null) {
            stage.setWidth(1020);
            stage.setHeight(840);
            stage.centerOnScreen();
        } else {
            Alerta.showAlert("ERROR", "No se pudo iniciar el adminHome", "No se ha podido iniciar el admin Home por un error inesperado");
        }
    }
}
