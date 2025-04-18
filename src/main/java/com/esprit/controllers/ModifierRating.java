package com.esprit.controllers;

import com.esprit.models.RatingActivity;
import com.esprit.services.RatingService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class ModifierRating {

    @FXML
    private TextField ratingField;

    @FXML
    private TextField reviewField;

    private RatingActivity currentRating;
    private final RatingService ratingService = new RatingService();

    public void initData(RatingActivity rating) {
        if (rating == null) {
            showError("Erreur : Aucune évaluation sélectionnée.");
            return;
        }
        this.currentRating = rating;

        // Remplissage des champs avec les valeurs actuelles
        ratingField.setText(String.valueOf(rating.getRatingStars()));
        reviewField.setText(rating.getReview());
    }

    @FXML
    private void enregistrerModification() {
        if (currentRating == null) {
            showError("Aucune évaluation à modifier.");
            return;
        }

        // Vérification et validation des champs
        String ratingText = ratingField.getText().trim();
        String reviewText = reviewField.getText().trim();

        if (ratingText.isEmpty() || reviewText.isEmpty()) {
            showError("Tous les champs doivent être remplis.");
            return;
        }

        try {
            int newRating = Integer.parseInt(ratingText);

            if (newRating < 1 || newRating > 5) {
                showError("La note doit être entre 1 et 5.");
                return;
            }

            if (reviewText.length() < 10 || reviewText.length() > 300) {
                showError("L'avis doit contenir entre 10 et 300 caractères.");
                return;
            }

            // Mise à jour de l'objet RatingActivity
            currentRating.setRatingStars(newRating);
            currentRating.setReview(reviewText);

            // Modification en base de données
            ratingService.modifier(currentRating);

            showSuccess("Évaluation modifiée avec succès.");

            // Confirmation de navigation
            if (confirmNavigation()) {
                naviguerVersAffichageRating();
            }

        } catch (NumberFormatException e) {
            showError("Veuillez entrer une valeur numérique valide pour la note.");
        }
    }

    private boolean confirmNavigation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Retour à l'affichage");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous retourner à l'affichage des évaluations ?");

        ButtonType oui = new ButtonType("Oui");
        ButtonType non = ButtonType.CANCEL; // Réutilise directement ButtonType.CANCEL
        alert.getButtonTypes().setAll(oui, non);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == oui;
    }

    private void naviguerVersAffichageRating() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageRating.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ratingField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Impossible de charger la page des évaluations.");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
