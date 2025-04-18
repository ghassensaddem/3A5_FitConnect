package com.esprit.controllers;

import com.esprit.models.Client;
import com.esprit.services.ClientService;
import com.esprit.tests.MainProg;
import com.esprit.utils.CryptoUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class Profile {
    public DatePicker DateNaissance;
    public TextField mdp;
    public TextField email;
    public TextField prenom;
    public TextField nom;
    public ImageView image;
    public Label nomPrenom;
    public String pdpPathField,motDePasse;
    public Button toggleMdp;
    public ImageView toggleIcon;
    public LineChart poidsChart;

    private boolean isPasswordVisible = false;


    public void setNom(String nom) {
        this.nom.setText(nom);
    }

    public void setPrenom(String prenom) {
        this.prenom.setText(prenom);
    }

    public void setNomPrenom(String nom, String prenom) {
        this.nomPrenom.setText(nom + " " + prenom);
    }

    public void setImage(String image) {
        this.image.setImage(new Image(image));
    }

    public void setMdp(String mdp) {
        CryptoUtils cryptoUtils = new CryptoUtils();
        mdp=cryptoUtils.decrypt(mdp);
        this.mdp.setText("********");
        this.mdp.setDisable(true);
        motDePasse=mdp;
        isPasswordVisible = false;

    }

    public void setDateNaissance(String dateNaissance) {
        if (dateNaissance != null && !dateNaissance.isEmpty()) {
            this.DateNaissance.setValue(LocalDate.parse(dateNaissance));
        }
    }
    public void setEmail(String email) {
        this.email.setText(email);
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
            pdpPathField=selectedFile.getAbsolutePath();
            image.setImage(new Image(imagePath));
        }
    }

    @FXML
    public void initialize()
    {
        ClientService cs=new ClientService() ;
        Client client=cs.rechercherParId(MainProg.idClient);
        setNom(client.getNom());
        setPrenom(client.getPrenom());
        setEmail(client.getEmail());
        setMdp(client.getMdp());
        setDateNaissance(client.getDateNaissance().toString());
        setNomPrenom(client.getNom(),client.getPrenom());
        setImage(client.getImage());
        pdpPathField=client.getImage();
        toggleMdp.setOnAction(event -> MdpVisible());
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        poidsChart.getData().add(series);
        Platform.runLater(() -> {
            if (series.getNode() != null) {
                series.getNode().getStyleClass().add("poids-series-0");
            }
        });
        poidsChart.getStyleClass().add("poids-chart");
        poidsChart.lookup(".chart-title").getStyleClass().add("poids-chart-title");
        poidsChart.lookup(".chart-legend").getStyleClass().add("poids-chart-legend");
        poidsChart.lookup(".axis").getStyleClass().add("poids-chart-axis");
        if(!Objects.equals(email.getText(), "rayanferjani55@gmail.com"))
            afficherStatistiquePoids();










    }


    public void MettreAjourProfle(ActionEvent event) {
        ClientService cs=new ClientService() ;
        CryptoUtils cryptoUtils = new CryptoUtils();
        if (isPasswordVisible)
            motDePasse=mdp.getText();
        if (validateFields()) {
            cs.mettreAJourProfil(MainProg.idClient,nom.getText(),prenom.getText(),email.getText(),cryptoUtils.encrypt(motDePasse),DateNaissance.getValue().toString(),pdpPathField);
            initialize();

        }


    }



    public boolean validateFields() {
        // Vérification du nom
        if (nom.getText().trim().isEmpty()) {
            showAlert("Erreur de saisie", "Le champ 'Nom' ne peut pas être vide.");
            return false;
        }

        // Vérification du prénom
        if (prenom.getText().trim().isEmpty()) {
            showAlert("Erreur de saisie", "Le champ 'Prénom' ne peut pas être vide.");
            return false;
        }

        // Vérification de l'email (format correct)
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,6}$", email.getText())) {
            showAlert("Erreur de saisie", "Veuillez entrer une adresse email valide.");
            return false;
        }

        // Vérification du mot de passe (au moins une majuscule, une minuscule et un chiffre, min 8 caractères)
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", mdp.getText()) && !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", motDePasse) ) {
            showAlert("Erreur de saisie", "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre.");
            return false;
        }

        // Vérification de la date de naissance
        if (DateNaissance.getValue() == null) {
            showAlert("Erreur de saisie", "Veuillez sélectionner une date de naissance.");
            return false;
        }

        return true; // Si tout est bon
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void MdpVisible() {
        if (isPasswordVisible) {
            mdp.setPromptText("Mot de passe");
            motDePasse = mdp.getText();
            mdp.setText("********");
            mdp.setDisable(true);
            //toggleIcon.setImage(new Image("@Styles/eye.png"));
            isPasswordVisible = false;
        } else {
            mdp.setPromptText("");
            mdp.setText(motDePasse);
            mdp.setDisable(false);
            //toggleIcon.setImage(new Image("@Styles/eye-slash.png"));
            isPasswordVisible = true;
        }
    }


    public void SupprimerCompte(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer votre compte ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
        ClientService cs=new ClientService() ;
        cs.supprimerID(MainProg.idClient);
        MainProg.idClient=0;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SigninClient.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) nom.getScene().getWindow();
        stage.getScene().setRoot(root);
        }


    }



    public void afficherStatistiquePoids() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Poids initial
        double poids = 45.0;

        // Récupérer la date actuelle
        LocalDate currentDate = LocalDate.now();


        double[] gains = {3.0, 4.5, 3.0, 2.5, 1.5, 2.8, 2.3, 1.2, 2.7, 1.4, 3.2, 2.9};

        // Parcourir les 12 derniers mois
        for (int i = 11; i >= 0; i--) {
            LocalDate monthDate = currentDate.minusMonths(i);
            String mois = monthDate.getMonth().toString() + " " + monthDate.getYear();

            // Ajouter la donnée au graphique
            series.getData().add(new XYChart.Data<>(mois, poids));

            // Réduire le poids
            poids += gains[11 - i];
        }

        poidsChart.getData().add(series);
    }


    public void deconnexion(ActionEvent event) {
        MainProg.idClient=0;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SigninClient.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void activities(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/AffichageActivityFront.fxml");
    }

    public void programme(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/listeProgramme.fxml");
    }

    public void equipement(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/market.fxml");
    }

    public void evennement(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/list.fxml");
    }
    public void forum(ActionEvent event) throws IOException {
        NAVIGATOR.changeScene(event, "/afficherPoste.fxml");
    }
}
