package com.esprit.controllers;

import com.esprit.models.Client;
import com.esprit.models.Post;
import com.esprit.services.ClientService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.esprit.BadWordsChecker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

public class AjouterPost {

    @FXML
    private TextField authorComboBox;
    @FXML
    private TextArea contentArea;
    @FXML
    private ImageView postImageView;

    private int ClientID=881;

    private String imagePath = ""; // Stocke le chemin de l'image sélectionnée

    public void initialize() {
        PostService postService = new PostService();
        ClientService clientService = new ClientService();
        List<String> emails = postService.getAllClientEmails();
        if(MainProg.idClient!=0)
            ClientID= MainProg.idClient;
        Client client = clientService.rechercherParId(ClientID);
        authorComboBox.setText(client.getEmail());

    }

    @FXML
    void importImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Vérification si c'est bien une image
                String mimeType = Files.probeContentType(selectedFile.toPath());
                if (mimeType == null || !mimeType.startsWith("image")) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Fichier non valide", "Veuillez sélectionner une image.");
                    return;
                }

                // Déplacer l'image vers le dossier "uploads"
                File destinationDir = new File("uploads");
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs();
                }

                File destinationFile = new File(destinationDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Stocker le chemin relatif de l'image
                imagePath = "uploads/" + selectedFile.getName();

                // Afficher l'image sélectionnée
                postImageView.setImage(new Image(destinationFile.toURI().toString()));

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Importation échouée", "Une erreur est survenue lors de l'importation de l'image.");
            }
        }
    }

    @FXML
    void Validate(ActionEvent event) throws IOException {
        String author = authorComboBox.getText();
        String content = contentArea.getText().trim();

        // Vérification de l'auteur
        if (author == null || author.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champ obligatoire", "Veuillez sélectionner un auteur.");
            return;
        }

        // Vérification qu'au moins un des deux (contenu ou image) est fourni
        if ((content == null || content.isEmpty()) && imagePath.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs manquants", "Veuillez ajouter au moins un contenu ou une image.");
            return;
        }

        // Vérifier si le contenu contient des mots interdits (uniquement si le contenu n'est pas vide)
        if (!content.isEmpty() && BadWordsChecker.containsBadWords(content)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Contenu interdit", "Le contenu contient des mots interdits !");
            System.out.println("Bad words detected!");
            return;
        } else if (!content.isEmpty()) {
            System.out.println("No bad words found.");
        }

        // Récupérer l'ID du client
        PostService postService = new PostService();
        int clientId = postService.getClientIdByEmail(author);

        // Vérifier si l'ID du client est valide
        if (clientId == -1) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Client non trouvé", "L'email sélectionné ne correspond à aucun client.");
            return;
        }

        // Vérifier si une image a été sélectionnée, sinon utiliser une image par défaut
        String imageUrl = imagePath.isEmpty() ? "images/icon.png" : imagePath;

        // Création du post
        Post newPost = new Post(author, content, 0, 0, imageUrl, LocalDate.now(), clientId);
        postService.ajouter(newPost);

        // Confirmation d'ajout
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Post ajouté", "Le post a été ajouté avec succès !");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherposte.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) contentArea.getScene().getWindow();

            // Utiliser le Stage actuel pour changer la scène
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("afficher Post");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode utilitaire pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}