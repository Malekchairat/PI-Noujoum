package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import models.Commande;
import services.CommandeService;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BAfficherCommande {

    @FXML
    private GridPane gridCommandes;
    @FXML
    private Button btnShowCharts;

    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        refreshGrid();
    }

    public void refreshGrid() {
        gridCommandes.getChildren().clear();

        try {
            String[] headers = {"Rue", "Ville", "Code Postal", "Etat", "Montant", "Methode de Paiement", "Produits", "Actions"};
            for (int i = 0; i < headers.length; i++) {
                Label headerLabel = new Label(headers[i]);
                headerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: darkgrey; -fx-padding: 5px;");
                gridCommandes.add(headerLabel, i, 0);
            }

            List<Commande> commandes = commandeService.recuperer();
            int row = 1;
            for (Commande cmd : commandes) {
                gridCommandes.add(createStyledLabel(cmd.getRue()), 0, row);
                gridCommandes.add(createStyledLabel(cmd.getVille()), 1, row);
                gridCommandes.add(createStyledLabel(cmd.getCode_postal()), 2, row);
                gridCommandes.add(createStyledLabel(cmd.getEtat()), 3, row);
                gridCommandes.add(createStyledLabel(String.valueOf(cmd.getMontant_total())), 4, row);
                gridCommandes.add(createStyledLabel(cmd.getMethodePaiment()), 5, row);
                gridCommandes.add(createStyledLabel(cmd.getProduit()), 6, row);

                Button btnModifier = new Button("Modifier");
                btnModifier.setOnAction(event -> ouvrirFenetreModification(cmd));
                btnModifier.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                btnModifier.setOnMouseEntered(event -> btnModifier.setStyle("-fx-background-color: black; -fx-text-fill: yellow;"));
                btnModifier.setOnMouseExited(event -> btnModifier.setStyle("-fx-background-color: yellow; -fx-text-fill: black;"));
                gridCommandes.add(btnModifier, 7, row);

                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btnSupprimer.setOnAction(event -> supprimerCommande(cmd));
                btnSupprimer.setOnMouseEntered(event -> btnSupprimer.setStyle("-fx-background-color: darkred; -fx-text-fill: white;"));
                btnSupprimer.setOnMouseExited(event -> btnSupprimer.setStyle("-fx-background-color: red; -fx-text-fill: white;"));
                gridCommandes.add(btnSupprimer, 8, row);

                row++;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des commandes : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: darkgrey;");  // Change text color to dark grey
        return label;
    }

    private void ouvrirFenetreModification(Commande commande) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BModifierCommande.fxml"));
            Parent root = loader.load();

            BModifierCommande controller = loader.getController();
            controller.setCommande(commande);
            controller.setBAfficherCommandeController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Commande");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void supprimerCommande(Commande commande) {
        try {
            commandeService.supprimer(commande.getCommande_id());
            refreshGrid();
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }
    @FXML
    public void handleStatPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChartPage.fxml"));
            Parent root = loader.load();

            // Create a new scene for the chart page
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Charts");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    private void showEtatStatistics() {
        Map<String, Integer> etatCounts = new HashMap<>();
        List<Commande> commandes = commandeService.recuperer();

        for (Commande cmd : commandes) {
            etatCounts.put(cmd.getEtat(), etatCounts.getOrDefault(cmd.getEtat(), 0) + 1);
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : etatCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Répartition des Commandes par État");
        pieChart.setLabelsVisible(true);

        // Change the text color of the labels in the chart to dark grey
        pieChart.getData().forEach(data -> data.getNode().setStyle("-fx-text-fill: darkgrey;"));

        // Ensure the chart is added to the HBox correctly

    }
    */


}

