package com.esprit.controllers;

import com.esprit.models.activiteevent;
import com.esprit.services.activiteEventService;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.geometry.Insets;

import java.io.IOException;

public class SigninClient {
    @FXML
    private BorderPane borderPaneLog;

    @FXML
    private VBox VboxLog1;

    @FXML
    private Button SigninSlide;

    @FXML
    private VBox infoContainer;

    @FXML
    private Label titreLabel;

    @FXML
    private Label descriptionLabel;

    // --- Second BorderPane and its components ---
    @FXML
    private BorderPane borderPaneSign;

    @FXML
    private VBox VboxSign1;

    @FXML
    private Button loginslide;

    @FXML
    private VBox VboxSign2;

    @FXML
    private Label infoLabel;


    @FXML
    private Label errorAUTH;

    @FXML
    private VBox VboxLog2; // Added VboxLog2
    @FXML
    private GridPane usersGrid;
    @FXML
    private ScrollPane scrollPane;
    private activiteevent activiteevent;

    @FXML
    public void initialize() {
        activiteevent = new activiteevent("14h:30", 12, 20, 17);

        System.out.println("Initialisation de SigninClient...");
        System.out.println("Activité chargée : " + activiteevent.getHoraire() + ", Participants : " + activiteevent.getNbrparticipant());

        // Création d'un GridPane avec une seule colonne
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Définition d'une seule colonne
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        gridPane.getColumnConstraints().add(column);

        // Ajout de labels au GridPane
        if (activiteevent != null) {
            Label horaireLabel = new Label("🕒 Horaire: " + activiteevent.getHoraire());
            Label participantsLabel = new Label("💰 Nombre de participants: " + activiteevent.getNbrparticipant());

            gridPane.add(horaireLabel, 0, 0); // Colonne 0, Ligne 0
            gridPane.add(participantsLabel, 0, 1); // Colonne 0, Ligne 1
        } else {
            Label noInfoLabel = new Label("Aucune information disponible.");
            gridPane.add(noInfoLabel, 0, 0);
        }

        // Ajout du GridPane dans infoContainer (VBox)
        infoContainer.getChildren().clear(); // Nettoyer avant d'ajouter
        infoContainer.getChildren().add(gridPane);
    }



    // Method to set activiteEvent object
    public void setActiviteEvent(activiteevent activiteevent) {
        this.activiteevent = activiteevent;
        initialize();
    }

    @FXML
    void AUTH(ActionEvent event) throws IOException {
        activiteEventService cs = new activiteEventService();

        // Load a new scene with event data
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/list.fxml"));
        Parent root = loader.load();

        // Get the current stage from the event source (usually a button or any other UI element)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set new scene to stage
        stage.getScene().setRoot(root);
        stage.setMaximized(true);

        // Initialize ListUsers controller (if needed)
        ListUsers ls = loader.getController();
        ls.initialize();
    }

    @FXML
    void Slider(ActionEvent event) {
        boolean isLogin = borderPaneSign.isVisible();

        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(0.5), VboxSign1);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), VboxSign2);
        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(0.5), VboxLog1);
        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(0.5), VboxLog2);

        if (isLogin) {
            transition1.setToX(-400);
            transition2.setToX(-400);
            transition3.setToX(0);
            transition4.setToX(0);
            borderPaneLog.setRight(null);
            borderPaneLog.setLeft(VboxLog1);
            borderPaneLog.setVisible(true);

            transition1.setOnFinished(e -> borderPaneSign.setVisible(false));
        } else if (borderPaneLog.isVisible()) {
            transition3.setToX(350);
            transition4.setToX(350);
            transition1.setToX(0);
            transition2.setToX(0);

            borderPaneSign.setLeft(null);
            borderPaneSign.setRight(VboxSign1);
            borderPaneSign.setVisible(true);

            transition3.setOnFinished(e -> borderPaneLog.setVisible(false));
        }

        // Play all transitions simultaneously
        transition1.play();
        transition2.play();
        transition3.play();
        transition4.play();
    }

}
