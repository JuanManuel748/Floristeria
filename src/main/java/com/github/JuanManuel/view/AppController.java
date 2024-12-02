package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable {
    @FXML
    private BorderPane borderPane;
    private Controller centerController;

    /**
     * This method is called when the AppController is opened.
     * By default, it sets the initial scene to the WELCOME screen.
     *
     * @param input Data that can be passed to the controller (not used in this case).
     * @throws Exception if loading the initial scene fails.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        changeScene(Scenes.WELCOME, null);
    }

    /**
     * Initializes the AppController. This method is called automatically when the controller
     * is loaded by the JavaFX framework. Currently, no specific initialization is performed.
     *
     * @param url Not used, typically contains the location of the FXML file.
     * @param rb  Not used, typically contains resources for localization.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {}

    /**
     * Loads an FXML file and returns the associated view and controller as a View object.
     *
     * @param scenes The scene enum representing the FXML file to load.
     * @return A View object containing the loaded scene and its controller.
     * @throws Exception if the FXML file cannot be loaded.
     */
    public static View loadFXML(Scenes scenes) throws Exception {
        String url = scenes.getURL();
        System.out.println(url);
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
        Parent p = loader.load();
        Controller c = loader.getController();
        View view = new View();
        view.scene = p;
        view.controller = c;
        return view;
    }

    /**
     * Changes the scene displayed in the center of the BorderPane layout.
     *
     * @param scene The scene to load and display.
     * @param data  Data to pass to the new scene's controller.
     * @throws Exception if the scene cannot be loaded.
     */
    public void changeScene(Scenes scene, Object data) throws Exception {
        View view = loadFXML(scene);
        borderPane.setCenter(view.scene);
        this.centerController = view.controller;
        this.centerController.onOpen(data);
    }

    /**
     * Opens a new modal dialog with the specified scene and title.
     *
     * @param scene  The scene to load and display in the modal dialog.
     * @param title  The title of the modal dialog.
     * @param parent The parent controller (not used in this implementation).
     * @param data   Data to pass to the modal dialog's controller.
     * @throws Exception if the modal dialog cannot be loaded.
     */
    public void openModal(Scenes scene, String title, Controller parent, Object data) throws Exception {
        View view = loadFXML(scene);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.stage);
        Scene _scene = new Scene(view.scene);
        stage.setScene(_scene);
        view.controller.onOpen(parent);
        stage.showAndWait();
    }

    /**
     * This method is called when the AppController is closed.
     * Currently, it does not perform any specific actions.
     *
     * @param output Data that can be passed when closing (not used in this case).
     */
    @Override
    public void onClose(Object output) {
        // No specific close actions required
    }
}
