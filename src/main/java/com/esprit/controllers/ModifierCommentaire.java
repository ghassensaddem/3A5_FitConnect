package com.esprit.controllers;

import com.esprit.models.Commentaire;
import com.esprit.services.CommentaireService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierCommentaire {

    @FXML
    private TextField txtcomId, txtcomAuthor, txtcomImage;
    @FXML
    private TextArea txtcomContent;
    @FXML
    private Button btncomModifier;

    private final CommentaireService commentaireService = new CommentaireService();
    private Commentaire currentCommentaire;

    public void initData(Commentaire commentaire) {
        this.currentCommentaire = commentaire;
        txtcomId.setText(String.valueOf(commentaire.getId()));
        txtcomAuthor.setText(commentaire.getAuthor());
        txtcomContent.setText(commentaire.getContenu());

    }

    @FXML
    private void modifierCommentaire() {
        try {
            // Mettre à jour les attributs de currentCommentaire avec les nouvelles données saisies
            currentCommentaire.setAuthor(txtcomAuthor.getText());  // Met à jour l'auteur du commentaire
            currentCommentaire.setContenu(txtcomContent.getText());  // Met à jour le contenu du commentaire


            // Appel à la méthode de service pour effectuer la modification dans la base de données
            commentaireService.modifier(currentCommentaire);

            // Affichage d'un message de succès
            showAlert("Modification réussie", "Le commentaire a été mis à jour avec succès.");

            // Fermer la fenêtre de modification
            Stage stage = (Stage) btncomModifier.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la modification.");
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
