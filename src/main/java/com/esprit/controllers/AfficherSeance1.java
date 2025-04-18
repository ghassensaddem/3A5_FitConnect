
package com.esprit.controllers;

import com.esprit.models.seance;
import com.esprit.services.SeanceService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.nio.file.Files;
import java.nio.file.Paths;


public class AfficherSeance1 {

    @FXML
    private GridPane usersGrid;

    private final SeanceService seanceService = new SeanceService();
    private int programmeId;



    public void setProgrammeId(int programmeId) {
        this.programmeId = programmeId;
        loadSeances();
    }

    @FXML
    public void initialize() {
        loadSeances();
        setupDragAndDrop(usersGrid);

    }

    private void loadSeances() {
        usersGrid.getChildren().clear();
        List<seance> seances = seanceService.rechercherParProgramme(this.programmeId);

        // Define number of columns
        int columns = 3;

        // Calculate needed rows
        int rows = (int) Math.ceil(seances.size() / (double) columns);

        // Reset grid if needed
        while (usersGrid.getRowConstraints().size() < rows) {
            usersGrid.getRowConstraints().add(new RowConstraints(300)); // Adjust height as needed
        }

        int col = 0;
        int row = 0;

        for (seance seance : seances) {
            VBox seanceBox = createSeanceBox(seance);

            // Add minimum height/width constraints to the VBox
            seanceBox.setMinHeight(250);
            seanceBox.setMinWidth(200);
            seanceBox.setPrefHeight(300);
            seanceBox.setPrefWidth(250);

            // Add proper CSS to ensure boxes don't overflow
            seanceBox.setStyle("-fx-padding: 20; -fx-border-radius: 15; -fx-border-color: #ddd; " +
                    "-fx-background-color: #8FBF87; -fx-border-width: 2; " +
                    "-fx-alignment: center; -fx-spacing: 10;");

            // Add to grid and handle column/row counting correctly
            usersGrid.add(seanceBox, col, row);

            // Move to next column or row
            col++;
            if (col >= columns) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void scanQRCode() {
        // Create file chooser for QR code image selection
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Sélectionner un QR Code");
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        // Show file chooser dialog
        Stage stage = (Stage) usersGrid.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Read the selected image file
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                if (bufferedImage == null) {
                    showErrorMessage("Erreur de lecture", "Impossible de lire l'image sélectionnée.");
                    return;
                }

                // Show processing dialog
                Stage processingStage = new Stage();
                processingStage.setTitle("Traitement du QR Code");
                VBox processingLayout = new VBox(10);
                processingLayout.setAlignment(javafx.geometry.Pos.CENTER);
                processingLayout.setPadding(new javafx.geometry.Insets(20));

                Label processingLabel = new Label("Analyse du QR Code en cours...");
                ProgressIndicator progress = new ProgressIndicator();

                // Add image preview
                ImageView imagePreview = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
                imagePreview.setFitWidth(250);
                imagePreview.setFitHeight(250);
                imagePreview.setPreserveRatio(true);

                processingLayout.getChildren().addAll(imagePreview, processingLabel, progress);
                processingStage.setScene(new Scene(processingLayout, 300, 350));
                processingStage.show();

                // Process QR code in a background thread
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> {
                    try {
                        // Decode QR code from image
                        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                        Result result = new MultiFormatReader().decode(bitmap);
                        if (result != null) {
                            String qrContent = result.getText();
                            System.out.println("QR Code détecté: " + qrContent);

                            // Close processing dialog and open seance details
                            Platform.runLater(() -> {
                                processingStage.close();
                                openSeanceFromQRCode(qrContent);
                            });
                        } else {
                            Platform.runLater(() -> {
                                processingStage.close();
                                showErrorMessage("QR Code non détecté",
                                        "Aucun QR Code n'a été trouvé dans l'image sélectionnée.");
                            });
                        }
                    } catch (NotFoundException e) {
                        Platform.runLater(() -> {
                            processingStage.close();
                            showErrorMessage("QR Code non détecté",
                                    "Aucun QR Code n'a été trouvé dans l'image sélectionnée.");
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> {
                            processingStage.close();
                            showErrorMessage("Erreur de traitement",
                                    "Erreur lors de l'analyse du QR Code: " + e.getMessage());
                        });
                    } finally {
                        executor.shutdown();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                showErrorMessage("Erreur de lecture",
                        "Impossible de lire l'image sélectionnée: " + e.getMessage());
            }
        }
    }

    // Helper method to handle drag and drop of QR code images
    private void setupDragAndDrop(Pane targetPane) {
        targetPane.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        targetPane.setOnDragDropped(event -> {
            boolean success = false;
            if (event.getDragboard().hasFiles()) {
                List<File> files = event.getDragboard().getFiles();
                if (!files.isEmpty()) {
                    File file = files.get(0);
                    if (file.getName().toLowerCase().endsWith(".png") ||
                            file.getName().toLowerCase().endsWith(".jpg") ||
                            file.getName().toLowerCase().endsWith(".jpeg") ||
                            file.getName().toLowerCase().endsWith(".gif") ||
                            file.getName().toLowerCase().endsWith(".bmp")) {

                        processQRCodeFile(file);
                        success = true;
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    // Helper method to process the QR code file
    private void processQRCodeFile(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            if (bufferedImage == null) {
                showErrorMessage("Erreur de lecture", "Impossible de lire l'image sélectionnée.");
                return;
            }

            // Decode QR code from image
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                Result result = new MultiFormatReader().decode(bitmap);
                if (result != null) {
                    String qrContent = result.getText();
                    System.out.println("QR Code détecté: " + qrContent);
                    openSeanceFromQRCode(qrContent);
                } else {
                    showErrorMessage("QR Code non détecté",
                            "Aucun QR Code n'a été trouvé dans l'image.");
                }
            } catch (NotFoundException e) {
                showErrorMessage("QR Code non détecté",
                        "Aucun QR Code n'a été trouvé dans l'image.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Erreur de lecture",
                    "Impossible de lire l'image: " + e.getMessage());
        }
    }
    private VBox createSeanceBox(seance seance) {
        VBox box = new VBox(15);
        box.setAlignment(javafx.geometry.Pos.CENTER);
        box.setStyle("-fx-padding: 20; -fx-border-radius: 15; -fx-border-color: #ddd; -fx-background-color: #8FBF87; -fx-border-width: 2;");
        box.setMinHeight(250);
        box.setPrefHeight(300);

        Label labelDate = new Label("Date: " + seance.getDate());
        labelDate.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label labelHoraire = new Label("Horaire: " + seance.getHoraire());
        labelHoraire.setStyle("-fx-font-size: 14px;");

        Button btnAjouter = new Button("Ajouter un avis");
        Button btnVoir = new Button("Voir les avis");

        // Ajouter les gestionnaires d'événements qui étaient dans votre code original
        btnAjouter.setOnAction(event -> openAjouterAvisWindow(seance));
        btnVoir.setOnAction(event -> openListeAvisWindow(seance));

        ImageView qrCodeView = generateQRCode(seance);
        qrCodeView.setFitWidth(120);
        qrCodeView.setFitHeight(120);
        qrCodeView.setPreserveRatio(true);

        // Ajouter également le gestionnaire d'événements pour le QR code
        Tooltip tooltip = new Tooltip("Cliquer pour voir les détails de la séance");
        Tooltip.install(qrCodeView, tooltip);

        qrCodeView.setOnMouseClicked(event -> {
            String qrContent = "seance:" + seance.getId() + "\n" +
                    "Date:" + seance.getDate() + "\n" +
                    "Horaire:" + seance.getHoraire() + "\n" +
                    "Programme:" + seance.getProgramme_id() + "\n" +
                    "Activité:" + seance.getActivite_id();
            openSeanceFromQRCode(qrContent);
        });

        HBox buttonBox = new HBox(10, btnAjouter, btnVoir);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        box.getChildren().addAll(labelDate, labelHoraire, qrCodeView, buttonBox);

        return box;
    }
    private ImageView generateQRCode(seance s) {
        int width = 100;
        int height = 100;
        // Contenu enrichi du QR code avec plusieurs données de la séance
        String data = "seance:" + s.getId() + "\n" +
                "Date:" + s.getDate() + "\n" +
                "Horaire:" + s.getHoraire() + "\n" +
                "Programme:" + s.getProgramme_id() + "\n" +
                "Activité:" + s.getActivite_id();

        String filePath = "qrcodes/seance_" + s.getId() + ".png";
        File qrFile = new File(filePath);

        try {
            // Si le répertoire n'existe pas, le créer
            File qrDir = new File("qrcodes");
            if (!qrDir.exists()) {
                qrDir.mkdirs();
            }

            // Générer le QR code seulement s'il n'existe pas
            if (!qrFile.exists() || qrFile.length() == 0) {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

                Path path = FileSystems.getDefault().getPath(filePath);
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

                if (!qrFile.exists() || qrFile.length() == 0) {
                    throw new IOException("Le fichier du QR code n'a pas été généré correctement.");
                }
            }

            BufferedImage bufferedImage = ImageIO.read(qrFile);
            if (bufferedImage == null) {
                throw new IOException("Impossible de lire l'image du QR code.");
            }

            ImageView qrImageView = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
            qrImageView.setFitWidth(100);
            qrImageView.setFitHeight(100);
            // Stocker le contenu du QR code dans le userData pour pouvoir le récupérer lors du clic
            qrImageView.setUserData(data);
            return qrImageView;

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            showErrorMessage("Erreur de génération", "Impossible de générer le QR Code : " + e.getMessage());
            return new ImageView();
        }
    }
    private void openSeanceFromQRCode(String qrContent) {
        try {
            // Parse QR content
            String[] lines = qrContent.split("\n");
            if (lines.length == 0) {
                throw new IllegalArgumentException("Contenu du QR code vide.");
            }

            // Extraction des informations du QR Code
            int seanceId = -1;
            String date = "", horaire = "", programme = "", activite = "";

            for (String line : lines) {
                if (line.startsWith("seance:")) {
                    seanceId = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Date:")) {
                    date = line.split(":")[1].trim();
                } else if (line.startsWith("Horaire:")) {
                    horaire = line.split(":")[1].trim();
                } else if (line.startsWith("Programme:")) {
                    programme = line.split(":")[1].trim();
                } else if (line.startsWith("Activité:")) {
                    activite = line.split(":")[1].trim();
                }
            }

            // Vérifier si la séance existe
            seance selectedSeance = seanceService.getSeanceById(seanceId);
            if (selectedSeance == null) {
                showErrorMessage("Séance introuvable", "Aucune séance trouvée avec l'ID: " + seanceId);
                return;
            }

            // Charger le contenu HTML du template
            String htmlContent;
            try {
                // Essayer d'abord avec getResourceAsStream qui est plus fiable
                var inputStream = getClass().getResourceAsStream("/templateSeance.html");
                if (inputStream != null) {
                    htmlContent = new String(inputStream.readAllBytes(), "UTF-8");
                } else {
                    // Fallback à l'ancienne méthode
                    htmlContent = new String(Files.readAllBytes(Paths.get(getClass().getResource("/templateSeance.html").toURI())), "UTF-8");
                }
            } catch (Exception e) {
                // Si le fichier n'est pas trouvé dans les ressources, essayez un chemin relatif
                htmlContent = new String(Files.readAllBytes(Paths.get("src/main/resources/templateSeance.html")), "UTF-8");
            }

            // Remplacer les placeholders par les informations extraites du QR Code
            htmlContent = htmlContent.replace("{{seanceId}}", String.valueOf(seanceId))
                    .replace("{{date}}", date)
                    .replace("{{horaire}}", horaire)
                    .replace("{{programme}}", programme)
                    .replace("{{activite}}", activite);

            // Créer un WebView pour afficher le contenu HTML
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent(htmlContent);

            // Afficher le WebView dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Détails de la Séance");
            stage.setScene(new Scene(webView, 600, 400));
            stage.show();

        } catch (Exception e) {
            showErrorMessage("Erreur", "Erreur lors de l'ouverture des détails: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void openAjouterAvisWindow(seance selectedSeance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutAvis.fxml"));
            Parent root = loader.load();
            AjouterAvis1 controller = loader.getController();
            controller.setSeanceId(selectedSeance.getId());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorMessage("Erreur de chargement", "Impossible de charger l'ajout d'avis.");
        }
    }

    private void openListeAvisWindow(seance selectedSeance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listeAvis.fxml"));
            Parent root = loader.load();
            AfficherAvis1 controller = loader.getController();
            controller.loadAvisForSeance(selectedSeance.getId());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorMessage("Erreur de chargement", "Impossible de charger la liste des avis.");
        }
    }

    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
