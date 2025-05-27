package com.example.apod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    // IMPORTANT: Replace DEMO_KEY with your own NASA API key from https://api.nasa.gov/
    private static final String NASA_API_KEY = "6crXnws6iWMqRX5bfdcXnl9tLat567AtQCw8Sugv";
    private static final String NASA_APOD_URL = "https://api.nasa.gov/planetary/apod?api_key=" + NASA_API_KEY;

    public static void main(String[] args) {
        // Set the port the server will run on
        port(8080);

        // Serve static files from the 'public' directory in resources
        staticFiles.location("/public");

        // Define the /apod endpoint
        get("/apod", (request, response) -> {
            response.type("application/json");
            String apodDataJson = fetchNasaApodData();

            if (apodDataJson == null) {
                response.status(500); // Internal Server Error
                return "{\"error\": \"Failed to fetch data from NASA API\"}";
            }

            // Parse the full JSON response from NASA
            JsonObject nasaResponse = JsonParser.parseString(apodDataJson).getAsJsonObject();

            // Extract only the fields we need for the frontend
            Map<String, String> frontendData = new HashMap<>();
            frontendData.put("title", nasaResponse.has("title") ? nasaResponse.get("title").getAsString() : "No Title");
            frontendData.put("explanation", nasaResponse.has("explanation") ? nasaResponse.get("explanation").getAsString() : "No Explanation");
            frontendData.put("url", nasaResponse.has("url") ? nasaResponse.get("url").getAsString() : "");
            frontendData.put("hdurl", nasaResponse.has("hdurl") ? nasaResponse.get("hdurl").getAsString() : "");
            frontendData.put("media_type", nasaResponse.has("media_type") ? nasaResponse.get("media_type").getAsString() : "unknown");
            frontendData.put("copyright", nasaResponse.has("copyright") ? nasaResponse.get("copyright").getAsString() : "");


            return new Gson().toJson(frontendData);
        });

        System.out.println("Server started on http://localhost:8080");
    }

    private static String fetchNasaApodData() {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(NASA_APOD_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 5 seconds
            conn.setReadTimeout(5000);    // 5 seconds

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                }
                return result.toString();
            } else {
                System.err.println("NASA API request failed with response code: " + responseCode);
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String errorLine;
                    StringBuilder errorResult = new StringBuilder();
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResult.append(errorLine);
                    }
                    System.err.println("Error details: " + errorResult.toString());
                } catch (Exception e) {
                    System.err.println("Error reading error stream: " + e.getMessage());
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
