package com.esprit.tests;

import com.esprit.controllers.AfficherProgramme;
import com.esprit.controllers.AjouterProgramme;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainProgGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/seance3.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et définir le mode (frontend = true, backend = falAse)
//            AfficherProgramme controller = loader.getController();

  //          controller.setMode(true); // true = frontend, false = backend

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("PIDEV ");
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
