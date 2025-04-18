package com.esprit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject; // Importer la bibliothèque JSON

public class BadWordsChecker {
    private static final String API_URL = "https://api.api-ninjas.com/v1/profanityfilter";
    private static final String API_KEY = "PL51TGkh8hJNVBeBV1CU5Nx7FUcawQA4kQlub2Rp"; // Remplace par ta clé API

    public static boolean containsBadWords(String text) {
        try {
            // Encoder le texte pour l'URL
            String encodedText = URLEncoder.encode(text, "UTF-8");
            System.out.println("Encoded Text: " + encodedText);  // Afficher le texte encodé

            // Construire l'URL de l'API
            String fullUrl = API_URL + "?text=" + encodedText;
            System.out.println("Request URL: " + fullUrl);  // Afficher l'URL complète

            // Ouvrir une connexion HTTP
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Api-Key", API_KEY);  // Ajouter l'API Key dans les headers

            // Afficher les en-têtes de la requête pour vérification
            System.out.println("Request Headers: " + conn.getRequestProperties());

            // Obtenir le code de réponse
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);  // Afficher le code de réponse

            // Lire la réponse de l'API
            if (responseCode == 200) {  // Si la requête est réussie
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("API Response: " + response.toString());  // Afficher la réponse pour débogage

                // Parser la réponse JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                boolean hasProfanity = jsonResponse.getBoolean("has_profanity");
                System.out.println("Contains Bad Words: " + hasProfanity);  // Afficher le résultat de la vérification
                return hasProfanity;
            } else {
                // Lire le message d'erreur si la requête échoue
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorResponse = errorReader.readLine();
                errorReader.close();
                System.out.println("Error Response: " + errorResponse);  // Afficher la réponse d'erreur
            }
        } catch (Exception e) {
            e.printStackTrace();  // Afficher l'exception en cas d'erreur
        }
        return false;  // Retourner false si aucune erreur n'est trouvée
    }


}