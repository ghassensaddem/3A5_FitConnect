package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.seance;
import com.esprit.services.SeanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModifierSeance {
    @FXML
    private TextField idToDelete;
    @FXML
    private TextField id;
    @FXML
    private DatePicker date;
    @FXML
    private TextField horaire;
    @FXML
    private TextField lieu;
    @FXML
    private TextField programme_id;
    @FXML
    private TextField activite_id;

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

    public void setSeanceData(int seanceId, LocalDate seanceDate, LocalTime seanceHoraire, String seanceLieu, int programmeId, int activiteId) {
        id.setText(String.valueOf(seanceId));
        date.setValue(seanceDate);
        horaire.setText(seanceHoraire != null ? seanceHoraire.toString() : "");
        lieu.setText(seanceLieu);
        programme_id.setText(String.valueOf(programmeId));
        activite_id.setText(String.valueOf(activiteId));
    }

    @FXML
    private void Modifier(ActionEvent event) {
        if (id.getText().isEmpty() || date.getValue() == null || horaire.getText().isEmpty() ||
                lieu.getText().isEmpty() || programme_id.getText().isEmpty() || activite_id.getText().isEmpty()) {
            showAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs avant de soumettre le formulaire.", Alert.AlertType.ERROR);
            return;
        }
        try {
            int seanceId = Integer.parseInt(id.getText());
            LocalDate dateSeance = date.getValue();
            LocalTime horaireSeance = LocalTime.parse(horaire.getText(), DateTimeFormatter.ISO_LOCAL_TIME);
            String lieuSeance = lieu.getText();
            int programmeId = Integer.parseInt(programme_id.getText());
            int activiteId = Integer.parseInt(activite_id.getText());

            seance updatedSeance = new seance(seanceId, dateSeance, horaireSeance, lieuSeance, programmeId, activiteId);
            SeanceService seanceService = new SeanceService();
            boolean isUpdated = seanceService.modifier(updatedSeance);

            if (isUpdated) {
                showAlert("Succès", "Séance modifiée", "La séance a été modifiée avec succès.", Alert.AlertType.INFORMATION);
                NavigationHelper.changerPage(event, "/seance3.fxml");
            } else {
                showAlert("Erreur", "Séance non modifiée", "La séance n'a pas pu être modifiée.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide", "L'ID, Programme ID et Activité ID doivent être des nombres.", Alert.AlertType.ERROR);
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format d'horaire invalide", "Veuillez entrer un horaire au format HH:mm (ex: 09:30).", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void Supprimer(ActionEvent actionEvent) {
        String idText = idToDelete.getText();
        if (idText.isEmpty()) {
            showAlert("Erreur", "ID manquant", "Veuillez entrer un ID à supprimer.", Alert.AlertType.ERROR);
            return;
        }
        try {
            int idValue = Integer.parseInt(idText);
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer cette séance ?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    SeanceService seanceService = new SeanceService();
                    boolean isDeleted = seanceService.supprimer(idValue);
                    if (isDeleted) {
                        showAlert("Succès", "Séance supprimée", "La séance a été supprimée avec succès.", Alert.AlertType.INFORMATION);
                        NavigationHelper.changerPage(actionEvent, "/seance3.fxml");
                    } else {
                        showAlert("Erreur", "Échec de la suppression", "La séance n'a pas pu être supprimée.", Alert.AlertType.ERROR);
                    }
                }
            });
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide", "L'ID doit être un nombre valide.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
