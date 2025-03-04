package com.esprit.controllers;



import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.InetAddress;


public class Map {

    @FXML
    private MapView mapView;
    @FXML
    private Label coordinatesLabel;
    @FXML
    private Label weatherLabel;
    @FXML
    private ImageView weatherIcon;

    private final String API_KEY = "";

    private void showMap() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/map.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage mapStage = new Stage();
            mapStage.setScene(scene);
            mapStage.setTitle("Carte Interactif");
            mapStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MapView getMapView() {
        return mapView;
    }
    @FXML
    public void initialize() throws MalformedURLException {
        System.out.println("Initialisation de la carte...");
        mapView.initialize();

        mapView.initializedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Carte initialisée avec succès !");
                mapView.setMapType(com.sothawo.mapjfx.MapType.OSM);
                mapView.setZoom(10);
                mapView.setCenter(new Coordinate(36.222222, 10.13333));
            }
        });

        mapView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            Coordinate coord = mapView.getCenter();
            if (coord != null) {
                double latitude = coord.getLatitude();
                double longitude = coord.getLongitude();
                coordinatesLabel.setText("Coordonnées sélectionnées : " + latitude + ", " + longitude);
                if (isInternetAvailable()) {
                    fetchWeather(latitude, longitude);
                } else {
                    showAlert("Erreur de connexion", "Aucune connexion Internet disponible.");
                }
            }
        });
    }

    public String getLocationName(double latitude, double longitude) {
        try {
            String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.optString("display_name", "Lieu inconnu");

        } catch (Exception e) {
            e.printStackTrace();
            return "Lieu inconnu";
        }
    }

    private void fetchWeather(double latitude, double longitude) {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            double temp = jsonResponse.getJSONObject("main").getDouble("temp");
            String weather = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            String iconCode = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

            System.out.println("URL de l'icône météo : " + "http://openweathermap.org/img/wn/" + iconCode + "@2x.png");

            weatherLabel.setText("Météo: " + weather + " | Température: " + temp + "°C");
            weatherIcon.setImage(new Image("http://openweathermap.org/img/wn/" + iconCode + "@2x.png"));

        } catch (Exception e) {
            e.printStackTrace();
            weatherLabel.setText("Impossible de récupérer les données météo.");
        }
    }

    private boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(2000);
        } catch (IOException e) {
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void closeMapWindow() {
        Stage stage = (Stage) coordinatesLabel.getScene().getWindow();
        stage.close();
    }
}
