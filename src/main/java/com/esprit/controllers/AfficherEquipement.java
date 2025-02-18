package com.esprit.controllers;

import com.esprit.models.Equipement;
import com.esprit.services.EquipementService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherEquipement implements Initializable {
    @FXML
    private TableView<Equipement> equipementTable;

    @FXML
    private TableColumn<Equipement, Integer> colId;

    @FXML
    private TableColumn<Equipement, String> colNom;

    @FXML
    private TableColumn<Equipement, String> colDescription;

    @FXML
    private TableColumn<Equipement, Double> colPrix;

    @FXML
    private TableColumn<Equipement, Integer> colQuantiteStock;

    @FXML
    private TableColumn<Equipement, String> colCategorie;

    @FXML
    private TableColumn<Equipement, String> colImage; // Colonne pour l'image

    @FXML
    private TableColumn<Equipement, Void> colActions;

    private EquipementService equipementService = new EquipementService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurer les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colQuantiteStock.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorieNom()));

        // Colonne pour l'image
        colImage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImage()));
        colImage.setCellFactory(new Callback<TableColumn<Equipement, String>, TableCell<Equipement, String>>() {
            @Override
            public TableCell<Equipement, String> call(TableColumn<Equipement, String> param) {
                return new TableCell<Equipement, String>() {
                    private final ImageView imageView = new ImageView();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Image image = new Image(item); // Charger l'image à partir de l'URL ou du chemin
                            imageView.setImage(image);
                            imageView.setFitHeight(50);  // Ajuste la taille de l'image
                            imageView.setFitWidth(50);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });

        addButtonToTable();

        // Charger les équipements dans la TableView
        loadEquipements();
    }

    public void loadEquipements() {
        try {
            List<Equipement> equipements = equipementService.rechercher();
            if (equipements != null && !equipements.isEmpty()) {
                equipementTable.getItems().setAll(equipements);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des équipements : " + e.getMessage());
        }
    }

    private void addButtonToTable() {
        Callback<TableColumn<Equipement, Void>, TableCell<Equipement, Void>> cellFactory =
                new Callback<>() {
                    public TableCell<Equipement, Void> call(final TableColumn<Equipement, Void> param) {
                        return new TableCell<>() {
                            private final Button btnModifier = new Button("Modifier");
                            private final Button btnSupprimer = new Button("Supprimer");
                            private final HBox hbox = new HBox(10, btnModifier, btnSupprimer);

                            {
                                // Ajouter une action au bouton Supprimer
                                btnSupprimer.setOnAction(event -> {
                                    Equipement equipement = getTableView().getItems().get(getIndex());
                                    equipementService.supprimer(equipement);
                                    loadEquipements(); // Rafraîchir la TableView après la suppression
                                });

                                // Ajouter une action au bouton Modifier (à implémenter)
                                btnModifier.setOnAction(event -> {
                                    Equipement equip = getTableView().getItems().get(getIndex());
                                    handleModifierButton(equip);
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

    private void handleModifierButton(Equipement equipement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifier_equipement.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et passer les données
            ModifierEquipement controller = loader.getController();
            controller.setEquipement(equipement);

            // Remplacer la scène actuelle par la nouvelle scène
            Stage stage = (Stage) equipementTable.getScene().getWindow(); // Obtenir la fenêtre actuelle
            stage.setScene(new Scene(root)); // Charger la nouvelle scène dans la même fenêtre
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la fenêtre de modification : " + e.getMessage());
        }
    }

























    @FXML
    public void naviguer(ActionEvent actionEvent) {
        try {
            // Charger la page AjouterEquipement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouter_equipement.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Scene currentScene = ((Node) actionEvent.getSource()).getScene();

            // Remplacer la scène actuelle par la nouvelle scène
            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page AjouterEquipement.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
