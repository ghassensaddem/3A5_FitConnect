package com.esprit.controllers;

import com.esprit.models.Commentaire;
import com.esprit.services.CommentaireService;
import com.esprit.services.PostService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherCommentaire {

    @FXML
    private VBox commentVBox;  // Conteneur des commentaires
    @FXML
    private Label noCommentsLabel;  // Label pour afficher un message "Aucun commentaire"
    @FXML
    private Button ajouterCommentaireButton; // Bouton pour ajouter un commentaire
    @FXML
    private Button backToForumButton;  // Bouton pour revenir au forum

    private final PostService postService = new PostService();
    private final CommentaireService comService = new CommentaireService();
    private int postId;  // Variable pour stocker l'ID du post

    @FXML
    public void initialize() {
        loadData();
    }

    // Cette méthode est appelée pour définir l'ID du post et charger les commentaires
    public void setPostId(int postId) {
        this.postId = postId;  // Stocker l'ID du post
        loadData();  // Charger les commentaires associés à ce post
    }

    // Charger et afficher les commentaires associés au post
    public void loadData() {
        commentVBox.getChildren().clear();  // Vider la VBox avant d'ajouter les commentaires

        // Utiliser la méthode afficherCommentairesParPost pour obtenir les commentaires du post
        List<Commentaire> commentaires = postService.afficherCommentairesParPost(postId);

        // Si aucun commentaire n'est trouvé, afficher le message
        if (commentaires.isEmpty()) {
            noCommentsLabel.setVisible(true);  // Afficher le message "Aucun commentaire"
        } else {
            noCommentsLabel.setVisible(false);  // Cacher le message si des commentaires existent
        }

        // Afficher les commentaires
        for (Commentaire commentaire : commentaires) {
            VBox commentaireContainer = new VBox(20); // Espacement plus large pour les commentaires
            commentaireContainer.setStyle(
                    "-fx-padding: 15px; " +  // Augmenter le padding
                            "-fx-border-radius: 10px; " +
                            "-fx-border-color: #e0e0e0; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);"
            );

            commentaireContainer.setPrefWidth(1200);  // Augmenter la largeur du conteneur du commentaire

            // Ajouter les informations sur le commentaire
            Label authorLabel = new Label("Commenté par : " + commentaire.getAuthor());
            authorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #053536;");

            Label contentLabel = new Label(commentaire.getContenu());
            contentLabel.setStyle("-fx-font-size: 14px;");

            Label dateLabel = new Label("Le : " + commentaire.getDate().toString());
            dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: grey;");

            // Labels pour les upvotes et downvotes
            Label upvotesLabel = new Label("Upvotes: " + commentaire.getUpvotes());
            upvotesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

            Label downvotesLabel = new Label("Downvotes: " + commentaire.getDownvotes());
            downvotesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #f44336;");

            // Boutons pour les votes
            ImageView likeImageView = new ImageView(new Image("/images/upvote.png"));
            likeImageView.setFitHeight(20.0);
            likeImageView.setFitWidth(20.0);
            Button upvoteButton = new Button();
            upvoteButton.setGraphic(likeImageView);
            upvoteButton.setOnAction(e -> incrementUpvote(commentaire, upvotesLabel));

            ImageView dislikeImageView = new ImageView(new Image("/images/downvote.png"));
            dislikeImageView.setFitHeight(20.0);
            dislikeImageView.setFitWidth(20.0);
            Button downvoteButton = new Button();
            downvoteButton.setGraphic(dislikeImageView);
            downvoteButton.setOnAction(e -> incrementDownvote(commentaire, downvotesLabel));

            // Boutons de modification et suppression
            Button btnEdit = new Button("Modifier");
            btnEdit.setOnAction(e -> openEditCommentaire(commentaire));

            Button btnDelete = new Button("Supprimer");
            btnDelete.setOnAction(e -> deleteCommentaire(commentaire));

            // HBox pour les upvotes et downvotes (alignement horizontal)
            HBox votesContainer = new HBox(10);  // Espacement entre les éléments
            votesContainer.setStyle("-fx-alignment: center;");

            // Ajouter les labels et boutons pour les votes
            votesContainer.getChildren().addAll(upvotesLabel, upvoteButton, downvotesLabel, downvoteButton);

            // VBox pour les actions (alignement vertical des boutons Modifier et Supprimer)
            VBox actionsContainer = new VBox(10);  // Espacement vertical entre les boutons
            actionsContainer.setStyle("-fx-alignment: center;");

            // Ajouter les boutons Modifier et Supprimer à la VBox
            actionsContainer.getChildren().addAll(btnEdit, btnDelete);

            // HBox pour contenir les votes et actions
            HBox container = new HBox(20);  // Espacement entre les votes et actions
            container.setStyle("-fx-alignment: center;");
            container.getChildren().addAll(votesContainer, actionsContainer);

            // Ajouter les éléments dans le conteneur du commentaire
            commentaireContainer.getChildren().addAll(authorLabel, contentLabel, dateLabel, container);

            // Ajouter le conteneur du commentaire à la VBox
            commentVBox.getChildren().add(commentaireContainer);
        }
    }

    private void incrementUpvote(Commentaire commentaire, Label upvotesLabel) {
        commentaire.setUpvotes(commentaire.getUpvotes() + 1);
        comService.modifier(commentaire);
        upvotesLabel.setText("Upvotes: " + commentaire.getUpvotes());
    }

    private void incrementDownvote(Commentaire commentaire, Label downvotesLabel) {
        commentaire.setDownvotes(commentaire.getDownvotes() + 1);
        comService.modifier(commentaire);
        downvotesLabel.setText("Downvotes: " + commentaire.getDownvotes());
    }

    private void deleteCommentaire(Commentaire commentaire) {
        comService.supprimer(commentaire);
        loadData();
    }

    private void openEditCommentaire(Commentaire commentaire) {
        try {
            // Charger le fichier FXML de modification de commentaire
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifiercommentaire.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur du fichier FXML
            ModifierCommentaire controller = loader.getController();

            // Passer le commentaire à modifier au contrôleur
            controller.initData(commentaire);

            // Créer et afficher la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Commentaire");

            // Recharger les données des commentaires lorsque la fenêtre est fermée
            stage.setOnHiding(event -> loadData());

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToForum() {
        try {
            // Charger le fichier FXML du forum
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherposte.fxml"));
            Parent root = loader.load();

            // Créer et afficher la fenêtre du forum
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Forum");

            // Fermer la fenêtre actuelle (commentaire)
            Stage currentStage = (Stage) backToForumButton.getScene().getWindow();
            currentStage.close();

            // Afficher la fenêtre du forum
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
