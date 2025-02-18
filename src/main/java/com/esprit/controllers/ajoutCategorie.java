package com.esprit.controllers;

import com.esprit.models.CategorieEquipement;
import com.esprit.services.CategorieService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ajoutCategorie {

    @FXML
    private TextField nom;

    @FXML
    private TextField description;

    @FXML
    private void Validate() throws IOException {
        String nomCategorie = nom.getText().trim(); // Supprimer les espaces inutiles
        String descCategorie = description.getText().trim();

        // Validation des champs
        if (!validateNom(nomCategorie) || !validateDescription(descCategorie)) {
            return; // Arrêter l'exécution si la validation échoue
        }

        // Si tout est valide, créer et ajouter la catégorie
        CategorieService e = new CategorieService();
        CategorieEquipement categorie = new CategorieEquipement(nomCategorie, descCategorie);
        e.ajouter(categorie);

        // Afficher un message de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Catégorie ajoutée avec succès.");
        alert.show();

        // Charger la vue afficher_categorie.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher_categorie.fxml"));
        Parent root = loader.load();
        nom.getScene().setRoot(root);

    }

    // Méthode pour valider le nom
    private boolean validateNom(String nom) {
        if (nom.isEmpty()) {
            showError("Erreur de saisie", "Le champ 'Nom' est obligatoire.");
            return false;
        }

        if (nom.length() > 50) {
            showError("Erreur de saisie", "Le nom ne doit pas dépasser 50 caractères.");
            return false;
        }

        if (!nom.matches("[a-zA-Z\\s-]+")) { // Autorise uniquement les lettres, espaces et tirets
            showError("Erreur de saisie", "Le nom ne doit contenir que des lettres, espaces et tirets.");
            return false;
        }

        return true;
    }

    // Méthode pour valider la description
    private boolean validateDescription(String description) {
        if (description.isEmpty()) {
            showError("Erreur de saisie", "Le champ 'Description' est obligatoire.");
            return false;
        }

        if (description.length() > 200) {
            showError("Erreur de saisie", "La description ne doit pas dépasser 200 caractères.");
            return false;
        }

        return true;
    }

    // Méthode pour afficher les messages d'erreur
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}