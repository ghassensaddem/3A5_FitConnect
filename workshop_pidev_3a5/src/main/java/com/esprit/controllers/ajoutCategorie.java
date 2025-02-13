package com.esprit.controllers;
import com.esprit.models.CategorieEquipement;

import com.esprit.services.CategorieService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.awt.*;

public class ajoutCategorie {

    @FXML
    private TextField nom;

    @FXML
    private TextField description;

    @FXML
    private void Validate() {
        CategorieService e=new CategorieService();
        String nomCategorie = nom.getText();
        String descCategorie = description.getText();

        // Créer une nouvelle catégorie d'équipement
        CategorieEquipement categorie = new CategorieEquipement(nomCategorie, descCategorie);
        e.ajouter(categorie);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("categorie ajoutée");
        alert.show();

    }
}