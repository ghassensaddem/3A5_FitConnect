package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.seance;
import com.esprit.services.HistoriqueService;
import com.esprit.services.ProgrammeService;
import com.esprit.services.SeanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierSeance implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private DatePicker date;
    @FXML
    private TextField horaire;

    @FXML
    private ComboBox<Integer> programme_id;
    @FXML
    private ComboBox<Integer> activite_id;

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
        chargerProgrammes();// Cache le champ ID dès l’ouverture
    }
    private void chargerProgrammes() {
        ProgrammeService programmeService = new ProgrammeService();
        List<Integer> programmeIds = programmeService.getAllProgrammeIds();  // Appel correct
        programme_id.getItems().addAll(programmeIds);
    }
    public void setSeanceData(int seanceId, LocalDate seanceDate, LocalTime seanceHoraire, int programmeId, int activiteId) {
        id.setText(String.valueOf(seanceId));
        date.setValue(seanceDate);
        horaire.setText(seanceHoraire != null ? seanceHoraire.toString() : "");

        programme_id.setValue(programmeId);
        activite_id.setValue(activiteId);
    }

    @FXML
    private void Modifier(ActionEvent event) {
        if (id.getText().isEmpty() || date.getValue() == null || horaire.getText().isEmpty() ||
                 programme_id.getValue() == null || activite_id.getValue() == null) {
            showAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs avant de soumettre le formulaire.", Alert.AlertType.ERROR);
            return;
        }
        try {
            int seanceId = Integer.parseInt(id.getText());
            LocalDate dateSeance = date.getValue();
            LocalTime horaireSeance = LocalTime.parse(horaire.getText(), DateTimeFormatter.ISO_LOCAL_TIME);

            int programmeId = programme_id.getValue();
            int activiteId = activite_id.getValue();

            seance updatedSeance = new seance(seanceId, dateSeance, horaireSeance,  programmeId, activiteId);
            SeanceService seanceService = new SeanceService();
            boolean isUpdated = seanceService.modifier(updatedSeance);
            HistoriqueService historiqueService = new HistoriqueService();
            historiqueService.ajouterHistorique("Modification", "Seance",
                    "Seance modifié : " + updatedSeance.getId());

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

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
