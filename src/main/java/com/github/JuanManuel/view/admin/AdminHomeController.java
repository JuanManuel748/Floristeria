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

    /**
     * Cierra la sesión del usuario actual y redirige a la pantalla de login.
     *
     * @param actionEvent El evento que activa la acción de cierre de sesión.
     */
    public void logout(ActionEvent actionEvent) {
        try {
            User u = Session.getInstance().getCurrentUser();
            Session.getInstance().logOut(u);
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Redirige a la pantalla de administración de usuarios.
     *
     * @param mouseEvent El evento que activa la acción de cambio de escena.
     */
    public void goToUsers(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINUSERS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Redirige a la pantalla de administración de pedidos.
     *
     * @param mouseEvent El evento que activa la acción de cambio de escena.
     */
    public void goToPedidos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINPEDIDOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Redirige a la pantalla de administración de complementos.
     *
     * @param mouseEvent El evento que activa la acción de cambio de escena.
     */
    public void goToComplementos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINPRODUCTOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Redirige a la pantalla de administración de flores.
     *
     * @param mouseEvent El evento que activa la acción de cambio de escena.
     */
    public void goToFlores(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINFLORES, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Redirige a la pantalla de administración de ramos.
     *
     * @param mouseEvent El evento que activa la acción de cambio de escena.
     */
    public void goToRamos(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINRAMOS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Redirige a la pantalla de administración de centros.
     *
     * @param mouseEvent El evento que activa la acción de cambio de escena.
     */
    public void goToCentros(MouseEvent mouseEvent) {
        try {
            App.currentController.changeScene(Scenes.ADMINCENTROS, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método de apertura de la vista, utilizado cuando se inicializa la vista.
     *
     * @param input El parámetro de entrada que puede ser utilizado en la apertura.
     * @throws Exception Si ocurre un error durante la apertura de la vista.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // Lógica de apertura
    }

    /**
     * Método de cierre de la vista, utilizado cuando se cierra la vista.
     *
     * @param output El parámetro de salida que puede ser utilizado en el cierre.
     */
    @Override
    public void onClose(Object output) {
        // Lógica de cierre
    }

    /**
     * Inicializa la vista de administración de inicio ajustando el tamaño
     * y la posición de la ventana.
     *
     * @param url La URL de la vista FXML.
     * @param resourceBundle El recurso que contiene las configuraciones de idioma.
     */
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
