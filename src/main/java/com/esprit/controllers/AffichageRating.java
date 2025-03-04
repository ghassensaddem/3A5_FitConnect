package com.esprit.controllers;

import com.esprit.models.RatingActivity;
import com.esprit.services.RatingService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Optional;

public class AffichageRating {
    @FXML
    private TableView<RatingActivity> ratingTable;
    @FXML
    private TableColumn<RatingActivity, Integer> colIdActivity;
    @FXML
    private TableColumn<RatingActivity, Integer> colIdSalle;
    @FXML
    private TableColumn<RatingActivity, String> colRating;
    @FXML
    private TableColumn<RatingActivity, String> colReview;
    @FXML
    private TableColumn<RatingActivity, Void> colActions;

    private final RatingService ratingService = new RatingService();
    private final ObservableList<RatingActivity> ratingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Utilisation des propriétés JavaFX pour les colonnes
        colIdActivity.setCellValueFactory(cellData -> cellData.getValue().idActivityProperty().asObject());
        colIdSalle.setCellValueFactory(cellData -> cellData.getValue().idSalleProperty().asObject());

        // Utilisation de getRatingStars() pour afficher les étoiles et review
        colRating.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getRatingStars())));
        colReview.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReview()));

        // Ajout des boutons Modifier / Supprimer
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox buttonsBox = new HBox(5, btnModifier, btnSupprimer);

            {
                btnModifier.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");

                btnModifier.setOnAction(event -> {
                    RatingActivity selectedRating = getTableView().getItems().get(getIndex());
                    ouvrirModifierRating(selectedRating);
                });

                btnSupprimer.setOnAction(event -> {
                    RatingActivity selectedRating = getTableView().getItems().get(getIndex());
                    supprimerRating(selectedRating);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });

        afficherRatings();
    }

    public void afficherRatings() {
        ratingList.clear();
        ratingList.addAll(ratingService.rechercher());
        ratingTable.setItems(ratingList);
    }

    private void supprimerRating(RatingActivity selectedRating) {
        if (selectedRating == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune évaluation sélectionnée", "Veuillez sélectionner une évaluation à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer cette évaluation ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ratingService.supprimer(selectedRating);
                afficherRatings();
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "L'évaluation a été supprimée avec succès.");
            } catch (Exception e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression : " + e.getMessage());
            }
        }
    }

    private void ouvrirModifierRating(RatingActivity selectedRating) {
        if (selectedRating == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner une évaluation à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierRating.fxml"));
            Parent root = loader.load();

            ModifierRating controller = loader.getController();
            controller.initData(selectedRating);

            Stage stage = (Stage) ratingTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger ModifierRating.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
