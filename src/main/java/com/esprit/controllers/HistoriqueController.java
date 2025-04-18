package com.esprit.controllers;

import com.esprit.models.Historique;
import com.esprit.services.HistoriqueService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class HistoriqueController implements Initializable {
    @FXML private TableView<Historique> tableView;
    @FXML private TableColumn<Historique, Integer> colId;
    @FXML private TableColumn<Historique, String> colAction;
    @FXML private TableColumn<Historique, String> colEntite;
    @FXML private TableColumn<Historique, Timestamp> colDate;
    @FXML private TableColumn<Historique, String> colDetails;
    @FXML private Button btnActualiser;

    private final HistoriqueService historiqueService = new HistoriqueService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Liaison des colonnes aux attributs du modèle Historique
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        colEntite.setCellValueFactory(new PropertyValueFactory<>("entite"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDetails.setCellValueFactory(new PropertyValueFactory<>("details"));

        // Chargement des données
        loadHistorique();

        // Bouton pour actualiser la liste
        btnActualiser.setOnAction(event -> loadHistorique());
    }

    private void loadHistorique() {
        List<Historique> historiqueList = historiqueService.getAllHistorique();
        tableView.getItems().setAll(historiqueList);
    }


}
