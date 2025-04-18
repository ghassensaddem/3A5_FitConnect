package com.esprit.controllers;

import com.esprit.models.Equipement;
import com.esprit.models.CategorieEquipement;
import com.esprit.services.EquipementService;
import com.esprit.services.CategorieService;
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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ModifierEquipement {

    @FXML
    private TextField id;

    @FXML
    private TextField nom;

    @FXML
    private TextField description;

    @FXML
    private TextField prix;

    @FXML
    private TextField quantiteStock;

    @FXML
    private ComboBox<String> categorieComboBox;

    @FXML
    private Button browseButton;

    @FXML
    private Label imagePathLabel;

    @FXML
    private ImageView imageView;

    private final CategorieService categorieService = new CategorieService();
    private final EquipementService equipementService = new EquipementService();

    private List<CategorieEquipement> categories;
    private String imagePath; // Chemin de l'image sélectionnée

    @FXML
    public void initialize() {
        // Récupérer toutes les catégories une seule fois
        categories = categorieService.rechercher();

        // Ajouter les noms des catégories au ComboBox
        for (CategorieEquipement categorie : categories) {
            categorieComboBox.getItems().add(categorie.getNom());
        }
    }

    public void setEquipement(Equipement equipement) {
        // Remplir les champs avec les données de l'équipement à modifier
        id.setText(String.valueOf(equipement.getId()));
        nom.setText(equipement.getNom());
        description.setText(equipement.getDescription());
        prix.setText(String.valueOf(equipement.getPrix()));
        quantiteStock.setText(String.valueOf(equipement.getQuantiteStock()));

        // Sélectionner la catégorie correspondante dans le ComboBox
        String nomCategorie = CategorieService.getCategorieNomById(equipement.getIdCategorie());
        categorieComboBox.setValue(nomCategorie);

        // Afficher l'image actuelle de l'équipement dans l'ImageView
        if (equipement.getImage() != null && !equipement.getImage().isEmpty()) {
            imagePath = equipement.getImage(); // Stocker le chemin de l'image

            // Charger l'image dans l'ImageView
            try {
                // Convertir le chemin relatif en chemin absolu
                String imageAbsolutePath = "src/main/resources/" + imagePath;
                File imageFile = new File(imageAbsolutePath);

                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    imageView.setImage(image); // Afficher l'image dans l'ImageView
                } else {
                    System.err.println("Le fichier image n'existe pas : " + imageAbsolutePath);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }

        javafx.application.Platform.runLater(() -> nom.positionCaret(nom.getText().length()));
    }

    @FXML
    private void handleBrowseButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");

        // Ajouter un filtre pour ne permettre que les fichiers image
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Ouvrir la boîte de dialogue pour sélectionner un fichier
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Stocker le chemin de l'image (au cas où il est nécessaire pour l'enregistrement)
            imagePath = selectedFile.getAbsolutePath();

            // Afficher uniquement l'image sélectionnée sans montrer l'URL
            imageView.setFitWidth(200);  // Largeur de l'image
            imageView.setFitHeight(200); // Hauteur de l'image
            imageView.setPreserveRatio(true); // Maintenir les proportions
            imageView.setImage(new Image(selectedFile.toURI().toString()));
            // Supprimer la ligne qui affiche le chemin de l'image
            // imagePathLabel.setText(imagePath);
        }
    }

    @FXML
    private void modifierEquipement() {
        try {
            // Récupérer les valeurs des champs
            int idEquipement = Integer.parseInt(id.getText());
            String nomEquipement = nom.getText();
            String descriptionEquipement = description.getText();
            double prixEquipement = Double.parseDouble(prix.getText());
            int quantiteStockEquipement = Integer.parseInt(quantiteStock.getText());
            String nomCategorie = categorieComboBox.getValue();

            // Valider les champs
            if (!validateNom(nomEquipement)) return;
            if (!validateDescription(descriptionEquipement)) return;
            if (!validatePrix(String.valueOf(prixEquipement))) return;
            if (!validateQuantiteStock(String.valueOf(quantiteStockEquipement))) return;

            // Trouver la catégorie sélectionnée
            CategorieEquipement categorieSelectionnee = categories.stream()
                    .filter(c -> c.getNom().equals(nomCategorie))
                    .findFirst()
                    .orElse(null);

            if (categorieSelectionnee == null) {
                showError("Erreur", "Catégorie non trouvée.");
                return;
            }

            // Copier l'image dans un répertoire spécifique
            String imageFileName = null;
            if (imagePath != null && !imagePath.isEmpty()) {
                File source = new File(imagePath);
                File destination = new File("src/main/resources/images/" + source.getName());
                Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                imageFileName = "images/" + source.getName();
            }

            // Créer un nouvel équipement avec les données mises à jour
            Equipement equipement = new Equipement(
                    idEquipement,
                    nomEquipement,
                    descriptionEquipement,
                    prixEquipement,
                    quantiteStockEquipement,
                    categorieSelectionnee.getId(),
                    imageFileName
            );

            // Modifier l'équipement dans la base de données
            equipementService.modifier(equipement);

            // Afficher une confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Équipement modifié avec succès !");
            alert.showAndWait(); // Utilisez showAndWait() pour attendre que l'utilisateur ferme l'alerte

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher_equipement.fxml"));
            Parent root = loader.load();

            // Passer des données au contrôleur de la nouvelle page
            AfficherEquipement controller = loader.getController();
            controller.loadEquipements(); // Recharger les données

            Stage stage = (Stage) nom.getScene().getWindow(); // Obtenir la fenêtre actuelle
            stage.setScene(new Scene(root)); // Définir la nouvelle scène
            stage.show(); // Afficher la nouvelle scène

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

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

    // Méthode pour valider le prix
    private boolean validatePrix(String prix) {
        if (prix.isEmpty()) {
            showError("Erreur de saisie", "Le champ 'Prix' est obligatoire.");
            return false;
        }

        try {
            double prixValue = Double.parseDouble(prix);
            if (prixValue <= 0) {
                showError("Erreur de saisie", "Le prix doit être supérieur à 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Erreur de saisie", "Le prix doit être un nombre valide.");
            return false;
        }

        return true;
    }

    // Méthode pour valider la quantité en stock
    private boolean validateQuantiteStock(String quantiteStock) {
        if (quantiteStock.isEmpty()) {
            showError("Erreur de saisie", "Le champ 'Quantité en stock' est obligatoire.");
            return false;
        }

        try {
            int quantiteStockValue = Integer.parseInt(quantiteStock);
            if (quantiteStockValue < 0) {
                showError("Erreur de saisie", "La quantité en stock ne peut pas être négative.");
                return false;
            }
            else if (quantiteStockValue == 0) {
                showError("Erreur de saisie", "La quantité en stock ne peut pas être zéro.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Erreur de saisie", "La quantité en stock doit être un nombre entier valide.");
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