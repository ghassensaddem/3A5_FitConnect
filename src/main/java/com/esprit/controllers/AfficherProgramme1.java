package com.esprit.controllers;

import com.esprit.models.Programme;
import com.esprit.services.ProgrammeService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherProgramme1 {


    @FXML
    private GridPane usersGrid;

    private final ProgrammeService programmeService = new ProgrammeService();

    @FXML
    public void initialize() {
        loadProgrammes(); // Charger les programmes depuis la base de données
    }



    private VBox createDescriptionBox(String description, int programmeId) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        box.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f0f0f0;");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Add debugging print statement
        System.out.println("Creating description box for programme ID: " + programmeId);

        // Add an event handler to the label to check if the click is being detected
        descriptionLabel.setOnMouseClicked(event -> {
            System.out.println("Label clicked! Programme ID: " + programmeId);  // Debugging click event
            afficherSeances(programmeId);
        });

        box.getChildren().add(descriptionLabel);
        return box;
    }




    private void afficherSeances(int programmeId) {
        try {
            System.out.println("Loading the Seance page for Programme ID: " + programmeId);  // Debugging the Programme ID being passed

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listeSeance.fxml"));

            // Check if the FXML file is being loaded correctly
            if (loader.getLocation() == null) {
                System.out.println("FXML file could not be found. Please check the path.");
            } else {
                System.out.println("FXML file loaded successfully.");
            }

            Parent root = loader.load();

            // Pass the programmeId to the controller
            AfficherSeance1 controller = loader.getController();
            controller.setProgrammeId(programmeId);

            // Debugging: Check if setProgrammeId was called successfully
            System.out.println("Programme ID set in AfficherSeance1 controller: " + programmeId);

            // Show the new window with sessions
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Séances du programme");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la page des séances : " + e.getMessage());
        }
    }

    // Méthode pour charger tous les programmes depuis la base de données
    private void loadProgrammes() {
        usersGrid.getChildren().clear(); // Nettoyer la grille avant de charger les données
        List<Programme> programmes = programmeService.rechercher(); // Récupérer tous les programmes

        int columns = 5; // Nombre de colonnes dans la grille
        int row = 0;
        int col = 0;

        for (Programme programme : programmes) {
            VBox descriptionBox = createDescriptionBox(programme.getDescription(), programme.getId()); // Créer le box de description
            usersGrid.add(descriptionBox, col, row);

            col++;
            if (col == columns) { // Passer à la ligne suivante après X colonnes
                col = 0;
                row++;
            }
        }
    }
}
