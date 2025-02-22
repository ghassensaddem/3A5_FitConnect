package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.application;
import com.esprit.services.AvisService;
import com.esprit.services.applicationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ModifierApplication {

    @FXML
    private TextField id;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private TextField idProgramme;
    @FXML
    private TextField idClient;
    @FXML
    private TextField idToDelete;
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
    public void setApplicationData(int applicationId, LocalDate applicationDateDebut, LocalDate applicationDateFin, int applicationProgrammeId, int applicationClientId) {
        id.setText(String.valueOf(applicationId));
        dateDebut.setValue(applicationDateDebut);
        dateFin.setValue(applicationDateFin);
        idProgramme.setText(String.valueOf(applicationProgrammeId));
        idClient.setText(String.valueOf(applicationClientId));
    }

    @FXML
    private void Modifier(ActionEvent event) {
        // Vérifier que les champs ne sont pas vides
        if (id.getText().isEmpty() || dateDebut.getValue() == null || dateFin.getValue() == null ||
                idProgramme.getText().isEmpty() || idClient.getText().isEmpty()) {

            // Afficher une alerte si les champs sont vides
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre le formulaire.");
            alert.showAndWait();
        } else {
            // Si tout est rempli, afficher un message de succès
            int applicationId = Integer.parseInt(id.getText());
            LocalDate applicationDateDebut = dateDebut.getValue();
            LocalDate applicationDateFin = dateFin.getValue();
            int applicationProgrammeId = Integer.parseInt(idProgramme.getText());
            int applicationClientId = Integer.parseInt(idClient.getText());
            if (applicationDateFin.isBefore(applicationDateDebut)) {
                showAlert("Erreur", "Date incorrecte", "La date de fin doit être après la date de début.", AlertType.ERROR);
                return;  // Sortir de la méthode si les dates sont incorrectes
            }
            // Créer un objet application avec les nouvelles valeurs
            application updatedApplication = new application(applicationId, applicationDateDebut, applicationDateFin, applicationProgrammeId, applicationClientId);

            // Appeler le service pour modifier l'application
            applicationService applicationService = new applicationService();
            boolean isUpdated = applicationService.modifier(updatedApplication);

            Alert alert = new Alert(AlertType.INFORMATION);
            if (isUpdated) {
                alert.setTitle("Succès");
                alert.setHeaderText("Application modifiée");
                alert.setContentText("L'application a été modifiée avec succès.");
                alert.showAndWait();
                NavigationHelper.changerPage(event, "/application3.fxml");
            } else {
                alert.setTitle("Échec");
                alert.setHeaderText("Application non modifiée");
                alert.setContentText("L'application n'a pas pu être modifiée.");
                alert.showAndWait();
            }
        }
    }


    @FXML
    public void Supprimer(ActionEvent actionEvent) {
        String idText = idToDelete.getText();  // Récupère l'ID depuis le champ texte

        if (idText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("ID manquant");
            alert.setContentText("Veuillez entrer un ID à supprimer.");
            alert.showAndWait();
            return;
        }

        try {
            int idValue = Integer.parseInt(idText); // Convertir en entier

            // Demander confirmation avant de supprimer
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Suppression de l'application");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette application ?");

            confirmation.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
                    // Appel au service pour supprimer l'avis
                    applicationService applicationService = new applicationService();
                    boolean isDeleted = applicationService.supprimer(idValue);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    if (isDeleted) {
                        alert.setTitle("Succès");
                        alert.setHeaderText("Application supprimée");
                        alert.setContentText("L'application a été supprimée avec succès.");
                    } else {
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Échec de la suppression");
                        alert.setContentText("L'application n'a pas pu être supprimée.");
                    }
                    alert.showAndWait();

                    // Rediriger vers la liste des séances après suppression
                    NavigationHelper.changerPage(actionEvent, "/application3.fxml");
                }
            });

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format invalide");
            alert.setContentText("L'ID doit être un nombre valide.");
            alert.showAndWait();
        }

    }
    private void showAlert(String erreur, String dateIncorrecte, String s, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Erreur");
        alert.setHeaderText(erreur);
        alert.setContentText(dateIncorrecte);
        alert.showAndWait();
    }
}



