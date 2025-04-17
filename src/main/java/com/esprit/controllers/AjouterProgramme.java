package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.Programme;
import com.esprit.services.ProgrammeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class AjouterProgramme {

    @FXML
    private TextField id;
    @FXML
    private TextField prix;
    @FXML
    private TextField description;
    @FXML
    private TextField coach_id;

    @FXML
    private void allerVersAjouterSeance(ActionEvent event) {
        NavigationHelper.changerPage(event, "/seance.fxml");
    }

    @FXML
    private void allerVersAjouterApplication(ActionEvent event) {
        NavigationHelper.changerPage(event, "/application.fxml");
    }

    @FXML
    private void allerVersAjouterAvis(ActionEvent event) {
        NavigationHelper.changerPage(event, "/avis.fxml");
    }

    @FXML
    private void allerVersAjouterProgramme(ActionEvent event) {
        NavigationHelper.changerPage(event, "/programme.fxml");
    }

    @FXML
    private void Validate(ActionEvent event) {
        // Vérification des champs vides
        if (id.getText().trim().isEmpty() || prix.getText().trim().isEmpty() ||
                description.getText().trim().isEmpty() || coach_id.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Conversion des champs
            int idVal = Integer.parseInt(id.getText().trim());
            double prixVal = Double.parseDouble(prix.getText().trim());
            String descriptionVal = description.getText().trim();
            int coach_idVal = Integer.parseInt(coach_id.getText().trim());

            // Création et enregistrement du programme
            Programme newProgramme = new Programme(idVal, prixVal, descriptionVal, coach_idVal);
            ProgrammeService programmeService = new ProgrammeService();
            programmeService.ajouter(newProgramme);


            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le programme a été enregistré avec succès.");

            // Rediriger vers programme3.fxml
            changerScene(event, "/programme3.fxml");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer des valeurs numériques valides.");
        }
    }

    private void changerScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) id.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du changement de scène : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
