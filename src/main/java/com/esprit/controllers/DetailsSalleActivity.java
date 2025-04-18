package com.esprit.controllers;

import com.esprit.models.*;
import com.esprit.services.ClientService;
import com.esprit.services.TwilioService;
import com.esprit.tests.MainProg;
import com.esprit.utils.AssemblyAIService;
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
import java.nio.file.Path;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;

public class DetailsSalleActivity {

    @FXML
    private ImageView backButton;

    @FXML
    private VBox commentsContainer;

    @FXML
    private TextField commentInput;

    @FXML
    private Button btnSubmitComment, btnRecordVoice;

    @FXML
    private Label lblNomActivity, lblNomSalle, lblCapacityMax, lblNombreInscription, errorLabel;

    @FXML
    private ImageView imgActivity, star1, star2, star3, star4, star5, star6;
    @FXML
    private Button btnTranslate;

    @FXML
    private TextField codeVerificationInput; // Champ pour le code de vérification

    private int code_verif; // Code de vérification récupéré depuis la base de données
    private final String FIXED_PHONE_NUMBER = "+21648048587"; // Numéro de téléphone fixe

    private RatingService ratingService;
    private int idActivity;
    private int idSalle;
    private int selectedRating = 0;
    private int userId; // ID de l'utilisateur connecté
    private AssemblyAIService assemblyAIService;
    private ClientService utilisateurService;

    @FXML
    public void initialize() {
        ratingService = new RatingService();
        assemblyAIService = new AssemblyAIService();
        utilisateurService = new ClientService(); // Initialiser le service utilisateur

        btnSubmitComment.setOnAction(event -> verifierEtAjouterCommentaire());

        commentInput.setPromptText("Commentez ici ...");

        commentInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && commentInput.getText().trim().isEmpty()) {
                commentInput.setText("Commentez ici ...");
            }
        });

        updateStarImages(0);
        if(MainProg.idClient!=0)
            userId= MainProg.idClient;
    }

    public void setDetails(activity activity, SalleSportif salle, PlanningActivity planning, int userId) {
        this.idActivity = activity.getIdActivity();
        this.idSalle = salle.getIdSalle();
        this.userId = userId;

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
    private void traduireCommentaire() {
        new Thread(() -> {
            try {
                String texte = commentInput.getText().trim();
                if (texte.isEmpty() || texte.equals("Commentez ici ...")) {
                    Platform.runLater(() -> afficherErreur("Le commentaire est vide !"));
                    return;
                }

                // Encoder le texte pour l'URL
                String texteEncode = URLEncoder.encode(texte, "UTF-8");
                String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=en&dt=t&q=" + texteEncode;

                // Connexion HTTP
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                // Lire la réponse
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Extraction de la traduction
                JSONArray jsonArray = new JSONArray(response.toString());
                String traduction = jsonArray.getJSONArray(0).getJSONArray(0).getString(0);

                // Mise à jour du champ avec la traduction
                Platform.runLater(() -> commentInput.setText(traduction));

            } catch (Exception e) {
                Platform.runLater(() -> afficherErreur("Erreur de traduction : " + e.getMessage()));
            }
        }).start();
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

    @FXML
    private void enregistrerCommentaireVocal() {
        new Thread(() -> {
            try {
                // Enregistrement de l'audio
                Path recordedFile = assemblyAIService.recordAudio();

                // Mise à jour de l'interface pour afficher un message d'attente
                Platform.runLater(() -> commentInput.setPromptText("Transcription en cours..."));

                // Transcription de l'audio en texte
                String transcript = assemblyAIService.transcribeAudio(recordedFile.toString());

                // Mise à jour de l'interface avec le texte transcrit
                Platform.runLater(() -> commentInput.setText(transcript));
            } catch (Exception e) {
                Platform.runLater(() -> afficherErreur("Erreur d'enregistrement audio : " + e.getMessage()));
            }
        }).start();
    }

    private void chargerCommentaires() {
        Platform.runLater(() -> {
            commentsContainer.getChildren().clear();
            List<RatingActivity> commentaires = ratingService.getCommentaires(idActivity, idSalle);
            commentaires.forEach(this::afficherCommentaire);  //methode terminal (ref)
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

    @FXML
    private void envoyerCodeVerification() {
        // Récupérer la personne par son ID
        ClientService utilisateurService = new ClientService();
        Client utilisateur = utilisateurService.rechercherParId(userId);
        if (utilisateur != null) {
            // Récupérer le code_verif depuis la base de données
            code_verif = utilisateur.getId_prog();

            // Envoyer le code par SMS au numéro fixe
            TwilioService.envoyerSms(FIXED_PHONE_NUMBER, "Votre code de vérification est : " + code_verif);

            afficherSucces("Code de vérification envoyé !");
        } else {
            afficherErreur("Utilisateur non trouvé !");
        }
    }

    @FXML
    private void verifierEtAjouterCommentaire() {
        String codeSaisi = codeVerificationInput.getText().trim();

        try {
            int codeSaisiInt = Integer.parseInt(codeSaisi);

            if (codeSaisiInt == code_verif) {
                ajouterCommentaire(); // Ajouter le commentaire si le code est correct
            } else {
                afficherErreur("Code de vérification incorrect !");
            }
        } catch (NumberFormatException e) {
            afficherErreur("Veuillez entrer un code valide !");
        }
    }

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
}