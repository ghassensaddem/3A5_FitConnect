package com.esprit.controllers;

import com.esprit.models.Client;
import com.esprit.services.ClientService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class ModifierClient {
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
    private Spinner<Double> poids;
    @FXML
    private Spinner<Double> taille;
    @FXML
    public CheckBox checkHomme;
    @FXML
    public CheckBox checkFemme;
    public Label errorNom, errorPrenom, errorEmail, errorMdp, errorDateNaissance, errorPoids, errorTaille, errorSexe;



    public void initialize() {
        checkHomme.setOnAction(event -> {
            if (checkHomme.isSelected()) {
                checkFemme.setSelected(false);
            }
        });

        checkFemme.setOnAction(event -> {
            if (checkFemme.isSelected()) {
                checkHomme.setSelected(false);
            }
        });
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setNom(String nom) {
        this.nom.setText(nom);
    }

    public void setPrenom(String prenom) {
        this.prenom.setText(prenom);
    }

    public void setCheckHomme() {
        this.checkHomme.setSelected(true);
    }

    public void setCheckFemme() {
        this.checkFemme.setSelected(true);
    }

    public void setMdp(String mdp) {
        this.mdp.setText(mdp);
    }

    public void setDateNaissance(String dateNaissance) {
        if (dateNaissance != null && !dateNaissance.isEmpty()) {
            this.DateNaissance.setValue(LocalDate.parse(dateNaissance));
        }
    }

    public void setPoids(float poids) {
        if (this.poids.getValueFactory() != null) {
            this.poids.getValueFactory().setValue((double) poids);
        } else {
            this.poids.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(30, 200, poids, 1));
        }
    }

    public void setTaille(float taille) {
        if (this.taille.getValueFactory() != null) {
            this.taille.getValueFactory().setValue((double) taille);
        } else {
            this.taille.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(100.0, 250.0, taille, 1));
        }
    }

    void setPdpIconLabelVisible(boolean visible) {
        pdpIconLabel.setVisible(visible);
    }

    void setPdpImageView(String imagePath) {
        pdpImageView.setImage(new Image(imagePath));
    }

    void setPdpPathField(String imagePath) {
        pdpPathField.setText(imagePath);
    }

    public void setEmail(String email) {
        this.email.setText(email);
    }

    public void Update(ActionEvent actionEvent) throws IOException {
        if(validateFields()){
        ClientService cs=new ClientService() ;
        String sexe="homme";
        if(checkFemme.isSelected())
            sexe="femme";
        cs.update(new Client(id,nom.getText(),prenom.getText(), sexe,mdp.getText(),DateNaissance.getValue().toString(),email.getText(),pdpPathField.getText(),poids.getValue().floatValue(),taille.getValue().floatValue()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Le client a ete modifier");
        alert.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUsers.fxml"));
        Parent root = loader.load();
        nom.getScene().setRoot(root);

        ListUsers ls=loader.getController();
        ls.initialize();}

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
        if (poids.getValue() == null || poids.getValue() < 30 || poids.getValue() > 200) {
            errorPoids.setText("Valeur invalide (30-200 kg).");
            isValid = false;
        } else {
            errorPoids.setText("");
        }

        // Vérification de la taille
        if (taille.getValue() == null || taille.getValue() < 100 || taille.getValue() > 250) {
            errorTaille.setText("Valeur invalide (100-250 cm).");
            isValid = false;
        } else {
            errorTaille.setText("");
        }

        return isValid;
    }



}
