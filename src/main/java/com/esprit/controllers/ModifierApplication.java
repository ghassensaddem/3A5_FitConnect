package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.application;
import com.esprit.services.HistoriqueService;
import com.esprit.services.ProgrammeService;
import com.esprit.services.applicationService;
import com.esprit.tests.MainProg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierApplication implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private ComboBox<Integer> idProgramme;

    int ClientID=881;

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
        chargerProgrammes();
        if(MainProg.idClient!=0)
            ClientID= MainProg.idClient;
    }

    private void chargerProgrammes() {
        ProgrammeService programmeService = new ProgrammeService();
        List<Integer> programmeIds = programmeService.getAllProgrammeIds();  // Appel correct
        idProgramme.getItems().addAll(programmeIds);
    }

    public void setApplicationData(int applicationId, LocalDate applicationDateDebut, LocalDate applicationDateFin, int applicationProgrammeId) {
        id.setText(String.valueOf(applicationId));
        dateDebut.setValue(applicationDateDebut);
        dateFin.setValue(applicationDateFin);
        idProgramme.setValue(applicationProgrammeId);  // Correction ici

    }

    @FXML
    private void Modifier(ActionEvent event) {
        // Vérifier que les champs ne sont pas vides
        if (id.getText() == null || id.getText().isEmpty() ||
                dateDebut.getValue() == null || dateFin.getValue() == null ||
                idProgramme.getValue() == null
        ) {

            showAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs avant de soumettre le formulaire.", AlertType.ERROR);
            return;
        }

        // Récupérer les valeurs
        int applicationId = Integer.parseInt(id.getText());
        LocalDate applicationDateDebut = dateDebut.getValue();
        LocalDate applicationDateFin = dateFin.getValue();
        int idProgrammeVal = idProgramme.getValue();


        // Vérifier que la date de fin est après la date de début
        if (applicationDateFin.isBefore(applicationDateDebut)) {
            showAlert("Erreur", "Date incorrecte", "La date de fin doit être après la date de début.", AlertType.ERROR);
            return;
        }

        // Créer un objet application avec les nouvelles valeurs
        application updatedApplication = new application(applicationId, applicationDateDebut, applicationDateFin, idProgrammeVal, ClientID);

        // Appeler le service pour modifier l'application
        applicationService applicationService = new applicationService();
        boolean isUpdated = applicationService.modifier(updatedApplication);

        if (isUpdated) {
            showAlert("Succès", "Application modifiée", "L'application a été modifiée avec succès.", AlertType.INFORMATION);
            NavigationHelper.changerPage(event, "/application3.fxml");
        } else {
            showAlert("Échec", "Application non modifiée", "L'application n'a pas pu être modifiée.", AlertType.ERROR);
        }

        HistoriqueService historiqueService = new HistoriqueService();
        historiqueService.ajouterHistorique("Modification", "Programme",
                "Programme modifié : " + updatedApplication.getId());

    }

    private void showAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
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

}
