package com.esprit.controllers;

import com.esprit.models.Event;
import com.esprit.services.EventService;
import com.sothawo.mapjfx.Configuration;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapType;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    private Stage mapStage;
    public ImageView getPdpImageView() {
        return pdpImageView;
    }
    /*@FXML
    private void initialize() {
        li.setOnMouseClicked(event -> showMap());
    }
*/
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
                new FileChooser.ExtensionFilter("Images", ".png", ".jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                // Définir le dossier de destination
                String outputPath = "C:/xampp/htdocs/images/";

                // Générer un nom de fichier unique avec l'extension d'origine
                String fileExtension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String fileName = "client_" + System.currentTimeMillis() + fileExtension;
                File destinationFile = new File(outputPath + fileName);

                // Copier l'image sélectionnée dans le dossier cible
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour le champ pdpPathField avec le nouveau chemin
                pdpPathField.setText("C:/xampp/htdocs/images/" + fileName);

                // Afficher l'image copiée dans ImageView
                pdpImageView.setImage(new Image(destinationFile.toURI().toString()));
                pdpIconLabel.setVisible(false);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("❌ Erreur lors de la copie de l'image !");
            }
        }
    }
    @FXML
    private void initialize() {
        li.setOnMouseClicked(event -> openMap());
    }

    private void openMap() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/map.fxml"));
            Parent root = loader.load();
            Map mapController = loader.getController(); // Récupérer le contrôleur Map

            // Créer une nouvelle fenêtre pour la carte
            Stage mapStage = new Stage();
            mapStage.setScene(new Scene(root));
            mapStage.setTitle("Sélectionnez un emplacement");
            mapStage.show();

            // Ajouter un listener pour récupérer les coordonnées de la carte
            mapController.getMapView().addEventHandler(MapViewEvent.MAP_CLICKED, e -> {
                Coordinate coord = e.getCoordinate();
                String locationName = mapController.getLocationName(coord.getLatitude(), coord.getLongitude());

                // Retirer les virgules du nom du lieu
                String cleanLocationName = locationName.replace(",", " ");

                li.setText(cleanLocationName); // Afficher le nom du lieu sans virgules
                mapStage.close(); // Fermer la carte après sélection
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


