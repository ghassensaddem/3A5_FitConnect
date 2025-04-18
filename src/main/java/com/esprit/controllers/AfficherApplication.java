package com.esprit.controllers;

import com.esprit.models.application;
import com.esprit.models.NavigationHelper;
import com.esprit.services.HistoriqueService;
import com.esprit.services.applicationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AfficherApplication implements Initializable {


    @FXML
    private TableView<application> applicationTable;

    @FXML
    private TableColumn<application, Integer> colId;

    @FXML
    private TableColumn<application, Date> colDateDebut;  // Changement ici

    @FXML
    private TableColumn<application, Date> colDateFin;  // Changement ici

    @FXML
    private TableColumn<application, Integer> colIdProgramme;  // Changement ici


    @FXML
    private TableColumn<application, Void> colAction;

    private final applicationService appService = new applicationService();

    private ObservableList<application> data = FXCollections.observableArrayList();
    private applicationService applicationService = new applicationService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ajouterBoutonSuppression(); // ✅ Ajoute les boutons "Supprimer" dans la TableView

        colId.setVisible(false);
        applicationTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        loadData();

        // Gestion du double-clic pour ouvrir la fenêtre de modification
        applicationTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleRowClick();
            }
        });
    }

    private void ajouterBoutonSuppression() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-border-radius: 5px;");
                deleteButton.setOnAction(event -> {
                    application app = getTableView().getItems().get(getIndex());
                    supprimerApplication(app);
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

        // Charger les données depuis la base de données via applicationService
        data.addAll(applicationService.rechercher());

        // Associer les colonnes aux attributs de l'entité application
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));  // Changement ici
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));  // Changement ici

        colIdProgramme.setCellValueFactory(new PropertyValueFactory<>("idProgramme"));  // Changement ici

        applicationTable.setItems(data); // Affichage des données dans la TableV
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        loadData();
        showAlert("Mise à jour", "Les données des applications ont été rafraîchies avec succès.");
    }

    void handleRowClick() {
        application selectedApplication = applicationTable.getSelectionModel().getSelectedItem(); // Récupère l'avis sélectionné

        if (selectedApplication != null) {
            ouvrirPageModification(selectedApplication);  // Ouvre la page de modification
        }
    }


    private void ouvrirPageModification(application application) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application2.fxml"));

            Parent root = loader.load();

            // Récupérer le contrôleur de la page ModifierAvis
            ModifierApplication controller = loader.getController();
            controller.setApplicationData(application.getId(), application.getDateDebut(), application.getDateFin(), application.getIdProgramme());

            // Afficher la fenêtre de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Application");
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
        NavigationHelper.changerPage(event, "/avis3.fxml");
    }

    @FXML
    private void allerVersAjouterProgramme(ActionEvent event) {
        NavigationHelper.changerPage(event, "/programme.fxml");
    }

    private void supprimerApplication(application app) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer l'application ?");
        confirmation.setContentText("Voulez-vous vraiment supprimer cette application ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean isDeleted = appService.supprimer(app.getId()); // ✅ Utilisation de appService
                if (isDeleted) {
                    showAlert("Succès", "L'application a été supprimée avec succès !");
                    loadData(); // ✅ Recharge la TableView
                    HistoriqueService historiqueService = new HistoriqueService();
                    historiqueService.ajouterHistorique("Suppression", "Application",
                            "Application supprimé:  " + app.getId());
                } else {
                    showAlert("Erreur", "Impossible de supprimer l'application.");
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
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




