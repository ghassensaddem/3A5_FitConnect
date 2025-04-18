package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.seance;
import com.esprit.services.ActivityService;
import com.esprit.services.HistoriqueService;
import com.esprit.services.ProgrammeService;
import com.esprit.services.SeanceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.security.GeneralSecurityException;

public class AjouterSeance implements Initializable {

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
        NavigationHelper.changerPage(event, "/programme.fxml");}
    @FXML
    private TextField id;
    @FXML
    private DatePicker date;
    @FXML
    private TextField horaire;

    @FXML
    private ComboBox<Integer> programme_id;
    @FXML
    private ComboBox<Integer>activite_id;



    // Initialisation de l'interface
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setManaged(false);
        chargerProgrammes();
        chargerSeance();

    }

    // Charger les programmes
    private void chargerProgrammes() {
        ProgrammeService programmeService = new ProgrammeService();
        List<Integer> programmeIds = programmeService.getAllProgrammeIds();
        programme_id.getItems().addAll(programmeIds);
    }

    private void chargerSeance() {
        ProgrammeService programmeService = new ProgrammeService();
        List<Integer> programmeIds = programmeService.getAllActivityIds();
        activite_id.getItems().addAll(programmeIds);
    }

    // Méthode pour récupérer et afficher les événements du calendrier


    // Méthode pour valider et enregistrer une séance
    @FXML
    private void Validate(ActionEvent event) {
        if (date.getValue() == null || horaire.getText().isEmpty() || programme_id.getValue() == null || activite_id.getValue() == null) {
            showAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs avant de soumettre le formulaire.", Alert.AlertType.ERROR);
            return;
        }

        try {
            SeanceService seanceService = new SeanceService();
            int idVal = 0;
            LocalDate dateVal = date.getValue();
            String horaireText = horaire.getText();
            LocalTime horaireVal;

            try {
                horaireVal = LocalTime.parse(horaireText);
            } catch (DateTimeParseException e) {
                showAlert("Erreur", "Format d'horaire invalide", "Veuillez entrer un horaire au format HH:mm (ex: 09:30).", Alert.AlertType.ERROR);
                return;
            }

            int programmeIdVal = programme_id.getValue();
            int activiteIdVal = activite_id.getValue();

            seance newSeance = new seance(dateVal, horaireVal, programmeIdVal, activiteIdVal);
            seanceService.enregistrer(newSeance);
            HistoriqueService historiqueService = new HistoriqueService();
            historiqueService.ajouterHistorique("Ajout", "Seance", "Seance ajouté: " + newSeance.getId());

            showAlert("Succès", "Séance enregistrée", "La séance a été enregistrée avec succès.", Alert.AlertType.INFORMATION);

            // Redirection vers AfficherSeance.fxml
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

    // Affichage des alertes
    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}