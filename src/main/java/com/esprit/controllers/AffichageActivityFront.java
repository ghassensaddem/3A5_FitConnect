package com.esprit.controllers;

import com.esprit.models.activity;
import com.esprit.models.PlanningActivity;
import com.esprit.models.SalleSportif;
import com.esprit.models.CategorieActivity;
import com.esprit.services.ActivityService;
import com.esprit.services.CategorieServiceS;
import com.esprit.services.PlanningService;
import com.esprit.services.CategorieService;
import com.esprit.tests.MainProg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


public class AffichageActivityFront {

    @FXML
    private GridPane gridPaneActivities;
    @FXML
    private VBox salleContainer;
    @FXML
    private Label aucuneSalleLabel;
    @FXML
    private ScrollPane salleScrollPane;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField searchField;


    private final ActivityService activityService = new ActivityService();
    private final PlanningService planningService = new PlanningService();
    private final CategorieServiceS categorieService = new CategorieServiceS();

    private boolean categoriesChargees = false; // Pour éviter de recharger inutilement

    private int ClientID=881;

    @FXML
    public void initialize() {
        salleContainer.setVisible(false);
        salleContainer.setManaged(false);
        afficherActivitiesEnCartes();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerActivitiesParNom(newValue);
        });


        categoryComboBox.setOnMouseClicked(event -> {
            if (!categoriesChargees) {
                chargerCategories();
                categoriesChargees = true;
            }
        });
        categoryComboBox.setOnAction(event -> filtrerActivitiesParCategorie());

        sortComboBox.getItems().addAll("A-Z", "Z-A");
        sortComboBox.setOnAction(event -> trierActivities());

        if(MainProg.idClient!=0)
            ClientID= MainProg.idClient;
    }

    private void chargerCategories() {
        categoryComboBox.getItems().clear();
        List<CategorieActivity> categories = categorieService.rechercher();
        for (CategorieActivity categorie : categories) {
            categoryComboBox.getItems().add(categorie.getNomCategorie());
        }
    }

    private void filtrerActivitiesParCategorie() {
        String selectedCategory = categoryComboBox.getValue();
        if (selectedCategory != null) {
            CategorieActivity categorie = categorieService.getCategorieByName(selectedCategory);
            if (categorie != null) {
                afficherActivitiesEnCartesParCategorie(categorie.getIdCategorie());
            }
        }
    }

    private void afficherActivitiesEnCartesParCategorie(int idCategorie) {
        gridPaneActivities.getChildren().clear();
        List<activity> activitiesList = activityService.getActivitiesByCategorie(idCategorie);
        afficherActivities(activitiesList);
    }

    public void afficherActivitiesEnCartes() {
        List<activity> activitiesList = activityService.rechercher();
        afficherActivities(activitiesList);
    }

    private void afficherActivities(List<activity> activitiesList) {
        gridPaneActivities.getChildren().clear();
        if (activitiesList.isEmpty()) {
            Label noDataLabel = new Label("Aucune activité trouvée.");
            noDataLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");
            gridPaneActivities.add(noDataLabel, 0, 0);
            return;
        }

        int column = 0;
        int row = 0;

        for (activity act : activitiesList) {
            VBox card = new VBox();
            card.getStyleClass().add("activity-card");
            card.setSpacing(5);
            card.setPrefWidth(300);
            card.setPrefHeight(180);
            card.setAlignment(Pos.CENTER);

            Label titleLabel = new Label(act.getNomActivity());
            titleLabel.getStyleClass().add("activity-title");

            HBox titleContainer = new HBox(titleLabel);
            titleContainer.getStyleClass().add("activity-title-container");
            titleContainer.setAlignment(Pos.CENTER);
            titleContainer.setPrefWidth(400);
            titleContainer.setTranslateY(-35);

            ImageView imageView = new ImageView();
            imageView.getStyleClass().add("activity-image");
            imageView.setFitWidth(150);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            try {
                imageView.setImage(new Image("file:" + act.getIconActivity(), true));
            } catch (Exception e) {
                System.out.println("Erreur de chargement de l'image : " + e.getMessage());
            }

            card.setOnMouseClicked(event -> afficherSallesPourActivite(act));
            card.getChildren().addAll(titleContainer, imageView);
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

        if (salles == null || salles.isEmpty()) {
            salleContainer.setVisible(false);
            salleContainer.setManaged(false);
        } else {
            salleContainer.setVisible(true);
            salleContainer.setManaged(true);

            for (SalleSportif salle : salles) {
                PlanningActivity planning = planningService.getPlanningByActivityAndSalle(activity.getIdActivity(), salle.getIdSalle());

                Label salleLabel = new Label(salle.getNomSalle());
                salleLabel.getStyleClass().add("salle-nom");

                Label locationLabel = new Label(salle.getAddresseSalle());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String horaireOuverture = (salle.getHoraireOuverture() != null) ? salle.getHoraireOuverture().format(formatter) : "Non défini";
                String horaireFermeture = (salle.getHoraireFermeture() != null) ? salle.getHoraireFermeture().format(formatter) : "Non défini";

                Label timeLabel = new Label("Ouverture: " + horaireOuverture + " - Fermeture: " + horaireFermeture);
                Label capacityLabel = new Label("Capacité: " + salle.getCapacity());

                Button readMoreButton = new Button("Read More");
                readMoreButton.setStyle("-fx-background-color: #053536; -fx-text-fill: white;");
                readMoreButton.setOnAction(event -> afficherDetailsSalle(event, activity, salle, planning));

                HBox salleInfo = new HBox(10, salleLabel, locationLabel, timeLabel, capacityLabel);
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
            controller.setDetails(activity, salle, planning, ClientID);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture de la page des détails : " + e.getMessage());
        }
    }

    private void trierActivities() {
        String selectedSort = sortComboBox.getValue();
        if (selectedSort == null) return;

        List<activity> activitiesList = activityService.rechercher();

        if ("A-Z".equals(selectedSort)) {
            activitiesList.sort((a1, a2) -> a1.getNomActivity().compareToIgnoreCase(a2.getNomActivity()));
        } else if ("Z-A".equals(selectedSort)) {
            activitiesList.sort((a1, a2) -> a2.getNomActivity().compareToIgnoreCase(a1.getNomActivity()));
        }

        afficherActivities(activitiesList);
    }
    private void filtrerActivitiesParNom(String query) {
        List<activity> allActivities = activityService.rechercher();
        List<activity> filteredActivities = allActivities.stream()
                .filter(act -> act.getNomActivity().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        afficherActivities(filteredActivities);
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