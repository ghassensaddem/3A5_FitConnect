package com.esprit.controllers;

import com.esprit.models.Avis;
import com.esprit.models.NavigationHelper;
import com.esprit.models.Programme;
import com.esprit.services.HistoriqueService;
import com.esprit.services.ProgrammeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class AfficherProgramme implements Initializable {

    @FXML
    private TableView<Programme> ProgrammeTable;

    @FXML
    private TableColumn<Programme, Integer> colId;

    @FXML
    private TableColumn<Programme, String> colPrix;

    @FXML
    private TableColumn<Programme, String> colDescription;

    @FXML
    private TableColumn<Programme, String> colLieu;

    @FXML
    private TableColumn<Programme, Void> colAction;

    private ObservableList<Programme> data = FXCollections.observableArrayList();
    private ProgrammeService ProgrammeService = new ProgrammeService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ajouterBoutonSuppression(); // ✅ Ajoute les boutons "Supprimer" dans la TableView
        colId.setVisible(false);
        ProgrammeTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        loadData(); // Charge les données dans la table
        loadPriceStatistics(); // Statistiques pour les prix
        ProgrammeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleRowClick();
            }
        });
    }

    @FXML
    private PieChart pricePieChart;

    private void loadPriceStatistics() {
        int countAbove6 = ProgrammeService.countProgrammesAbove6();
        int countBelow6 = ProgrammeService.countProgrammesBelowOrEqual6();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Prix > 6", countAbove6),
                new PieChart.Data("Prix ≤ 6", countBelow6)
        );

        pricePieChart.setData(pieChartData);
    }

    private void ajouterBoutonSuppression() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-border-radius: 5px;");
                deleteButton.setOnAction(event -> {
                    Programme programme = getTableView().getItems().get(getIndex());
                    supprimerProgramme(programme);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    @FXML
    public void loadData() {
        data.clear();  // Nettoyer la liste avant de la remplir

        // Charger les données depuis la base de données via ProgrammeService
        data.addAll(ProgrammeService.rechercher());

        // Associer les colonnes aux attributs de l'entité Programme
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));

        ProgrammeTable.setItems(data);  // Afficher les données dans la TableView
    }

    @FXML
    private void refreshTable(ActionEvent event) {
        loadData();
        showAlert("Mise à jour", "Les données des programmes ont été rafraîchies avec succès.");
    }

    private void handleRowClick() {
        Programme selectedProgramme = ProgrammeTable.getSelectionModel().getSelectedItem();  // Récupérer le programme sélectionné

        if (selectedProgramme != null) {
            ouvrirPageModification(selectedProgramme);  // Ouvre la page de modification
        }
    }

    private void ouvrirPageModification(Programme programme) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/programme2.fxml"));
            Parent root = loader.load();

            // Passer le programme à la page de modification en fixant coach_id à 2
            ModifierProgramme controller = loader.getController();
            controller.setProgrammeData(programme.getId(), programme.getPrix(), programme.getDescription(), programme.getLieu());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Programme");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la page de modification : " + e.getMessage());
        }
    }

    @FXML
    private void allerVersAjouterSeance(ActionEvent event) {
        NavigationHelper.changerPage(event, "/seance.fxml");
    }

    @FXML
    private void allerVersAjouterApplication(ActionEvent event) {
        NavigationHelper.changerPage(event, "/application.fxml");
    }

    @FXML
    private void allerVersAjouterAvis(ActionEvent event) {
        NavigationHelper.changerPage(event, "/avis3.fxml");
    }

    @FXML
    private void allerVersAjouterProgramme(ActionEvent event) {
        NavigationHelper.changerPage(event, "/programme.fxml");
    }

    private void supprimerProgramme(Programme programme) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le programme ?");
        confirmation.setContentText("Voulez-vous vraiment supprimer ce programme ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean isDeleted = ProgrammeService.supprimer(programme.getId());
                if (isDeleted) {
                    showAlert("Succès", "Le programme a été supprimée avec succès !");
                    loadData(); // Mise à jour de la table après suppression
                    HistoriqueService historiqueService = new HistoriqueService();
                    historiqueService.ajouterHistorique("Suppression", "Programme",
                            "Programme supprimé:  " + programme.getId());
                } else {
                    showAlert("Erreur", "Impossible de supprimer le programme.");
                }
            }
        });
    }

    @FXML
    private void afficherHistorique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Historique.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Historique des actions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
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

    public void forum(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/afficherPosteB.fxml");
    }
}
