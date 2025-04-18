package com.esprit.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFileFormat;


import org.json.JSONObject;

public class AssemblyAIService {
    private static final String API_KEY = "5eff2bebc87749e2983460fcd8d050ec";
    private static final String UPLOAD_URL = "https://api.assemblyai.com/v2/upload";
    private static final String TRANSCRIBE_URL = "https://api.assemblyai.com/v2/transcript";
    private static final String AUDIO_FILE_PATH = "resources/audio_record.wav";

    public Path recordAudio() {
        // Vérifier si le dossier "resources" existe, sinon le créer
        File parentDir = new File("resources/");
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Définir le fichier audio
        File audioFile = new File("resources/audio_record.wav");

        // Définir un format audio compatible
        AudioFormat format = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100, 16, 1, 2, 44100, false // false = little-endian
        );
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        TargetDataLine microphone = null;
        try {
            // Vérifier si la ligne audio est disponible
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("⚠ Format audio non supporté !");
                return null;
            }

            // Ouvrir et démarrer le microphone
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            System.out.println("🎤 Enregistrement en cours... Parlez !");
            AudioInputStream audioStream = new AudioInputStream(microphone);

            // Sauvegarde l'audio dans un fichier
            AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
            System.out.println("✅ Enregistrement terminé : " + audioFile.getAbsolutePath());

        } catch (LineUnavailableException e) {
            System.err.println("❌ Erreur d'accès au microphone : " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'écriture du fichier audio : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // S'assurer que le microphone est bien fermé
            if (microphone != null) {
                microphone.stop();
                microphone.close();
            }
        }

        return audioFile.toPath();
    }


    public static String transcribeAudio(String filePath) throws Exception {
        String uploadedUrl = uploadAudio(filePath);
        return requestTranscription(uploadedUrl);
    }

    private static String uploadAudio(String filePath) throws Exception {
        byte[] audioData = Files.readAllBytes(Paths.get(filePath));
        URL url = new URL(UPLOAD_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", API_KEY);
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(audioData);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = br.readLine();
        br.close();

        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getString("upload_url");
    }

    private static String requestTranscription(String audioUrl) throws Exception {
        URL url = new URL(TRANSCRIBE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("audio_url", audioUrl);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonRequest.toString().getBytes());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = br.readLine();
        br.close();

        JSONObject jsonResponse = new JSONObject(response);
        return getTranscriptionResult(jsonResponse.getString("id"));
    }

    private static String getTranscriptionResult(String transcriptId) throws Exception {
        URL url = new URL(TRANSCRIBE_URL + "/" + transcriptId);
        HttpURLConnection conn;
        String status;
        JSONObject jsonResponse;

        do {
            Thread.sleep(5000);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", API_KEY);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = br.readLine();
            br.close();

            jsonResponse = new JSONObject(response);
            status = jsonResponse.getString("status");
        } while (!status.equals("completed"));

        return jsonResponse.getString("text");
    }
}
