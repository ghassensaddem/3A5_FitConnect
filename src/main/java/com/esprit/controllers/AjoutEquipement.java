package com.esprit.controllers;

import com.esprit.models.Equipement;
import com.esprit.models.CategorieEquipement;
import com.esprit.services.EquipementService;
import com.esprit.services.CategorieService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

public class AjoutEquipement {
    private void changeScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private TableColumn<Equipement, ImageView> colImage; // Nouvelle colonne pour l'image

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

    private CategorieService categorieService = new CategorieService();
    private EquipementService equipementService = new EquipementService();

    private List<CategorieEquipement> categories;
    private String imagePath;

    @FXML
    public void initialize() {
        categories = categorieService.rechercher();
        for (CategorieEquipement categorie : categories) {
            categorieComboBox.getItems().add(categorie.getNom());
        }
        if (!categorieComboBox.getItems().isEmpty()) {
            categorieComboBox.getSelectionModel().selectFirst();
        }
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
    private void Validate() throws IOException {
        try {
            String nomEquipement = nom.getText().trim();
            String descriptionEquipement = description.getText().trim();
            String prixText = prix.getText().trim();
            String quantiteStockText = quantiteStock.getText().trim();
            String nomCategorie = categorieComboBox.getValue();

            if (!validateNom(nomEquipement) || !validateDescription(descriptionEquipement) || !validatePrix(prixText) || !validateQuantiteStock(quantiteStockText)) {
                return;
            }

            double prixEquipement = Double.parseDouble(prixText);
            int quantiteStockEquipement = Integer.parseInt(quantiteStockText);

            CategorieEquipement categorieSelectionnee = null;
            for (CategorieEquipement categorie : categories) {
                if (categorie.getNom().equals(nomCategorie)) {
                    categorieSelectionnee = categorie;
                    break;
                }
            }

            // Copier l'image dans un répertoire spécifique et obtenir le chemin relatif
            String imageFileName = null;
            if (imagePath != null && !imagePath.isEmpty()) {
                File source = new File(imagePath);
                File destination = new File("src/main/resources/images/" + source.getName());
                Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                imageFileName = "images/" + source.getName();
            }

            Equipement equipement = new Equipement(nomEquipement, descriptionEquipement, prixEquipement, quantiteStockEquipement, categorieSelectionnee.getId(), imageFileName);

            equipementService.ajouter(equipement);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Équipement ajouté avec succès !");
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
    @FXML
    public void handleActivities(ActionEvent event) throws IOException {
        changeScene(event, "/AffichageActivity.fxml");
    }
    @FXML
    public void handleSalles(ActionEvent event) throws IOException {
        changeScene(event, "/AffichageSalle.fxml");
    }
    @FXML
    public void handleEvenement(ActionEvent event) throws IOException {
        changeScene(event, "/event3.fxml");
    }


}