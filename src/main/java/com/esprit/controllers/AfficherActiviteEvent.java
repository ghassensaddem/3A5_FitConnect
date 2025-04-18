package com.esprit.controllers;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import com.esprit.models.activiteevent;
import com.esprit.services.activiteEventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficherActiviteEvent {

    @FXML
    private TableView<activiteevent> eventTable2;
    @FXML
    private TableColumn<activiteevent, Integer> colId;
    @FXML
    private TableColumn<activiteevent, String> colDate;
    @FXML
    private TableColumn<activiteevent, Double> colPrix;
    @FXML
    private TableColumn<activiteevent, String> colLieu;
    @FXML
    private TableColumn<activiteevent, String> colHoraire;

    private ObservableList<activiteevent> data = FXCollections.observableArrayList();
    private  activiteEventService eventService= new activiteEventService();

    @FXML
    public void initialize() {
        // Appliquer le fichier CSS
        eventTable2.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());

        // Charger les données dans la table
        loadData();

        // Ajouter un écouteur pour gérer les clics (gauche pour modifier, droit pour menu)
        eventTable2.setOnMouseClicked(activiteevent -> {
            if (activiteevent.getButton() == MouseButton.PRIMARY && activiteevent.getClickCount() == 2) {
                // Si c'est un double clic gauche, ouvrir la modification
                handleRowClick();
            }
        });

        // Configurer le menu contextuel pour la suppression (clic droit)
        setupContextMenu();
    }

    @FXML
    public void loadData() {
        data.clear(); // Nettoyer l'ObservableList avant de recharger les données

        // Charger les données depuis la base de données via EventService
        data.addAll(eventService.rechercher());

        // Associer les colonnes aux attributs de l'entité Event
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("horaire"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("nbrparticipant"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("idEvent"));
        colHoraire.setCellValueFactory(new PropertyValueFactory<>("idTypeActivite"));

        eventTable2.setItems(data); // Affichage des données dans la TableView
    }

    @FXML
    private void refreshTable() {
        loadData();
        showAlert("Mise à jour", "Les données ont été rafraîchies avec succès.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void openModifyEventWindow(activiteevent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activiteevent2.fxml"));
            Parent root = loader.load();

            ModifierActiviteevent controller = loader.getController();
            controller.initData2(event);

            Stage stage = new Stage();
            stage.setTitle("Modifier Événement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleRowClick() {
        // Vérifier si une ligne est sélectionnée
        TablePosition<?, ?> position = eventTable2.getSelectionModel().getSelectedCells()
                .stream().findFirst().orElse(null);

        if (position != null) {
            int rowIndex = position.getRow();

            // Vérifier que l'index est valide
            if (rowIndex >= 0 && rowIndex < eventTable2.getItems().size()) {
                activiteevent selectedEvent = eventTable2.getItems().get(rowIndex);

                if (selectedEvent != null) {
                    try {
                        // Charger l'interface de modification
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/activiteevent3.fxml"));
                        Parent root = loader.load();

                        // Récupérer le contrôleur de la page de modification
                        ModifierActiviteevent modifierController = loader.getController();

                        // Envoyer les données de l’événement sélectionné
                        modifierController.initData2(selectedEvent);

                        // Changer la scène
                        Stage stage = (Stage) eventTable2.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("L'élément sélectionné est null.");
                }
            } else {
                System.out.println("Index de ligne invalide.");
            }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }
    private int clickCount = 0;
    private activiteevent lastClickedEvent = null;

    @FXML
    public void initialize2() {
        // Charger les données dans la table
        loadData();

        // Ajouter le menu contextuel pour suppression avec un clic droit
        setupContextMenu();
    }

    private void setupContextMenu() {
        eventTable2.setRowFactory(tv -> {
            TableRow<activiteevent> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            // Option "Supprimer"
            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> {
                activiteevent selectedEvent = row.getItem();
                if (selectedEvent != null) {
                    confirmAndDeleteEvent(selectedEvent);
                }
            });

            // Ajouter l'option au menu contextuel
            contextMenu.getItems().add(deleteItem);

            // Afficher le menu contextuel uniquement au clic droit
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            });

            return row;
        });
    }

    private void confirmAndDeleteEvent(activiteevent event) {
        if (event == null) {
            showAlert("Erreur", "Aucune activité sélectionné.");
            return;
        }

        // Afficher une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer cet événement ?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                    activiteEventService eventService = new activiteEventService();
                eventService.supprimer(event); // Suppression de l'événement

                // Mettre à jour l'affichage de la table
                eventTable2.getItems().remove(event);
                eventTable2.refresh();

                showAlert("Suppression réussie", "L'événement a été supprimé avec succès.");
            }
        });
    }

    @FXML
    private void goToAjouterEvent(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToList2(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activiteevent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToList3(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/typeactivite3.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

