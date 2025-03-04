package com.esprit.controllers.api;

import com.esprit.models.Event;
import com.esprit.models.activiteevent;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EventQRCodeGenerator {

    public static void generateAndShowEventQRCode(activiteevent event) {
        try {
            // Construire le texte à encoder dans le QR code
            String eventDetails = "Activité: " + "Horaire: " + event.getHoraire() +
                    "\nParticipants: " + event.getNbrparticipant()  +
                    "\nDetails: ";

            // Générer le QR Code
            int width = 300, height = 300;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(eventDetails, BarcodeFormat.QR_CODE, width, height);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Convertir BufferedImage en Image JavaFX
            Image qrImage = SwingFXUtils.toFXImage(bufferedImage, null);

            // Afficher le QR Code dans une boîte de dialogue
            ImageView imageView = new ImageView(qrImage);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("QR Code - Événement");
            alert.setHeaderText("QR Code pour l'événement : " + event.getHoraire());
            alert.setGraphic(imageView);
            alert.showAndWait();

        } catch (WriterException e) {  // Supprimé IOException car elle n'est jamais levée
            e.printStackTrace();
        }
    }
    public static BufferedImage generateQRCodeImage(activiteevent event) throws WriterException {
        String eventDetails = "Activité: " + event.getHoraire() +
                "\nParticipants: " + event.getNbrparticipant() +
                "\nDetails: ";

        int width = 300, height = 300;
        BitMatrix bitMatrix = new MultiFormatWriter().encode(eventDetails, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

}
