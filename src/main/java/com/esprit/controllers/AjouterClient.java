package com.esprit.controllers;

import com.esprit.models.Client;
import com.esprit.services.AdminService;
import com.esprit.services.ClientService;
import com.esprit.services.CoachService;
import com.esprit.utils.CryptoUtils;
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

public class AjouterClient {
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
        poids.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(30, 200, 0, 1));
        taille.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(30, 200, 0, 1));
    }


    @FXML
    void Add(ActionEvent event) throws IOException {
        if(validateFields()){
            ClientService cs=new ClientService() ;
            CryptoUtils cryptoUtils = new CryptoUtils();
            String sexe="homme";
            if(checkFemme.isSelected())
                sexe="femme";
            if(pdpPathField.getText().equals(""))
                pdpPathField.setText("/Styles/logo.png");
            cs.ADD(new Client(0,nom.getText(),prenom.getText(), sexe,cryptoUtils.encrypt(mdp.getText()),DateNaissance.getValue().toString(),email.getText(),pdpPathField.getText(),poids.getValue().floatValue(),taille.getValue().floatValue()));

            nom.setText("");
            prenom.setText("");
            mdp.setText("");
            email.setText("");
            poids.getValueFactory().setValue(0.0);
            taille.getValueFactory().setValue(0.0);
            DateNaissance.setValue(null);
            checkFemme.setSelected(false);
            checkHomme.setSelected(false);
            pdpPathField.setText("");
            pdpImageView.setImage(new Image("/Styles/Calque1.png"));
            pdpIconLabel.setVisible(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Votre compte a ete ajoutée");
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUsers.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.getScene().setRoot(root);
            ListUsers ls=loader.getController();
            ls.initialize();

            }



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
        ClientService cs=new ClientService() ;
        AdminService as=new AdminService();
        CoachService cs2=new CoachService();

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
        }
        else if(cs.existe(email.getText()) || as.existe(email.getText()) || cs2.existe(email.getText()))
        {
            errorEmail.setText("Email deja existant.");
            isValid = false;
        }
        else {
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
