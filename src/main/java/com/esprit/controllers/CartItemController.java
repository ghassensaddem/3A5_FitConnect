package com.esprit.controllers;
import com.esprit.controllers.MarketController;
import com.esprit.controllers.PanierController;
import com.esprit.models.Commande;
import com.esprit.services.CommandeService;
import com.esprit.services.MyListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;

public class CartItemController {
    private MarketController marketController;


    private Commande commande;
    private CommandeService commandeService = new CommandeService();
    private PanierController panierController;
    @FXML
    private HBox cartItem;
    private int quantity;

    @FXML
    private ImageView itemImage;

    @FXML
    private Label itemTitle;

    @FXML
    private Label itemDescription;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label itemPrice;
    public void setPanierController(PanierController panierController) {
        this.panierController = panierController;
    }
    public void setMarketController(MarketController marketController) {
        this.marketController = marketController;
    }

    public void setData(Commande commande) {
        this.commande = commande; // Stocker la commande
        itemTitle.setText(commande.getEquipement().getNom());
        itemDescription.setText(commande.getEquipement().getDescription());
        itemPrice.setText(commande.getEquipement().getPrix() + " DT");
        quantityLabel.setText(String.valueOf(commande.getQuantite()));

        // Charger l'image
        try {
            String imageAbsolutePath = "src/main/resources/" + commande.getEquipement().getImage();
            File imageFile = new File(imageAbsolutePath);

            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                itemImage.setImage(image);
            } else {
                System.err.println("Le fichier image n'existe pas : " + imageAbsolutePath);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }
    @FXML
    private void increaseQuantity() {
        commande.setQuantite(commande.getQuantite() + 1);
        commandeService.modifier_quantite(commande);
        quantityLabel.setText(String.valueOf(commande.getQuantite()));

        if (panierController != null) {
            panierController.loadCart();
        }

        // 🔥 Mettre à jour le badge du panier
        if (marketController != null) {
            marketController.updateBadgeQuantite();
        }
    }


    @FXML
    private void decreaseQuantity() {
        if (commande.getQuantite() > 1) {
            // Diminuer la quantité dans la base de données
            commande.setQuantite(commande.getQuantite() - 1);
            commandeService.modifier_quantite(commande);

            // Mettre à jour l'affichage
            quantityLabel.setText(String.valueOf(commande.getQuantite()));

            if (panierController != null) {
                panierController.loadCart();
            }

            // 🔥 Mettre à jour le badge du panier
            if (marketController != null) {
                marketController.updateBadgeQuantite();
            }
        }
    }

    @FXML
    private void deleteItem() {
        // Supprimer la commande de la base de données
        CommandeService commandeService = new CommandeService();
        commandeService.supprimer(commande);

        panierController.loadCart();

        // 🔥 Mettre à jour le badge du panier
        if (marketController != null) {
            marketController.updateBadgeQuantite();
        }
    }



}