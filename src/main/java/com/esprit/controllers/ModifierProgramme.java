package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.Programme;
import com.esprit.models.seance;
import com.esprit.services.AvisService;
import com.esprit.services.HistoriqueService;
import com.esprit.services.ProgrammeService;
import com.esprit.services.SeanceService;
import com.esprit.tests.MainProg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;


public class ModifierProgramme implements Initializable {


     // Champ ID
     @FXML
     private TextField id;
    @FXML
    private TextField prix;  // Champ Date
    @FXML
    private TextField description;  // Champ Horaire
    @FXML
    private TextField coach_id;
    @FXML
    private TextField lieu;

    int CoachID=18;
    // Champ Lieu
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
        id.setManaged(false);  // Cache le champ ID dès l’ouverture
        if(MainProg.idCoach!=0)
            CoachID= MainProg.idCoach;// Cache le champ IDです l’ouverture
    }
    // Méthode pour charger les données de la séance à modifier
    // Méthode pour charger les données du programme à modifier
    public void setProgrammeData(int ProgrammeId, Double ProgrammePrix, String ProgrammeDescription, String ProgrammeLieu) {
        // Remplir les champs avec les données existantes
        id.setText(String.valueOf(ProgrammeId));
        prix.setText(ProgrammePrix.toString());
        description.setText(ProgrammeDescription);
        lieu.setText(ProgrammeLieu);
    }



    // Méthode pour modifier la séance
    @FXML
    private void Modifier(ActionEvent event) {
        // Vérifier que les champs ne sont pas vides
        if (id.getText().isEmpty() || prix.getText().isEmpty() ||
                description.getText().isEmpty() || lieu.getText().isEmpty()) {

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
            String programmeLieu = lieu.getText();



            // Créer un objet programme avec les nouvelles valeurs
            Programme updatedProgramme = new Programme(programmeId, programmePrix, programmeDescription, CoachID, programmeLieu);

            // Appeler le service pour modifier le programme
            ProgrammeService programmeService = new ProgrammeService();
            boolean isUpdated = programmeService.modifier(updatedProgramme);
            Alert alert = new Alert(AlertType.INFORMATION);
            if (isUpdated) {

                alert.setTitle("Succès");
                alert.setHeaderText("Programme modifié");
                alert.setContentText("Le programme a été modifié avec succès.");
                alert.showAndWait();
                NavigationHelper.changerPage(event, "/programme3.fxml");

                HistoriqueService historiqueService = new HistoriqueService();
                historiqueService.ajouterHistorique("Modification", "Programme",
                        "Programme modifié : " + updatedProgramme.getId());
            } else {
                alert.setTitle("Erreur");
                alert.setHeaderText("Programme non modifié");
                alert.setContentText("Le programme n'a pas été modifié.");
                alert.showAndWait();
            }
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

}


