package com.esprit.controllers;

import com.esprit.models.Event;
import com.esprit.models.activiteevent;
import com.esprit.services.EventService;
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

import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModifierEvent {

    @FXML
    private TextField idField;
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

    public void setDt(DatePicker dt) {
        this.dt = dt;
    }

    public void setPr(TextField pr) {
        this.pr = pr;
    }

    public void setLi(TextField li) {
        this.li = li;
    }

    public void setHo(TextField ho) {
        this.ho = ho;
    }

    public void setIdField(TextField idField) {
        this.idField = idField;
    }



    private Event selectedEvent;
    private EventService eventService = new EventService();

    // Méthode pour recevoir l'événement et pré-remplir les champs
    public void initData(Event event) {
        this.selectedEvent = event;

        idField.setText(String.valueOf(event.getId()));

        try {
            DateTimeFormatter formatter;

            // Vérifier si la date est déjà au format ISO (yyyy-MM-dd) ou au format d/M/yyyy
            if (event.getDate().contains("-")) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Format ISO standard
            } else {
                formatter = DateTimeFormatter.ofPattern("d/M/yyyy"); // Format avec slash
            }

            LocalDate date = LocalDate.parse(event.getDate(), formatter);
            dt.setValue(date); // Mettre à jour le DatePicker
        } catch (DateTimeParseException e) {
            System.out.println("Erreur de parsing de la date : " + e.getMessage());
            e.printStackTrace();
        }

        pr.setText(String.valueOf(event.getPrixdupass()));
        li.setText(event.getLieu());
        ho.setText(event.getHoraire());

        idField.setDisable(true); // Empêcher la modification de l'ID
    }



    @FXML
    void Validate(ActionEvent event) throws IOException {
        // Récupération des valeurs des champs
        String idText = idField.getText().trim();
        String date = (dt.getValue() != null) ? dt.getValue().toString() : "";
        String prixText = pr.getText().trim();
        String lieu = li.getText().trim();
        String horaire = ho.getText().trim();
        String imagePath = pdpPathField.getText().trim();

        // Vérifications : tous les champs doivent être remplis
        if (idText.isEmpty() || date.isEmpty() || prixText.isEmpty() || lieu.isEmpty() || horaire.isEmpty() || imagePath.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis !");
            return;
        }

        // Vérification que l'ID est un entier valide
        int id;
        try {
            id = Integer.parseInt(idText);
            if (id <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "L'ID doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un ID valide !");
            return;
        }

        // Vérification que le prix est un nombre positif
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

        // Modification de l'événement après validation
        EventService e = new EventService();
        e.modifier(new Event(id, date, prix, lieu, horaire, imagePath));

        // Affichage d'un message de confirmation
        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Événement modifié avec succès !");
    }

    // Méthode générique pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


    // Méthode pour revenir à l'affichage des événements
    private void retournerVersAfficherEvents() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherEvents.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Liste des événements");
        stage.show();
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
    @FXML
    private void goToList(ActionEvent event) {
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

    }

    public void goToList3(ActionEvent event) {

    }
}
