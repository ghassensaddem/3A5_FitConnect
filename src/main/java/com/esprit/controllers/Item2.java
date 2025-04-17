package com.esprit.controllers;

import com.esprit.models.CategorieEquipement;
import com.esprit.services.MyListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import jdk.jfr.Category;

public class Item2 {

    @FXML
    private Label nameLabel;
    private CategorieEquipement categorie;
    private MyListener listener;
    @FXML
    void click(MouseEvent mouseEvent) {
        listener.onClickListener(categorie);
    }
    public void setData(CategorieEquipement categorie, MyListener listener) {
        this.listener = listener;
        this.categorie = categorie;
        nameLabel.setText(categorie.getNom());
    }

}
