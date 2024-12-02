package com.github.JuanManuel;

import com.github.JuanManuel.view.AppController;
import com.github.JuanManuel.view.Scenes;
import com.github.JuanManuel.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    // Atributos estáticos que permiten el acceso global a la escena, al escenario y al controlador actual.
    public static Scene scene;
    public static Stage stage;
    public static AppController currentController;

    public static void main(String[] args) {
        launch();
    }

    public static Stage getPrimaryStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        // Cargar la vista inicial (definida en el archivo FXML correspondiente)
        View view = AppController.loadFXML(Scenes.ROOT);

        // Crear una escena con la vista cargada, configurando las dimensiones (800x600)
        scene = new Scene(view.scene, 800, 600);

        // Obtener el controlador asociado a la vista cargada y llamar al método onOpen()
        currentController = (AppController) view.controller;
        currentController.onOpen(null);

        // Configurar el escenario (ventana de la aplicación)
        stage.setScene(scene);
        stage.setTitle("Floristería Reyes");  // Título de la ventana
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/github/JuanManuel/view/images/logoBlanco.png")));  // Establecer icono para la ventana
        stage.show();  // Mostrar la ventana
    }
}
