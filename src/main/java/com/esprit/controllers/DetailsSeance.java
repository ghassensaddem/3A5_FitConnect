package com.esprit.controllers;

import com.esprit.models.Avis;
import com.esprit.models.seance;
import com.esprit.services.SeanceService;
import com.esprit.services.AvisService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.List;

public class DetailsSeance {

    @FXML private Label labelDate;
    @FXML private Label labelHoraire;
    @FXML private Label labelProgramme;
    @FXML private Label labelDescription;
    @FXML private ListView<String> listViewAvis;
    @FXML private Button btnRetour;

    private final SeanceService seanceService = new SeanceService();
    private final AvisService avisService = new AvisService();

    private int seanceId;

    // Initialisation de la page
    public void initialize() {
        loadSeanceDetails();
        loadAvisForSeance();
    }

    // Méthode pour initialiser l'ID de la séance à afficher
    public void setSeanceId(int seanceId) {
        this.seanceId = seanceId;
    }

    // Charger les détails de la séance
    private void loadSeanceDetails() {
        seance selectedSeance = seanceService.getSeanceById(seanceId);

        if (selectedSeance != null) {
            labelDate.setText(selectedSeance.getDate().toString());
            labelHoraire.setText(selectedSeance.getHoraire().toString());  // Pas besoin de parsing, juste l'afficher
        } else {
            showErrorMessage("Séance introuvable", "Aucune séance trouvée avec cet ID.");
        }
    }


    // Charger les avis de la séance
    private void loadAvisForSeance() {
        List<Avis> avisList = avisService.getAvisForSeance(seanceId);

        if (!avisList.isEmpty()) {
            listViewAvis.getItems().setAll(String.valueOf(avisList));
        } else {
            listViewAvis.getItems().add("Aucun avis pour cette séance.");
        }
    }

    // Gestion du bouton Retour
    @FXML
    private void handleRetour() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();  // Fermer la fenêtre actuelle
    }

    // Méthode pour afficher un message d'erreur
    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
