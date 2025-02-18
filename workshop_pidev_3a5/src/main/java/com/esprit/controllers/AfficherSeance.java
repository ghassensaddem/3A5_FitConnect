package com.esprit.controllers;

import com.esprit.models.NavigationHelper;
import com.esprit.models.seance;
import com.esprit.services.SeanceService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AfficherSeance implements Initializable {

    @FXML
    private TableView<seance> seanceTable;

    @FXML
    private TableColumn<seance, Integer> colId;

    @FXML
    private TableColumn<seance, String> colDate;

    @FXML
    private TableColumn<seance, String> colHoraire;

    @FXML
    private TableColumn<seance, String> colLieu;

    @FXML
    private TableColumn<seance, String> colProgramme_id;

    @FXML
    private TableColumn<seance, String> colActivite_id;

    private ObservableList<seance> data = FXCollections.observableArrayList();
    private SeanceService seanceService = new SeanceService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        seanceTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        loadData();

        // Gestion du double-clic pour ouvrir la fenêtre de modification
        seanceTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleRowClick();
            }
        });
    }

    @FXML
    public void loadData() {
        data.clear(); // Nettoyer l'ObservableList avant de recharger les données

        // Charger les données depuis la base de données via SeanceService
        data.addAll(seanceService.rechercher());

        // Associer les colonnes aux attributs de l'entité Seance
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().toString())  // Format LocalDate en String
        );

        colHoraire.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHoraire().toString())  // Format LocalTime en String
        );

        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colProgramme_id.setCellValueFactory(new PropertyValueFactory<>("programme_id"));
        colActivite_id.setCellValueFactory(new PropertyValueFactory<>("activite_id"));

        seanceTable.setItems(data); // Affichage des données dans la TableView
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        loadData();
        showAlert("Mise à jour", "Les données des séances ont été rafraîchies avec succès.");
    }

    private void handleRowClick() {
        seance selectedSeance = seanceTable.getSelectionModel().getSelectedItem(); // Récupère la séance sélectionnée

        if (selectedSeance != null) {
            ouvrirPageModification(selectedSeance);  // Ouvre la page de modification
        }
    }

    private void ouvrirPageModification(seance seance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/seance2.fxml"));

            Parent root = loader.load();

            // Récupérer le contrôleur de la page ModifierSeance
            ModifierSeance controller = loader.getController();

            controller.setSeanceData(seance.getId(), seance.getDate().toLocalDate(), seance.getHoraire().toLocalTime(), seance.getLieu(), seance.getProgramme_id(), seance.getActivite_id());

            // Afficher la fenêtre de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier une Séance");
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
