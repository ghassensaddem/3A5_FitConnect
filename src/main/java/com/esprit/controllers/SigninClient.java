package com.esprit.controllers;

import com.esprit.models.Client;
import com.esprit.models.Coach;
import com.esprit.services.AdminService;
import com.esprit.services.ClientService;
import com.esprit.services.CoachService;
import com.esprit.tests.MainProg;
import com.esprit.utils.CaptchaGenerator;
import com.esprit.utils.CryptoUtils;
import com.esprit.utils.MailSender;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.regex.Pattern;

import javafx.scene.Scene;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;


import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;


import javax.imageio.ImageIO;
import javax.swing.SwingUtilities.*;









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
    public Label forgotPassword;

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


    @FXML private ImageView captchaImageView;
    @FXML private TextField captchaInput;
    @FXML private Label errorCaptcha;

    private String generatedCaptcha;

    private OpenCVFrameGrabber grabber;
    private ImageView cameraView;
    private Stage cameraStage;
    private boolean isCapturing = true;
    private CascadeClassifier faceDetector;



    @FXML
    private Button googleSignInBtn;

    private static final String CLIENT_SECRET_FILE = "D:/java/TESTGUI/TESTGUI/src/main/resources/client_secret_84102474659-isd7315dqh7sm3c7elbtdjq5efi5n2pi.apps.googleusercontent.com.json";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String APPLICATION_NAME = "E-Learning";
    private static final String USER = "user";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final java.util.List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");


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
        regenererCaptcha();
        faceDetector = new CascadeClassifier("D:/java/TESTGUI/TESTGUI/src/main/resources/haarcascade_frontalface_default.xml");


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
        else if (pdpPathField.getText().equals("writeableImage")) {
            // Définir le dossier où enregistrer l'image
            String outputPath = "C:/xampp/htdocs/images/";

            // Générer un nom de fichier unique (par exemple timestamp + extension)
            String fileName = "client_" + System.currentTimeMillis() + ".png";
            File outputFile = new File(outputPath + fileName);

            try {
                // Récupérer l'image actuelle du pdpImageView
                WritableImage writableImage = (WritableImage) pdpImageView.getImage();

                // Convertir en BufferedImage
                int width = (int) writableImage.getWidth();
                int height = (int) writableImage.getHeight();
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                PixelReader pixelReader = writableImage.getPixelReader();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
                    }
                }

                // Sauvegarder l'image sous format PNG
                ImageIO.write(bufferedImage, "png", outputFile);

                // Mettre à jour le champ pdpPathField avec le chemin du fichier
                pdpPathField.setText("C:/xampp/htdocs/images/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de l'enregistrement de l'image !");
            }
        }

            CryptoUtils cryptoUtils = new CryptoUtils();
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

        Slider(event);}



    }
    @FXML
    void AUTH(ActionEvent event) throws IOException {
        ClientService cs=new ClientService() ;
        String retour=validateAUTH();
        System.out.println(retour);
        if(retour.equals("client"))
        {
            Client client=cs.GetClient(email2.getText());
            MainProg.idClient=client.getId();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.getScene().setRoot(root);

            Profile p=loader.getController();
            p.initialize();

            stage.setMaximized(true);

        }
        else if(retour.equals("admin") || retour.equals("supadmin"))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUsers.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setMaximized(true);
            MainProg.role=retour;

            ListUsers ls=loader.getController();
            if(retour.equals("admin"))
                ls.setBtnAdminsdisabled();
            ls.initialize();
        }
        else if(retour.equals("coach"))
        {
            /// /////////// A changer ////////////////////////////////////////
            CoachService cc=new CoachService() ;
            Coach coach=cc.GetCoach(email2.getText());
            MainProg.idCoach=coach.getId();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setMaximized(true);
            MainProg.role=retour;
            ListUsers ls=loader.getController();
            AfficherEvents a=loader.getController();
            ls.setBtnAdminsdisabled();
            ls.setBtnClientsdisabled();
            ls.setBtnCoachsDisabled();


            a.initialize();

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
            try {
                // Définir le dossier de destination
                String outputPath = "C:/xampp/htdocs/images/";

                // Générer un nom de fichier unique avec l'extension d'origine
                String fileExtension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String fileName = "client_" + System.currentTimeMillis() + fileExtension;
                File destinationFile = new File(outputPath + fileName);

                // Copier l'image sélectionnée dans le dossier cible
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour le champ pdpPathField avec le nouveau chemin
                pdpPathField.setText("C:/xampp/htdocs/images/" + fileName);

                // Afficher l'image copiée dans ImageView
                pdpImageView.setImage(new Image(destinationFile.toURI().toString()));
                pdpIconLabel.setVisible(false);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("❌ Erreur lors de la copie de l'image !");
            }
        }
    }

    public boolean validateFields() {
        ClientService cs=new ClientService() ;
        AdminService as=new AdminService();
        CoachService cs2=new CoachService();
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
    public String validateAUTH() {
        ClientService cs=new ClientService() ;
        AdminService as=new AdminService();
        CoachService cs2=new CoachService();
        CryptoUtils cryptoUtils = new CryptoUtils();
        String retour="";
        if(cs.AUTH(email2.getText(),cryptoUtils.encrypt(mdp2.getText())))
        {
            errorAUTH.setText("");
            retour = "client";

        }
        else if( as.AUTH(email2.getText(),cryptoUtils.encrypt(mdp2.getText())))
        {
            errorAUTH.setText("");

            retour= as.GetAdmin(email2.getText()).getRole();
        }
        else if(cs2.AUTH(email2.getText(),cryptoUtils.encrypt(mdp2.getText())))
        {
            errorAUTH.setText("");
            retour= "coach";
        }
        else {

            errorAUTH.setText("Email ou mot de passe invalide.");
        }
        if(generatedCaptcha.equals(captchaInput.getText()) && !retour.equals(""))
        {

            errorCaptcha.setText("");
            return retour;
        }
        else
        {
            errorCaptcha.setText("Captcha incorrect.");
        }


        return "";


    }

    public void handleForgotPassword(MouseEvent mouseEvent) {
        if(email2.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez entrer votre adresse email.");
            alert.showAndWait();


        }
        else {
            ClientService cs = new ClientService();
            int userId = cs.GetClient(email2.getText()).getId();
            String encryptedId = CryptoUtils.encrypt(String.valueOf(userId));
            String emailUtilisateur = email2.getText(); // Récupérer l'email entré
            String sujet = "Réinitialisation de votre mot de passe";
            String contenu = "Cliquez sur le lien pour réinitialiser votre mot de passe : \n" +
                    "http://localhost/resetPassword.php?id=" + encryptedId;

            boolean envoiReussi = MailSender.envoyerEmail(emailUtilisateur, sujet, contenu);

            if (envoiReussi) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Un lien de réinitialisation de mot de passe a été envoyé à votre adresse email.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Une erreur s'est produite lors de l'envoi du lien de réinitialisation de mot de passe.");
                alert.showAndWait();
            }
        }
    }

   @FXML
   private void regenererCaptcha() {

       CaptchaGenerator captchaGenerator = new CaptchaGenerator();
       try {
           captchaImageView.setImage(captchaGenerator.generateCaptcha().getImage());
           generatedCaptcha = captchaGenerator.getCaptchaText();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

    @FXML
    private void ouvrirMenuContextuel(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        // Option pour choisir une image
        MenuItem choisirImage = new MenuItem("Choisir une image");
        choisirImage.setOnAction(e -> choisirImage());

        // Option pour prendre une photo avec la caméra
        MenuItem prendrePhoto = new MenuItem("Prendre une photo");
        prendrePhoto.setOnAction(e -> capturerPhoto());

        contextMenu.getItems().addAll(choisirImage, prendrePhoto);
        contextMenu.show(pdpImageView, event.getScreenX(), event.getScreenY());
    }

    @FXML
    private void capturerPhoto() {
        cameraStage = new Stage();
        cameraStage.setTitle("Capture de Photo");

        cameraView = new ImageView();
        cameraView.setFitWidth(400);
        cameraView.setFitHeight(300);

        Button captureButton = new Button("📸 Capturer");
        captureButton.setStyle("-fx-background-color: #065F46; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        captureButton.setOnAction(e -> capturerEtFermer());

        VBox root = new VBox(cameraView, captureButton);
        root.setSpacing(10);
        root.setStyle("-fx-alignment: center; -fx-padding: 15px; -fx-background-color: white;");

        Scene scene = new Scene(root, 420, 380);
        cameraStage.setScene(scene);
        cameraStage.show();

        startCamera();
    }

    private void startCamera() {
        isCapturing = true;
        grabber = new OpenCVFrameGrabber(0);
        new Thread(() -> {
            try {
                grabber.start();
                while (isCapturing) {
                    Frame frame = grabber.grab();
                    if (frame != null) {
                        // Conversion en BufferedImage pour affichage
                        Java2DFrameConverter converter = new Java2DFrameConverter();
                        BufferedImage bufferedImage = converter.convert(frame);

                        // Convertir en format OpenCV
                        OpenCVFrameConverter.ToMat matConverter = new OpenCVFrameConverter.ToMat();
                        Mat matImage = matConverter.convert(frame);

                        // Détection des visages et ajout des rectangles
                        Mat processedImage = drawFaceRectangles(matImage, detectFaces(matImage));

                        // Convertir l'image modifiée en BufferedImage
                        Frame processedFrame = matConverter.convert(processedImage);
                        BufferedImage processedBufferedImage = converter.convert(processedFrame);

                        // Convertir en WritableImage pour l'affichage en JavaFX
                        WritableImage writableImage = bufferedImageToWritableImage(processedBufferedImage);

                        // Mettre à jour l'affichage de la caméra en temps réel
                        Platform.runLater(() -> cameraView.setImage(writableImage));
                    }
                }
                grabber.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void capturerEtFermer() {
        isCapturing = false;
        try {
            Frame frame = grabber.grab();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bufferedImage = converter.convert(frame);

            OpenCVFrameConverter.ToMat matConverter = new OpenCVFrameConverter.ToMat();
            Mat matImage = matConverter.convert(frame);

            // Détection des visages
            RectVector faces = detectFaces(matImage);

            // Vérifier le nombre de visages
            if (faces.size() == 0) {
                System.out.println("❌ Aucun visage détecté !");
                grabber.stop();
                cameraStage.close();
                return;
            } else if (faces.size() > 1) {
                System.out.println("❌ Plusieurs visages détectés !");
                grabber.stop();
                cameraStage.close();
                return;
            }

            // ✅ Si un seul visage, convertir l’image originale (sans rectangle)
            WritableImage writableImage = bufferedImageToWritableImage(bufferedImage);

            // Afficher l’image dans l'interface (sans rectangle)
            pdpImageView.setImage(writableImage);
            pdpPathField.setText("writeableImage");
            cameraView.setImage(writableImage);

            // Arrêter la caméra et fermer la fenêtre
            grabber.stop();
            cameraStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private boolean isValidFace(Mat image) {
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(image, gray, opencv_imgproc.COLOR_BGR2GRAY);

        RectVector faces = new RectVector();
        faceDetector.detectMultiScale(gray, faces);

        return faces.size() == 1; // Retourne vrai seulement si un seul visage est détecté
    }


    private Mat drawFaceRectangles(Mat image, RectVector faces) {
        Scalar color;
        int thickness = 2;

        for (int i = 0; i < faces.size(); i++) {
            Rect face = faces.get(i);

            // Si un seul visage, carré vert - Sinon, carrés rouges
            if (faces.size() == 1) {
                color = new Scalar(0, 255, 0, 1); // Vert
            } else {
                color = new Scalar(0, 0, 255, 1); // Rouge
            }

            // Dessiner le rectangle autour du visage
            opencv_imgproc.rectangle(image, new Point(face.x(), face.y()),
                    new Point(face.x() + face.width(), face.y() + face.height()), color, thickness, 8, 0);
        }
        return image;
    }


    private WritableImage bufferedImageToWritableImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
            }
        }
        return writableImage;
    }


    private RectVector detectFaces(Mat image) {
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(image, gray, opencv_imgproc.COLOR_BGR2GRAY);

        RectVector faces = new RectVector();
        faceDetector.detectMultiScale(gray, faces);
        return faces;
    }




    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new FileReader(CLIENT_SECRET_FILE));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(USER);
    }

    @FXML
    private void handleGoogleSignIn(ActionEvent event) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = getCredentials(HTTP_TRANSPORT);

            PeopleService peopleService = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Person profile = peopleService.people().get("people/me")
                    .setPersonFields("names,emailAddresses,genders,birthdays")
                    .execute();

            String nom = "";
            String prenom = "";
            String email = "";
            String sexe = "";
            String dateNaissance = "";
            String image = "/Styles/logo.png";

            if (profile.getNames() != null && !profile.getNames().isEmpty()) {
                nom = profile.getNames().get(0).getFamilyName();
                prenom = profile.getNames().get(0).getGivenName();
            }

            if (profile.getEmailAddresses() != null && !profile.getEmailAddresses().isEmpty()) {
                email = profile.getEmailAddresses().get(0).getValue();
            }

            if (profile.getGenders() != null && !profile.getGenders().isEmpty()) {
                sexe = profile.getGenders().get(0).getValue().equalsIgnoreCase("male") ? "homme" : "femme";
            }

            if (profile.getBirthdays() != null && !profile.getBirthdays().isEmpty()) {
                if (profile.getBirthdays().get(0).getDate() != null) {
                    int year = profile.getBirthdays().get(0).getDate().getYear();
                    int month = profile.getBirthdays().get(0).getDate().getMonth();
                    int day = profile.getBirthdays().get(0).getDate().getDay();
                    dateNaissance = String.format("%04d-%02d-%02d", year, month, day);
                }
            }



            ClientService cs = new ClientService();
            CryptoUtils cryptoUtils = new CryptoUtils();

            if (!cs.existe(email)) {
                Client client = new Client(0, nom, prenom, sexe,cryptoUtils.encrypt("Rayan1234"), dateNaissance, email, image, 70, 175);
                cs.ADD(client);
                System.out.println("Client ajouté avec succès !");
                client = cs.GetClient(email);
                email2.setText(client.getEmail());
                mdp2.setText(cryptoUtils.decrypt(client.getMdp()));
                AUTH(event);
            }
            else
            {
                Client client = cs.GetClient(email);
                email2.setText(client.getEmail());
                mdp2.setText(cryptoUtils.decrypt(client.getMdp()));
                AUTH(event);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'authentification Google !");
        }
    }




}

