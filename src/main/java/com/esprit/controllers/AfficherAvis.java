package com.esprit.controllers;

import com.esprit.models.Avis;

import com.esprit.models.NavigationHelper;
import com.esprit.services.AvisService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class AfficherAvis implements Initializable {

    @FXML
    private TableView<Avis> AvisTable;

    @FXML
    private TableColumn<Avis, Integer> colId;

    @FXML
    private TableColumn<Avis, String> colCommentaire;

    @FXML
    private TableColumn<Avis, String> colNote;

    @FXML
    private TableColumn<Avis, String> colseanceID;



    private ObservableList<Avis> data = FXCollections.observableArrayList();
    private AvisService AvisService = new AvisService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AvisTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        loadData();

        // Gestion du double-clic pour ouvrir la fenêtre de modification
        AvisTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleRowClick();
            }
        });
    }


    @FXML
    public void loadData() {
        data.clear(); // Nettoyer l'ObservableList avant de recharger les données

        // Charger les données depuis la base de données via SeanceService
        data.addAll(AvisService.rechercher());

        // Associer les colonnes aux attributs de l'entité Seance
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCommentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colseanceID.setCellValueFactory(new PropertyValueFactory<>("seanceId"));


        AvisTable.setItems(data); // Affichage des données dans la TableView
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        loadData();
        showAlert("Mise à jour", "Les données des avis ont été rafraîchies avec succès.");
    }


    private void handleRowClick() {
        Avis selectedAvis = AvisTable.getSelectionModel().getSelectedItem(); // Récupère l'avis sélectionné

        if (selectedAvis != null) {
            ouvrirPageModification(selectedAvis);  // Ouvre la page de modification
        }
    }

    private void ouvrirPageModification(Avis avis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/avis2.fxml"));

            Parent root = loader.load();

            // Récupérer le contrôleur de la page ModifierAvis
            ModifierAvis controller = loader.getController();
            controller.setAvisData(avis.getId(), avis.getCommentaire(), avis.getNote(), avis.getSeanceId());

            // Afficher la fenêtre de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Avis");
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
