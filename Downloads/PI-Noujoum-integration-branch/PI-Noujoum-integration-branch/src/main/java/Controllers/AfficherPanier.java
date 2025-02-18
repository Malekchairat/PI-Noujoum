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
import java.util.List;

public class AfficherPanier {

    @FXML
    private GridPane gridPaniers;
    private final PanierService panierService = new PanierService();

    @FXML
    public void initialize() {
        refreshGrid();
    }

    public void refreshGrid() {
        gridPaniers.getChildren().clear();
        try {
            List<Panier> paniers = panierService.recuperer();
            int row = 1;
            for (Panier panier : paniers) {
                gridPaniers.add(new Label(String.valueOf(panier.getId_panier())), 0, row);
                gridPaniers.add(new Label(String.valueOf(panier.getId_produit())), 1, row);
                gridPaniers.add(new Label(String.valueOf(panier.getId_user())), 2, row);
                gridPaniers.add(new Label(String.valueOf(panier.getNbr_produit())), 3, row);

                Button btnModifier = new Button("Modifier");
                btnModifier.setOnAction(event -> ouvrirFenetreModification(panier));
                gridPaniers.add(btnModifier, 4, row);

                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setOnAction(event -> supprimerPanier(panier.getId_panier()));
                gridPaniers.add(btnSupprimer, 5, row);

                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void supprimerPanier(int idPanier) {
        panierService.supprimer(idPanier);
        refreshGrid();
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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
