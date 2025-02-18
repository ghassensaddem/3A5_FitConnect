package com.esprit.controllers;

import com.esprit.models.PlanningActivity;
import com.esprit.models.activity;
import com.esprit.models.SalleSportif;
import com.esprit.services.PlanningService;
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

public class AjouterPlanning {

    private final PlanningService planningService = new PlanningService();
    private final ActivityService activityService = new ActivityService();
    private final SalleSportService salleSportService = new SalleSportService();

    @FXML
    private ComboBox<activity> comboActivity;

    @FXML
    private ComboBox<SalleSportif> comboSalle;

    @FXML
    private TextField capacityMax;

    @FXML
    private TextField nombreInscription;

    @FXML
    public void initialize() {
        chargerActivites();
        chargerSalles();
    }

    private void chargerActivites() {
        List<activity> activities = activityService.rechercher();
        if (activities != null && !activities.isEmpty()) {
            ObservableList<activity> activityList = FXCollections.observableArrayList(activities);
            comboActivity.setItems(activityList);
        } else {
            showAlert("Erreur", "Aucune activité disponible", Alert.AlertType.WARNING);
        }
    }

    private void chargerSalles() {
        List<SalleSportif> salles = salleSportService.rechercher();
        if (salles != null && !salles.isEmpty()) {
            ObservableList<SalleSportif> salleList = FXCollections.observableArrayList(salles);
            comboSalle.setItems(salleList);
        } else {
            showAlert("Erreur", "Aucune salle disponible", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void ajouterPlanning() {
        activity selectedActivity = comboActivity.getValue();
        SalleSportif selectedSalle = comboSalle.getValue();
        String capacity = capacityMax.getText().trim();
        String inscription = nombreInscription.getText().trim();

        if (selectedActivity == null || selectedSalle == null || capacity.isEmpty() || inscription.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !", Alert.AlertType.WARNING);
            return;
        }

        try {
            int idActivity = selectedActivity.getIdActivity();
            int idSalle = selectedSalle.getIdSalle();

            if (planningService.existsPlanning(idActivity, idSalle)) {
                showAlert("Erreur", "Cette planification existe déjà pour cette activité et cette salle.", Alert.AlertType.WARNING);
                return;
            }

            int capacityMaxValue = validateAndParseInt(capacity, "Capacité maximale");
            if (capacityMaxValue == -1 || capacityMaxValue <= 0) {
                showAlert("Erreur", "La capacité maximale doit être un nombre positif.", Alert.AlertType.ERROR);
                return;
            }

            int nombreInscriptionValue = validateAndParseInt(inscription, "Nombre d'inscriptions");
            if (nombreInscriptionValue == -1 || nombreInscriptionValue < 0) {
                showAlert("Erreur", "Le nombre d'inscriptions ne peut pas être négatif.", Alert.AlertType.ERROR);
                return;
            }
            if (nombreInscriptionValue > capacityMaxValue) {
                showAlert("Erreur", "Le nombre d'inscriptions ne peut pas dépasser la capacité maximale.", Alert.AlertType.ERROR);
                return;
            }

            PlanningActivity planning = new PlanningActivity(idActivity, idSalle, capacityMaxValue, nombreInscriptionValue);
            planningService.ajouter(planning);

            showAlert("Succès", "Planification ajoutée avec succès !", Alert.AlertType.INFORMATION);
            clearFields();
            naviguerVersAffichagePlanning();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private int validateAndParseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer une valeur numérique valide pour " + fieldName, Alert.AlertType.ERROR);
            return -1;
        }
    }

    private void naviguerVersAffichagePlanning() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichagePlanning.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) comboActivity.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger l'affichage des planifications : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        comboActivity.getSelectionModel().clearSelection();
        comboSalle.getSelectionModel().clearSelection();
        capacityMax.clear();
        nombreInscription.clear();
    }
}
