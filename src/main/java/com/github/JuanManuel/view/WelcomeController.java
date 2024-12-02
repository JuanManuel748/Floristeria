package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;  // Contenedor principal para el layout de la vista de bienvenida.

    @FXML
    private Button adminWayButton;  // Botón para acceder a la pantalla de administración.

    @Override
    public void onOpen(Object input) throws Exception {
        // Este método no se utiliza, pero se podría sobrecargar para inicializar datos o acciones cuando se abre la vista.
    }

    @Override
    public void onClose(Object output) {
        // Este método no se utiliza, pero se podría sobrecargar para realizar limpieza de recursos o manejar eventos cuando se cierra la vista.
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Método utilizado para inicializar la vista, aunque en este caso está vacío.
    }

    // Método para manejar la acción del botón 'adminWayButton'.
    public void setAdminWayButton() throws Exception {
        // Cambia la escena a la vista de login (puede ser un acceso al panel de administración).
        App.currentController.changeScene(Scenes.LOGIN, null);
    }
}
