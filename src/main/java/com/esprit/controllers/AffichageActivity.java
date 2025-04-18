package com.esprit.controllers;

import com.esprit.models.activity;
import com.esprit.services.ActivityService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class AffichageActivity {
    @FXML
    private TableView<activity> activityTable;
    @FXML
    private TableColumn<activity, Integer> colId;
    @FXML
    private TableColumn<activity, String> colNom;
    @FXML
    private TableColumn<activity, String> colIcon;
    @FXML
    private TableColumn<activity, String> colCategorie;
    @FXML
    private TableColumn<activity, Void> colActions;



    private final ActivityService activityService = new ActivityService();
    private final ObservableList<activity> activitiesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idActivity"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomActivity"));
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorieNom()));

        // Gestion de l'affichage des icônes dans la TableView
        colIcon.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIconActivity()));
        colIcon.setCellFactory(new Callback<TableColumn<activity, String>, TableCell<activity, String>>() {
            @Override
            public TableCell<activity, String> call(TableColumn<activity, String> param) {
                return new TableCell<activity, String>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);
                        if (empty || imagePath == null || imagePath.trim().isEmpty()) {
                            setGraphic(null);
                        } else {
                            try {
                                File file = new File(imagePath);
                                String imageUrl = file.exists() ? file.toURI().toString() : imagePath;
                                Image image = new Image(imageUrl, 50, 50, true, true);
                                imageView.setImage(image);
                                imageView.setFitWidth(50);
                                imageView.setFitHeight(50);
                                setGraphic(imageView);
                            } catch (Exception e) {
                                System.out.println("Erreur chargement image : " + e.getMessage());
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });

        // Ajout des boutons Modifier / Supprimer
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox buttonsBox = new HBox(5, btnModifier, btnSupprimer);

            {
                btnModifier.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");

                btnModifier.setOnAction(event -> {
                    activity selectedActivity = getTableView().getItems().get(getIndex());
                    ouvrirModifierActivity(selectedActivity);
                });

                btnSupprimer.setOnAction(event -> {
                    activity selectedActivity = getTableView().getItems().get(getIndex());
                    supprimerActivity(selectedActivity);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });

        afficherActivities();
    }

    // Chargement des activités dans la TableView
    public void afficherActivities() {
        activitiesList.clear();
        activitiesList.addAll(activityService.rechercher());

        for (activity act : activitiesList) {
            System.out.println("Vérification image : " + act.getIconActivity());
        }

        activityTable.setItems(activitiesList);
    }

    // Suppression d'une activité avec confirmation
    private void supprimerActivity(activity selectedActivity) {
        if (selectedActivity == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune activité sélectionnée", "Veuillez sélectionner une activité à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer cette activité ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                activityService.supprimer(selectedActivity);
                afficherActivities();
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "L'activité a été supprimée avec succès.");
            } catch (Exception e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression : " + e.getMessage());
            }
        }
    }

    // Ouvrir l'interface de modification avec les données de l'activité sélectionnée
    private void ouvrirModifierActivity(activity selectedActivity) {
        if (selectedActivity == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner une activité à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierActivity.fxml"));
            Parent root = loader.load();

            ModifierActivity controller = loader.getController();
            controller.initData(selectedActivity);

            Stage stage = (Stage) activityTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger ModifierActivity.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Affichage d'une alerte
    private void afficherAlerte(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    public void home(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/listUsers.fxml");
    }

    public void activities(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/AffichageActivity.fxml");
    }

    public void programme(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/programme.fxml");
    }

    public void equipement(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/afficher_equipement.fxml");
    }

    public void evennement(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/event3.fxml");
    }

    public void naviguer(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/ActivityAjouter.fxml");
    }

    public void planning(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/AffichagePlanning.fxml");
    }

    public void rating(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/AffichageRating.fxml");
    }

    public void salle(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/AffichageSalle.fxml");
    }
}
