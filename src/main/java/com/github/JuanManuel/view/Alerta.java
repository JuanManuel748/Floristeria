package com.github.JuanManuel.view;

import javafx.scene.control.Alert;

/**
 * The Alerta class provides a utility method to display alerts of various types.
 * This class simplifies the process of creating and showing different types of alert dialogs
 * in the JavaFX application.
 */
public class Alerta {

    /**
     * Displays an alert dialog with the specified type, title, and content.
     *
     * @param type    The type of the alert (e.g., "ERROR", "INFORMATION", "CONFIRMATION", "WARNING").
     * @param title   The title of the alert dialog.
     * @param content The message to be displayed in the alert dialog.
     */
    public static void showAlert(String type, String title, String content) {
        // Create an Alert instance with a default type of NONE
        Alert alert = new Alert(Alert.AlertType.NONE);

        // Determine the alert type based on the input parameter
        switch (type) {
            case "ERROR":
                alert.setAlertType(Alert.AlertType.ERROR);
                break;
            case "INFORMATION":
                alert.setAlertType(Alert.AlertType.INFORMATION);
                break;
            case "CONFIRMATION":
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                break;
            case "WARNING":
                alert.setAlertType(Alert.AlertType.WARNING);
                break;
            default:
                break;
        }

        // Set the title of the alert dialog
        alert.setTitle(title);
        // Remove the default header text
        alert.setHeaderText(null);
        // Set the main content/message of the alert dialog
        alert.setContentText(content);

        // Display the alert dialog and wait for the user's response
        alert.showAndWait();
    }
}
