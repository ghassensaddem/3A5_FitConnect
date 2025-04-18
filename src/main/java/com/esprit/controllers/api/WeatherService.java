package com.esprit.controllers.api;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {
    private final String API_KEY = "294172e55663c6d252179c242db11ad0"; // ⚠️ Pense à externaliser cette clé !
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public JSONObject getWeather(String city) {
        try {
            String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) { // Erreur HTTP
                throw new RuntimeException("Erreur HTTP : " + responseCode);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return new JSONObject(response.toString());

        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de la météo : " + e.getMessage());
            return null; // ou gérer une réponse vide
        }
    }
}

