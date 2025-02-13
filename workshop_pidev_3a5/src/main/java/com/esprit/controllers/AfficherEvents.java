package com.esprit.controllers;

import com.esprit.models.Event;
import com.esprit.services.EventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AfficherEvents  {

    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, Integer> colId;
    @FXML
    private TableColumn<Event, String> colDate;
    @FXML
    private TableColumn<Event, Double> colPrix;
    @FXML
    private TableColumn<Event, String> colLieu;
    @FXML
    private TableColumn<Event, String> colHoraire;

    private ObservableList<Event> data = FXCollections.observableArrayList();
    private EventService eventService = new EventService();


    public void initialize() {
        eventTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        loadData();
    }

    @FXML

    public void loadData() {
        data.clear(); // Nettoyer l'ObservableList avant de recharger les données

        // Charger les données depuis la base de données via EventService
        data.addAll(eventService.rechercher());

        // Associer les colonnes aux attributs de l'entité Event
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixdupass"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colHoraire.setCellValueFactory(new PropertyValueFactory<>("horaire"));

        eventTable.setItems(data); // Affichage des données dans la TableView
    }

    @FXML
    private void refreshTable(ActionEvent event) {
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
}
