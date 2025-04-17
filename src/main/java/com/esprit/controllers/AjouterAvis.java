package com.esprit.controllers;

import com.esprit.models.Avis;
import com.esprit.models.NavigationHelper;
import com.esprit.models.seance;
import com.esprit.services.AvisService;
import com.esprit.services.SeanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;

public class AjouterAvis {

    // Déclaration des éléments du formulaire
    @FXML
    private TextField id;  // Champ ID
    @FXML
    private TextField commentaire;  // Champ Date
    @FXML
    private TextField note;
    @FXML
    private TextField seanceID;

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
        // Vérifier que les champs ne sont pas vides
        if (id.getText().isEmpty() || commentaire.getText().isEmpty() || note.getText().isEmpty() ||
                seanceID.getText().isEmpty() ) {

            // Afficher une alerte si les champs sont vides
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre le formulaire.");
            alert.showAndWait();
        } else {
            // Si tout est rempli, créer l'objet Séance et enregistrer dans la base de données
            AvisService avisService = new AvisService();

            // Récupérer les valeurs des champs
            int idVal = Integer.parseInt(id.getText());  // Récupérer l'ID
            String commentaireVal = commentaire.getText();  // Récupérer la date en tant que LocalDate
            int noteVal = Integer.parseInt(note.getText());
            int seanceIDVal = Integer.parseInt(seanceID.getText());


            // Créer une nouvelle séance
            Avis newAvis = new Avis(idVal, commentaireVal, noteVal, seanceIDVal);

            // Appeler la méthode pour enregistrer la séance
            avisService.enregistrer(newAvis);

            // Afficher une alerte de succès
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Avis enregistrée");
            alert.setContentText("L'avis a été enregistrée avec succès.");
            alert.showAndWait();

            // Mettre à jour la TableView en appelant la méthode loadData du contrôleur AfficherSeance
            // Ici, il faudrait trouver un moyen de notifier le contrôleur d'affichage (comme passer une référence de ce contrôleur).
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/avis3.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) id.getScene().getWindow();  // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));  // Changer la scène vers AfficherSeance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}