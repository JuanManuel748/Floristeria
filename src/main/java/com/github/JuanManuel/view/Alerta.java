package com.github.JuanManuel.view;

import javafx.scene.control.Alert;

public class Alerta {
    protected static void showAlert(String type, String title, String content) {
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
