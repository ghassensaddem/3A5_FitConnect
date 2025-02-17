


package com.esprit.controllers;

import com.esprit.models.Event;
import com.esprit.models.activiteevent;
import com.esprit.models.typeactivite;
import com.esprit.services.EventService;
import com.esprit.services.activiteEventService;
import com.esprit.services.typeactiviteService;
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

import java.util.ArrayList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ListUsers {

    public TextField txtSearch;

    @FXML
    private GridPane usersGrid;
    @FXML
    private ScrollPane scrollPane;

    public void initialize() {
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);  // Barre de défilement verticale toujours visible
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);   // Pas de barre de défilement horizontale
        scrollPane.setFitToWidth(true);  // Ajuste automatiquement la largeur du contenu à celle du ScrollPane
        scrollPane.setFitToHeight(false);  // Ne pas ajuster la hauteur automatiquement (permet de scroller)

        EventService eventService = new EventService(); // Création d'une instance
        ArrayList<Event> users = eventService.rechercher(); // Appel via l'instance
        usersGrid.getChildren().clear();
        populateUsersGrid(users);
    }

    private void populateUsersGrid(ArrayList<Event> users) {
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
        for (Event user : users) {
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


    private VBox createUserCard(Event user) {
        VBox card = new VBox(10); // Espacement vertical entre les éléments
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("user-card");
        card.setUserData(user);

        // 🎨 Ajout de l'effet d'ombre pour un meilleur design
        // 🎨 Ajout de l'effet d'ombre pour un meilleur design
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10); // Taille de l'ombre
        shadow.setOffsetX(5); // Déplacement horizontal
        shadow.setOffsetY(5); // Déplacement vertical
        shadow.setColor(Color.rgb(0, 0, 0, 0.5)); // Couleur noire avec 30% d'opacité
        card.setEffect(shadow); // Appliquer l'ombre à la carte


        // 🔹 Forcer la taille de la carte
        card.setMinSize(250, 400);
        card.setPrefSize(250, 400);
        card.setMaxSize(250, 400);

        // 🎨 En-tête en vert (date) - prendre toute la largeur et arrondir
        StackPane header = new StackPane();
        header.getStyleClass().add("card-header");

        // ✅ Ajouter du padding en bas pour espacer l'en-tête du reste
        header.setPadding(new Insets(0, 0, 15, 0));

        // 📅 Date en blanc avec une taille plus grande
        TextField dateField = new TextField("📅 Date: " + (user.getDate() != null ? user.getDate() : "N/A"));
        dateField.setFont(new Font(18));
        dateField.setEditable(false);
        dateField.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0;" +
                        "-fx-alignment: center;" +
                        "-fx-font-size: 24px;"
        );

        header.getChildren().add(dateField);
        StackPane.setAlignment(dateField, Pos.CENTER);

        // 🖼️ Image - Agrandie et conservant le ratio
        ImageView imageView = new ImageView();
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            imageView.setImage(new Image("file:" + user.getImage()));
        }
        imageView.setFitWidth(250);
        imageView.setFitHeight(210);
        imageView.setPreserveRatio(true);

        // Création de l'effet d'ombre pour le label
        DropShadow labelShadow = new DropShadow();
        labelShadow.setRadius(5);  // Rayon de l'ombre
        labelShadow.setOffsetX(2);  // Décalage horizontal
        labelShadow.setOffsetY(2);  // Décalage vertical
        labelShadow.setColor(Color.rgb(0, 0, 0, 0.3));  // Couleur de l'ombre (noir transparent)

// Création du label avec les informations
        Label infoLabel = new Label("🕒 Horaire: " + (user.getHoraire() != null ? user.getHoraire() : "N/A") +
                "\n📍 Lieu: " + (user.getLieu() != null ? user.getLieu() : "N/A") +
                "\n💰 Prix du Pass: " + (user.getPrixdupass() > 0 ? String.format("%.2f TND", user.getPrixdupass()) : "Gratuit"));

        infoLabel.getStyleClass().add("event-label");
        infoLabel.setWrapText(true);  // Permet au texte de passer à la ligne
        infoLabel.setFont(new Font(16));  // Taille de la police

// Appliquer l'ombre au label
        infoLabel.setEffect(labelShadow);

        // ✅ Ajouter un espaceur pour forcer l'expansion de la carte
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setMargin(imageView, new Insets(0, 0, -10, 0));

        // 📦 Ajout des éléments avec un bon espacement
        card.getChildren().addAll(header, imageView, spacer, infoLabel);

        return card;
    }


    private void createGridWithUserCards(ArrayList<Event> users) {
        // Créer un GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);  // Espacement horizontal entre les éléments
        gridPane.setVgap(10);  // Espacement vertical entre les lignes de cartes
        gridPane.setPadding(new Insets(10)); // Padding autour du GridPane

        // Nombre de colonnes souhaitées
        int columns = 2; // Ajustez le nombre de colonnes ici

        // Ajouter des contraintes de colonnes au GridPane (si nécessaire)
        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS); // La colonne doit grandir en fonction de l'espace disponible
            gridPane.getColumnConstraints().add(colConstraints);
        }

        // Ajouter des cartes utilisateurs au GridPane
        for (int i = 0; i < users.size(); i++) {
            Event user = users.get(i);
            VBox userCard = createUserCard(user);  // Créer la carte utilisateur

            // Calculer la ligne et la colonne où la carte doit être ajoutée
            int row = i / columns;  // Calcul de la ligne
            int col = i % columns;  // Calcul de la colonne

            // Si la ligne dépasse la taille du GridPane, on ajoute une nouvelle ligne
            if (gridPane.getRowConstraints().size() <= row) {
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setVgrow(Priority.ALWAYS); // Laisser la ligne grandir selon l'espace disponible
                gridPane.getRowConstraints().add(rowConstraints);
            }

            // Ajouter la carte dans la cellule correspondante
            gridPane.add(userCard, col, row);
        }

        // Ajoutez gridPane à un conteneur parent si nécessaire (ex. root.getChildren().add(gridPane));
    }

}