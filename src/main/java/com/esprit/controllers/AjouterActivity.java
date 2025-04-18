package com.esprit.controllers;

import com.esprit.models.activity;
import com.esprit.models.CategorieActivity;
import com.esprit.services.ActivityService;
import com.esprit.services.CategorieService;
import com.esprit.services.CategorieServiceS;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AjouterActivity {

    private final ActivityService activityService = new ActivityService();
    private final CategorieServiceS categorieService = new CategorieServiceS();
    private String imagePath; // Stocke le chemin absolu local

    @FXML
    private TextField nomActivity;

    @FXML
    private ComboBox<CategorieActivity> categorieComboBox; // ComboBox pour choisir la catégorie

    @FXML
    private ImageView imagePreview; // Pour afficher l'image choisie

    @FXML
    private Button choisirImageButton;

    @FXML
    public void initialize() {
        chargerCategories(); // Charge les catégories au démarrage
    }

    private void chargerCategories() {
        List<CategorieActivity> categories = categorieService.rechercher(); // Récupère les catégories
        categorieComboBox.getItems().addAll(categories);
    }

    @FXML
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers Image", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            imagePath = file.getAbsolutePath(); // Stocke le chemin local
            System.out.println("Image sélectionnée : " + imagePath); // Log pour debug
            imagePreview.setImage(new Image(file.toURI().toString())); // Affiche l'aperçu
        }
    }

    @FXML
    private void ajouterActivity() {
        String nom = nomActivity.getText().trim();
        CategorieActivity categorie = categorieComboBox.getValue(); // Récupère la catégorie sélectionnée

        if (!validerSaisie(nom, categorie)) {
            return; // Si la validation échoue, on arrête
        }

        // Création de l'objet activité avec l'ID de la catégorie
        activity newActivity = new activity(0, nom, imagePath, categorie.getIdCategorie());

        try {
            activityService.ajouter(newActivity);
            showAlert("Succès", "Activité ajoutée avec succès !");
            clearFields();
            naviguerVersAffichageActivity();
        } catch (Exception e) {
            showAlert("Erreur", "L'ajout de l'activité a échoué : " + e.getMessage());
        }
    }

    private boolean validerSaisie(String nom, CategorieActivity categorie) {
        if (nom.isEmpty() || categorie == null || imagePath == null || imagePath.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs et choisir une image !");
            return false;
        }

        if (nom.length() < 3) {
            showAlert("Erreur", "Le nom doit contenir au moins 3 caractères !");
            return false;
        }

        if (!nom.matches("[a-zA-Z\\s]+")) {
            showAlert("Erreur", "Le nom ne doit contenir que des lettres et des espaces !");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomActivity.clear();
        categorieComboBox.getSelectionModel().clearSelection();
        imagePreview.setImage(null);
        imagePath = null;
    }

    private void naviguerVersAffichageActivity() {
        try {
            URL fxmlLocation = getClass().getResource("/AffichageActivity.fxml");
            if (fxmlLocation == null) {
                showAlert("Erreur", "Le fichier AffichageActivity.fxml est introuvable !");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            AffichageActivity controller = loader.getController();
            controller.afficherActivities();

            Stage stage = (Stage) nomActivity.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de naviguer vers l'affichage des activités : " + e.getMessage());
        }
    }
}
