package com.esprit.controllers;

import com.esprit.models.Equipement;
import com.esprit.services.MyListener;
import com.esprit.tests.MainProgGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ItemController {

    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label price;
    private MyListener myListener;

    private Equipement equipement;

    @FXML
    void click(MouseEvent mouseEvent) {
        myListener.onClickListener(equipement);
    }

    public void setData(Equipement equipement,MyListener myListener) {
        String imagePath;
        this.equipement = equipement;
        this.myListener = myListener;
        nameLabel.setText(equipement.getNom());
        price.setText(MainProgGUI.CURRENCY + equipement.getPrix());

        // Afficher l'image actuelle de l'équipement dans l'ImageView
        if (equipement.getImage() != null && !equipement.getImage().isEmpty()) {
            imagePath = equipement.getImage(); // Stocker le chemin de l'image

            // Charger l'image dans l'ImageView
            try {
                // Convertir le chemin relatif en chemin absolu
                String imageAbsolutePath = "src/main/resources/" + imagePath;
                File imageFile = new File(imageAbsolutePath);

                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    img.setImage(image); // Afficher l'image dans l'ImageView
                } else {
                    System.err.println("Le fichier image n'existe pas : " + imageAbsolutePath);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }
    }




}

