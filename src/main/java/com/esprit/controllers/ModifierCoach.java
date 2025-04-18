package com.esprit.controllers;

import com.esprit.models.Client;
import com.esprit.models.Coach;
import com.esprit.services.ClientService;
import com.esprit.services.CoachService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class ModifierCoach{
    public  int id;
    public ImageView pdpImageView;
    public TextField pdpPathField;
    public Label pdpIconLabel;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private PasswordField mdp;
    @FXML
    private DatePicker DateNaissance;
    @FXML
    private TextField email;
    @FXML
    public CheckBox checkHomme;
    @FXML
    public CheckBox checkFemme;
    public Label errorNom, errorPrenom, errorEmail, errorMdp, errorDateNaissance, errorPoids, errorTaille, errorSexe,errorLieuEngagement,errorSpecialite;

    @FXML
    private ComboBox<String> lieuEngagement;

    @FXML
    private ComboBox<String> specialite;

    void setId(int id) {
        this.id = id;
    }

    void setNom(String nom) {
        this.nom.setText(nom);
    }

    void setPrenom(String prenom) {
        this.prenom.setText(prenom);
    }

    void setMdp(String mdp) {
        this.mdp.setText(mdp);
    }

    void setEmail(String email) {
        this.email.setText(email);
    }

    void setLieuEngagement(String lieuEngagement) {
        this.lieuEngagement.setValue(lieuEngagement);
    }

    void setSpecialite(String specialite) {
        this.specialite.setValue(specialite);
    }

    void setDateNaissance(String dateNaissance) {
        if (dateNaissance != null && !dateNaissance.isEmpty()) {
            this.DateNaissance.setValue(LocalDate.parse(dateNaissance));
        }
    }

    void setPdpImageView(String imagePath) {
        pdpImageView.setImage(new Image(imagePath));
    }

    void setPdpPathField(String imagePath) {
        pdpPathField.setText(imagePath);
    }

    void setPdpIconLabelVisible(boolean visible) {
        pdpIconLabel.setVisible(visible);
    }

    void setSexe(String sexe) {
        if (sexe.equals("homme")) {
            checkHomme.setSelected(true);
        } else {
            checkFemme.setSelected(true);
        }
    }



    public void initialize() {
        CoachService coachService = new CoachService();
        checkHomme.setOnAction(event -> {
            checkFemme.setSelected(false);
        });
        checkFemme.setOnAction(event -> {
            checkHomme.setSelected(false);
        });
        lieuEngagement.getItems().addAll(coachService.getAdressesSalles());

        specialite.getItems().addAll(coachService.getSpecialites());
    }
    public void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            pdpPathField.setText(selectedFile.getAbsolutePath());
            pdpImageView.setImage(new Image(imagePath));
            pdpIconLabel.setVisible(false);
        }
    }

    public boolean validateFields() {
        boolean isValid = true;

        // Vérification du nom
        if (nom.getText().trim().isEmpty()) {
            errorNom.setText("Le nom est obligatoire.");
            isValid = false;
        } else {
            errorNom.setText("");
        }

        // Vérification du prénom
        if (prenom.getText().trim().isEmpty()) {
            errorPrenom.setText("Le prénom est obligatoire.");
            isValid = false;
        } else {
            errorPrenom.setText("");
        }

        // Vérification de l'email (format correct)
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,6}$", email.getText())) {
            errorEmail.setText("Email invalide.");
            isValid = false;
        } else {
            errorEmail.setText("");
        }


        // Vérification du mot de passe (longueur minimum)
        if (!Pattern.matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", mdp.getText())) {
            errorMdp.setText("Mot de passe doit contenir une maj,min,lettre");
            isValid = false;
        } else {
            errorMdp.setText("");
        }
        if(!checkHomme.isSelected() && !checkFemme.isSelected())
        {
            isValid=false;
            errorSexe.setText("Veuillez choisir un sexe.");
        }

        else
            errorSexe.setText("");

        // Vérification de la date de naissance
        if (DateNaissance.getValue() == null) {
            errorDateNaissance.setText("Veuillez sélectionner une date.");
            isValid = false;
        } else {
            errorDateNaissance.setText("");
        }

        // Vérification du poids
        if (lieuEngagement.getValue() == null) {
            errorLieuEngagement.setText("Aucun lieu d'engagement  ");
            isValid = false;
        } else {
            errorLieuEngagement.setText("");
        }

        // Vérification de la taille
        if (specialite.getValue() == null) {
            errorSpecialite.setText("Aucune spécialité sélectionnée ");
            isValid = false;
        } else {
            errorSpecialite.setText("");
        }

        return isValid;
    }

    @FXML
    void Update(ActionEvent event) throws IOException {
        if(validateFields()){
            CoachService cs=new CoachService() ;
            String sexe="homme";
            if(checkFemme.isSelected())
                sexe="femme";
            if(pdpPathField.getText().equals(""))
                pdpPathField.setText("/Styles/logo.png");
            cs.modifier(new Coach(id,nom.getText(),prenom.getText(), sexe,mdp.getText(),DateNaissance.getValue().toString(),email.getText(),pdpPathField.getText(),lieuEngagement.getValue(),specialite.getValue()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("le Coach a ete modifier");
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUsers.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.getScene().setRoot(root);
            ListUsers ls=loader.getController();
            ls.initialize();
            ls.setBtnCoachs();


        }



    }

}
