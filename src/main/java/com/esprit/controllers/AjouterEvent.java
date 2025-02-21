package com.esprit.controllers;

import com.esprit.models.Event;
import com.esprit.services.EventService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.io.File;
import java.io.IOException;

public class AjouterEvent {
    @FXML
    private DatePicker dt;
    @FXML
    private TextField pr;
    @FXML
    private TextField li;
    @FXML
    private TextField ho;
    @FXML
    private ImageView pdpImageView;
    @FXML
    private TextField pdpPathField;
    @FXML
    private Label pdpIconLabel;

    public ImageView getPdpImageView() {
        return pdpImageView;
    }

    public void setPdpImageView(ImageView pdpImageView) {
        this.pdpImageView = pdpImageView;
    }

    public TextField getPdpPathField() {
        return pdpPathField;
    }

    public void setPdpPathField(TextField pdpPathField) {
        this.pdpPathField = pdpPathField;
    }

    public Label getPdpIconLabel() {
        return pdpIconLabel;
    }

    public void setPdpIconLabel(Label pdpIconLabel) {
        this.pdpIconLabel = pdpIconLabel;
    }

    @FXML
    void Validate(ActionEvent event) throws IOException {
        // Récupération des valeurs des champs
        String date = (dt.getValue() != null) ? dt.getValue().toString() : "";
        String prixText = pr.getText().trim();
        String lieu = li.getText().trim();
        String horaire = ho.getText().trim();
        String imagePath = pdpPathField.getText().trim();

        // Vérifications
        if (date.isEmpty() || prixText.isEmpty() || lieu.isEmpty() || horaire.isEmpty() || imagePath.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis !");
            return;
        }

        // Vérification du prix (doit être un nombre positif)
        float prix;
        try {
            prix = Float.parseFloat(prixText);
            if (prix <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le prix doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un prix valide !");
            return;
        }

        // Vérification que la date est bien sélectionnée et qu'elle est future
        LocalDate selectedDate = dt.getValue();
        if (selectedDate == null || selectedDate.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez choisir une date valide et future !");
            return;
        }

        // Ajout de l'événement après validation
        EventService e = new EventService();
        e.ajouter(new Event(date, prix, lieu, horaire, imagePath));

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
    public void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            pdpPathField.setText(selectedFile.getAbsolutePath());
            pdpImageView.setImage(new Image(pdpPathField.getText()));
            pdpIconLabel.setVisible(false);
        }
    }
}


