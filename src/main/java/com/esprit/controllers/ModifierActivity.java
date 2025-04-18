package com.esprit.controllers;

import com.esprit.models.activity;
import com.esprit.models.CategorieActivity;
import com.esprit.services.ActivityService;
import com.esprit.services.CategorieService;
import com.esprit.services.CategorieServiceS;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ModifierActivity {
    @FXML
    private TextField nomActivity;
    @FXML
    private TextField iconActivity;
    @FXML
    private ComboBox<String> categorieActivity; // Utilisation d'une ComboBox pour la catégorie
    @FXML
    private ImageView imagePreview;
    @FXML
    private Button btnSelectImage;

    private activity currentActivity;
    private final ActivityService activityService = new ActivityService();
    private final CategorieServiceS categorieService = new CategorieServiceS(); // Service pour gérer les catégories

    public void initData(activity activity) {
        if (activity == null) return;
        this.currentActivity = activity;

        // Remplissage des champs avec les données actuelles de l'activité
        nomActivity.setText(activity.getNomActivity());
        iconActivity.setText(activity.getIconActivity());

        // Chargement des catégories dans la ComboBox
        chargerCategories();

        // Sélection de la catégorie actuelle dans la ComboBox
        CategorieActivity categorie = categorieService.getCategorieById(activity.getIdCategorie());
        if (categorie != null) {
            categorieActivity.setValue(categorie.getNomCategorie());
        }

        // Chargement de l'image actuelle
        if (activity.getIconActivity() != null && !activity.getIconActivity().isEmpty()) {
            File file = new File(activity.getIconActivity());
            if (file.exists()) {
                imagePreview.setImage(new Image(file.toURI().toString()));
            }
        }
    }

    private void chargerCategories() {
        List<CategorieActivity> categories = categorieService.rechercher(); // Remplace getAllCategories()
        for (CategorieActivity categorie : categories) {
            categorieActivity.getItems().add(categorie.getNomCategorie());
        }
    }

    @FXML
    private void enregistrerModification() {
        if (currentActivity == null) return;

        String nom = nomActivity.getText().trim();
        String categorieNom = categorieActivity.getValue();
        String imagePath = iconActivity.getText().trim();

        // Vérification des saisies
        if (!validerSaisie(nom, categorieNom, imagePath)) {
            return;
        }

        // Récupérer l'ID de la catégorie à partir du nom
        Optional<CategorieActivity> categorie = categorieService.rechercher().stream()
                .filter(c -> c.getNomCategorie().equals(categorieNom))
                .findFirst();

        if (!categorie.isPresent()) {
            showAlert("Erreur", "La catégorie sélectionnée est invalide !");
            return;
        }

        // Mise à jour de l'activité avec les nouvelles valeurs
        currentActivity.setNomActivity(nom);
        currentActivity.setIconActivity(imagePath);
        currentActivity.setIdCategorie(categorie.get().getIdCategorie());

        // Sauvegarde dans la base de données
        try {
            activityService.modifier(currentActivity);
            showAlert("Succès", "Activité modifiée avec succès !");
            naviguerVersAffichageActivity();
        } catch (Exception e) {
            showAlert("Erreur", "Échec de la modification : " + e.getMessage());
        }
    }

    private boolean validerSaisie(String nom, String categorie, String imagePath) {
        if (nom.isEmpty() || categorie == null || imagePath.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
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

        File file = new File(imagePath);
        if (!file.exists()) {
            showAlert("Erreur", "L'image sélectionnée est invalide !");
            return false;
        }

        return true;
    }

    @FXML
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            iconActivity.setText(file.getAbsolutePath());
            imagePreview.setImage(new Image(file.toURI().toString()));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void naviguerVersAffichageActivity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageActivity.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomActivity.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de naviguer vers AffichageActivity : " + e.getMessage());
        }
    }
}
