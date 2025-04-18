package com.esprit;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChatbotAPI {

    private static final String API_KEY = "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"; // Remplacez par votre clé API
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String getResponse(String userMessage) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Créer la requête HTTP
            HttpPost httpPost = new HttpPost(API_URL);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + API_KEY);

            // Créer le corps de la requête
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", "gpt-4o-mini"); // Modèle GPT-4
            requestBody.addProperty("store", true); // Stocker la conversation
            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", userMessage);
            requestBody.add("messages", new JsonParser().parse("[" + message.toString() + "]"));

            httpPost.setEntity(new StringEntity(requestBody.toString()));

            // Envoyer la requête
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                return jsonResponse.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Désolé, je n'ai pas pu traiter votre demande.";
        }
    }


}