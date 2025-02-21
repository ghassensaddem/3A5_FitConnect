package com.esprit.controllers;

import com.esprit.models.RatingActivity;
import com.esprit.models.activity;
import com.esprit.models.SalleSportif;
import com.esprit.services.RatingService;
import com.esprit.services.ActivityService;
import com.esprit.services.SalleSportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class AjouterRating {

    private final RatingService ratingService = new RatingService();
    private final ActivityService activityService = new ActivityService();
    private final SalleSportService salleSportService = new SalleSportService();

    @FXML
    private ComboBox<activity> comboActivity;

    @FXML
    private ComboBox<SalleSportif> comboSalle;

    @FXML
    private Spinner<Integer> ratingStars;

    @FXML
    private TextArea review;

    @FXML
    public void initialize() {
        chargerActivites();
        chargerSalles();
        configurerSpinner();
    }

    private void chargerActivites() {
        ObservableList<activity> activityList = FXCollections.observableArrayList(activityService.rechercher());
        comboActivity.setItems(activityList);
    }

    private void chargerSalles() {
        ObservableList<SalleSportif> salleList = FXCollections.observableArrayList(salleSportService.rechercher());
        comboSalle.setItems(salleList);
    }

    private void configurerSpinner() {
        ratingStars.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3));
        ratingStars.setEditable(false);
    }

    @FXML
    private void ajouterRating() {
        activity selectedActivity = comboActivity.getValue();
        SalleSportif selectedSalle = comboSalle.getValue();
        Integer rating = ratingStars.getValue();
        String reviewText = review.getText().trim();

        if (selectedActivity == null || selectedSalle == null || reviewText.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !", Alert.AlertType.WARNING);
            return;
        }

        if (reviewText.length() < 10 || reviewText.length() > 300) {
            showAlert("Erreur", "L'avis doit contenir entre 10 et 300 caractères.", Alert.AlertType.WARNING);
            return;
        }

        if (rating < 1 || rating > 5) {
            showAlert("Erreur", "La note doit être comprise entre 1 et 5 étoiles.", Alert.AlertType.WARNING);
            return;
        }

        try {
            RatingActivity ratingActivity = new RatingActivity(
                    selectedActivity.getIdActivity(),
                    selectedSalle.getIdSalle(),
                    RatingActivity.ID_CLIENT_FIXE,
                    rating,
                    reviewText
            );
            ratingService.ajouter(ratingActivity);

            showAlert("Succès", "Évaluation ajoutée avec succès !", Alert.AlertType.INFORMATION);
            clearFields();

            if (confirmNavigation()) {
                naviguerVersAffichageRating();
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean confirmNavigation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous voir toutes les évaluations maintenant ?", ButtonType.YES, ButtonType.NO);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private void naviguerVersAffichageRating() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageRating.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) comboActivity.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger l'affichage des évaluations : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void clearFields() {
        comboActivity.getSelectionModel().clearSelection();
        comboSalle.getSelectionModel().clearSelection();
        if (ratingStars.getValueFactory() != null) {
            ratingStars.getValueFactory().setValue(3);
        }
        review.clear();
    }
}
