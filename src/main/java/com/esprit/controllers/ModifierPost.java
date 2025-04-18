package com.esprit.controllers;

import com.esprit.models.Post;
import com.esprit.services.PostService;
import com.esprit.BadWordsChecker;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField; // Ajouté pour gérer txtId
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ModifierPost {

    @FXML
    private TextArea txtContent;
    /*@FXML
    private TextField txtId; // Le champ texte pour afficher l'ID*/
    @FXML
    private Button btnModifier, btnImportImagemodif;
    @FXML
    private ImageView postImageViewmodif;

    private final PostService postService = new PostService();
    private Post currentPost;
    private String imagePath = ""; // Chemin de l'image sélectionnée

    public void initData(Post post) {
        this.currentPost = post;
        txtContent.setText(post.getContenu());
       // txtId.setText(String.valueOf(post.getId())); // Affichage de l'ID dans le champ texte txtId

        // Afficher l'image actuelle du post directement à partir de l'URL
        String imageUrl = post.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // Utiliser l'URL pour afficher l'image dans l'ImageView
                postImageViewmodif.setImage(new Image("file:" + imageUrl));  // "file:" pour indiquer un chemin de fichier local
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "L'image ne peut pas être affichée.");
            }
        } else {
            // Si l'URL est vide, afficher une image par défaut
            postImageViewmodif.setImage(new Image("default-image.png"));
        }
    }

    @FXML
    private void importImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Déplacer l'image dans le dossier du projet (ex: dossier "uploads")
                File destinationDir = new File("uploads");
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs();
                }

                File destinationFile = new File(destinationDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Stocker le chemin relatif
                imagePath = "uploads/" + selectedFile.getName();

                // Afficher l'image sélectionnée
                postImageViewmodif.setImage(new Image(destinationFile.toURI().toString()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    private void modifierPost() {
        try {
            // Vérifier si le contenu contient des gros mots
            if (!txtContent.getText().isEmpty() && BadWordsChecker.containsBadWords(txtContent.getText())) {
                showAlert("Erreur", "Le contenu contient des mots interdits !");
                System.out.println("Bad words detected!");
                return;
            } else if (!txtContent.getText().isEmpty()) {
                System.out.println("No bad words found.");
            }

            currentPost.setContenu(txtContent.getText());
            currentPost.setImage(imagePath.isEmpty() ? currentPost.getImage() : imagePath); // Garde l'image existante si pas modifiée

            postService.modifier(currentPost);

            showAlert("Modification réussie", "Le post a été mis à jour avec succès.");

            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherposte.fxml"));
            Parent root = loader.load();


            Stage currentStage = (Stage) btnModifier.getScene().getWindow();


            // Utiliser le Stage actuel pour changer la scène
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("afficher Post");
            currentStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }







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
