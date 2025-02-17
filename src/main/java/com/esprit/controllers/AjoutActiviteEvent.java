package com.esprit.controllers;

import com.esprit.models.activiteevent;
import com.esprit.services.activiteEventService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AjoutActiviteEvent {
    @FXML
    private TextField hr;
    @FXML
    private TextField np;
    @FXML
    private ChoiceBox<Integer> id1;
    @FXML
    private ChoiceBox<Integer> id2;

    @FXML
    void Validate(ActionEvent event) throws IOException {
        // Récupération des valeurs des champs
        String horaire = hr.getText().trim();
        String nbParticipantsText = np.getText().trim();
        Integer id1Value = (id1.getValue() instanceof Integer) ? (Integer) id1.getValue() : null;
        Integer id2Value = (id2.getValue() instanceof Integer) ? (Integer) id2.getValue() : null;

        // Vérifications : tous les champs doivent être remplis
        if (horaire.isEmpty() || nbParticipantsText.isEmpty() || id1Value == null || id2Value == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis !");
            return;
        }

        // Vérification du nombre de participants (doit être un entier positif)
        int nbParticipants;
        try {
            nbParticipants = Integer.parseInt(nbParticipantsText);
            if (nbParticipants <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nombre de participants doit être un entier positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un nombre de participants valide !");
            return;
        }

        // Ajout de l'activité après validation
        activiteEventService e = new activiteEventService();
        e.ajouter(new activiteevent(horaire, nbParticipants, id1Value, id2Value));

        // Affichage d'un message de confirmation
        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Événement ajouté avec succès !");
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
    @FXML
    private void initialize() {
        activiteEventService service = new activiteEventService();

        // Charger les IDs de la table correspondante (ex: type d'activité)
        id1.getItems().addAll(service.getAvailableIds("event", "id"));
        id2.getItems().addAll(service.getAvailableIds("typeactivite", "id"));

        // Optionnel : Sélectionner la première valeur par défaut
        if (!id1.getItems().isEmpty()) id1.setValue(id1.getItems().get(0));
        if (!id2.getItems().isEmpty()) id2.setValue(id2.getItems().get(0));
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


