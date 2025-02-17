package Controllers;

import models.Commande;
import services.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherCommande {

    @FXML
    private GridPane gridCommandes;
    @FXML
    private Button btnFermer;

    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        refreshGrid(); // Charger les commandes au démarrage
    }

    public void refreshGrid() {
        gridCommandes.getChildren().clear(); // Supprime les anciennes données

        try {
            // Ajouter les en-têtes de colonne
            String[] headers = {"ID", "ID User", "ID Panier", "Rue", "Ville", "Code Postal", "État", "Montant", "Paiement", "Actions"};
            for (int i = 0; i < headers.length; i++) {
                Label headerLabel = new Label(headers[i]);
                headerLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5px;");
                gridCommandes.add(headerLabel, i, 0);
            }

            // Charger les commandes depuis la base de données
            List<Commande> commandes = commandeService.recuperer();

            int row = 1;
            for (Commande cmd : commandes) {
                gridCommandes.add(new Label(String.valueOf(cmd.getCommande_id())), 0, row);
                gridCommandes.add(new Label(String.valueOf(cmd.getId_user())), 1, row);
                gridCommandes.add(new Label(String.valueOf(cmd.getId_panier())), 2, row);
                gridCommandes.add(new Label(cmd.getRue()), 3, row);
                gridCommandes.add(new Label(cmd.getVille()), 4, row);
                gridCommandes.add(new Label(cmd.getCode_postal()), 5, row);
                gridCommandes.add(new Label(cmd.getEtat()), 6, row);
                gridCommandes.add(new Label(String.valueOf(cmd.getMontant_total())), 7, row);
                gridCommandes.add(new Label(cmd.getMethodePaiment()), 8, row);

                // Bouton Modifier
                Button btnModifier = new Button("Modifier");
                btnModifier.setOnAction(event -> ouvrirFenetreModification(cmd));
                gridCommandes.add(btnModifier, 9, row);

                // Bouton Supprimer
                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btnSupprimer.setOnAction(event -> supprimerCommande(cmd));
                gridCommandes.add(btnSupprimer, 10, row);

                row++;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des commandes : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ouvrirFenetreModification(Commande commande) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCommande.fxml"));
            Parent root = loader.load();

            ModifierCommande controller = loader.getController();
            controller.setCommande(commande);
            controller.setAfficherCommandeController(this);

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
            commandeService.supprimer(commande);
            refreshGrid(); // Rafraîchir après suppression
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    private void fermer() {
        btnFermer.getScene().getWindow().hide();
    }
}

