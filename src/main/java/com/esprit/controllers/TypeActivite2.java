package com.esprit.controllers;

import com.esprit.models.activiteevent;
import com.esprit.models.typeactivite;
import com.esprit.services.activiteEventService;
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

public class TypeActivite2 {
    @FXML
    private TextField id;
    @FXML
    private TextField hr;
    @FXML
    private TextArea ds; // Correction ici

    @FXML
    void Validate(ActionEvent event) throws IOException {
        // Récupération des valeurs des champs
        String idText = id.getText().trim();
        String horaire = hr.getText().trim();
        String description = ds.getText().trim();

        // Vérification que tous les champs sont remplis
        if (idText.isEmpty() || horaire.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis !");
            return;
        }

        // Vérification de l'ID (doit être un entier positif)
        int typeId;
        try {
            typeId = Integer.parseInt(idText);
            if (typeId <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "L'ID doit être un entier positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un ID valide !");
            return;
        }

        // Vérification supplémentaire : la description ne doit pas être trop courte
        if (description.length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La description doit contenir au moins 5 caractères !");
            return;
        }

        // Modification du type d'activité après validation
        typeactiviteService e = new typeactiviteService();
        e.modifier(new typeactivite(typeId, horaire, description));

        // Affichage d'un message de confirmation
        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Type modifié avec succès !");
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
    private typeactivite selectedEvent;
    private typeactiviteService eventService = new typeactiviteService();
    public void initData2(typeactivite event) {
        this.selectedEvent = event;

        id.setText(String.valueOf(event.getId()));
        hr.setText(event.getTitle());
        ds.setText(event.getDescription());

        id.setDisable(true); // Empêcher la modification de l'ID
    }
}


