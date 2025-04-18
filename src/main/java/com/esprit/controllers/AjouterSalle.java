package com.esprit.controllers;

import com.esprit.models.SalleSportif;
import com.esprit.services.SalleSportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AjouterSalle {

    @FXML
    private TextField nomSalle;
    @FXML
    private ComboBox<String> addresseSalle;
    @FXML
    private TextField horaireOuverture;
    @FXML
    private TextField horaireFermeture;
    @FXML
    private TextField capacity;

    @FXML
    public void initialize() {
        // Ajouter les adresses à la ComboBox
        ObservableList<String> adresses = FXCollections.observableArrayList(
                "Centre Urbain Nord", "Lac 1", "Lac 2", "Berges du Lac",
                "Menzah 5", "Ennasr", "La Marsa", "Sousse Corniche",
                "Hammamet Centre", "Sfax Ville", "Monastir Marina", "Djerba Midoun"
        );
        addresseSalle.setItems(adresses);
    }

    @FXML
    private void ajouterSalle() {
        String nom = nomSalle.getText().trim();
        String adresse = addresseSalle.getValue(); // Récupérer la valeur sélectionnée
        String ouverture = horaireOuverture.getText().trim();
        String fermeture = horaireFermeture.getText().trim();
        String cap = capacity.getText().trim();

        if (!validerSaisie(nom, adresse, ouverture, fermeture, cap)) {
            return;
        }

        try {
            int capacite = Integer.parseInt(cap);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime ouvertureTime = LocalTime.parse(ouverture, formatter);
            LocalTime fermetureTime = LocalTime.parse(fermeture, formatter);

            // Vérifier que l'heure de fermeture est après l'heure d'ouverture
            if (fermetureTime.isBefore(ouvertureTime)) {
                showAlert("Erreur", "L'heure de fermeture doit être après l'heure d'ouverture !");
                return;
            }

            SalleSportif salle = new SalleSportif(0, nom, adresse, ouvertureTime, fermetureTime, capacite);
            SalleSportService salleService = new SalleSportService();
            salleService.ajouter(salle);

            showAlert("Succès", "Salle ajoutée avec succès !");
            clearFields();
            naviguerVersAffichageSalle();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un nombre valide pour la capacité !");
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format d'heure invalide ! Utilisez HH:mm (ex: 14:30)");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout de la salle : " + e.getMessage());
        }
    }

    private boolean validerSaisie(String nom, String adresse, String ouverture, String fermeture, String cap) {
        if (nom.isEmpty() || adresse == null || ouverture.isEmpty() || fermeture.isEmpty() || cap.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return false;
        }

        if (nom.length() < 3 || !nom.matches("[a-zA-Z\\s]+")) {
            showAlert("Erreur", "Le nom de la salle doit contenir au moins 3 lettres et ne doit pas inclure de chiffres ou de caractères spéciaux !");
            return false;
        }

        try {
            int capacite = Integer.parseInt(cap);
            if (capacite <= 0) {
                showAlert("Erreur", "La capacité doit être un nombre positif !");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un nombre valide pour la capacité !");
            return false;
        }

        return true;
    }

    private void naviguerVersAffichageSalle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageSalle.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomSalle.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger l'affichage des salles : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomSalle.clear();
        addresseSalle.getSelectionModel().clearSelection();
        horaireOuverture.clear();
        horaireFermeture.clear();
        capacity.clear();
    }
}
