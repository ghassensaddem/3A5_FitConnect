package com.esprit.controllers;

import com.esprit.models.activiteevent;
import com.esprit.services.activiteEventService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModifierActiviteevent {

    @FXML
    private TextField id;
        @FXML
        private TextField hr;
        @FXML
        private TextField np;
        @FXML
        private ChoiceBox<Integer> id1;
        @FXML
        private ChoiceBox<Integer> id2;

        @FXML
        void Validate2(ActionEvent event) throws IOException{
            activiteEventService e = new activiteEventService();
            e.modifier(new activiteevent(Integer.parseInt(id.getText()),hr.getText(), Integer.parseInt(np.getText()), id1.getValue(), id2.getValue()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Event ajoutée");
            alert.show();



        }

    private activiteevent selectedEvent;
    private     activiteEventService eventService = new activiteEventService();

    // Méthode pour recevoir l'événement et pré-remplir les champs
    public void initData2(activiteevent event) {
        this.selectedEvent = event;

        id.setText(String.valueOf(event.getId()));
        hr.setText(event.getHoraire());
        np.setText(String.valueOf(event.getNbrparticipant()));
        id1.getValue();
       id2.getValue();
        id.setDisable(true); // Empêcher la modification de l'ID
    }



    @FXML
    void Validate(ActionEvent event) throws IOException{
        activiteEventService e = new activiteEventService();
        e.modifier(new activiteevent(Integer.parseInt(id.getText()),hr.getText(), Integer.parseInt(np.getText()), id1.getValue(), id2.getValue()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Event Modifier");
        alert.show();
    }

    // Méthode pour revenir à l'affichage des événements
    private void retournerVersAfficherEvents() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/event.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Liste des événements");
        stage.show();
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

    public void goToList3(ActionEvent event) {
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
}
