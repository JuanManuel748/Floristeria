package com.github.JuanManuel.view;

import javafx.scene.control.Alert;

public class Alerta {

    /**
     * Displays an alert dialog with the specified type, title, and content.
     *
     * @param type    The type of the alert (e.g., "ERROR", "INFORMATION", "CONFIRMATION", "WARNING").
     * @param title   The title of the alert dialog.
     * @param content The message to be displayed in the alert dialog.
     */
    public static void showAlert(String type, String title, String content) {
        Alert alert = new Alert(Alert.AlertType.NONE);

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
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
