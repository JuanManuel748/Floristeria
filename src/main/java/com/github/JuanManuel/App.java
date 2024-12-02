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

public class App extends Application {
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

        View view = AppController.loadFXML(Scenes.ROOT);

        scene = new Scene(view.scene, 800, 600);

        currentController = (AppController) view.controller;
        currentController.onOpen(null);

        stage.setScene(scene);
        stage.setTitle("Floristería Reyes");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/github/JuanManuel/view/images/logoBlanco.png")));
        stage.show();
    }
}
