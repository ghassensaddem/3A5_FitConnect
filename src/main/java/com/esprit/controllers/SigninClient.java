package com.esprit.controllers;

import com.esprit.models.Client;
import com.esprit.services.ClientService;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;


public class SigninClient {
    @FXML
    public VBox VboxLog1;
    @FXML
    public BorderPane borderPaneSign;
    @FXML

    public BorderPane borderPaneLog;

    @FXML
    public VBox VboxLog2;
    @FXML
    public VBox VboxSign1;
    @FXML
    public VBox VboxSign2;
    public Button SigninSlide;
    public Button Login;
    public Button loginslide;
    public Button Signin;
    public CheckBox checkHomme;
    public CheckBox checkFemme;
    public TextField pdpPathField;
    public ImageView pdpImageView;
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
    private TextField email2;
    @FXML
    private TextField mdp2;
    public Label errorNom, errorPrenom, errorEmail, errorMdp, errorDateNaissance, errorPoids, errorTaille, errorSexe,errorAUTH;



    @FXML
    public void initialize() {
        poids.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(30, 200, 0, 1));
        taille.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(30, 200, 0, 1));

        borderPaneLog.setVisible(false);
        borderPaneSign.setVisible(true);
        borderPaneSign.setLeft(null);
        borderPaneSign.setRight(VboxSign1);

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


    @FXML
    void Add(ActionEvent event) throws IOException {
        if(validateFields()){
        ClientService cs=new ClientService() ;
        String sexe="homme";
        if(checkFemme.isSelected())
            sexe="femme";
        if(pdpPathField.getText().equals(""))
            pdpPathField.setText("/Styles/logo.png");
        cs.ADD(new Client(0,nom.getText(),prenom.getText(), sexe,mdp.getText(),DateNaissance.getValue().toString(),email.getText(),pdpPathField.getText(),poids.getValue().floatValue(),taille.getValue().floatValue()));

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

        Slider(event);}



    }
    @FXML
    void AUTH(ActionEvent event) throws IOException {
        ClientService cs=new ClientService() ;
        if(validateAUTH())
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUsers.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setMaximized(true);
            ListUsers ls=loader.getController();
            ls.initialize();

        }




    }
    @FXML
    void Slider(ActionEvent event) {

        boolean isLogin = borderPaneSign.isVisible();


        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(0.5), VboxSign1);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), VboxSign2);
        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(0.5), VboxLog1);
        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(0.5), VboxLog2);

        if (isLogin) {

            transition1.setToX(-400);
            transition2.setToX(-400);
            transition3.setToX(0);
            transition4.setToX(0);
            borderPaneLog.setRight(null);
            borderPaneLog.setLeft(VboxLog1);
            borderPaneLog.setVisible(true);

            transition1.setOnFinished(e -> {

                borderPaneSign.setVisible(false);







            });



        } else if (borderPaneLog.isVisible()){

            transition3.setToX(350);
            transition4.setToX(350);
            transition1.setToX(0);
            transition2.setToX(0);




            borderPaneSign.setLeft(null);
            borderPaneSign.setRight(VboxSign1);
            borderPaneSign.setVisible(true);

            transition3.setOnFinished(e -> {
                borderPaneLog.setVisible(false);

            });

        }


        transition1.play();
        transition2.play();
        transition3.play();
        transition4.play();
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
        ClientService cs=new ClientService() ;
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
        }
        else if(cs.existe(email.getText()))
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
            errorSexe.setText("Veuillez choisir un sexe.");
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
    public boolean validateAUTH() {
        ClientService cs=new ClientService() ;
        boolean isValid = true;
        if(!cs.AUTH(email2.getText(),mdp2.getText()))
        {
            errorAUTH.setText("Email ou mot de passe invalide.");
            isValid = false;
        }
        else {
            errorAUTH.setText("");
        }
        return isValid;
    }
}

