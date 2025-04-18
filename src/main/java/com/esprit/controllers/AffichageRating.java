package com.esprit.controllers;

import com.esprit.models.RatingActivity;
import com.esprit.services.RatingService;
import com.esprit.services.TwilioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.util.Map;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.InputStream;



public class AffichageRating {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<RatingActivity> ratingTable;
    @FXML
    private TableColumn<RatingActivity, String> colIdActivity;
    @FXML
    private TableColumn<RatingActivity, String> colIdSalle;
    @FXML
    private TableColumn<RatingActivity, String> colRating;
    @FXML
    private TableColumn<RatingActivity, String> colReview;
    @FXML
    private BarChart<String, Number> ratingChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private MenuButton notificationMenu;
    @FXML
    private TableColumn<RatingActivity, String> colActivityName;
    @FXML
    private TableColumn<RatingActivity, String> colSalleName;


    private final RatingService ratingService = new RatingService();
    private final ObservableList<RatingActivity> ratingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colIdActivity.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getIdActivity())));
        colIdSalle.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getIdSalle())));
        colRating.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRatingStars())));
        colReview.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReview()));

        afficherRatings();
        afficherGraphiqueRatings();
        afficherNotifications();
    }

    public void afficherRatings() {
        ratingList.clear();
        ratingList.addAll(ratingService.rechercher());
        ratingTable.setItems(ratingList);
        mettreAJourNotifications();

    }


    private void afficherGraphiqueRatings() {
        ratingChart.getData().clear();
        Map<Integer, Double> moyenneRatings = ratingService.calculerMoyenneRatingsParSalle();
        Map<Integer, String> nomsSalles = ratingService.recupererNomsSalles();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Moyenne des évaluations");

        int index = 0;
        for (Map.Entry<Integer, Double> entry : moyenneRatings.entrySet()) {
            int idSalle = entry.getKey();
            String nomSalle = nomsSalles.getOrDefault(idSalle, "Salle inconnue");

            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(nomSalle, entry.getValue());
            series.getData().add(dataPoint);

            // Appliquer une couleur différente à chaque barre
            final int colorIndex = index;
            dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " + getColor(colorIndex) + ";");
                }
            });

            index++;
        }

        ratingChart.getData().add(series);
    }
    private void afficherNotifications() {
        notificationMenu.getItems().clear();
        ObservableList<RatingActivity> ratings = FXCollections.observableArrayList(ratingService.rechercher());

        for (RatingActivity rating : ratings) {
            String activityName = ratingService.getActivityNameById(rating.getIdActivity());
            String userId = String.valueOf(rating.getIdClient());
            String stars = "⭐".repeat(rating.getRatingStars());
            String imageUrl = ratingService.getActivityImageById(rating.getIdActivity());

            // Charger l'image de l'activité
            Image image = new Image(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : "/images/default.png", 50, 50, true, true);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);

            // Créer le label avec les détails de l'évaluation
            Label label = new Label(activityName + "\nUtilisateur ID: " + userId +
                    "\nAvis: " + rating.getReview() + "\nÉtoiles: " + stars);

            // Bouton pour initier l'appel avec Twilio
            Button callButton = new Button("📞 Appeler");
            callButton.setOnAction(event -> initierAppel(rating.getIdClient()));

            // Ajouter l'image, le label et le bouton dans un VBox
            VBox content = new VBox(imageView, label, callButton);
            content.setSpacing(5);

            // Créer un CustomMenuItem pour la notification
            CustomMenuItem notificationItem = new CustomMenuItem(content);
            notificationItem.setHideOnClick(false); // Empêche la fermeture du menu lors du clic

            // Ajouter la notification au menu
            notificationMenu.getItems().add(notificationItem);




            jouerSonNotification("/audio/notification.mp3");
        }
    }


    private void initierAppel(int idClient) {
        String clientPhoneNumber = ratingService.getClientPhoneNumber(idClient);
        if (clientPhoneNumber == null || clientPhoneNumber.isEmpty()) {
            System.out.println("⚠️ Numéro de téléphone introuvable pour l'utilisateur ID: " + idClient);
            return;
        }

        if (!clientPhoneNumber.startsWith("+")) {
            clientPhoneNumber = "+216" + clientPhoneNumber; // Ajoutez l'indicatif pour la Tunisie
        }

        TwilioService.envoyerAppel(clientPhoneNumber);
    }


    // Met à jour les notifications
    private void mettreAJourNotifications() {
        Platform.runLater(() -> {
            List<RatingActivity> nouvellesEvaluations = ratingService.rechercher();
            notificationMenu.getItems().clear();

            if (nouvellesEvaluations.isEmpty()) {
                notificationMenu.setText("🔔 Notifications (0)");
                notificationMenu.setStyle("-fx-background-color: gray;");
            } else {
                notificationMenu.setText("🔔 Notifications (" + nouvellesEvaluations.size() + ")");
                notificationMenu.setStyle("-fx-background-color: red;");

                for (RatingActivity rating : nouvellesEvaluations) {
                    String imageUrl = ratingService.getActivityImageById(rating.getIdActivity());
                    ImageView imageView = new ImageView(new Image(imageUrl, 50, 50, true, true));
                    Label label = new Label("Activité: " + ratingService.getActivityNameById(rating.getIdActivity()) +
                            " - Salle: " + ratingService.getSalleNameById(rating.getIdSalle()) +
                            "\nAvis: " + rating.getReview());

                    // Ajouter le bouton d'appel
                    Button callButton = new Button("📞 Appeler");
                    callButton.setOnAction(event -> initierAppel(rating.getIdClient()));

                    VBox content = new VBox(imageView, label, callButton);
                    MenuItem notificationItem = new MenuItem();
                    notificationItem.setGraphic(content);
                    notificationMenu.getItems().add(notificationItem);
                }
            }
        });
    }
    private void jouerSonNotification(String chemin) {
        try {
            // Chargement du fichier depuis les ressources du classpath
            InputStream inputStream = getClass().getResourceAsStream(chemin);
            if (inputStream == null) {
                System.err.println("Fichier audio introuvable : " + chemin);
                throw new IllegalArgumentException("Fichier audio introuvable dans le classpath !");
            }


            Player player = new Player(inputStream);
            new Thread(() -> {
                try {
                    player.play();
                } catch (Exception e) {
                    System.err.println("Erreur lors de la lecture du son : " + e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture du son : " + e.getMessage());
        }
    }

    public void afficherAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nouvelle évaluation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private String getColor(int index) {
        String[] colors = {"#3498db", "#e74c3c", "#2ecc71", "#f39c12", "#9b59b6", "#1abc9c"};
        return colors[index % colors.length];
    }
    public void nouvelleEvaluationAjoutee(RatingActivity ratingActivity) {
        Platform.runLater(() -> {
            String message = "Nouvelle évaluation : \n" +
                    "- Utilisateur: " + ratingActivity.getIdClient() + "\n" +
                    "- Activité: " + ratingActivity.getIdActivity() + "\n" +
                    "- Salle: " + ratingActivity.getIdSalle() + "\n" +
                    "- Commentaire: " + ratingActivity.getReview();
            afficherAlerte(message);
            afficherNotifications();
        });
    }

}
