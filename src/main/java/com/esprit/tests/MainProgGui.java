package com.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainProgGui extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainProgGui.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPlanning.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("PIDEV");
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du fichier FXML", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur inconnue", e);
        }
    }
} // ← Cette accolade doit bien être présente !

