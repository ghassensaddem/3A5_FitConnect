package com.esprit.controllers;

import com.esprit.models.Avis;
import com.esprit.services.AvisService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherAvis1 {

    @FXML
    private GridPane usersGrid;

    private final AvisService avisService = new AvisService();
    private int seanceId;

    // Méthode pour afficher les avis dans le GridPane
    public void setAvisList(List<Avis> avisList) {
        usersGrid.getChildren().clear();  // Vider le GridPane avant d'ajouter les nouveaux avis

        int row = 0;
        int col = 0;
        int columns = 2;

        for (Avis avis : avisList) {
            VBox avisBox = new VBox();
            avisBox.setSpacing(5);
            avisBox.setStyle("-fx-padding: 20; -fx-border-radius: 15; -fx-border-color: #ddd; " +
                    "-fx-background-color: #8FBF87; -fx-border-width: 2; " +
                    "-fx-alignment: center; -fx-spacing: 10;");

            Label commentaireLabel = new Label("Commentaire: " + avis.getCommentaire());
            Label noteLabel = new Label("Note: " + avis.getNote() + "/10");

            commentaireLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            noteLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            HBox hBox = new HBox();
            Button modifierButton = new Button("Modifier Avis");
            Button supprimerButton = new Button("Supprimer Avis");

            modifierButton.setOnAction(event -> ouvrirModifierAvis(avis));
            supprimerButton.setOnAction(event -> supprimerAvis(avis));

            hBox.getChildren().addAll(modifierButton, supprimerButton);
            hBox.setSpacing(10);

            avisBox.getChildren().addAll(commentaireLabel, noteLabel, hBox);

            usersGrid.add(avisBox, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }
        usersGrid.requestLayout();
    }

    // Méthode pour ouvrir la fenêtre de modification d'un avis
    private void ouvrirModifierAvis(Avis avis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierAvis.fxml"));
            Parent root = loader.load();

            ModifierAvis1 modifierAvisController = loader.getController();
            modifierAvisController.setAvisToModify(avis);
            modifierAvisController.setAfficherAvisController(this); // Passer le contrôleur actuel

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Avis");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un avis
    private void supprimerAvis(Avis avis) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet avis ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean deleted = avisService.supprimer(avis.getId());
                if (deleted) {
                    System.out.println("L'avis a été supprimé.");
                    loadAvisForSeance(seanceId); // Rafraîchir la liste
                } else {
                    System.out.println("Erreur : impossible de supprimer l'avis.");
                }
            }
        });
    }

    public void loadAvisForSeance(int seanceId) {
        this.seanceId = seanceId;
        List<Avis> avisList = avisService.rechercherAvisParSeance(seanceId);
        setAvisList(avisList);
    }
}
