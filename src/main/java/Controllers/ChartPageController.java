package Controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Commande;
import services.CommandeService;

public class ChartPageController {

    @FXML
    private PieChart etatChart;

    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        showEtatStatistics();
    }

    private void showEtatStatistics() {
        Map<String, Integer> etatCounts = new HashMap<>();
        List<Commande> commandes = commandeService.recuperer();

        // Count the occurrences of each state (like "Tunis", "Ben Arous", etc.)
        for (Commande cmd : commandes) {
            String etat = cmd.getEtat(); // Assuming etat is the state (Tunis, Ben Arous, etc.)
            etatCounts.put(etat, etatCounts.getOrDefault(etat, 0) + 1);
        }

        // Calculate the total number of commandes
        int totalCommandes = commandes.size();

        for (Map.Entry<String, Integer> entry : etatCounts.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            etatChart.getData().add(data);

            // Calculate the percentage for each part
            double percentage = (double) entry.getValue() / totalCommandes * 100;

            // Set text label with bold, white color and percentage value
            data.getNode().setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            // Add percentage to each slice's label
            data.setName(String.format("%s (%.1f%%)", entry.getKey(), percentage));

            // Customize label appearance: font size, bold, and color
            data.getNode().setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

            // Optional: Customize each slice with different colors for a fun and creative look
            // Here you can keep it dynamic based on etat (state) and the data it contains
            switch (entry.getKey()) {
                case "Tunis":
                    data.getNode().setStyle("-fx-pie-color: #FF5733; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                    break;
                case "Ben Arous":
                    data.getNode().setStyle("-fx-pie-color: #28A745; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                    break;
                case "Sfax":
                    data.getNode().setStyle("-fx-pie-color: #DC3545; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                    break;
                case "Nabeul":
                    data.getNode().setStyle("-fx-pie-color: #007BFF; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                    break;
                default:
                    data.getNode().setStyle("-fx-pie-color: #9C27B0; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                    break;
            }
        }
    }
}
