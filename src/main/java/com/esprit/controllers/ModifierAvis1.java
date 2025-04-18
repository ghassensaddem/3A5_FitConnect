package com.esprit.controllers;

import com.esprit.models.Avis;
import com.esprit.services.AvisService;
import com.esprit.models.NavigationHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ModifierAvis1 {

    @FXML
    private TextField commentaireField;

    @FXML
    private TextField noteField;

    @FXML
    private Button enregistrerButton;

    private final AvisService avisService = new AvisService();
    private Avis avisToModify;
    private AfficherAvis1 afficherAvisController;  // 🔹 Référence au contrôleur d'affichage

    public void setAvisToModify(Avis avis) {
        this.avisToModify = avis;
        commentaireField.setText(avis.getCommentaire());
        noteField.setText(String.valueOf(avis.getNote()));
    }

    public void setAfficherAvisController(AfficherAvis1 controller) {
        this.afficherAvisController = controller;
    }

    @FXML
    private void enregistrerModifications(ActionEvent event) {
        String commentaire = commentaireField.getText();
        String noteText = noteField.getText();

        if (commentaire.isEmpty() || noteText.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis", Alert.AlertType.ERROR);
            return;
        }

        int note;
        try {
            note = Integer.parseInt(noteText);
            if (note < 0 || note > 10) {
                showAlert("Erreur", "La note doit être comprise entre 0 et 10", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La note doit être un nombre entier valide", Alert.AlertType.ERROR);
            return;
        }

        // 🔹 Mettre à jour l'avis et sauvegarder
        avisToModify.setCommentaire(commentaire);
        avisToModify.setNote(note);
        avisService.modifier(avisToModify);

        // 🔹 Recharger la liste des avis SANS CHANGER DE PAGE
        if (afficherAvisController != null) {
            afficherAvisController.loadAvisForSeance(avisToModify.getSeanceId());
        }

        // 🔹 Fermer la fenêtre actuelle
        closeWindow();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) enregistrerButton.getScene().getWindow();
        stage.close();
    }
}
