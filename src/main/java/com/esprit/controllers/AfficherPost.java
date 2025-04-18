package com.esprit.controllers;

import com.esprit.models.Post;
import com.esprit.services.PostService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.esprit.controllers.AfficherCommentaire;  // Importer le contrôleur AfficherCommentaire


import java.io.IOException;
import java.util.List;

public class AfficherPost {

    @FXML
    private VBox postVBox;

    @FXML
    private Button btnAddPost;

    private final PostService postService = new PostService();

    private int postId;

    @FXML
    public void initialize() {
        loadData();
    }

    @FXML
    public void loadData() {
        postVBox.getChildren().clear();
        List<Post> posts = postService.rechercher();

        for (Post post : posts) {
            VBox postContainer = new VBox(15); // Augmenter l'écart entre les éléments à 15px
            postContainer.setStyle(
                    "-fx-padding: 10px; " +
                            "-fx-border-radius: 10px; " +
                            "-fx-border-color: #e0e0e0; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);"
            );

            Label authorLabel = new Label("Ajouté par : " + post.getAuthor() +  "  le  " + post.getDate());
            authorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #053536;");

            // Ajouter un espace beaucoup plus large entre l'auteur et l'image
            Region spaceBetweenAuthorAndImage = new Region();
            VBox.setVgrow(spaceBetweenAuthorAndImage, javafx.scene.layout.Priority.ALWAYS);
            spaceBetweenAuthorAndImage.setPrefHeight(60); // Augmenter l'espace à 60 pixels

            // Content area (TextArea)
            TextArea contentTextArea = new TextArea(post.getContenu());
            contentTextArea.setEditable(false);  // Empêcher la modification du texte
            contentTextArea.setWrapText(true);   // Activer le retour à la ligne automatique
            contentTextArea.setStyle("-fx-font-size: 18px; -fx-text-fill: #333333;"); // Taille de texte plus grande
            contentTextArea.setMaxWidth(1000);   // Largeur maximale ajustée pour être beaucoup plus grande
            contentTextArea.setPrefHeight(1500); // Hauteur du TextArea encore plus grande

            // Image (si disponible)
            ImageView imageView = new ImageView();
            if (post.getImage() != null && !post.getImage().isEmpty()) {
                Image image = new Image("file:" + post.getImage());
                imageView.setImage(image);
                imageView.setFitHeight(300.0);  // Image beaucoup plus grande
                imageView.setFitWidth(500.0);   // Image beaucoup plus large
                imageView.setPreserveRatio(true);
            }

            // Region pour l'espace vertical entre les éléments
            Region verticalSpace = new Region();
            VBox.setVgrow(verticalSpace, javafx.scene.layout.Priority.ALWAYS);
            verticalSpace.setPrefHeight(15); // Ajuster la hauteur comme nécessaire

            // Boutons et actions (upvotes, downvotes, modifier, supprimer)
            Label upvotesLabel = new Label("Upvotes: " + post.getUpvotes());
            upvotesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

            Label downvotesLabel = new Label("Downvotes: " + post.getDownvotes());
            downvotesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #f44336;");

            ImageView likeImageView = new ImageView(new Image("/images/upvote.png"));
            likeImageView.setFitHeight(15.0);
            likeImageView.setFitWidth(15.0);
            Button upvoteButton = new Button();
            upvoteButton.setGraphic(likeImageView);
            upvoteButton.setOnAction(e -> incrementUpvote(post, upvotesLabel));

            ImageView dislikeImageView = new ImageView(new Image("/images/downvote.png"));
            dislikeImageView.setFitHeight(15.0);
            dislikeImageView.setFitWidth(15.0);
            Button downvoteButton = new Button();
            downvoteButton.setGraphic(dislikeImageView);
            downvoteButton.setOnAction(e -> incrementDownvote(post, downvotesLabel));



            Button btnEdit = new Button("Modifier");
            btnEdit.setOnAction(e -> openEditPost(post));

            Button btnDelete = new Button("Supprimer");
            btnDelete.setOnAction(e -> deletePost(post));

            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            HBox actionsContainer = new HBox(15, upvotesLabel, upvoteButton, downvotesLabel, downvoteButton, spacer, btnEdit, btnDelete);
            actionsContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            // Création du bouton "Commenter"
            ImageView commentButtonImageView = new ImageView(new Image("/images/comment.png"));
            commentButtonImageView.setFitHeight(30.0);
            commentButtonImageView.setFitWidth(30.0);
            Button commentButton = new Button();
            commentButton.setGraphic(commentButtonImageView);
            commentButton.setOnAction(e -> ouvrirpageCommentaire(post.getId()));


            // Création du bouton "Ajouter un Commentaire"
            Button addCommentButton = new Button("Ajouter un Commentaire");
            addCommentButton.setOnAction(e -> ouvrirFormulaireCommentaire(post.getId()));



            // Ajouter tous les éléments au conteneur du post
            postContainer.getChildren().addAll(authorLabel, spaceBetweenAuthorAndImage, contentTextArea, imageView, verticalSpace, actionsContainer,commentButton,addCommentButton);

            Label separator = new Label();
            separator.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 0 0 1px 0; -fx-padding: 10px 0 10px 0;");
            postContainer.getChildren().add(separator);

            postVBox.getChildren().add(postContainer);
        }
    }

    private void incrementUpvote(Post post, Label upvotesLabel) {
        post.setUpvotes(post.getUpvotes() + 1);
        postService.modifier(post);
        upvotesLabel.setText("Upvotes: " + post.getUpvotes());
    }

    private void incrementDownvote(Post post, Label downvotesLabel) {
        post.setDownvotes(post.getDownvotes() + 1);
        postService.modifier(post);
        downvotesLabel.setText("Downvotes: " + post.getDownvotes());
    }

    private void deletePost(Post post) {
        postService.supprimer(post);
        loadData();
    }

    private void openEditPost(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierposte.fxml"));
            Parent root = loader.load();
            ModifierPost controller = loader.getController();
            controller.initData(post);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Post");
            stage.setOnHiding(event -> loadData());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addPost() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterposte.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Post");
            stage.setOnHiding(event -> loadData());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void ouvrirpageCommentaire(int postId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichercommentaire.fxml"));
            Parent root = loader.load();

            // Passer l'ID du post au contrôleur AfficherCommentaire
            AfficherCommentaire controller = loader.getController();
            controller.setPostId(postId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Commentaires");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterCommentaire() {
        ouvrirFormulaireCommentaire(postId);
    }

    private void ouvrirFormulaireCommentaire(int postId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutercommentaire.fxml"));
            Parent root = loader.load();

            // Passer l'ID du post au contrôleur AjouterCommentaire
            AjouterCommentaire controller = loader.getController();
            controller.setPostId(postId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Commentaire");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
