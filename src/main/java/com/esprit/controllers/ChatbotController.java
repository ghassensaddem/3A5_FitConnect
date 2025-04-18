package com.esprit.controllers;

import com.esprit.ChatbotAPI;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatbotController {

    @FXML
    private TextArea chatArea; // Zone de texte pour afficher la conversation

    @FXML
    private TextField inputField; // Champ de texte pour saisir le message

    // Méthode appelée lorsque l'utilisateur clique sur le bouton "Envoyer"
    @FXML
    private void handleSendButton() {
        String userMessage = inputField.getText(); // Récupérer le message de l'utilisateur
        if (!userMessage.isEmpty()) {
            chatArea.appendText("Vous: " + userMessage + "\n"); // Afficher le message de l'utilisateur
            inputField.clear(); // Effacer le champ de texte

            // Obtenir la réponse du chatbot
            String botResponse = ChatbotAPI.getResponse(userMessage);
            chatArea.appendText("Bot: " + botResponse + "\n"); // Afficher la réponse du chatbot
        }
    }
}