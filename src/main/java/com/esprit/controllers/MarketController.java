package com.esprit.controllers;
import com.esprit.services.CommandeService;
import com.esprit.models.CategorieEquipement;
import com.esprit.models.Equipement;
import com.esprit.services.CategorieService;
import com.esprit.services.EquipementService;
import com.esprit.services.MyListener;
import com.esprit.tests.MainProg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import com.esprit.models.Commande;
import com.esprit.services.CommandeService;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;









public class MarketController implements Initializable {
    @FXML
    private TextField searchField; // TextField pour la recherche

    @FXML
    private Button searchButton; // Bouton pour la recherche
    @FXML
    private Label badgeQuantite;
    private Equipement selectedEquipement;
    @FXML
    private Label quantityLabel;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private ImageView fruitImg;

    @FXML
    private Label fruitNameLable;

    @FXML
    private Label fruitPriceLabel;
    @FXML
    private Label fruitDescriptionLabel;

    @FXML
    private GridPane category_grid;
    int clientId=881;

    @FXML
    private GridPane grid;
    public VBox getChosenFruitCard() {
        return chosenFruitCard;
    }

    private MyListener<Equipement> myListener;
    private MyListener<CategorieEquipement> myListener2;
    private int quantity = 0;
    private CommandeService commandeService = new CommandeService();
    @FXML
    private ScrollPane scroll;

    private String imagePath;
    private CategorieService categorieService = new CategorieService();
    private EquipementService equipementService = new EquipementService();
    private List<Equipement> equipements = new ArrayList<>();
    private List<CategorieEquipement> categories = categorieService.rechercher();

    private void setChosenEquipement(Equipement equipement) {
        this.selectedEquipement = equipement;

        fruitNameLable.setText(equipement.getNom());
        fruitDescriptionLabel.setText(equipement.getDescription());
        fruitPriceLabel.setText(MainProg.CURRENCY + equipement.getPrix());
        if (equipement.getImage() != null && !equipement.getImage().isEmpty()) {
            imagePath = equipement.getImage();

            try {
                String imageAbsolutePath = "src/main/resources/" + imagePath;
                File imageFile = new File(imageAbsolutePath);

                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    fruitImg.setImage(image);
                } else {
                    System.err.println("Le fichier image n'existe pas : " + imageAbsolutePath);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }

        chosenFruitCard.setStyle("-fx-background-color: #006400; -fx-background-radius: 30;");
    }

    public void afficher_categories() {
        if (categories.size() > 0) {
            myListener2 = new MyListener<CategorieEquipement>() {
                @Override
                public void onClickListener(CategorieEquipement categorie) {
                    equipements = equipementService.rechercherParId(categorie.getId());
                    updateEquipementsGrid();
                }
            };
        }

        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < categories.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/item2.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                Item2 item2 = fxmlLoader.getController();
                item2.setData(categories.get(i), myListener2);
                category_grid.add(anchorPane, column++, row);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEquipementsGrid() {
        grid.getChildren().clear(); // Clear the existing equipment display
        if(equipements.size()>0){
            //setChosenEquipement(equipements.get(0));
            myListener = new MyListener<Equipement>() {
                @Override
                public void onClickListener(Equipement equipement) {
                    setChosenEquipement(equipement);
                }
            };
        }

        int column = 0;
        int row = 1;

        try {
            for (int i = 0; i < equipements.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();
                itemController.setData(equipements.get(i), myListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row);

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.updateBadgeQuantite();
        this.afficher_categories();
        quantityLabel.setText(String.valueOf(quantity));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearchByName(newValue);
        });
    }

    @FXML
    private void increaseQuantity() {
        quantity++;
        quantityLabel.setText(String.valueOf(quantity));
    }

    @FXML
    private void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
            quantityLabel.setText(String.valueOf(quantity));
        }
    }
    @FXML
    private void addToCart() {
        if (selectedEquipement != null && quantity > 0) {
            // Créer une nouvelle commande
            Commande commande = new Commande(
                    clientId, // Remplacez par l'ID du client connecté
                    selectedEquipement.getId(), // ID de l'équipement
                    "En attente", // État de la commande
                    "Non payé", // Statut du paiement
                    quantity // Quantité
            );

            // Ajouter la commande à la base de données
            CommandeService commandeService = new CommandeService();
            commandeService.ajouter(commande);
            updateBadgeQuantite();

            // Afficher un message de succès
            System.out.println("Équipement ajouté au panier !");
        } else {
            System.out.println("Veuillez sélectionner un équipement et une quantité valide.");
        }
    }


    @FXML
    private void openCart() {
        try {
            // Charger le fichier FXML du panier
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/panier.fxml"));
            Parent root = loader.load();
            // Récupérer le contrôleur du panier
            PanierController panierController = loader.getController();

            // Passer l'instance du MarketController au PanierController
            panierController.setMarketController(this);

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Récupérer la fenêtre principale (market.fxml)
            Stage primaryStage = (Stage) chosenFruitCard.getScene().getWindow(); // Remplace marketButton par un élément de ton interface

            // Récupérer la position et la taille de la fenêtre principale
            double primaryX = primaryStage.getX();
            double primaryY = primaryStage.getY();
            double primaryWidth = primaryStage.getWidth();
            double primaryHeight = primaryStage.getHeight();

            // Créer une nouvelle fenêtre (stage) pour le panier
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED); // Supprime la barre de titre et les boutons de contrôle
            stage.setScene(scene);
            stage.setTitle("Panier");
            panierController.setPanierStage(stage);

            // Définir la position du panier (à droite et au-dessus de market.fxml)
            double panierWidth = 400; // Largeur de la fenêtre du panier (ajuste selon besoin)
            double panierHeight = primaryHeight * 0.8; // Hauteur relative à la fenêtre principale

            stage.setX(primaryX + primaryWidth - panierWidth); // Position à droite
            stage.setY(primaryY + (primaryHeight - panierHeight) / 2); // Centré verticalement

            // Définir la taille du panier
            stage.setWidth(panierWidth);
            stage.setHeight(panierHeight);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page du panier : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void updateBadgeQuantite() {
        int totalQuantite = commandeService.getTotalQuantitePanier(clientId);
        System.out.println("Quantité dans updateCartBadge() : " + totalQuantite);

        if (totalQuantite > 0) {
            badgeQuantite.setText(String.valueOf(totalQuantite));
            badgeQuantite.setVisible(true);
        } else {
            badgeQuantite.setVisible(false);
        }
    }

    @FXML
    private void navigateToCommande() {
        try {
            // Charger la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/commande.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Scene scene = new Scene(root);

            // Obtenir la Stage (fenêtre) actuelle
            Stage stage = (Stage) chosenFruitCard.getScene().getWindow(); // Utilisez marketButton ici

            // Changer la scène de la Stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la fenêtre commande.fxml");
        }
    }

    // Méthode pour gérer la recherche par nom
    private void handleSearchByName(String searchText) {
        if (searchText != null && !searchText.isEmpty()) {

            equipements = equipementService.searchByName(searchText);
        }
        updateEquipementsGrid();
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
