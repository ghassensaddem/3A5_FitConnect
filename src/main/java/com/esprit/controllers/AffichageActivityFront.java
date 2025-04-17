package com.esprit.controllers;

import com.esprit.models.activity;
import com.esprit.models.PlanningActivity;
import com.esprit.models.SalleSportif;
import com.esprit.services.ActivityService;
import com.esprit.services.PlanningService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Node;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AffichageActivityFront {

    @FXML
    private GridPane gridPaneActivities;

    @FXML
    private VBox salleContainer;

    private final ActivityService activityService = new ActivityService();
    private final PlanningService planningService = new PlanningService();

    @FXML
    public void initialize() {
        afficherActivitiesEnCartes();
    }

    public void afficherActivitiesEnCartes() {
        gridPaneActivities.getChildren().clear();
        List<activity> activitiesList = activityService.rechercher();

        int column = 0;
        int row = 0;

        for (activity act : activitiesList) {
            VBox card = new VBox();
            card.getStyleClass().add("activity-card");
            card.setSpacing(10);
            card.setPrefWidth(200);
            card.setPrefHeight(250);
            card.setAlignment(Pos.CENTER);

            Label titleLabel = new Label(act.getNomActivity());
            titleLabel.getStyleClass().add("activity-title");

            ImageView imageView = new ImageView();
            imageView.getStyleClass().add("activity-image");
            imageView.setFitWidth(160);
            imageView.setFitHeight(140);
            imageView.setPreserveRatio(true);

            try {
                System.out.println("Chemin image : file:" + act.getIconActivity());
                imageView.setImage(new Image("file:" + act.getIconActivity(), true));
            } catch (Exception e) {
                System.out.println("Erreur de chargement de l'image : " + e.getMessage());
            }

            card.setOnMouseClicked(event -> afficherSallesPourActivite(act));
            card.getChildren().addAll(titleLabel, imageView);
            gridPaneActivities.add(card, column++, row);

            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    public void afficherSallesPourActivite(activity activity) {
        salleContainer.getChildren().clear();
        List<SalleSportif> salles = planningService.getSallesByActivity(activity.getIdActivity());

        if (salles.isEmpty()) {
            salleContainer.getChildren().add(new Label("Aucune salle disponible pour cette activité."));
        } else {
            for (SalleSportif salle : salles) {
                PlanningActivity planning = planningService.getPlanningByActivityAndSalle(activity.getIdActivity(), salle.getIdSalle());

                // Création de l'image de la salle
                ImageView salleImage = new ImageView(new Image(getClass().getResource("/images/room.png").toExternalForm()));

                salleImage.setFitWidth(50);
                salleImage.setFitHeight(50);

                // Nom de la salle
                Label salleLabel = new Label(salle.getNomSalle());
                salleLabel.getStyleClass().add("salle-nom");

                // Adresse de la salle
                ImageView locationIcon = new ImageView(new Image(getClass().getResource("/images/location.png").toExternalForm()));

                locationIcon.setFitWidth(20);
                locationIcon.setFitHeight(20);
                Label locationLabel = new Label(salle.getAddresseSalle());

                // Horaires d'ouverture et de fermeture
                ImageView timeIcon = new ImageView(new Image(getClass().getResource("/images/time.png").toExternalForm()));

                        timeIcon.setFitWidth(20);
                timeIcon.setFitHeight(20);

                // Format d'affichage pour LocalTime (HH:mm)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String horaireOuverture = (salle.getHoraireOuverture() != null) ? salle.getHoraireOuverture().format(formatter) : "Non défini";
                String horaireFermeture = (salle.getHoraireFermeture() != null) ? salle.getHoraireFermeture().format(formatter) : "Non défini";

                Label timeLabel = new Label("Ouverture: " + horaireOuverture + " - Fermeture: " + horaireFermeture);

                // Capacité de la salle
                ImageView capacityIcon = new ImageView(new Image(getClass().getResource("/images/capacity.png").toExternalForm()));
                capacityIcon.setFitWidth(20);
                capacityIcon.setFitHeight(20);
                Label capacityLabel = new Label("Capacité: " + salle.getCapacity());

                // Bouton "Read More"
                Button readMoreButton = new Button("Read More");
                readMoreButton.setStyle("-fx-background-color: #053536; -fx-text-fill: white;");
                readMoreButton.setOnAction(event -> afficherDetailsSalle(event, activity, salle, planning));

                // Organisation des éléments dans un HBox
                HBox salleInfo = new HBox(10, salleImage, salleLabel, locationIcon, locationLabel, timeIcon, timeLabel, capacityIcon, capacityLabel);
                salleInfo.setAlignment(Pos.CENTER_LEFT);
                salleInfo.getStyleClass().add("salle-info");

                HBox container = new HBox(10, salleInfo, readMoreButton);
                container.setAlignment(Pos.CENTER_LEFT);

                salleContainer.getChildren().add(container);
            }
        }
    }

    private void afficherDetailsSalle(javafx.event.ActionEvent event, activity activity, SalleSportif salle, PlanningActivity planning) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsSalleActivity.fxml"));
            Parent root = loader.load();

            DetailsSalleActivity controller = loader.getController();
            controller.setDetails(activity, salle, planning, 1);

            // Récupération de la fenêtre actuelle et mise à jour de la scène
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture de la page des détails : " + e.getMessage());
        }
    }
}
