package com.github.JuanManuel.view;

import com.github.JuanManuel.App;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

/**
 * The abstract Controller class serves as a base class for all controllers in the application.
 * It defines common functionality and provides methods for opening and closing the views.
 */
public abstract class Controller {

    // Reference to the main application instance
    App app;

    /**
     * Sets the main application instance for this controller.
     * This method allows the controller to interact with the main application.
     *
     * @param app The main application instance to be set.
     */
    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Abstract method that should be implemented by subclasses to define behavior when the view is opened.
     *
     * @param input The data passed to the controller when opening the view.
     * @throws Exception if an error occurs while opening the view.
     */
    public abstract void onOpen(Object input) throws Exception;

    /**
     * Abstract method that should be implemented by subclasses to define behavior when the view is closed.
     *
     * @param output The data passed to the controller when closing the view.
     */
    public abstract void onClose(Object output);
}
