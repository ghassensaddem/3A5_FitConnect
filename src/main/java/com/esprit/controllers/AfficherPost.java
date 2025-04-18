package com.esprit.controllers;

import com.esprit.models.Post;
import com.esprit.services.PostService;
import com.esprit.services.CommentaireService;
import com.esprit.tests.MainProg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
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
import com.esprit.services.Vote_PostService1;



import java.io.IOException;
import java.util.List;

public class AfficherPost {

    @FXML
    private VBox postVBox;

    @FXML
    private Button btnAddPost;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button btnchatbot;


    private int ClientID=871;







    private final PostService postService = new PostService();
    private final Vote_PostService1 voteService = new Vote_PostService1();
    private final CommentaireService commentaireService = new CommentaireService();



    private int postId;

    @FXML
    public void initialize() {

        loadData();
        if(MainProg.idClient!=0)
            ClientID= MainProg.idClient;


    }

    @FXML
    public void loadData() {
        postVBox.getChildren().clear();
        List<Post> posts = postService.rechercher();

        displayPosts(posts);}


    @FXML
    public void searchPosts() {
        String searchQuery = searchField.getText().trim(); // Récupérer le texte de recherche
        if (searchQuery.isEmpty()) {
            loadData(); // Si la recherche est vide, afficher tous les posts
        } else {
            // Appeler la méthode rechercherParAuteur pour récupérer les posts correspondants
            List<Post> posts = postService.rechercherParAuteur(searchQuery); // Recherche par auteur
            displayPosts(posts); // Afficher les posts filtrés
        }
    }

    public void trier() {

            // Appeler la méthode rechercherParAuteur pour récupérer les posts correspondants
            List<Post> posts = postService.trierParDate(); // Recherche par auteur
            displayPosts(posts); // Afficher les posts filtrés
        }



    private void displayPosts(List<Post> posts) {
        postVBox.getChildren().clear(); // Effacer les anciens résultats

        for (Post post : posts) {
            VBox postContainer = new VBox(15); // Augmenter l'écart entre les éléments à 15px
            postContainer.setStyle(
                    "-fx-padding: 10px; " +
                            "-fx-border-radius: 10px; " +
                            "-fx-border-color: #b0b0b0; " +  // Couleur du contour plus foncée
                            "-fx-border-width: 2px; " +      // Largeur du bord
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);"
            );


            Label authorLabel = new Label("Ajouté par : " + post.getAuthor() +  "  le  " + post.getDate());
            authorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #053536;");

            // Ajouter un label pour afficher le nombre de commentaires
            int nombreCommentaires = commentaireService.getNombreCommentairesParPost(post.getId());
            Label commentairesLabel = new Label("Commentaires: " + nombreCommentaires);
            commentairesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

            // Ajouter un espace beaucoup plus large entre l'auteur et l'image
            Region spaceBetweenAuthorAndImage = new Region();
            VBox.setVgrow(spaceBetweenAuthorAndImage, Priority.ALWAYS);
            spaceBetweenAuthorAndImage.setPrefHeight(60); // Augmenter l'espace à 60 pixels

            // Content area (TextArea)
            TextArea contentTextArea = new TextArea(post.getContenu());
            contentTextArea.setEditable(false);  // Empêcher la modification du texte
            contentTextArea.setWrapText(true);   // Activer le retour à la ligne automatique
            contentTextArea.setStyle(
                    "-fx-font-size: 18px; " +          // Taille de texte plus grande
                            "-fx-text-fill: #333333; " +       // Couleur du texte
                            "-fx-control-inner-background: #f0f0f0;" // Couleur de fond grise
            );
            contentTextArea.setMaxWidth(1000);   // Largeur maximale ajustée
            //contentTextArea.setPrefHeight(Region.USE_COMPUTED_SIZE); // Hauteur automatique en fonction du contenu
            contentTextArea.setMinHeight(50); // Hauteur minimale
            contentTextArea.setPrefHeight(150); // Hauteur préférée réduite
            contentTextArea.setMaxHeight(300); // Hauteur maximale pour éviter qu'il devienne trop grand

            contentTextArea.setMinHeight(Region.USE_PREF_SIZE);







            // Ajouter une région pour l'espace entre le contenu et l'image
            Region spaceBetweenContentAndImage = new Region();
            spaceBetweenContentAndImage.setPrefHeight(5); // Espace de 30 pixels (ajustez selon vos besoins)

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
            VBox.setVgrow(verticalSpace, Priority.ALWAYS);
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
            upvoteButton.setOnAction(e -> incrementUpvote(post, upvotesLabel, downvotesLabel));

            ImageView dislikeImageView = new ImageView(new Image("/images/downvote.png"));
            dislikeImageView.setFitHeight(15.0);
            dislikeImageView.setFitWidth(15.0);
            Button downvoteButton = new Button();
            downvoteButton.setGraphic(dislikeImageView);
            downvoteButton.setOnAction(e -> incrementDownvote(post, upvotesLabel, downvotesLabel));



            Button btnSortByDate = new Button("trier");


            // Appliquer un effet hover en CSS via un event
            btnSortByDate.setOnMouseEntered(e -> btnSortByDate.setStyle(
                    "-fx-background-color: #45a049; " + // Vert plus foncé au survol
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-padding: 10px;"
            ));
            btnSortByDate.setOnMouseExited(e -> btnSortByDate.setStyle(
                    "-fx-background-color: #4CAF50; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-padding: 10px;"
            ));
            btnSortByDate.setOnAction(e -> trier()); // Correct the method call


            Button searchButton = new Button("Chercher");
            searchButton.setStyle(
                    "-fx-background-color: #4CAF50; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-padding: 10px;"
            );

            // Appliquer un effet hover en CSS via un event
            searchButton.setOnMouseEntered(e -> searchButton.setStyle(
                    "-fx-background-color: #45a049; " + // Vert plus foncé au survol
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-padding: 10px;"
            ));
            searchButton.setOnMouseExited(e -> searchButton.setStyle(
                    "-fx-background-color: #4CAF50; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-padding: 10px;"
            ));
            searchButton.setOnAction(e -> searchPosts()); // Correct the method call

            if(post.getClientId()== ClientID){
                Button btnEdit = new Button("Modifier");
                btnEdit.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                );

                // Appliquer un effet hover en CSS via un event
                btnEdit.setOnMouseEntered(e -> btnEdit.setStyle(
                        "-fx-background-color: #45a049; " + // Vert plus foncé au survol
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                ));
                btnEdit.setOnMouseExited(e -> btnEdit.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                ));
                btnEdit.setOnAction(e -> {
                    Stage currentStage = (Stage) btnEdit.getScene().getWindow();
                    openEditPost(post, currentStage);
                });

