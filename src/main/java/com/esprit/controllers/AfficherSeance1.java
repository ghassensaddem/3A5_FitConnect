package com.esprit.controllers;

import com.esprit.models.seance;
import com.esprit.services.SeanceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherSeance1 implements Initializable {

    @FXML
    private GridPane usersGrid; // GridPane to hold the sessions

    private final SeanceService seanceService = new SeanceService();
    private int programmeId ; // Variable for programmeId to filter sessions

    // Initialisation du programmeId pour récupérer les séances
    public void setProgrammeId(int programmeId) {
        this.programmeId = programmeId;
        System.out.println("Programme ID set in AfficherSeance1: " + this.programmeId);
        loadSeances(); // Call loadSeances after setting programmeId
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSeances(); // Load the seances specific to the programmeId
    }

    // Method to load the seances and populate the GridPane
    private void loadSeances() {
        usersGrid.getChildren().clear(); // Clear the grid before adding new data

        List<seance> seances = seanceService.rechercherParProgramme(this.programmeId);
         // Get seances based on programmeId


        int columns = 5; // Number of columns in the grid
        int row = 0;
        int col = 0;

        for (seance seance : seances) {
            VBox seanceBox = createSeanceBox(seance); // Create a VBox for each seance

            // Add the VBox to the GridPane at the right position
            usersGrid.add(seanceBox, col, row);

            col++;
            if (col == columns) { // Move to the next row after X columns
                col = 0;
                row++;
            }
        }
    }

    // Method to create a VBox to display the session (seance) details
    private VBox createSeanceBox(seance seance) {
        VBox box = new VBox();
        box.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f0f0f0;");
        box.setSpacing(5);

        // Labels for session data (ID, Date, Horaire, Lieu, etc.)

        Label labelDate = new Label("Date: " + seance.getDate().toString());
        Label labelHoraire = new Label("Horaire: " + seance.getHoraire().toString());
        Label labelLieu = new Label("Lieu: " + seance.getLieu());

        // Add labels to the VBox
        box.getChildren().addAll( labelDate, labelHoraire, labelLieu);

        return box;
    }

    // Method to show an error message
    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
