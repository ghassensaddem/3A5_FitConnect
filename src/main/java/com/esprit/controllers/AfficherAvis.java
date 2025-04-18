package com.esprit.controllers;

import com.esprit.models.Avis;
import com.esprit.models.NavigationHelper;
import com.esprit.services.AvisService;
import com.esprit.services.HistoriqueService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML
    private TableColumn<Avis, Void> colAction;

    private ObservableList<Avis> data = FXCollections.observableArrayList();
    private AvisService AvisService = new AvisService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ajouterBoutonSuppression(); // Ajoute les boutons "Supprimer" dans la TableView
        colId.setVisible(false);
        AvisTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        loadData();
    }

    private void ajouterBoutonSuppression() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-border-radius: 5px;");
                deleteButton.setOnAction(event -> {
                    Avis avis = getTableView().getItems().get(getIndex());
                    supprimerAvis(avis);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    @FXML
    public void loadData() {
        data.clear(); // Nettoyer l'ObservableList avant de recharger les données
        data.addAll(AvisService.rechercher()); // Charger les données depuis la base de données

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCommentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colseanceID.setCellValueFactory(new PropertyValueFactory<>("seanceId"));

        AvisTable.setItems(data);
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        loadData();
        showAlert("Mise à jour", "Les données des avis ont été rafraîchies avec succès.");
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
        NavigationHelper.changerPage(event, "/avis3.fxml");
    }

    @FXML
    private void allerVersAjouterProgramme(ActionEvent event) {
        NavigationHelper.changerPage(event, "/programme.fxml");
    }

    private void supprimerAvis(Avis avis) {
        System.out.println("ID de l'avis à supprimer: " + avis.getId());

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer l'avis ?");
        confirmation.setContentText("Voulez-vous vraiment supprimer cet avis ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean isDeleted = AvisService.supprimer(avis.getId());
                if (isDeleted) {
                    showAlert("Succès", "L'avis a été supprimé avec succès !");
                    loadData();
                    HistoriqueService historiqueService = new HistoriqueService();
                    historiqueService.ajouterHistorique("Suppression", "Avis",
                            "Avis supprimé:  " + avis.getId());
                } else {
                    showAlert("Erreur", "Impossible de supprimer l'avis.");
                }
            }
        });
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
