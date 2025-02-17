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
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AjouterCommentaire {

    @FXML
    private ComboBox<String> authorcomComboBox;
    @FXML
    private TextArea contentcomArea;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.show();
            return;
        }

        // Récupérer l'ID du client à partir de l'email sélectionné
        PostService postService = new PostService();
        int clientId = postService.getClientIdByEmail(author);

        // Vérifier si l'ID du client est valide
        if (clientId == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Client non trouvé avec cet email.");
            alert.show();
            return;
        }

        // Création du commentaire sans date, la base gère la date avec CURRENT_TIMESTAMP
        Commentaire newCom = new Commentaire(author, content, 0, 0, LocalDate.now(), 51, clientId);

        // Service pour ajouter le commentaire
        CommentaireService comservice = new CommentaireService();
        comservice.ajouter(newCom);

        // Affichage de la confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Commentaire ajouté avec succès !");
        alert.show();

        // Navigation vers AfficherCommentaire
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichercommentaire.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) authorcomComboBox.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
