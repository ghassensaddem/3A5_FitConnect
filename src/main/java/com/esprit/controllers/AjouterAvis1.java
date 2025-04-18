package com.esprit.controllers;

import com.esprit.models.Avis;
import com.esprit.services.AvisService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterAvis1 {
    private int seanceId;
    @FXML
    private TextField commentaireField;

    @FXML
    private TextField noteField;

    @FXML
    private Button enregistrerButton;

    // Instance du service pour gérer les avis
    private AvisService avisService = new AvisService();

    @FXML
    public void initialize() {
        // Définir l'action à effectuer lorsqu'on clique sur le bouton enregistrer
        enregistrerButton.setOnAction(event -> enregistrerAvis());
    }

    public void setSeanceId(int seanceId) {
        this.seanceId = seanceId;
    }
    public void ouvrirAjouterAvis(int seanceId) {
        try {
            // Load the FXML for adding reviews
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterAvis.fxml"));
            Parent root = loader.load();

            // Get the controller and set the seance ID
            AjouterAvis1 ajouterAvisController = loader.getController();
            ajouterAvisController.setSeanceId(seanceId);

            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Ajouter un avis");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Make the window modal
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            // You could show an alert here
        }
    }

    // This method would be connected to your "Ajouter Avis" button in your FXML


    // Méthode qui enregistre un nouvel avis
    @FXML
    private void enregistrerAvis() {
        String commentaire = commentaireField.getText();
        String noteText = noteField.getText();
        System.out.println("Valeur de noteText : [" + noteText + "]");

        // Validation des champs
        if (commentaire.isEmpty() || noteText.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis", Alert.AlertType.ERROR);
            return;
        }

        // Conversion de la note en entier
        int note;
        try {
            note = Integer.parseInt(noteText);

            // Vérifier que la note est dans une plage valide
            if (note < 0 || note > 10) {
                showAlert("Erreur", "La note doit être comprise entre 0 et 10", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La note doit être un nombre entier valide", Alert.AlertType.ERROR);
            return;
        }

        // Créer l'objet Avis
        Avis avis = new Avis(commentaire, note, this.seanceId);

        try {
            // Enregistrer l'avis
            avisService.ajouter(avis);

            // Afficher l'alerte de succès
            showAlert("Succès", "L'avis a été ajouté avec succès !", Alert.AlertType.INFORMATION);
            System.out.println("Avis ajouté avec succès !");

            // 🔹 Mise à jour dynamique de AfficherAvis1
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/listeAvis.fxml"));
                Parent root = loader.load();
                AfficherAvis1 afficherAvis1Controller = loader.getController();

                // Update the view directly
                afficherAvis1Controller.loadAvisForSeance(seanceId); // Rafraîchir la liste des avis

                // Fermer la fenêtre actuelle
                Stage stage = (Stage) enregistrerButton.getScene().getWindow();
                stage.close();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors du chargement de la liste des avis", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            // En cas d'exception lors de l'ajout
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'avis: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Méthode pour afficher les alertes
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}