                Button btnDelete = new Button("Supprimer");
                btnDelete.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                );

                // Appliquer un effet hover en CSS via un event
                btnDelete.setOnMouseEntered(e -> btnDelete.setStyle(
                        "-fx-background-color: #45a049; " + // Vert plus foncé au survol
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                ));
                btnDelete.setOnMouseExited(e -> btnDelete.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                ));
                btnDelete.setOnAction(e -> deletePost(post));
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                HBox actionsContainer = new HBox(15, upvotesLabel, upvoteButton, downvotesLabel, downvoteButton, spacer, btnEdit, btnDelete);
                actionsContainer.setAlignment(Pos.CENTER_LEFT);
                // Création du bouton "Commenter"
                ImageView commentButtonImageView = new ImageView(new Image("/images/comment.png"));
                commentButtonImageView.setFitHeight(30.0);
                commentButtonImageView.setFitWidth(30.0);
                Button commentButton = new Button();
                commentButton.setGraphic(commentButtonImageView);
                commentButton.setOnAction(e -> ouvrirpageCommentaire(post.getId()));




                // Création du bouton "Ajouter un Commentaire"
                Button addCommentButton = new Button("Ajouter un Commentaire");

                addCommentButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                );

                // Appliquer un effet hover en CSS via un event
                addCommentButton.setOnMouseEntered(e -> addCommentButton.setStyle(
                        "-fx-background-color: #45a049; " + // Vert plus foncé au survol
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                ));
                addCommentButton.setOnMouseExited(e -> addCommentButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                ));
                addCommentButton.setOnAction(e -> ouvrirFormulaireCommentaire(post.getId()));



                // Ajouter tous les éléments au conteneur du post
                postContainer.getChildren().addAll(authorLabel, contentTextArea,spaceBetweenContentAndImage, imageView, verticalSpace, actionsContainer,commentButton,addCommentButton,commentairesLabel);

                Label separator = new Label();
                separator.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 0 0 1px 0; -fx-padding: 10px 0 10px 0;");
                postContainer.getChildren().add(separator);

                postVBox.getChildren().add(postContainer);

            }
            else{
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                HBox actionsContainer = new HBox(15, upvotesLabel, upvoteButton, downvotesLabel, downvoteButton, spacer);
                actionsContainer.setAlignment(Pos.CENTER_LEFT);
                // Création du bouton "Commenter"
                ImageView commentButtonImageView = new ImageView(new Image("/images/comment.png"));
                commentButtonImageView.setFitHeight(30.0);
                commentButtonImageView.setFitWidth(30.0);
                Button commentButton = new Button();
                commentButton.setGraphic(commentButtonImageView);
                commentButton.setOnAction(e -> ouvrirpageCommentaire(post.getId()));




                // Création du bouton "Ajouter un Commentaire"
                Button addCommentButton = new Button("Ajouter un Commentaire");

                addCommentButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                );

                // Appliquer un effet hover en CSS via un event
                addCommentButton.setOnMouseEntered(e -> addCommentButton.setStyle(
                        "-fx-background-color: #45a049; " + // Vert plus foncé au survol
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                ));
                addCommentButton.setOnMouseExited(e -> addCommentButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5px; " +
                                "-fx-padding: 10px;"
                ));
                addCommentButton.setOnAction(e -> ouvrirFormulaireCommentaire(post.getId()));



                // Ajouter tous les éléments au conteneur du post
                postContainer.getChildren().addAll(authorLabel, contentTextArea,spaceBetweenContentAndImage, imageView, verticalSpace, actionsContainer,commentButton,addCommentButton,commentairesLabel);

                Label separator = new Label();
                separator.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 0 0 1px 0; -fx-padding: 10px 0 10px 0;");
                postContainer.getChildren().add(separator);

                postVBox.getChildren().add(postContainer);
            }








        }
    }


    private void refreshVotes(Post post, Label upvotesLabel, Label downvotesLabel) {
        // Récupérer les nouveaux nombres de votes depuis la base de données
        int newUpvotes = voteService.countUpvotes(post.getId());
        int newDownvotes = voteService.countDownvotes(post.getId());

        // Mettre à jour les valeurs du post
        post.setUpvotes(newUpvotes);
        post.setDownvotes(newDownvotes);

        // Rafraîchir les labels d'affichage
        upvotesLabel.setText("Upvotes: " + newUpvotes);
        downvotesLabel.setText("Downvotes: " + newDownvotes);
    }





    private void incrementUpvote(Post post, Label upvotesLabel, Label downvotesLabel) {;
        if (ClientID != -1) {
            int currentVote = voteService.getVoteType(post.getId(), ClientID);

            if (currentVote == 1) {
                voteService.voter(post.getId(), ClientID, 0); // Annuler l'upvote
            } else {
                voteService.voter(post.getId(), ClientID, 1); // Ajouter un upvote
            }

            // Rafraîchir l'affichage après la mise à jour du vote
            refreshVotes(post, upvotesLabel, downvotesLabel);
        } else {
            System.out.println("Client introuvable pour l'email : " + post.getAuthor());
        }
    }

    private void incrementDownvote(Post post, Label upvotesLabel, Label downvotesLabel) {
        if (ClientID != -1) {
            int currentVote = voteService.getVoteType(post.getId(), ClientID);

            if (currentVote == -1) {
                voteService.voter(post.getId(), ClientID, 0); // Annuler le downvote
            } else {
                voteService.voter(post.getId(), ClientID, -1); // Ajouter un downvote
            }

            // Rafraîchir l'affichage après la mise à jour du vote
            refreshVotes(post, upvotesLabel, downvotesLabel);
        } else {
            System.out.println("Client introuvable pour l'email : " + post.getAuthor());
        }
    }


    private void deletePost(Post post) {
        postService.supprimer(post);
        loadData();
    }

    private void openEditPost(Post post, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierposte.fxml"));
            Parent root = loader.load();
            ModifierPost controller = loader.getController();
            controller.initData(post);



            // Utiliser le Stage actuel pour changer la scène
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Modifier un Post");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void addPost() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterposte.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) postVBox.getScene().getWindow();


            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Ajouter un Post");
            currentStage.setOnHiding(event -> loadData());
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void ouvrirchatbot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatbot.fxml"));
            Parent root = loader.load();

            Stage currentStage = new Stage();


            currentStage.setScene(new Scene(root));
            currentStage.setTitle("chatbot");

            currentStage.show();
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

            Stage currentStage = (Stage) postVBox.getScene().getWindow();

            // Utiliser le Stage actuel pour changer la scène
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("commentaires");
            currentStage.show();
            currentStage.setOnHiding(event -> loadData());
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


            Stage currentStage = (Stage) postVBox.getScene().getWindow();

            // Utiliser le Stage actuel pour changer la scène
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("commentaires");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void profile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) searchField.getScene().getWindow();
        stage.getScene().setRoot(root);

        Profile p=loader.getController();
        p.initialize();

        stage.setMaximized(true);
    }


    public void activities(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/AffichageActivityFront.fxml");
    }

    public void programme(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/listeProgramme.fxml");
    }

    public void equipement(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/market.fxml");
    }

    public void evennement(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/list.fxml");
    }
    public void forum(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/afficherPoste.fxml");
    }
}
