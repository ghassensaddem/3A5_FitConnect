package com.esprit.controllers;

import com.esprit.models.typeactivite;
import com.esprit.services.typeactiviteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class TypeActivite {
    @FXML
    private TextField hr;
    @FXML
    private TextArea ds; // Correction ici

    @FXML
    void Validate(ActionEvent event) throws IOException {
        // Récupération des valeurs des champs
        String horaire = hr.getText().trim();
        String description = ds.getText().trim();

        // Vérifications : tous les champs doivent être remplis
        if (horaire.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis !");
            return;
        }

        // Vérification supplémentaire : la description ne doit pas être trop courte
        if (description.length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La description doit contenir au moins 5 caractères !");
            return;
        }

        // Ajout du type d'activité après validation
        typeactiviteService e = new typeactiviteService();
        e.ajouter(new typeactivite(horaire, description));

        // Affichage d'un message de confirmation
        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Type ajouté avec succès !");
    }

    // Méthode générique pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


    @FXML
    private void goToAjouterEvent(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToList2(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activiteevent2.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToList3(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/typeactivite3.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


