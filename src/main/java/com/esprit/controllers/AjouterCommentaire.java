package com.esprit.controllers;

import com.esprit.BadWordsChecker;
import com.esprit.models.Client;
import com.esprit.models.Commentaire;
import com.esprit.services.ClientService;
import com.esprit.services.CommentaireService;
import com.esprit.services.PostService;
import com.esprit.tests.MainProg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AjouterCommentaire {

    @FXML
    private TextField authorcomComboBox;
    @FXML
    private TextArea contentcomArea;
    @FXML
    private VBox postVBox;

    private int postId; // Stocke l'ID du post sélectionné

    private int ClientID=881;

    // Setter pour récupérer l'ID du post depuis AfficherPost.java
    public void setPostId(int postId) {
        this.postId = postId;
        System.out.println("ID du post sélectionné : " + postId); // Debug
    }

    @FXML
    public void initialize() {
        // Récupérer les emails des clients
        PostService postService = new PostService();
        ClientService clientService = new ClientService();
        List<String> emails = postService.getAllClientEmails();

        if(MainProg.idClient!=0)
            ClientID= MainProg.idClient;
        Client client = clientService.rechercherParId(ClientID);
        authorcomComboBox.setText(client.getEmail());


    }

    @FXML
    void Validate(ActionEvent event) throws IOException {
        String author = authorcomComboBox.getText();
        String content = contentcomArea.getText().trim();

        // Vérification des champs vides
        if (author == null || author.isEmpty()) {
            showAlert("Erreur", "champ auteur est vide.", Alert.AlertType.ERROR);
            return;
        }
        if (content.isEmpty()){
            showAlert("Erreur", "le contenu ne peut pas etre vide.", Alert.AlertType.ERROR);
            return;
        }

        // Vérifier si le contenu contient des mots interdits
        if (BadWordsChecker.containsBadWords(content)) {
            showAlert("Erreur", "Le contenu contient des mots interdits !", Alert.AlertType.ERROR);
            System.out.println("Bad words detected!");
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
        Stage stage = (Stage) contentcomArea.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("afficherposte");
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