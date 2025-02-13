package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class TemplateTest {

    @FXML
    private void handleHomeClick() {
        System.out.println("Home clicked!");
    }

    @FXML
    private void handleWhyUsClick() {
        System.out.println("Why Us clicked!");
    }

    @FXML
    private void handleTrainersClick() {
        System.out.println("Trainers clicked!");
    }

    @FXML
    private void handleContactClick() {
        System.out.println("Contact Us clicked!");
    }

    @FXML
    private void handleReadMoreClick() {
        System.out.println("Read More clicked!");
    }
}
