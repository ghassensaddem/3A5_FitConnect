/*package com.esprit.controllers;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeScannerController {

    @FXML
    private ImageView qrImageView; // Pour afficher l'image du QR Code scanné

    // Ouvrir une boîte de dialogue pour sélectionner une image contenant un QR Code
    @FXML
    private void scanQRCode() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image QR Code");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                qrImageView.setImage(fxImage);

                String result = decodeQRCode(bufferedImage);
                if (result != null) {
                    openSeanceFromQRCode(result); // Ouvrir la page des détails de la séance
                } else {
                    showErrorMessage("Erreur", "Impossible de lire le QR Code.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Décoder un QR Code depuis une image
    private String decodeQRCode(BufferedImage image) {
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(image)));
            Result result = new MultiFormatReader().decode(binaryBitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }

    // Ouvrir la page de détails de la séance avec l'ID extrait du QR Code
    private void openSeanceFromQRCode(String url) {
        try {
            // Vérifier que l'URL est valide et contient le paramètre "id"
            if (url == null || !url.contains("=")) {
                showErrorMessage("Erreur", "URL invalide.");
                return;
            }
            String[] parts = url.split("=");
            if (parts.length < 2) {
                showErrorMessage("Erreur", "URL invalide.");
                return;
            }
            String idParam = parts[1].trim();
            int seanceId = Integer.parseInt(idParam);

            // Chargement de l'interface des détails depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailSeance.fxml"));
            Parent root = loader.load();

            // Récupération du contrôleur et passage de l'ID
            DetailsSeance controller = loader.getController();
            controller.setSeanceId(seanceId);

            // Affichage dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NumberFormatException e) {
            showErrorMessage("Erreur", "Impossible d'ouvrir la séance: " + e.getMessage());
        }
    }
    // Afficher une alerte d'erreur
    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}*/

package com.esprit.controllers;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class QRCodeScannerController {

    @FXML
    private ImageView qrImageView; // Pour afficher l'image du QR Code scanné

    // Ouvrir une boîte de dialogue pour sélectionner une image contenant un QR Code
    @FXML
    private void scanQRCode() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image QR Code");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                qrImageView.setImage(fxImage);

                String result = decodeQRCode(bufferedImage);
                if (result != null) {
                    showQRCodeDialog(result); // Afficher le QR Code dans une boîte de dialogue avancée
                } else {
                    showErrorMessage("Erreur", "Impossible de lire le QR Code.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Décoder un QR Code depuis une image
    private String decodeQRCode(BufferedImage image) {
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(image)));
            Result result = new MultiFormatReader().decode(binaryBitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }

    // Afficher une boîte de dialogue avec le contenu du QR Code
    private void showQRCodeDialog(String qrContent) {
        // Vous pouvez personnaliser ici l'URL où vous souhaitez afficher le contenu du QR code.
        String customURL = "https://your-custom-url.com/?qrContent=" + qrContent;

        // Affichage de la boîte de dialogue avec l'URL personnalisée
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Détails Séance");
        alert.setHeaderText(null);

        // Définir le style
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white;");

        // Ajouter une icône
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/qr-icon.png")));
        icon.setFitHeight(50);
        icon.setFitWidth(50);
        alert.setGraphic(icon);

        // Afficher l'URL dans le texte de la boîte de dialogue
        alert.setContentText("📅 Détails du QR Code :\n\n" + qrContent + "\n\n" + "URL personnalisée: " + customURL);

        // Personnalisation des boutons
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.OK);

        // Affichage de l'alerte
        alert.showAndWait();

        // Ouvrir l'URL dans le navigateur si l'utilisateur clique sur OK
        if (alert.getResult() == ButtonType.OK) {
            try {
                // Ouvrir l'URL dans le navigateur par défaut
                Desktop.getDesktop().browse(new URI(customURL));
            } catch (URISyntaxException | java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Afficher une alerte d'erreur
    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
