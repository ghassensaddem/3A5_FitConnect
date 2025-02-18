package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.Programme;
import com.esprit.models.seance;
import com.esprit.services.AvisService;
import com.esprit.services.ProgrammeService;
import com.esprit.services.SeanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;


public class ModifierProgramme {

    @FXML
    private TextField idToDelete;
    @FXML
    private TextField id;  // Champ ID
    @FXML
    private TextField prix;  // Champ Date
    @FXML
    private TextField description;  // Champ Horaire
    @FXML
    private TextField coach_id;  // Champ Lieu
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
    // Méthode pour charger les données de la séance à modifier
    public void setProgrammeData(int ProgrammeId, Double  ProgrammePrix, String  ProgrammeDescription, int  ProgrammeCoach_id) {
        // Remplir les champs avec les données existantes
        id.setText(String.valueOf( ProgrammeId));


        prix.setText( ProgrammePrix.toString()); // Affectation correcte
description.setText( ProgrammeDescription); // Affectation correcte
coach_id.setText(String.valueOf( ProgrammeCoach_id));

    }


    // Méthode pour modifier la séance
    @FXML
    private void Modifier(ActionEvent event) {
        // Vérifier que les champs ne sont pas vides
        if (id.getText().isEmpty()  || prix.getText().isEmpty() ||
                description.getText().isEmpty() || coach_id.getText().isEmpty() ) {

            // Afficher une alerte si les champs sont vides
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre le formulaire.");
            alert.showAndWait();
        } else {
            // Si tout est rempli, afficher un message de succès
            int programmeId = Integer.parseInt(id.getText());
            Double programmePrix = Double.parseDouble(prix.getText());
            String programmeDescription = description.getText();
            int programmeCoach_id = Integer.parseInt(coach_id.getText());
            // Créer un objet séance avec les nouvelles valeurs
           Programme updatedProgramme = new Programme(programmeId, programmePrix, programmeDescription, programmeCoach_id);

            // Appeler le service pour modifier la séance
            ProgrammeService ProgrammeService = new ProgrammeService();
            boolean isUpdated =  ProgrammeService.modifier(updatedProgramme);
            Alert alert = new Alert(AlertType.INFORMATION);
            if (isUpdated) {

                alert.setTitle("Succès");
                alert.setHeaderText("Séance modifiée");
                alert.setContentText("Le programme a été modifiée avec succès.");
                alert.showAndWait();
                NavigationHelper.changerPage(event, "/programme3.fxml");

                // Ici, tu pourrais ajouter des actions pour mettre à jour les données dans une base de données
            }
            else {
                alert.setTitle("Erreur");
                alert.setHeaderText("Séance non modifiée");
                alert.setContentText("Le programme n'a pas été modifiée.");
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
            confirmation.setHeaderText("Suppression du programme");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce programme ?");

            confirmation.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
                    // Appel au service pour supprimer l'avis
                    ProgrammeService programmeService = new ProgrammeService();
                    boolean isDeleted = programmeService.supprimer(idValue);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    if (isDeleted) {
                        alert.setTitle("Succès");
                        alert.setHeaderText("Programme supprimée");
                        alert.setContentText("Le programme a été supprimée avec succès.");
                    } else {
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Échec de la suppression");
                        alert.setContentText("Le programme n'a pas pu être supprimée.");
                    }
                    alert.showAndWait();

                    // Rediriger vers la liste des séances après suppression
                    NavigationHelper.changerPage(actionEvent, "/programme3.fxml");
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
}


