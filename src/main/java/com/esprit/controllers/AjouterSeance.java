package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.seance;
import com.esprit.services.SeanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AjouterSeance {

    // Déclaration des éléments du formulaire
    @FXML
    private TextField id;  // Champ ID
    @FXML
    private DatePicker date;  // Champ Date
    @FXML
    private TextField horaire;
    @FXML
    private TextField lieu;  // Champ Lieu
    @FXML
    private TextField programme_id;  // Champ Programme ID
    @FXML
    private TextField activite_id;  // Champ Activité ID

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
        if (id.getText().isEmpty() || date.getValue() == null || horaire.getText().isEmpty() ||
                lieu.getText().isEmpty() || programme_id.getText().isEmpty() || activite_id.getText().isEmpty()) {

            showAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs avant de soumettre le formulaire.", Alert.AlertType.ERROR);
            return;
        }

        try {
            SeanceService seanceService = new SeanceService();

            int idVal = Integer.parseInt(id.getText());
            LocalDate dateVal = date.getValue();
            String horaireText = horaire.getText();

            // ✅ Correction ici : Suppression de la double déclaration
            LocalTime horaireVal;
            try {
                horaireVal = LocalTime.parse(horaireText);
            } catch (DateTimeParseException e) {
                showAlert("Erreur", "Format d'horaire invalide", "Veuillez entrer un horaire au format HH:mm (ex: 09:30).", Alert.AlertType.ERROR);
                return;
            }

            String lieuVal = lieu.getText();
            int programmeIdVal = Integer.parseInt(programme_id.getText());
            int activiteIdVal = Integer.parseInt(activite_id.getText());

            seance newSeance = new seance(idVal, dateVal, horaireVal, lieuVal, programmeIdVal, activiteIdVal);
            seanceService.enregistrer(newSeance);

            showAlert("Succès", "Séance enregistrée", "La séance a été enregistrée avec succès.", Alert.AlertType.INFORMATION);

            // **Redirection vers AfficherSeance.fxml**
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/seance3.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) id.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide", "L'ID, Programme ID et Activité ID doivent être des nombres.", Alert.AlertType.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Correction : `showAlert` doit être DANS la classe
    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
