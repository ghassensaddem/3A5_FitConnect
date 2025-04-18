package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.Programme;
import com.esprit.services.HistoriqueService;
import com.esprit.services.ProgrammeService;
import com.esprit.tests.MainProg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AjouterProgramme implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private TextField prix;
    @FXML
    private TextField description;
    @FXML
    private TextField coach_id;

    @FXML
    private TextField lieu;

    int CoachID=18;
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
        NavigationHelper.changerPage(event, "/avis3.fxml");
    }

    @FXML
    private void allerVersAjouterProgramme(ActionEvent event) {
        NavigationHelper.changerPage(event, "/programme.fxml");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        id.setManaged(false);
        if (MainProg.idCoach!=0)
            CoachID=MainProg.idCoach;// Cache le champ ID dès l’ouverture
    }
    @FXML
    private void Validate(ActionEvent event) {
        // Vérification des champs vides
        if ( prix.getText().trim().isEmpty() ||
                description.getText().trim().isEmpty() ||  lieu.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Conversion des champs
            int idVal = 0;
            double prixVal = Double.parseDouble(prix.getText().trim());
            String descriptionVal = description.getText().trim();
            String lieuVal = lieu.getText().trim();

            // Création et enregistrement du programme
            Programme newProgramme = new Programme( prixVal, descriptionVal, CoachID,lieuVal);
            ProgrammeService programmeService = new ProgrammeService();


            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le programme a été enregistré avec succès.");

            // Rediriger vers programme3.fxml
            changerScene(event, "/programme3.fxml");
            programmeService.ajouter(newProgramme);
            HistoriqueService historiqueService = new HistoriqueService();
            historiqueService.ajouterHistorique("Ajout", "Programme",
                    "Programme ajouté : " +newProgramme.getId()  );
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer des valeurs numériques valides.");
        }
    }
    @FXML
    private void afficherHistorique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Historique.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Historique des actions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
