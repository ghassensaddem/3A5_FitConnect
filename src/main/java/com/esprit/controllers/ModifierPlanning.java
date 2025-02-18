package com.esprit.controllers;

import com.esprit.models.PlanningActivity;
import com.esprit.services.PlanningService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class ModifierPlanning {
    @FXML
    private TextField capacityMax;
    @FXML
    private TextField nombreInscription;

    private PlanningActivity currentPlanning;
    private final PlanningService planningService = new PlanningService();

    public void initData(PlanningActivity planning) {
        if (planning == null) return;
        this.currentPlanning = planning;

        // Remplissage des champs avec les données actuelles de la planification
        capacityMax.setText(String.valueOf(planning.getCapacityMax()));
        nombreInscription.setText(String.valueOf(planning.getNombreInscription()));
    }

    @FXML
    private void enregistrerModification() {
        if (currentPlanning == null) return;

        // Vérification de l'entrée utilisateur
        try {
            int newCapacityMax = validateAndParseInt(capacityMax.getText(), "Capacité maximale");
            int newNombreInscription = validateAndParseInt(nombreInscription.getText(), "Nombre d'inscriptions");

            if (newCapacityMax == -1 || newNombreInscription == -1) return; // Erreur déjà affichée

            if (newCapacityMax <= 0) {
                showError("La capacité maximale doit être un nombre positif.");
                return;
            }

            if (newNombreInscription < 0) {
                showError("Le nombre d'inscriptions ne peut pas être négatif.");
                return;
            }

            if (newNombreInscription > newCapacityMax) {
                showError("Le nombre d'inscriptions ne peut pas dépasser la capacité maximale.");
                return;
            }

            // Mise à jour de la planification avec les nouvelles valeurs
            currentPlanning.setCapacityMax(newCapacityMax);
            currentPlanning.setNombreInscription(newNombreInscription);

            // Sauvegarde dans la base de données
            planningService.modifier(currentPlanning);

            // Afficher un message de succès
            showSuccess("Planification mise à jour avec succès !");

            // Revenir à AffichagePlanning après modification
            naviguerVersAffichagePlanning();

        } catch (NumberFormatException e) {
            showError("Veuillez entrer des valeurs numériques valides.");
        }
    }

    private int validateAndParseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            showError("Veuillez entrer une valeur numérique valide pour " + fieldName + ".");
            return -1; // Indique une erreur dans la conversion
        }
    }

    private void naviguerVersAffichagePlanning() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichagePlanning.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) capacityMax.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Erreur de chargement de la page de planification.");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
