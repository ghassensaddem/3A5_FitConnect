package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.application;
import com.esprit.services.applicationService;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AjouterApplication {

    // Déclaration des éléments du formulaire
    @FXML
    private TextField id;  // Champ ID
    @FXML
    private DatePicker dateDebut;  // Champ Date
    @FXML
    private DatePicker dateFin;  // Champ Date
    @FXML
    private TextField idProgramme;  // Champ Lieu
    @FXML
    private TextField idClient;  // Champ Programme ID

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
        if (id.getText().isEmpty() || dateDebut.getValue() == null || dateFin.getValue() == null ||
                idProgramme.getText().isEmpty() || idClient.getText().isEmpty() ) {

            // Afficher une alerte si les champs sont vides
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre le formulaire.");
            alert.showAndWait();
        } else {
            // Si tout est rempli, créer l'objet Séance et enregistrer dans la base de données
            applicationService applicationService = new applicationService ();

            // Récupérer les valeurs des champs
            int idVal = Integer.parseInt(id.getText());  // Récupérer l'ID
            LocalDate dateDebutVal = dateDebut.getValue();  // Récupérer la date en tant que LocalDate
            LocalDate dateFinText = dateFin.getValue();
            int idProgrammeVal = Integer.parseInt(idProgramme.getText());
            int idClientVal = Integer.parseInt(idClient.getText());
            if (dateFinText.isBefore(dateDebutVal)) {
                showAlert("Erreur", "Date incorrecte", "La date de fin doit être après la date de début.", AlertType.ERROR);
                return;  // Sortir de la méthode si les dates sont incorrectes
            }


            // Créer une nouvelle séance
            application newApplication = new application(idVal, dateDebutVal, dateFinText, idProgrammeVal, idClientVal);

            // Appeler la méthode pour enregistrer la séance
            applicationService.enregistrer(newApplication);

            // Afficher une alerte de succès
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Application enregistrée");
            alert.setContentText("La nouvelle application a été enregistrée avec succès.");
            alert.showAndWait();

            // Mettre à jour la TableView en appelant la méthode loadData du contrôleur AfficherSeance
            // Ici, il faudrait trouver un moyen de notifier le contrôleur d'affichage (comme passer une référence de ce contrôleur).
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application3.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) id.getScene().getWindow();  // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));  // Changer la scène vers AfficherSeance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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