package com.esprit.controllers;

import com.esprit.models.SalleSportif;
import com.esprit.services.SalleSportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;

public class AffichageSalle {
    @FXML
    private TableView<SalleSportif> salleTable;
    @FXML
    private TableColumn<SalleSportif, Integer> colId;
    @FXML
    private TableColumn<SalleSportif, String> colNom;
    @FXML
    private TableColumn<SalleSportif, String> colAdresse;
    @FXML
    private TableColumn<SalleSportif, LocalTime> colOuverture;
    @FXML
    private TableColumn<SalleSportif, LocalTime> colFermeture;
    @FXML
    private TableColumn<SalleSportif, Integer> colCapacite;
    @FXML
    private TableColumn<SalleSportif, Void> colActions;

    private final SalleSportService salleService = new SalleSportService();
    private final ObservableList<SalleSportif> sallesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idSalle"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomSalle"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("addresseSalle"));
        colOuverture.setCellValueFactory(new PropertyValueFactory<>("horaireOuverture"));
        colFermeture.setCellValueFactory(new PropertyValueFactory<>("horaireFermeture"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox buttonsBox = new HBox(10, btnModifier, btnSupprimer);

            {
                btnModifier.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");

                btnModifier.setOnAction(event -> {
                    SalleSportif selectedSalle = getTableView().getItems().get(getIndex());
                    ouvrirModifierSalle(selectedSalle);
                });

                btnSupprimer.setOnAction(event -> {
                    SalleSportif selectedSalle = getTableView().getItems().get(getIndex());
                    supprimerSalle(selectedSalle);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                }
            }
        });

        afficherSalles();
    }

    public void afficherSalles() {
        sallesList.clear();
        sallesList.addAll(salleService.rechercher());
        salleTable.setItems(sallesList);
    }

    private void supprimerSalle(SalleSportif selectedSalle) {
        if (selectedSalle == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune salle sélectionnée", "Veuillez sélectionner une salle à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer cette salle ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                salleService.supprimer(selectedSalle);
                afficherSalles();
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "La salle a été supprimée avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la salle.");
            }
        }
    }

    private void ouvrirModifierSalle(SalleSportif selectedSalle) {
        if (selectedSalle == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Erreur", "Aucune salle sélectionnée !");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierSalle.fxml"));
            Parent root = loader.load();

            ModifierSalle controller = loader.getController();
            controller.initData(selectedSalle);

            // Définir le callback pour rafraîchir après modification
            controller.setOnModificationComplete(this::refreshTable);

            Stage stage = new Stage();
            stage.setTitle("Modifier Salle");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ModifierSalle.fxml : " + e.getMessage());
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la page de modification.");
        }
    }


    private void afficherAlerte(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    public void enregistrerModification(ActionEvent actionEvent) {
        SalleSportif selectedSalle = salleTable.getSelectionModel().getSelectedItem();

        if (selectedSalle == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner une salle à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierSalle.fxml"));
            Parent root = loader.load();

            // Récupération du contrôleur de la fenêtre ModifierSalle
            ModifierSalle controller = loader.getController();
            controller.initData(selectedSalle);

            // Création et affichage de la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Modifier Salle");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Attend la fermeture de la fenêtre avant de continuer

            // Rafraîchir la table après modification
            refreshTable();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ModifierSalle.fxml : " + e.getMessage());
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }

    private void refreshTable() {
        salleTable.getItems().clear(); // Effacer les anciennes données
        afficherSalles();

    }

}
