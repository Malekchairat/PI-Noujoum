package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Panier;
import services.PanierService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherPanier {

    @FXML
    private GridPane gridPaniers; // GridPane pour afficher les paniers

    private final PanierService panierService = new PanierService(); // Service pour interagir avec la base de données

    @FXML
    public void initialize() {
        refreshGrid(); // Charger les paniers au démarrage
    }

    /**
     * Rafraîchit le GridPane avec les paniers de la base de données.
     */
    public void refreshGrid() {
        gridPaniers.getChildren().clear(); // Supprime les anciennes données

        try {
            // Ajouter les en-têtes de colonne avec texte blanc
            String[] headers = {"ID Panier", "ID Produit", "ID User", "Nombre de Produits", "Actions"};
            for (int i = 0; i < headers.length; i++) {
                Label headerLabel = new Label(headers[i]);
                headerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 5px;");
                gridPaniers.add(headerLabel, i, 0);
            }

            // Charger les paniers depuis la base de données
            List<Panier> paniers = panierService.recuperer();

            int row = 1;
            for (Panier panier : paniers) {
                gridPaniers.add(createStyledLabel(String.valueOf(panier.getId_panier())), 0, row);
                gridPaniers.add(createStyledLabel(String.valueOf(panier.getId_produit())), 1, row);
                gridPaniers.add(createStyledLabel(String.valueOf(panier.getId_user())), 2, row);
                gridPaniers.add(createStyledLabel(String.valueOf(panier.getNbr_produit())), 3, row);

                // Bouton Modifier
                Button btnModifier = new Button("Modifier");
                btnModifier.setOnAction(event -> ouvrirFenetreModification(panier));
                btnModifier.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                btnModifier.setOnMouseEntered(event -> btnModifier.setStyle("-fx-background-color: black; -fx-text-fill: yellow;"));
                btnModifier.setOnMouseExited(event -> btnModifier.setStyle("-fx-background-color: yellow; -fx-text-fill: black;"));

                gridPaniers.add(btnModifier, 4, row);

                // Bouton Supprimer
                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btnSupprimer.setOnAction(event -> supprimerPanier(panier));
                btnSupprimer.setOnMouseEntered(event -> btnSupprimer.setStyle("-fx-background-color: darkred; -fx-text-fill: white;"));
                btnSupprimer.setOnMouseExited(event -> btnSupprimer.setStyle("-fx-background-color: red; -fx-text-fill: white;"));
                gridPaniers.add(btnSupprimer, 5, row);

                row++;
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des paniers : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white;"); // Applique la couleur blanche à chaque label
        return label;
    }

    private void supprimerPanier(Panier panier) {
        try {
            panierService.supprimer(panier.getId_panier());
            refreshGrid(); // Rafraîchir après suppression
            System.out.println("Panier supprimé avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du panier : " + e.getMessage());
        }
    }

    private void ouvrirFenetreModification(Panier panier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPanier.fxml"));
            Parent root = loader.load();

            ModifierPanier controller = loader.getController();
            controller.setPanier(panier);
            controller.setAfficherPanierController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Panier");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } } }
