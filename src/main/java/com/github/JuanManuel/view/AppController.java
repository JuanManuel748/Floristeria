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

/**
 * The AppController class manages the main application view and provides functionality
 * for changing scenes and opening modal dialogs. It acts as a central controller for
 * navigating through different parts of the application.
 */
public class AppController extends Controller implements Initializable {

    // Main layout container for the application
    @FXML
    private BorderPane borderPane;

    // Reference to the currently active controller for the center of the layout
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
        changeScene(Scenes.WELCOME, null); // Set the initial scene to WELCOME
    }

    /**
     * Initializes the AppController. This method is called automatically when the controller
     * is loaded by the JavaFX framework. Currently, no specific initialization is performed.
     *
     * @param url Not used, typically contains the location of the FXML file.
     * @param rb  Not used, typically contains resources for localization.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // No specific initialization required
    }

    /**
     * Loads an FXML file and returns the associated view and controller as a View object.
     *
     * @param scenes The scene enum representing the FXML file to load.
     * @return A View object containing the loaded scene and its controller.
     * @throws Exception if the FXML file cannot be loaded.
     */
    public static View loadFXML(Scenes scenes) throws Exception {
        String url = scenes.getURL(); // Get the URL for the FXML file
        System.out.println(url); // Debug log for the FXML URL
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url)); // Load the FXML file
        Parent p = loader.load(); // Parse the FXML into a JavaFX node
        Controller c = loader.getController(); // Get the associated controller
        View view = new View(); // Create a new View object
        view.scene = p; // Set the scene in the View object
        view.controller = c; // Set the controller in the View object
        return view; // Return the loaded View
    }

    /**
     * Changes the scene displayed in the center of the BorderPane layout.
     *
     * @param scene The scene to load and display.
     * @param data  Data to pass to the new scene's controller.
     * @throws Exception if the scene cannot be loaded.
     */
    public void changeScene(Scenes scene, Object data) throws Exception {
        View view = loadFXML(scene); // Load the specified scene
        borderPane.setCenter(view.scene); // Set the loaded scene in the center of the layout
        this.centerController = view.controller; // Update the reference to the active controller
        this.centerController.onOpen(data); // Pass data to the new controller
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
        View view = loadFXML(scene); // Load the specified scene
        Stage stage = new Stage(); // Create a new stage for the modal dialog
        stage.setTitle(title); // Set the title of the modal dialog
        stage.initModality(Modality.APPLICATION_MODAL); // Set the modality to block other windows
        stage.initOwner(App.stage); // Set the owner of the modal dialog
        Scene _scene = new Scene(view.scene); // Create a new scene with the loaded content
        stage.setScene(_scene); // Set the scene in the stage
        view.controller.onOpen(parent); // Pass the parent controller to the new controller
        stage.showAndWait(); // Show the modal dialog and wait for it to close
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
