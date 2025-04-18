package com.esprit.controllers;

import com.esprit.models.CategorieEquipement;
import com.esprit.services.CategorieService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierCategorie {
    @FXML
    private TextField nom; // Champ pour le nom

    @FXML
    private TextField description; // Champ pour la description
    @FXML
    private TextField id;  // Attribut pour stocker l'ID

    private final CategorieService categorieService = new CategorieService();
  

    public void setCategorieData(CategorieEquipement categorie) {
        id.setText(String.valueOf(categorie.getId()));
        nom.setText(categorie.getNom()); // Remplir le champ nom
        description.setText(categorie.getDescription());
        javafx.application.Platform.runLater(() -> nom.positionCaret(nom.getText().length()));// Placer le curseur à la fin sans sélectionner le texte
    }
    @FXML
    private void modifierCategorie() throws IOException {
        int categorieId = Integer.parseInt(id.getText());

        String nouveauNom = nom.getText();
        String nouvelleDescription = description.getText();
        if (!validateNom(nouveauNom ) || !validateDescription(nouvelleDescription)) {
            return; // Arrêter l'exécution si la validation échoue
        }

        // Créer un objet CategorieEquipement avec les nouvelles données
        CategorieEquipement categorie = new CategorieEquipement();
        categorie.setId(categorieId);
        categorie.setNom(nouveauNom);
        categorie.setDescription(nouvelleDescription);

        // Appeler le service pour mettre à jour la catégorie
        categorieService.modifier(categorie);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Categorie modifier");
        alert.show();

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
