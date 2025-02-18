package com.esprit.controllers;

import com.esprit.models.Event;
import com.esprit.services.EventService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
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
    void Validate(ActionEvent event) throws IOException{
        EventService e = new EventService();
        e.ajouter(new Event(dt.getValue().toString(), Float.parseFloat(pr.getText()), li.getText(), ho.getText()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Event ajoutée");
        alert.show();



    }
}


