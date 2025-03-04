package com.esprit.controllers.api;


import com.sothawo.mapjfx.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Scanner;

public class MapApp extends Application {

    private static final String API_KEY = "294172e55663c6d252179c242db11ad0";

    @Override
    public void start(Stage primaryStage) {}
      /*MapView mapView = new MapView();
        mapView.initialize(Configuration.builder().build());

        mapView.setOnMapClick(event -> {
            showContextMenu(event.getCoordinate(), mapView, LocalDate.now());
            fetchWeatherDataAndAddIcon(mapView, event.getCoordinate());
        });

        BorderPane root = new BorderPane(mapView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("E-Learning Map with Weather");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showContextMenu(Coordinate coordinate, MapView mapView, LocalDate eventDate) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem weatherItem = new MenuItem("Show Weather for Event");
        weatherItem.setOnAction(e -> fetchWeatherData(coordinate, eventDate));

        MenuItem coordinatesItem = new MenuItem("Show Coordinates");
        coordinatesItem.setOnAction(e -> showCoordinates(coordinate));

        MenuItem icsItem = new MenuItem("Generate ICS File");
        icsItem.setOnAction(e -> generateICSFile(coordinate, eventDate));

        contextMenu.getItems().addAll(weatherItem, coordinatesItem, icsItem);
        contextMenu.show(mapView, javafx.scene.input.MouseInfo.getPointerInfo().getLocation().getX(),
                javafx.scene.input.MouseInfo.getPointerInfo().getLocation().getY());
    }

    private void fetchWeatherDataAndAddIcon(MapView mapView, Coordinate coordinate) {
        String urlString = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric",
                coordinate.getLatitude(), coordinate.getLongitude(), API_KEY);
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                addWeatherIcon(mapView, coordinate, response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addWeatherIcon(MapView mapView, Coordinate coordinate, String weatherData) {
        JSONObject json = new JSONObject(weatherData);
        String iconCode = json.getJSONArray("weather").getJSONObject(0).getString("icon");
        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

        Image weatherIcon = new Image(iconUrl);
        ImageView iconView = new ImageView(weatherIcon);
        iconView.setFitWidth(30);
        iconView.setFitHeight(30);

        MapLabel weatherLabel = new MapLabel("🌡️").setPosition(coordinate);
        mapView.addLabel(weatherLabel);
    }

    private void fetchWeatherData(Coordinate coordinate, LocalDate eventDate) {
        String urlString = String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&appid=%s&units=metric",
                coordinate.getLatitude(), coordinate.getLongitude(), API_KEY);
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                parseAndShowWeather(response.toString(), eventDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseAndShowWeather(String responseBody, LocalDate eventDate) {
        JSONObject json = new JSONObject(responseBody);
        var list = json.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject entry = list.getJSONObject(i);
            String dateTime = entry.getString("dt_txt");
            if (dateTime.startsWith(eventDate.toString())) {
                JSONObject main = entry.getJSONObject("main");
                double temperature = main.getDouble("temp");
                String weatherDescription = entry.getJSONArray("weather").getJSONObject(0).getString("description");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Weather Info");
                alert.setHeaderText("Weather for Event Date");
                alert.setContentText(String.format("Temperature: %.1f°C\nWeather: %s\nDate: %s", temperature, weatherDescription, eventDate));
                alert.show();
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Weather Info");
        alert.setHeaderText("No Weather Data Available");
        alert.setContentText("No forecast data found for the selected event date.");
        alert.show();
    }

    private void showCoordinates(Coordinate coordinate) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coordinates Info");
        alert.setHeaderText("Selected Coordinates");
        alert.setContentText(String.format("Latitude: %.5f\nLongitude: %.5f", coordinate.getLatitude(), coordinate.getLongitude()));
        alert.show();
    }

    private void generateICSFile(Coordinate coordinate, LocalDate eventDate) {
        String icsContent = String.format("BEGIN:VCALENDAR\nVERSION:2.0\nBEGIN:VEVENT\nSUMMARY:Event at Selected Location\nLOCATION:%f, %f\nDTSTART:%sT120000Z\nDTEND:%sT130000Z\nEND:VEVENT\nEND:VCALENDAR",
                coordinate.getLatitude(), coordinate.getLongitude(), eventDate, eventDate);

        try {
            Path icsPath = Path.of("event.ics");
            Files.writeString(icsPath, icsContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ICS File Generated");
            alert.setHeaderText("ICS File Saved");
            alert.setContentText("ICS file saved at: " + icsPath.toAbsolutePath());
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }*/
}
