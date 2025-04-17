package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.Programme;
import com.esprit.services.ProgrammeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficherProgramme {

    @FXML
    private TableView<Programme> ProgrammeTable;

    @FXML
    private TableColumn<Programme, Integer> colId;

    @FXML
    private TableColumn<Programme, String> colPrix;

    @FXML
    private TableColumn<Programme, String> colDescription;

    @FXML
    private TableColumn<Programme, String> colCoach_id;

    private ObservableList<Programme> data = FXCollections.observableArrayList();
    private ProgrammeService ProgrammeService = new ProgrammeService();

    private boolean isFrontEnd = false;  // Variable pour vérifier le mode (front-end ou back-end)

    @FXML
    public void initialize() {
        ProgrammeTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        loadData();

        // Gérer le double-clic pour ouvrir la page de modification (si en mode back-end)
        ProgrammeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !isFrontEnd) {  // Activer seulement en mode back-end
                handleRowClick();
            }
        });
    }

    @FXML
    public void loadData() {
        data.clear();  // Nettoyer la liste avant de la remplir

        // Charger les données depuis la base de données via ProgrammeService
        data.addAll(ProgrammeService.rechercher());

        // Associer les colonnes aux attributs de l'entité Programme
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCoach_id.setCellValueFactory(new PropertyValueFactory<>("coach_id"));

        ProgrammeTable.setItems(data);  // Afficher les données dans la TableView
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        loadData();
        showAlert("Mise à jour", "Les données des programmes ont été rafraîchies avec succès.");
    }

    private void handleRowClick() {
        Programme selectedProgramme = ProgrammeTable.getSelectionModel().getSelectedItem();  // Récupérer le programme sélectionné

        if (selectedProgramme != null) {
            ouvrirPageModification(selectedProgramme);  // Ouvre la page de modification
        }
    }

    private void ouvrirPageModification(Programme programme) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/programme2.fxml"));
            Parent root = loader.load();

            // Passer le programme à la page de modification
            ModifierProgramme controller = loader.getController();
            controller.setProgrammeData(programme.getId(), programme.getPrix(), programme.getDescription(), programme.getCoach_id());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Programme");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la page de modification : " + e.getMessage());
        }
    }

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
