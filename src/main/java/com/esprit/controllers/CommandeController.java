package com.esprit.controllers;


import com.esprit.models.*;
import com.esprit.services.CommandeService;
import com.esprit.services.EquipementService;
import com.esprit.services.StripeService;
import com.esprit.tests.MainProg;
import com.stripe.model.PaymentIntent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommandeController {

    @FXML
    private Label dateCommandeLabel;

    @FXML
    private Label clientEmailLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label dateLivraisonLabel;

    @FXML
    private TableView<ArticleCommande> articlesTable;

    @FXML
    private TableColumn<ArticleCommande, ImageView> produitColumn;

    @FXML
    private TableColumn<ArticleCommande, String> descriptionColumn;

    @FXML
    private TableColumn<ArticleCommande, Double> prixColumn;

    @FXML
    private TableColumn<ArticleCommande, Integer> quantiteColumn;

    @FXML
    private TableColumn<ArticleCommande, Double> totalColumn;

    @FXML
    private Label grandTotalLabel;
    private StripeService stripeService = new StripeService();

    private CommandeService commandeService = new CommandeService();

    int clientId=881;

    @FXML
    public void initialize() {
        // Configurer les colonnes
        produitColumn.setCellValueFactory(new PropertyValueFactory<>("produit"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        if(MainProg.idClient!=0)
            clientId=MainProg.idClient;


        // Récupérer la liste des commandes
        List<Commande> commandes = commandeService.getCommandesByClientIdAndEtat(clientId, "En commande");

        if (!commandes.isEmpty()) {
            // Remplir les labels avec les informations de la première commande
            Commande premiereCommande = commandes.get(0);

            // Date de commande
            dateCommandeLabel.setText(premiereCommande.getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            // Email du client
            clientEmailLabel.setText(commandeService.getClientEmailById(premiereCommande.getClientId()));

            // Date de livraison (date de création + 2 jours)
            LocalDateTime dateLivraison = premiereCommande.getDateCreation().plusDays(2);
            dateLivraisonLabel.setText(dateLivraison.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            // Remplir le TableView avec les articles commandés
            double grandTotal = 0;

            for (Commande commande : commandes) {
                Equipement equipement = commande.getEquipement();

                if (equipement != null) {
                    // Calculer le total pour cet équipement
                    double totalEquipement = equipement.getPrix() * commande.getQuantite();
                    grandTotal += totalEquipement;

                    // Charger l'image
                    ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/" + equipement.getImage())));
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);

                    // Ajouter l'équipement à la table
                    articlesTable.getItems().add(new ArticleCommande(
                            imageView,
                            equipement.getDescription(),
                            equipement.getPrix(),
                            commande.getQuantite(),
                            totalEquipement
                    ));

                    // Log pour vérifier les données
                    System.out.println("Article ajouté : " + equipement.getDescription() + " - " + equipement.getPrix() + " DT");
                }
            }

            // Afficher le total général
            totalLabel.setText(String.format("%.2f DT", grandTotal));
            grandTotalLabel.setText(String.format("%.2f DT", grandTotal));
            // Envoyer un SMS au client
            String clientPhoneNumber = "+21627073247";
            if (clientPhoneNumber == null || clientPhoneNumber.isEmpty()) {
                System.out.println("Numéro de téléphone du client non trouvé ou invalide.");
                return;
            }

            String smsMessage = "Votre commande a été passée avec succès. Montant total : " + grandTotal + " DT";

            try {
                SMS.sendSms(clientPhoneNumber, smsMessage);
            } catch (Exception e) {
                System.out.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
            }

        }
    }

    @FXML
    private void onContinueShopping() {
        try {
            // Exemple : Récupérer l'ID du client Stripe et le PaymentMethod ID (à remplacer par les valeurs réelles)
            String customerId = "cus_Rr8up5UrbaEmDi";
            String paymentMethodId = "pm_1QxScv013eLvCqd8YPH5iHbh";

            // Récupérer le montant total depuis l'interface utilisateur
            String totalText = grandTotalLabel.getText()
                    .replace(" DT", "")  // Supprime " DT"
                    .replace(",", ".");  // Remplace la virgule par un point

            // Convertir le montant en double
            double grandTotal = Double.parseDouble(totalText);
            int amountInCents = (int) (grandTotal * 100);

            // Créer un PaymentIntent
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(amountInCents, "usd", customerId, paymentMethodId);

            // Attacher le PaymentMethod au client
            stripeService.attachPaymentMethodToCustomer(paymentMethodId, customerId);

            // Définir le PaymentMethod comme mode de paiement par défaut
            stripeService.setDefaultPaymentMethod(customerId, paymentMethodId);

            System.out.println("✅ Paiement réussi ! ID du PaymentIntent : " + paymentIntent.getId());



            // Récupérer les commandes du client
            List<Commande> commandes = commandeService.getCommandesByClientIdAndEtat(clientId, "En commande");

            // Mettre à jour les quantités des équipements et supprimer les commandes
            EquipementService equipementService = new EquipementService();
            for (Commande commande : commandes) {
                // Mettre à jour la quantité de l'équipement
                equipementService.mettreAJourQuantiteEquipement(commande.getEquipementId(), commande.getQuantite());
            }

            // Supprimer toutes les commandes du client
            commandeService.supprimerToutesLesCommandes(clientId);

            System.out.println("✅ Commandes supprimées et quantités des équipements mises à jour.");

            // Afficher une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Paiement réussi");
            alert.setHeaderText(null);
            alert.setContentText("Votre paiement a été effectué avec succès !");
            alert.showAndWait();

            // Rediriger vers la page /market
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/market.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) grandTotalLabel.getScene().getWindow(); // Récupère la fenêtre actuelle
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du paiement : " + e.getMessage());

            // Afficher une alerte d'erreur en cas d'échec
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de paiement");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors du paiement. Veuillez réessayer.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onBackArrowClicked() {
        try {
            // Charger la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/market.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Scene scene = new Scene(root);

            // Obtenir la Stage (fenêtre) actuelle
            Stage stage = (Stage) articlesTable.getScene().getWindow();

            // Changer la scène de la Stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la fenêtre market.fxml");
        }
    }
    @FXML
    private void onGeneratePDF() {
        try {
            PdfGenerator.generatePdf(clientId);
            System.out.println("PDF généré avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

}