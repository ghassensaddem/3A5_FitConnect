package com.esprit.controllers;

import com.esprit.models.Avis;
import com.esprit.models.NavigationHelper;
import com.esprit.services.AvisService;
import com.esprit.services.SeanceService;
import com.esprit.services.applicationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ModifierAvis {

    // Déclaration des éléments du formulaire
    @FXML
    private TextField idToDelete;
    @FXML
    private TextField id;  // Champ ID
    @FXML
    private TextField commentaire;  // Champ Date
    @FXML
    private TextField note;  // Champ Horaire
    @FXML
    private TextField seanceID;  // Champ Lieu
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
    // Méthode pour charger les données de l'avis à modifier
    public void setAvisData(int AvisId, String AvisCommentaire, int AvisNote, int AvisSeanceID) {
        // Remplir les champs avec les données existantes
        id.setText(String.valueOf(AvisId));
        commentaire.setText(AvisCommentaire); // Affectation correcte
        note.setText(String.valueOf(AvisNote)); // Affectation correcte
        seanceID.setText(String.valueOf(AvisSeanceID));
    }

    // Méthode pour modifier l'avis
    @FXML
    private void Modifier(ActionEvent event) {
        // Vérifier que les champs ne sont pas vides
        if (id.getText().isEmpty() || commentaire.getText().isEmpty() ||
                note.getText().isEmpty() || seanceID.getText().isEmpty()) {

            // Afficher une alerte si les champs sont vides
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre le formulaire.");
            alert.showAndWait();
        } else {
            // Si tout est rempli, afficher un message de succès
            int AvisId = Integer.parseInt(id.getText());
            String AvisCommentaire = commentaire.getText();
            int Avisnote = Integer.parseInt(note.getText());
            int AvisSeanceID = Integer.parseInt(seanceID.getText());
            // Créer un objet avis avec les nouvelles valeurs
            Avis updatedAvis = new Avis(AvisId, AvisCommentaire, Avisnote, AvisSeanceID);

            // Appeler le service pour modifier l'avis
            AvisService avisService = new AvisService();
            boolean isUpdated = avisService.modifier(updatedAvis);
            Alert alert = new Alert(AlertType.INFORMATION);
            if (isUpdated) {

                alert.setTitle("Succès");
                alert.setHeaderText("Avis modifiée");
                alert.setContentText("L'avis a été modifiée avec succès.");
                alert.showAndWait();
                NavigationHelper.changerPage(event, "/avis3.fxml");
            }
            else {
                alert.setTitle("Echec");
                alert.setHeaderText("Avis non modifiée");
                alert.setContentText("L'avis n'a pas pu étre modifiée.");
                alert.showAndWait();
            }
        }
    }

    // Méthode pour supprimer un avis
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
            confirmation.setHeaderText("Suppression de l'avis");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet avis ?");

            confirmation.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
                    // Appel au service pour supprimer l'avis
                    AvisService avisService = new AvisService();
                    boolean isDeleted = avisService.supprimer(idValue);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    if (isDeleted) {
                        alert.setTitle("Succès");
                        alert.setHeaderText("Avis supprimée");
                        alert.setContentText("L'avis a été supprimée avec succès.");
                    } else {
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Échec de la suppression");
                        alert.setContentText("L'avis n'a pas pu être supprimée.");
                    }
                    alert.showAndWait();

                    // Rediriger vers la liste des séances après suppression
                    NavigationHelper.changerPage(actionEvent, "/avis3.fxml");
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
