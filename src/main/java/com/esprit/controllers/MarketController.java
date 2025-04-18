package com.esprit.controllers;

import com.esprit.models.CategorieEquipement;
import com.esprit.models.Equipement;
import com.esprit.services.CategorieService;
import com.esprit.services.EquipementService;
import com.esprit.services.MyListener;
import com.esprit.tests.MainProg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MarketController implements Initializable {
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

    @FXML
    private GridPane grid;

    private MyListener<Equipement> myListener;
    private MyListener<CategorieEquipement> myListener2;
    private int quantity = 0;

    @FXML
    private ScrollPane scroll;

    private String imagePath;
    private CategorieService categorieService = new CategorieService();
    private EquipementService equipementService = new EquipementService();
    private List<Equipement> equipements = new ArrayList<>();
    private List<CategorieEquipement> categories = categorieService.rechercher();

    private void setChosenEquipement(Equipement equipement) {
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
        this.afficher_categories();
        quantityLabel.setText(String.valueOf(quantity));
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
}
