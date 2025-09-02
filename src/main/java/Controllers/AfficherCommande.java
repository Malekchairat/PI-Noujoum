package Controllers;

import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import models.Commande;
import services.CommandeService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.util.List;

public class AfficherCommande {

    @FXML
    private GridPane gridCommandes;

    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        gridCommandes.setPrefWidth(900); // Optimize width
        gridCommandes.setHgap(10); // Reduce horizontal spacing
        gridCommandes.setVgap(10); // Reduce vertical spacing
        gridCommandes.setStyle("-fx-background-color: #2c2f33; -fx-padding: 15px; -fx-border-radius: 10px;");
        setupColumns();
        refreshGrid();
    }

    private void setupColumns() {
        gridCommandes.getColumnConstraints().clear();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30); // 30% for Montant

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70); // 70% for Produits

        gridCommandes.getColumnConstraints().addAll(col1, col2);
    }

    public void refreshGrid() {
<<<<<<< HEAD
<<<<<<< HEAD
        gridCommandes.getChildren().clear(); // Supprimer les anciennes données

        try {
            // Ajouter les en-têtes de colonne avec texte blanc
            String[] headers = {"ID", "ID User", "ID Panier", "Rue", "Ville", "Code Postal", "État", "Montant", "Paiement", "Actions"};
            for (int i = 0; i < headers.length; i++) {
                Label headerLabel = new Label(headers[i]);
                headerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 5px;");
=======
        gridCommandes.getChildren().clear(); // Supprime les anciennes données
=======
        gridCommandes.getChildren().clear();
>>>>>>> origin/GestionCommande

        try {
            // Headers with better design
            String[] headers = {"Montant", "Produits"};
            for (int i = 0; i < headers.length; i++) {
                Label headerLabel = new Label(headers[i]);
<<<<<<< HEAD
                headerLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5px;");
>>>>>>> origin/integration-branch
=======
                headerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-padding: 8px;");
>>>>>>> origin/GestionCommande
                gridCommandes.add(headerLabel, i, 0);
            }

            // Load commandes from DB
            List<Commande> commandes = commandeService.recuperer();
            int row = 1;
            for (Commande cmd : commandes) {
<<<<<<< HEAD
<<<<<<< HEAD
                gridCommandes.add(createStyledLabel(String.valueOf(cmd.getCommande_id())), 0, row);
                gridCommandes.add(createStyledLabel(String.valueOf(cmd.getId_user())), 1, row);
                gridCommandes.add(createStyledLabel(String.valueOf(cmd.getId_panier())), 2, row);
                gridCommandes.add(createStyledLabel(cmd.getRue()), 3, row);
                gridCommandes.add(createStyledLabel(cmd.getVille()), 4, row);
                gridCommandes.add(createStyledLabel(cmd.getCode_postal()), 5, row);
                gridCommandes.add(createStyledLabel(cmd.getEtat()), 6, row);
                gridCommandes.add(createStyledLabel(String.valueOf(cmd.getMontant_total())), 7, row);
                gridCommandes.add(createStyledLabel(cmd.getMethodePaiment()), 8, row);
=======
                gridCommandes.add(new Label(String.valueOf(cmd.getCommande_id())), 0, row);
                gridCommandes.add(new Label(String.valueOf(cmd.getId_user())), 1, row);
                gridCommandes.add(new Label(String.valueOf(cmd.getId_panier())), 2, row);
                gridCommandes.add(new Label(cmd.getRue()), 3, row);
                gridCommandes.add(new Label(cmd.getVille()), 4, row);
                gridCommandes.add(new Label(cmd.getCode_postal()), 5, row);
                gridCommandes.add(new Label(cmd.getEtat()), 6, row);
                gridCommandes.add(new Label(String.valueOf(cmd.getMontant_total())), 7, row);
                gridCommandes.add(new Label(cmd.getMethodePaiment()), 8, row);
>>>>>>> origin/integration-branch

                // Bouton Modifier
                Button btnModifier = new Button("Modifier");
                btnModifier.setOnAction(event -> ouvrirFenetreModification(cmd));
<<<<<<< HEAD
                btnModifier.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");

// Effet hover pour Modifier
                btnModifier.setOnMouseEntered(event -> btnModifier.setStyle("-fx-background-color: black; -fx-text-fill: yellow;"));
                btnModifier.setOnMouseExited(event -> btnModifier.setStyle("-fx-background-color: yellow; -fx-text-fill: black;"));

                gridCommandes.add(btnModifier, 9, row);

// Bouton Supprimer
                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btnSupprimer.setOnAction(event -> supprimerCommande(cmd));

// Effet hover pour Supprimer
                btnSupprimer.setOnMouseEntered(event -> btnSupprimer.setStyle("-fx-background-color: darkred; -fx-text-fill: white;"));
                btnSupprimer.setOnMouseExited(event -> btnSupprimer.setStyle("-fx-background-color: red; -fx-text-fill: white;"));

                gridCommandes.add(btnSupprimer, 10, row);


                row++;
            }

        } catch (Exception e) {
=======
                gridCommandes.add(btnModifier, 9, row);

                // Bouton Supprimer
                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btnSupprimer.setOnAction(event -> supprimerCommande(cmd));
                gridCommandes.add(btnSupprimer, 10, row);
=======
                Label montantLabel = createStyledLabel(String.valueOf(cmd.getMontant_total()));
                Label produitsLabel = createStyledLabel(cmd.getProduit());

                // Add background to alternate rows for better readability
                if (row % 2 == 0) {
                    montantLabel.setStyle(montantLabel.getStyle() + "; -fx-background-color: #3a3d42; -fx-padding: 10px; -fx-border-radius: 5px;");
                    produitsLabel.setStyle(produitsLabel.getStyle() + "; -fx-background-color: #3a3d42; -fx-padding: 10px; -fx-border-radius: 5px;");
                }
>>>>>>> origin/GestionCommande

                gridCommandes.add(montantLabel, 0, row);
                gridCommandes.add(produitsLabel, 1, row);
                row++;
            }

<<<<<<< HEAD
        } catch (SQLException e) {
>>>>>>> origin/integration-branch
            System.err.println("Erreur lors du chargement des commandes : " + e.getMessage());
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white;"); // Applique la couleur blanche à chaque label
        return label;
    }

=======
>>>>>>> origin/integration-branch
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
<<<<<<< HEAD
            commandeService.supprimer(commande.getCommande_id());
=======
            commandeService.supprimer(commande);
>>>>>>> origin/integration-branch
            refreshGrid(); // Rafraîchir après suppression
=======
>>>>>>> origin/GestionCommande
        } catch (Exception e) {
            System.err.println("Error loading orders: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-padding: 5px;");
        return label;
    }
<<<<<<< HEAD
}
=======
}
<<<<<<< HEAD

>>>>>>> origin/integration-branch
=======
>>>>>>> origin/GestionCommande
