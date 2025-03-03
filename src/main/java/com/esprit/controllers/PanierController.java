package com.esprit.controllers;
import com.esprit.models.SMS;
import com.esprit.models.Commande;
import com.esprit.services.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class PanierController {
    private Stage panierStage;
    private MarketController marketController;
    public void setMarketController(MarketController marketController) {
        this.marketController = marketController;
    }
    public void setPanierStage(Stage panierStage) {
        this.panierStage = panierStage;
    }

    @FXML
    private Label clearAllButton;
    @FXML
    private Label closeButton;

    @FXML
    private VBox cartContainer; // Conteneur pour les articles du panier

    @FXML
    private Label subtotalPrice; // Label pour afficher le sous-total

    private CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        loadCart(); // Charger les articles du panier au démarrage
    }

    public void loadCart() {
        // Récupérer les commandes du client (remplacez 1 par l'ID du client connecté)
        List<Commande> commandes = commandeService.rechercherParClientId(1);

        // Effacer les anciens articles du panier
        cartContainer.getChildren().clear();

        // Afficher les commandes dans le panier
        for (Commande commande : commandes) {
            try {
                // Charger le fichier FXML pour un article du panier
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cart-item.fxml"));
                HBox cartItem = loader.load(); // Charger en tant que HBox

                // Récupérer le contrôleur et définir les données de l'article
                CartItemController cartItemController = loader.getController();
                cartItemController.setData(
                       commande
                );
                cartItemController.setPanierController(this);
                cartItemController.setMarketController(marketController);
                // Ajouter l'article au conteneur
                cartContainer.getChildren().add(cartItem);
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement de l'article du panier : " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Calculer et afficher le sous-total
        double subtotal = commandes.stream()
                .mapToDouble(c -> c.getQuantite() * c.getEquipement().getPrix())
                .sum();
        subtotalPrice.setText(String.format("%.2f DT", subtotal));
    }



    @FXML
    private void closeWindow() {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clearAllCommands() {
        // Supprimer toutes les commandes du client
        commandeService.supprimerToutesLesCommandes(1); // Remplacez 1 par l'ID du client connecté

        // Recharger le panier (vide)
        loadCart();
        // 🔥 Mettre à jour le badge du panier
        if (marketController != null) {
            marketController.updateBadgeQuantite();
        }
    }

    @FXML
    private void passerCommande() {
        try {
            // Remplace 1 par l'ID du client connecté
            commandeService.passerCommande(1);



            loadCart();

            // 🔥 Mettre à jour le badge du panier
            if (marketController != null) {
                marketController.updateBadgeQuantite();
            }

            // Fermer la fenêtre du panier
            if (panierStage != null) {
                panierStage.close();
            }

            // Redirection vers la fenêtre commande.fxml
            try {
                // Charger la nouvelle interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/commande.fxml")); // Utilisez le bon chemin
                Parent root = loader.load();

                // Obtenir la scène actuelle
                Scene scene = new Scene(root);

                // Obtenir la Stage (fenêtre) actuelle du MarketController
                Stage stage = (Stage) marketController.getChosenFruitCard().getScene().getWindow();

                // Changer la scène de la Stage
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de la fenêtre commande.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la passation de la commande : " + e.getMessage());
        }
    }


}