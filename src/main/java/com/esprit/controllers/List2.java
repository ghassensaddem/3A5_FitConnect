package com.esprit.controllers;

import com.esprit.models.Event;
import com.esprit.models.activiteevent;
import com.esprit.services.EventService;
import com.esprit.services.activiteEventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import com.esprit.models.typeactivite;
import com.esprit.services.typeactiviteService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class List2 {

    @FXML
    private TextField txtSearch;

    @FXML
    private GridPane usersGrid;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button btnBack;
    private int eventId; // Clé étrangère de l'événement sélectionné

    // Méthode pour récupérer et afficher les activités
    public void setEventId(int eventId) {
        this.eventId = eventId;
        loadActivities(); // Charger les activités liées à cet événement
    }

    private void loadActivities() {
        activiteEventService service = new activiteEventService();
        ArrayList<activiteevent> activities = service.getActivitiesByEventId(eventId);

        System.out.println("Chargement des activités pour l'événement ID: " + eventId);
        System.out.println("Nombre d'activités récupérées: " + activities.size());

        for (activiteevent act : activities) {
            System.out.println("Activité ID: " + act.getIdEvent()); // Vérification de chaque activité
        }

        if (activities.isEmpty()) {
            System.out.println("Aucune activité trouvée !");
        } else {
            populateUsersGrid(activities);
        }
    }

    private void populateUsersGrid(ArrayList<activiteevent> users) {
        int columns = 4;  // Nombre de colonnes souhaitées
        int column = 0;
        int row = 2; // Commencer à la ligne 2 pour laisser de la place aux labels

        usersGrid.getChildren().clear();
        usersGrid.getColumnConstraints().clear();
        usersGrid.getRowConstraints().clear();


        // 🔹 Ajouter des contraintes de colonnes
        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            usersGrid.getColumnConstraints().add(colConstraints);
        }

        // 🔹 Ajouter des contraintes de lignes
        int numRows = (int) Math.ceil(users.size() / (double) columns) + 2; // +2 pour les labels
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);
            usersGrid.getRowConstraints().add(rowConstraints);
        }

        // 🔹 Ajouter les cartes des événements
        for (activiteevent user : users) {
            VBox userCard = createUserCard(user);
            usersGrid.add(userCard, column, row);

            column++;
            if (column == columns) { // Passer à la ligne suivante si on atteint la limite
                column = 0;
                row++;
            }
        }

        // 🔹 Modifier la hauteur minimum pour que la GridPane s'adapte bien avec le contenu
        usersGrid.setMinHeight(600); // Cette ligne peut être ajustée pour ajuster la hauteur par défaut
        usersGrid.setPrefHeight(Region.USE_COMPUTED_SIZE); // Permet de s'ajuster à la taille du contenu

        // 🔹 Ajouter un ScrollPane (si ce n'est pas déjà fait dans votre FXML)
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(usersGrid);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);

        // Si vous avez déjà un ScrollPane, vous n'avez peut-être pas besoin de cette partie.
    }



    private VBox createUserCard(activiteevent user) {
        activiteEventService activiteEventService = new activiteEventService();
        String imagePath = activiteEventService.getImagePathFromDatabase(eventId);
        int a = activiteEventService.getidtipe(eventId);
        String[] typeDetails = activiteEventService.getTypeActiviteDetails(a);
        String titre = typeDetails[0];
        String description = typeDetails[1];

        VBox card = new VBox(0); // Espacement vertical entre les éléments réglé à 0
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("user-card");
        card.setUserData(user);

        // 🎨 Effet d'ombre pour un meilleur design
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        card.setEffect(shadow);

        // 🔹 Forcer la taille de la carte
        card.setMinSize(250, 400);
        card.setPrefSize(250, 400);
        card.setMaxSize(250, 400);

        // 🎨 En-tête (date)
        StackPane header = new StackPane();
        header.getStyleClass().add("card-header");
        header.setPadding(new Insets(0));  // Aucune marge supplémentaire

        TextField dateField = new TextField("📍 Titre: " + (titre != null ? titre : "N/A"));
        dateField.setStyle("-fx-font-size: 16px; " +
                "-fx-text-fill: #2c3e50; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px; " +
                "-fx-border-color: #3498db; " +
                "-fx-background-color: #ecf0f1; " +
                "-fx-border-radius: 0px; " +
                "-fx-background-radius: 0px;");
        dateField.setFont(new Font(18));
        dateField.setEditable(false);
        dateField.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 24px;");

        header.getChildren().add(dateField);
        StackPane.setAlignment(dateField, Pos.CENTER);

        // 🖼️ Ajout de l'image de l'événement
        ImageView imageView = new ImageView();
        System.out.println("Event ID: " + user.getIdEvent());

        System.out.println("Image Path: " + imagePath);

        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists() && file.isFile()) {
                imageView.setImage(new Image(file.toURI().toString(), 250, 200, true, true));
            } else {
                System.out.println("Image not found, using default image.");
                imageView.setImage(new Image(new File("resources/images/default.png").toURI().toString()));
            }
        } else {
            System.out.println("No image path found, using default image.");
            imageView.setImage(new Image(new File("resources/images/default.png").toURI().toString()));
        }

        imageView.setFitWidth(240);
        imageView.setFitHeight(220);
        imageView.setPreserveRatio(true);

        // 🏷️ Label avec les informations de l'événement (⚠️ Correction de `Lieu`)
        Label infoLabel = new Label();
        infoLabel.setText("🕒 Horaire: " + (user.getHoraire() != null ? user.getHoraire() : "N/A") + "\n"
                + "👥 Participants: " + (user.getNbrparticipant() > 0 ? user.getNbrparticipant() : "Welcome") + "\n"
                + "📝 " + (description != null ? (description.length() > 50 ? description.substring(0, 50) + "..." : description) : "N/A"));

        infoLabel.setStyle("-fx-font-size: 16px; " +
                "-fx-text-fill: #053536; " +  // Gris foncé pour le texte
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 15px; " +  // Padding ajusté pour une meilleure lisibilité
                "-fx-background-color: transparent; " + // Fond légèrement cassé (blanc cassé)
                "-fx-background-insets: 0px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.2, 2, 2); " +  // Ombre plus marquée
                "-fx-wrap-text: true;");


        // Ajouter un Tooltip si la description est longue
        if (description != null && description.length() > 50) {
            Tooltip tooltip = new Tooltip(description);
            tooltip.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #f1c40f; -fx-text-fill: #2c3e50;");
            Tooltip.install(infoLabel, tooltip);
        }

        infoLabel.setWrapText(true);
        infoLabel.setFont(new Font(16));

        DropShadow labelShadow = new DropShadow();
        labelShadow.setRadius(5);
        labelShadow.setOffsetX(2);
        labelShadow.setOffsetY(2);
        labelShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        infoLabel.setEffect(labelShadow);

        // 📌 Gestion du clic sur la carte
        card.setOnMouseClicked(event -> {
           /* try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/list2.fxml"));
                Parent root = loader.load();

                List2 controller = loader.getController();
                controller.setEventId(user.getId()); // Passer l'ID de l'événement

                card.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        });

        // ✅ Ajouter un espaceur
     //   Region spacer = new Region();
        //VBox.setVgrow(spacer, Priority.ALWAYS);

        // 📦 Ajout des éléments à la carte
        card.getChildren().addAll(header, imageView,  infoLabel);

        return card;
    }


    public void handleBackAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/list.fxml")); // Remplace par le bon fichier FXML
            Parent previousScene = loader.load();
            btnBack.getScene().setRoot(previousScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*private void rechercherPartitle(String nom) {
        usersGrid.getChildren().clear(); // Effacer l'ancien contenu de la grille

        activiteEventService eventService = new activiteEventService()  ; // Créer une instance du service
        ArrayList<typeactivite> events = eventService.rechercherParNom(nom); // Récupérer les événements filtrés

        populateUsersGrid(events); // Afficher les événements filtrés dans la grille
    }
*/
}
