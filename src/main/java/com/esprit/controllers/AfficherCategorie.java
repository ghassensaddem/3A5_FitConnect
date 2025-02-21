package com.esprit.controllers;

import com.esprit.models.CategorieEquipement;
import com.esprit.services.CategorieService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherCategorie implements Initializable {

    @FXML
    private TableView<CategorieEquipement> categorieTable; // TableView pour afficher les catégories

    @FXML
    private TableColumn<CategorieEquipement, Integer> colId; // Colonne pour l'ID

    @FXML
    private TableColumn<CategorieEquipement, String> colNom; // Colonne pour le nom

    @FXML
    private TableColumn<CategorieEquipement, String> colDescription; // Colonne pour la description

    @FXML
    private TableColumn<CategorieEquipement, Void> colActions; // Colonne pour les boutons Modifier et Supprimer

    private final CategorieService categorieService = new CategorieService(); // Service pour récupérer les catégories

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuration des colonnes de la TableView
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Ajout des boutons Modifier et Supprimer avec action
        addButtonToTable();

        // Chargement des données
        loadCategories();
    }

    private void handleModifierButton(CategorieEquipement categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifier_categorie.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et passer les données
            ModifierCategorie controller = loader.getController();
            controller.setCategorieData(categorie);

            // Remplacer la scène actuelle par la nouvelle scène
            Stage stage = (Stage) categorieTable.getScene().getWindow(); // Obtenir la fenêtre actuelle
            stage.setScene(new Scene(root)); // Charger la nouvelle scène dans la même fenêtre
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la fenêtre de modification : " + e.getMessage());
        }
    }


    /**
     * Charge les catégories depuis la base de données et les affiche dans la TableView.
     */
    private void loadCategories() {
        try {
            List<CategorieEquipement> categories = categorieService.rechercher();
            if (categories != null && !categories.isEmpty()) {
                categorieTable.getItems().setAll(categories);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des catégories : " + e.getMessage());
        }
    }

    /**
     * Ajoute les boutons Modifier et Supprimer à chaque ligne de la colonne Actions.
     */
    private void addButtonToTable() {
        Callback<TableColumn<CategorieEquipement, Void>, TableCell<CategorieEquipement, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<CategorieEquipement, Void> call(final TableColumn<CategorieEquipement, Void> param) {
                        return new TableCell<>() {
                            private final Button btnModifier = new Button("Modifier");
                            private final Button btnSupprimer = new Button("Supprimer");
                            private final HBox hbox = new HBox(10, btnModifier, btnSupprimer);

                            {
                                // Ajouter une action au bouton Supprimer
                                btnSupprimer.setOnAction(event -> {
                                    CategorieEquipement categorie = getTableView().getItems().get(getIndex());
                                    categorieService.supprimer(categorie);
                                    loadCategories(); // Rafraîchir la TableView après la suppression
                                });

                                // Ajouter une action au bouton Modifier (à implémenter)
                                btnModifier.setOnAction(event -> {
                                    CategorieEquipement categorie = getTableView().getItems().get(getIndex());
                                    handleModifierButton(categorie);

                                });
                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(hbox); // Afficher les boutons dans la cellule
                                }
                            }
                        };
                    }
                };

        colActions.setCellFactory(cellFactory); // Associer le cellFactory à la colonne Actions
    }
}