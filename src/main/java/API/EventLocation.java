package API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class EventLocation {

    // Nominatim API endpoint
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?format=json&q=";

    // Method to get latitude from address
    public double getLatitude(String address) {
        try {
            // Replace spaces with "+" for URL encoding
            String urlString = NOMINATIM_URL + address.replaceAll(" ", "+");
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");

            // Read the response from Nominatim API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JSONArray jsonArray = new JSONArray(response.toString());
            if (jsonArray.length() > 0) {
                JSONObject firstResult = jsonArray.getJSONObject(0);
                return firstResult.getDouble("lat"); // Extract latitude
            } else {
                System.out.println("No result found for the address: " + address);
                return 0.0; // Return a default value if no result found
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0; // Return a default value in case of an error
        }
    }

    // Method to get longitude from address
    public double getLongitude(String address) {
        try {
            // Replace spaces with "+" for URL encoding
            String urlString = NOMINATIM_URL + address.replaceAll(" ", "+");
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");

            // Read the response from Nominatim API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JSONArray jsonArray = new JSONArray(response.toString());
            if (jsonArray.length() > 0) {
                JSONObject firstResult = jsonArray.getJSONObject(0);
                return firstResult.getDouble("lon"); // Extract longitude
            } else {
                System.out.println("No result found for the address: " + address);
                return 0.0; // Return a default value if no result found
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0; // Return a default value in case of an error
        }
    }
}