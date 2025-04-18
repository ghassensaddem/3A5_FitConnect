package com.esprit.controllers;

import com.esprit.models.PlanningActivity;
import com.esprit.services.PlanningService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Desktop;
import java.io.PrintWriter;


public class AffichagePlanning {
    @FXML
    private TableView<PlanningActivity> planningTable;
    @FXML
    private TableColumn<PlanningActivity, Integer> colIdActivity;
    @FXML
    private TableColumn<PlanningActivity, Integer> colIdSalle;
    @FXML
    private TableColumn<PlanningActivity, Integer> colCapacityMax;
    @FXML
    private TableColumn<PlanningActivity, Integer> colNombreInscription;
    @FXML
    private TableColumn<PlanningActivity, Void> colActions;

    private final PlanningService planningService = new PlanningService();
    private final ObservableList<PlanningActivity> planningList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colIdActivity.setCellValueFactory(cellData -> cellData.getValue().idActivityProperty().asObject());
        colIdSalle.setCellValueFactory(cellData -> cellData.getValue().idSalleProperty().asObject());
        colCapacityMax.setCellValueFactory(cellData -> cellData.getValue().capacityMaxProperty().asObject());
        colNombreInscription.setCellValueFactory(cellData -> cellData.getValue().nombreInscriptionProperty().asObject());

        // Ajout des boutons Modifier / Supprimer
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox buttonsBox = new HBox(5, btnModifier, btnSupprimer);

            {
                btnModifier.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");

                btnModifier.setOnAction(event -> {
                    PlanningActivity selectedPlanning = getTableView().getItems().get(getIndex());
                    ouvrirModifierPlanning(selectedPlanning);
                });

                btnSupprimer.setOnAction(event -> {
                    PlanningActivity selectedPlanning = getTableView().getItems().get(getIndex());
                    supprimerPlanning(selectedPlanning);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });

        afficherPlannings();
    }

    public void afficherPlannings() {
        planningList.clear();
        planningList.addAll(planningService.rechercher());
        planningTable.setItems(planningList);
    }

    private void supprimerPlanning(PlanningActivity selectedPlanning) {
        if (selectedPlanning == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Aucune planification sélectionnée", "Veuillez sélectionner une planification à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer cette planification ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                planningService.supprimer(selectedPlanning);
                afficherPlannings();
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "La planification a été supprimée avec succès.");
            } catch (Exception e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression : " + e.getMessage());
            }
        }
    }

    private void ouvrirModifierPlanning(PlanningActivity selectedPlanning) {
        if (selectedPlanning == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner une planification à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPlanning.fxml"));
            Parent root = loader.load();

            ModifierPlanning controller = loader.getController();
            controller.initData(selectedPlanning);

            Stage stage = (Stage) planningTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger ModifierPlanning.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    public void csv(ActionEvent actionEvent) {
        String filePath = "C:\\xampp\\htdocs\\3A5_FitConnect\\src\\main\\resources\\planning_activities.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // En-tête du fichier CSV
            writer.println("ID Activité,ID Salle,Capacité Max,Nombre Inscriptions");

            // Récupérer les données du tableau
            List<PlanningActivity> planningList = planningTable.getItems();

            for (PlanningActivity planning : planningList) {
                // Créer des liens HTML cliquables pour ID Activité et ID Salle
                String idActivityLink = "=HYPERLINK(\"#ID_ACT_" + planning.getIdActivity() + "\", \"" + planning.getIdActivity() + "\")";
                String idSalleLink = "=HYPERLINK(\"#ID_SALLE_" + planning.getIdSalle() + "\", \"" + planning.getIdSalle() + "\")";

                // Écriture des données dans le CSV
                writer.println(idActivityLink + "," + idSalleLink + "," + planning.getCapacityMax() + "," + planning.getNombreInscription());
            }

            afficherAlerte(Alert.AlertType.INFORMATION, "Export CSV", "Fichier CSV enregistré avec succès : " + filePath);

        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur d'exportation", "Impossible d'enregistrer le fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void exportToHTML() {
        String filePath = "C:\\Users\\nassim\\IdeaProjects\\nassim_project_fitconnect\\planning_activities.html";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Début du fichier HTML
            writer.println("<html><head><title>Planning Activities</title></head><body>");
            writer.println("<h2>Liste des Plannings</h2>");
            writer.println("<table border='1'>");
            writer.println("<tr><th>ID Activité</th><th>ID Salle</th><th>Capacité Max</th><th>Nombre Inscriptions</th></tr>");

            // Récupérer les données du tableau
            for (PlanningActivity planning : planningTable.getItems()) {
                writer.println("<tr>");

                // Hyperliens qui appellent les contrôleurs JavaFX
                writer.println("<td><a href='javafx:activity:" + planning.getIdActivity() + "'>" + planning.getIdActivity() + "</a></td>");
                writer.println("<td><a href='javafx:salle:" + planning.getIdSalle() + "'>" + planning.getIdSalle() + "</a></td>");
                writer.println("<td>" + planning.getCapacityMax() + "</td>");
                writer.println("<td>" + planning.getNombreInscription() + "</td>");

                writer.println("</tr>");
            }

            writer.println("</table></body></html>");

            afficherAlerte(Alert.AlertType.INFORMATION, "Export HTML", "Fichier HTML généré avec succès : " + filePath);

            // Ouvrir automatiquement le fichier HTML après la génération
            File file = new File(filePath);
            java.awt.Desktop.getDesktop().browse(file.toURI());

        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur d'exportation", "Impossible d'enregistrer le fichier HTML : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void naviguer(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/AjouterPlanning.fxml");
    }
}
