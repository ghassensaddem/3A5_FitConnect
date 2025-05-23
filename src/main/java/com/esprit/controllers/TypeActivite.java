package com.esprit.controllers;

import com.esprit.models.typeactivite;
import com.esprit.services.typeactiviteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class TypeActivite {
    @FXML
    private TextField hr;
    @FXML
    private TextArea ds; // Correction ici
    @FXML
    private TextArea transcriptArea;
    @FXML
    private Button choose;
    @FXML
    void Validate(ActionEvent event) throws IOException {
        // Récupération des valeurs des champs
        String horaire = hr.getText().trim();
        String description = ds.getText().trim();

        // Vérifications : tous les champs doivent être remplis
        if (horaire.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis !");
            return;
        }

        // Vérification supplémentaire : la description ne doit pas être trop courte
        if (description.length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La description doit contenir au moins 5 caractères !");
            return;
        }

        // Ajout du type d'activité après validation
        typeactiviteService e = new typeactiviteService();
        e.ajouter(new typeactivite(horaire, description));

        // Affichage d'un message de confirmation
        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Type ajouté avec succès !");
    }

    // Méthode générique pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


    @FXML
    private void goToAjouterEvent(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToList2(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activiteevent2.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToList3(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/typeactivite3.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




        private static final String ACCESS_TOKEN = "YCFB3UKMP4B3VOAWQGZKS3GYKJZN3DIY"; // Remplacez par votre clé Wit.ai
        private static final String FILE_NAME = "recorded_audio.wav";
        private TargetDataLine microphone;
        private Thread recordingThread;
        private boolean isRecording = false;

        @FXML
        private Button startButton, stopButton;




        @FXML
        public void startRecording(ActionEvent event) {
            isRecording = true;

            // Définition du format audio
            AudioFormat format = new AudioFormat(16000, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            try {
                if (!AudioSystem.isLineSupported(info)) {
                    transcriptArea.setText("Microphone non supporté !");
                    return;
                }

                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();

                // Création du thread d'enregistrement
                recordingThread = new Thread(() -> {
                    File wavFile = new File(FILE_NAME);

                    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                         AudioInputStream ais = new AudioInputStream(microphone)) {

                        byte[] buffer = new byte[1024];
                        while (isRecording) {
                            int bytesRead = ais.read(buffer, 0, buffer.length);
                            if (bytesRead > 0) {
                                out.write(buffer, 0, bytesRead);
                            }
                        }

                        // Arrêter et fermer le microphone correctement
                        microphone.stop();
                        microphone.close();

                        // Sauvegarde du fichier audio avec un en-tête WAV valide
                        byte[] audioData = out.toByteArray();
                        try (ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
                             AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize())) {

                            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);
                        }

                        // Vérification : afficher l'emplacement du fichier
                        System.out.println("✅ Fichier enregistré : " + wavFile.getAbsolutePath());

                        // Envoyer le fichier audio à Wit.ai
                        sendAudioToWit(FILE_NAME);

                    } catch (IOException e) {
                        e.printStackTrace();
                        transcriptArea.setText("Erreur lors de l'enregistrement : " + e.getMessage());
                    }
                });

                recordingThread.start();
                transcriptArea.setText("Enregistrement en cours...");

            } catch (LineUnavailableException e) {
                e.printStackTrace();
                transcriptArea.setText("Erreur: " + e.getMessage());
            }
        }


    @FXML
        public void stopRecording(ActionEvent event) {
            isRecording = false;
            if (microphone != null) {
                microphone.stop();
                microphone.close();
            }
            transcriptArea.setText("Enregistrement arrêté. Envoi au serveur...");
        }

        private void sendAudioToWit(String audioFilePath) {
            File audioFile = new File(audioFilePath);
            try {
                URL url = new URL("https://api.wit.ai/speech?v=20250228");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
                connection.setRequestProperty("Content-Type", "audio/wav");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream();
                     FileInputStream fis = new FileInputStream(audioFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }

                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }

                    // Convertir la réponse JSON en objet
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    // Extraire la valeur complète du champ "text"
                    String recognizedText = jsonResponse.optString("text", "Aucune transcription");

                    // Afficher le texte complet
                    transcriptArea.setText(recognizedText.trim());
                }



            } catch (IOException e) {
                e.printStackTrace();
                transcriptArea.setText("Erreur d'envoi : " + e.getMessage());
            }
        }
    }




