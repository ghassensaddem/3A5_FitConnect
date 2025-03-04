package com.esprit.controllers;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.qrcode.WriterException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import com.esprit.models.Event;
import com.esprit.services.EventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.esprit.models.PDF;
import java.io.*;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.*;
import com.esprit.controllers.api.SendSMS;
import org.apache.poi.ss.usermodel.Workbook;

public class AfficherEvents {

    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, Integer> colId;
    @FXML
    private TableColumn<Event, String> colDate;
    @FXML
    private TableColumn<Event, Double> colPrix;
    @FXML
    private TableColumn<Event, String> colLieu;
    @FXML
    private TableColumn<Event, String> colHoraire;
    @FXML
    private TableColumn<Event, String> colImage;
    @FXML
    private Button btnexcel;

    private ObservableList<Event> data = FXCollections.observableArrayList();
    private EventService eventService = new EventService();


    @FXML



    public void initialize() {
        loadData();
        setupContextMenu(); // ← Ajoutez bien cette ligne !

        eventTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                handleRowDoubleClick(event);
            }
        });
    }




    @FXML

    public void loadData() {
        data.clear(); // Nettoyer l'ObservableList avant de recharger les données

        // Charger les données depuis la base de données via EventService
        data.addAll(eventService.rechercher());

        // Associer les colonnes aux attributs de l'entité Event
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixdupass"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colHoraire.setCellValueFactory(new PropertyValueFactory<>("horaire"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image")); // Récupère le chemin de l'image

        // Modifier la cellule pour afficher l'image
        colImage.setCellFactory(param -> new TableCell<Event, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    Image image = new Image("file:" + imagePath); // Charger depuis le fichier local
                    imageView.setImage(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        eventTable.setItems(data); // Affichage des données dans la TableView
    }

    @FXML
    private void refreshTable() {
        loadData();
        showAlert("Mise à jour", "Les données ont été rafraîchies avec succès.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void openModifyEventWindow(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event2.fxml"));
            Parent root = loader.load();

            ModifierEvent controller = loader.getController();
            controller.initData(event);

            Stage stage = new Stage();
            stage.setTitle("Modifier Événement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize2() {
        // Charger les données dans la table
        loadData();

        // Ajouter le menu contextuel pour suppression avec un clic droit
        setupContextMenu();

        // Ajouter un écouteur pour la fonctionnalité de double-clic pour modifier
        eventTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifie si c'est un double-clic
                handleRowDoubleClick(event); // Appelle la méthode pour modifier l'événement
            }
        });
    }
    @FXML
    public void handleRowDoubleClick(MouseEvent event) {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/event2.fxml"));
                Parent root = loader.load();

                ModifierEvent modifierController = loader.getController();
                modifierController.initData(selectedEvent);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Événement");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun événement sélectionné.");
        }
    }

    private void setupContextMenu() {
        eventTable.setRowFactory(tv -> {
            TableRow<Event> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> {
                Event selectedEvent = row.getItem();
                if (selectedEvent != null) {
                    confirmAndDeleteEvent(selectedEvent);
                }
            });

            // Associer le menu contextuel seulement si la ligne n'est pas vide
            row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    row.setContextMenu(null);
                } else {
                    row.setContextMenu(contextMenu);
                }
            });

            contextMenu.getItems().add(deleteItem);
            return row;
        });
    }





    private void confirmAndDeleteEvent(Event event) {
        if (event == null) {
            showAlert("Erreur", "Aucun événement sélectionné.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer cet événement ?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) { // Vérifie si l'utilisateur a confirmé
                eventService.supprimer(event); // Supprime dans la base de données

                // Supprime l'événement de la liste et met à jour la TableView
                data.remove(event);
                eventTable.getItems().remove(event);
                eventTable.refresh();

                showAlert("Suppression réussie", "L'événement a été supprimé avec succès.");
            }
        });
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

    public void goToList(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event.fxml"));
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
    @FXML
    private void exportToPdf() {
        EventService eventService = new EventService();
        try {
            ArrayList<Event> events = eventService.getEventsAvecRevenu();
            // Récupérer les événements
            PDF pdfGenerator = new PDF();
            ArrayList<Event> eventsAvecRevenu = eventService.getEventsAvecRevenu();
            pdfGenerator.GeneratePdf("Liste_Events", eventsAvecRevenu, eventService);
            System.out.println("✅ PDF généré avec succès !");
        } catch (DocumentException | IOException e) {
            System.err.println("❌ Erreur lors de l'exportation du PDF: " + e.getMessage());
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }


    public void SMS(ActionEvent event) {
        String result = SendSMS.sendWhatsAppMessage("+21629075314", "Hello depuis Twilio WhatsApp !");
        System.out.println(result);

    }
    @FXML

    private void excelfile(ActionEvent event) {
        ArrayList<Event> data = new ArrayList<Event>(); // Utilisation de ArrayList<Event>

        try {
            // Chemin de destination du fichier Excel
            String filename = "C:\\Users\\ghass\\Documents\\java\\event\\src\\main\\java\\com\\esprit\\excel\\DonnéeÉvénements.XLS";

            // Crée une instance de HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();

            // Créer une feuille nommée "Event Details"
            HSSFSheet sheet = workbook.createSheet("Event Details");

            // Créer la première ligne (0) pour les en-têtes de colonnes
            HSSFRow rowhead = sheet.createRow(0);
            rowhead.createCell(0).setCellValue("Event ID");
            rowhead.createCell(1).setCellValue("Date");
            rowhead.createCell(2).setCellValue("Horaire");
            rowhead.createCell(3).setCellValue("Lieu");
            rowhead.createCell(4).setCellValue("Prix du pass");
            rowhead.createCell(5).setCellValue("Image");
            rowhead.createCell(6).setCellValue("Revenu Total (TND)"); // Nouvelle colonne pour le revenu

            // Instancier EventService et récupérer les événements avec revenu
            EventService eventService = new EventService();
            ArrayList<Event> eventList = eventService.getEventsAvecRevenu(); // Utilisez getEventsAvecRevenu()
            int rownum = 1;

            // Ajouter les événements et les revenus dans le fichier Excel
            for (Event eventItem : eventList) {
                HSSFRow row = sheet.createRow(rownum++);
                row.createCell(0).setCellValue(eventItem.getId());
                row.createCell(1).setCellValue(eventItem.getDate());
                row.createCell(2).setCellValue(eventItem.getHoraire());
                row.createCell(3).setCellValue(eventItem.getLieu());
                row.createCell(4).setCellValue(eventItem.getPrixdupass());

                // Ajouter l'image si elle existe
                String imagePath = eventItem.getImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    // Vérification du chemin de l'image
                    File imageFile = new File(imagePath);
                    if (!imageFile.exists()) {
                        System.out.println("Image not found at: " + imagePath);
                        continue;
                    }

                    // Lire le fichier image en bytes
                    FileInputStream imageStream = new FileInputStream(imageFile);
                    byte[] imageBytes = imageStream.readAllBytes();
                    imageStream.close();

                    // Ajouter l'image dans le fichier Excel
                    int pictureIndex = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG); // Assurez-vous d'utiliser le bon type d'image

                    // Créer un ancrage pour la position de l'image
                    HSSFPatriarch drawing = sheet.createDrawingPatriarch();
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 5, rownum - 1, (short) 6, rownum); // Position de l'image
                    drawing.createPicture(anchor, pictureIndex);
                }

                // Ajouter le revenu total à la nouvelle colonne
                row.createCell(6).setCellValue(eventItem.getRevenu()); // Revenu total pour l'événement
            }

            // Écrire le fichier Excel sur le disque
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);

            // Fermer le fichier
            fileOut.close();
            workbook.close();

            // Message de confirmation
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Excel File Has Been Generated Successfully", ButtonType.OK);
            a.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void goToStat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoyageStat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToemail_sender(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/email_sender.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


