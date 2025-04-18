package com.esprit.controllers;

import com.esprit.models.Commentaire;
import com.esprit.services.CommentaireService;
import com.esprit.services.PostService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AjouterCommentaire {

    @FXML
    private ComboBox<String> authorcomComboBox;
    @FXML
    private TextArea contentcomArea;
    @FXML
    private VBox postVBox;

    private int postId; // Stocke l'ID du post sélectionné

    // Setter pour récupérer l'ID du post depuis AfficherPost.java
    public void setPostId(int postId) {
        this.postId = postId;
        System.out.println("ID du post sélectionné : " + postId); // Debug
    }

    @FXML
    public void initialize() {
        // Récupérer les emails des clients
        PostService postService = new PostService();
        List<String> emails = postService.getAllClientEmails();

        // Ajouter les emails dans le ComboBox
        authorcomComboBox.getItems().addAll(emails);
    }

    @FXML
    void Validate(ActionEvent event) throws IOException {
        String author = authorcomComboBox.getSelectionModel().getSelectedItem();
        String content = contentcomArea.getText().trim();

        // Vérification des champs vides
        if (author == null || author.isEmpty() || content.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }

        // Récupérer l'ID du client à partir de l'email sélectionné
        PostService postService = new PostService();
        int clientId = postService.getClientIdByEmail(author);

        // Vérifier si l'ID du client est valide
        if (clientId == -1) {
            showAlert("Erreur", "Client non trouvé avec cet email.", Alert.AlertType.ERROR);
            return;
        }

        // Vérification si l'ID du post est bien défini
        if (postId == 0) {
            showAlert("Erreur", "Impossible d'ajouter un commentaire sans post sélectionné.", Alert.AlertType.ERROR);
            return;
        }

        // Création du commentaire
        Commentaire newCom = new Commentaire(author, content, 0, 0,LocalDate.now(),postId, clientId);

        // Service pour ajouter le commentaire
        CommentaireService comservice = new CommentaireService();
        comservice.ajouter(newCom);

        // Affichage de la confirmation
        showAlert("Confirmation", "Commentaire ajouté avec succès !", Alert.AlertType.INFORMATION);

        // Navigation vers AfficherPost
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherposte.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur de la vue AfficherPost
        AfficherPost afficherPostController = loader.getController();


        // Changer de scène
        Stage stage = (Stage) postVBox.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}