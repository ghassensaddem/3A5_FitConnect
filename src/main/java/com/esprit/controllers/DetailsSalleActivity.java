package com.esprit.controllers;

import com.esprit.models.activity;
import com.esprit.models.SalleSportif;
import com.esprit.models.PlanningActivity;
import com.esprit.models.RatingActivity;
import com.esprit.services.RatingService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;

public class DetailsSalleActivity {

    @FXML
    private ImageView backButton;

    @FXML
    private VBox commentsContainer;

    @FXML
    private TextField commentInput;

    @FXML
    private Button btnSubmitComment;

    @FXML
    private Label lblNomActivity, lblNomSalle, lblCapacityMax, lblNombreInscription, errorLabel;

    @FXML
    private ImageView imgActivity, star1, star2, star3, star4, star5, star6;

    private RatingService ratingService;
    private int idActivity;
    private int idSalle;
    private int selectedRating = 0;
    private int userId=2; // ID de l'utilisateur connecté

    @FXML
    private void goBackToActivities(javafx.event.Event event) {
        try {
            // Charger la scène AffichageActivityFront
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageActivityFront.fxml"));
            Parent root = loader.load();

            // Obtenir le stage actuel et changer de scène
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du retour à AffichageActivityFront : " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        ratingService = new RatingService();
        btnSubmitComment.setOnAction(event -> ajouterCommentaire());

        commentInput.setPromptText("Commentez ici ...");

        commentInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && commentInput.getText().trim().isEmpty()) {
                commentInput.setText("Commentez ici ...");
            }
        });

        updateStarImages(0);
    }

    public void setDetails(activity activity, SalleSportif salle, PlanningActivity planning, int userId) {
        this.idActivity = activity.getIdActivity();
        this.idSalle = salle.getIdSalle();
        //this.userId = userId;

        lblNomActivity.setText("Nom de l'Activité: " + activity.getNomActivity());
        lblNomSalle.setText("Nom de la Salle: " + salle.getNomSalle());
        lblCapacityMax.setText("Capacité Maximale: " + salle.getCapacity());
        lblNombreInscription.setText("Nombre d'Inscriptions: " + planning.getNombreInscription());

        if (activity.getIconActivity() != null && !activity.getIconActivity().isEmpty()) {
            Image image = new Image("file:" + activity.getIconActivity(), false);
            imgActivity.setImage(image);
        }

        chargerCommentaires();
    }

    @FXML
    private void ajouterCommentaire() {
        String commentaire = commentInput.getText().trim();

        if (commentaire.isEmpty() || commentaire.equals("Commentez ici ...")) {
            afficherErreur("Le commentaire ne peut pas être vide !");
            return;
        }
        if (selectedRating < 1 || selectedRating > 6) {
            afficherErreur("Veuillez sélectionner une note entre 1 et 6 !");
            return;
        }
        clearErreur();

        RatingActivity newRating = new RatingActivity(idActivity, idSalle, userId, selectedRating, commentaire);
        ratingService.ajouter(newRating);

        rafraichirCommentaires();
        resetForm();
        afficherSucces("Commentaire ajouté avec succès !");
    }

    private void chargerCommentaires() {
        Platform.runLater(() -> {
            commentsContainer.getChildren().clear();
            List<RatingActivity> commentaires = ratingService.getCommentaires(idActivity, idSalle);
            commentaires.forEach(this::afficherCommentaire);
        });
    }

    private void afficherCommentaire(RatingActivity rating) {
        HBox commentBox = new HBox(10);
        Label label = new Label("★ " + rating.getRatingStars() + " - " + rating.getReview());
        label.getStyleClass().add("comment-label");
        commentBox.getChildren().add(label);

        if (rating.getIdClient() == userId) {
            Button btnEdit = new Button("Modifier");
            Button btnDelete = new Button("Supprimer");

            btnEdit.setOnAction(event -> modifierCommentaire(rating));
            btnDelete.setOnAction(event -> supprimerCommentaire(rating));

            commentBox.getChildren().addAll(btnEdit, btnDelete);
        }

        commentsContainer.getChildren().add(commentBox);
    }

    private void modifierCommentaire(RatingActivity rating) {
        commentInput.setText(rating.getReview());
        selectedRating = rating.getRatingStars();
        updateStarImages(selectedRating);

        btnSubmitComment.setText("Modifier");
        btnSubmitComment.setOnAction(event -> {
            rating.setReview(commentInput.getText());
            rating.setRatingStars(selectedRating);
            ratingService.modifier(rating);
            resetForm();
            rafraichirCommentaires();
            btnSubmitComment.setText("Commenter");
            btnSubmitComment.setOnAction(evt -> ajouterCommentaire());
        });
    }

    private void supprimerCommentaire(RatingActivity rating) {
        ratingService.supprimer(rating);
        rafraichirCommentaires();
    }

    private void rafraichirCommentaires() {
        Platform.runLater(this::chargerCommentaires);
    }

    @FXML private void setRating1() { setRating(1); }
    @FXML private void setRating2() { setRating(2); }
    @FXML private void setRating3() { setRating(3); }
    @FXML private void setRating4() { setRating(4); }
    @FXML private void setRating5() { setRating(5); }
    @FXML private void setRating6() { setRating(6); }

    private void setRating(int rating) {
        this.selectedRating = rating;
        updateStarImages(rating);
    }

    private void updateStarImages(int rating) {
        String filledStar = getClass().getResource("/images/star_filled.png").toExternalForm();
        String emptyStar = getClass().getResource("/images/star_empty.png").toExternalForm();

        ImageView[] stars = {star1, star2, star3, star4, star5, star6};
        for (int i = 0; i < stars.length; i++) {
            stars[i].setImage(new Image(i < rating ? filledStar : emptyStar));
        }
    }

    private void resetForm() {
        commentInput.clear();
        commentInput.setText("Commentez ici ...");
        selectedRating = 0;
        updateStarImages(0);
    }

    private void afficherErreur(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void afficherSucces(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: green;");
    }

    private void clearErreur() {
        errorLabel.setText("");
    }
}
