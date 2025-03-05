package Controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Commande;
import services.CommandeService;

public class ChartPageController {

    @FXML
    private PieChart etatChart;

    @FXML
    private PieChart produitChart; // New chart for most sold products

    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        showEtatStatistics();
        showProduitStatistics(); // Call the new method to display product stats
    }

    private void showEtatStatistics() {
        Map<String, Integer> etatCounts = new HashMap<>();
        List<Commande> commandes = commandeService.recuperer();

        for (Commande cmd : commandes) {
            String etat = cmd.getEtat();
            etatCounts.put(etat, etatCounts.getOrDefault(etat, 0) + 1);
        }

        int totalCommandes = commandes.size();

        for (Map.Entry<String, Integer> entry : etatCounts.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            etatChart.getData().add(data);

            double percentage = (double) entry.getValue() / totalCommandes * 100;
            data.setName(String.format("%s (%.1f%%)", entry.getKey(), percentage));

            // Set colors dynamically for different states
            switch (entry.getKey()) {
                case "Tunis":
                    data.getNode().setStyle("-fx-pie-color: #FF5733;");
                    break;
                case "Ben Arous":
                    data.getNode().setStyle("-fx-pie-color: #28A745;");
                    break;
                case "Sfax":
                    data.getNode().setStyle("-fx-pie-color: #DC3545;");
                    break;
                case "Nabeul":
                    data.getNode().setStyle("-fx-pie-color: #007BFF;");
                    break;
                default:
                    data.getNode().setStyle("-fx-pie-color: #9C27B0;");
                    break;
            }
        }
    }

    private void showProduitStatistics() {
        if (produitChart == null) {
            System.out.println("Error: produitChart is null.");
            return;
        }

        Map<String, Integer> produitCounts = new HashMap<>();
        List<Commande> commandes = commandeService.recuperer();

        int totalQuantities = 0; // To calculate percentage

        for (Commande cmd : commandes) {
            String produitData = cmd.getProduit(); // Get product string

            if (produitData == null || produitData.isEmpty()) {
                System.out.println("Skipping empty produit for: " + cmd);
                continue;
            }

            // Split by new lines
            String[] produits = produitData.split("\n");

            for (String produitEntry : produits) {
                produitEntry = produitEntry.trim();

                // Extract name and quantity using regex
                Pattern pattern = Pattern.compile("(.+) \\(Quantity: (\\d+)\\)");
                Matcher matcher = pattern.matcher(produitEntry);

                if (!matcher.matches()) {
                    System.out.println("Skipping invalid format: " + produitEntry);
                    continue;
                }

                String produitName = matcher.group(1).trim();
                int quantite = Integer.parseInt(matcher.group(2).trim());

                produitCounts.put(produitName, produitCounts.getOrDefault(produitName, 0) + quantite);
                totalQuantities += quantite;
            }
        }

        System.out.println("Produit Data: " + produitCounts); // DEBUG

        // Clear previous data
        produitChart.getData().clear();

        if (produitCounts.isEmpty()) {
            System.out.println("No product data found.");
            return;
        }

        // Assigning colors to each product
        String[] colors = {"#FF5733", "#28A745", "#007BFF", "#9C27B0", "#FFC107"};
        int colorIndex = 0;

        for (Map.Entry<String, Integer> entry : produitCounts.entrySet()) {
            String produitName = entry.getKey();
            int quantite = entry.getValue();
            double percentage = (double) quantite / totalQuantities * 100;

            // Format label: "Product Name (Quantity, Percentage%)"
            String label = String.format("%s (%d, %.1f%%)", produitName, quantite, percentage);

            PieChart.Data data = new PieChart.Data(label, quantite);
            produitChart.getData().add(data);

            // Apply colors dynamically
            String color = colors[colorIndex % colors.length];
            data.getNode().setStyle("-fx-pie-color: " + color + "; -fx-font-size: 14px; -fx-font-weight: bold;");

            colorIndex++;
        }

        System.out.println("Produit chart updated successfully.");
    }

}
