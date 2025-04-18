package com.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.esprit.services.EventService;
import javafx.stage.Stage;

import java.io.IOException;

public class EmailSenderController {

    @FXML
    private TextField toField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea bodyField;

    @FXML
    private Label messageLabel;

    public void handleSendEmail() {
        try {
            // Récupération des valeurs des champs
            String to = toField.getText();
            String subject = subjectField.getText();
            String body = bodyField.getText();

            // Vérification des champs vides
            if (to.isEmpty() || subject.isEmpty() || body.isEmpty()) {
                messageLabel.setText("Erreur : Tous les champs sont obligatoires !");
                return;
            }

            // Envoi de l'email
            EventService eventService = new EventService();
            eventService.envoyerEmail(to, subject, body);

            // Message de confirmation
            messageLabel.setText("Email envoyé avec succès !");
        } catch (Exception e) {
            messageLabel.setText("Erreur lors de l'envoi de l'email !");
        }
    }

    public void gotolist3(ActionEvent event) {
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
}
