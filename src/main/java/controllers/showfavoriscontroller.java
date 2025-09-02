package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import services.FavorisService;
import models.Favoris;

import java.io.IOException;
import java.util.List;

public class showfavoriscontroller {

    @FXML
    private TilePane favorisTilePane;

    @FXML
    private ScrollPane scrollPane;

    private final FavorisService favorisService = new FavorisService();

    @FXML
    public void loadFavoris() {
        if (favorisTilePane == null || scrollPane == null) {
            System.out.println("ERREUR: Un composant FXML est NULL !");
            return;
        }

        favorisTilePane.getChildren().clear();
        favorisTilePane.setHgap(30);
        favorisTilePane.setVgap(30);

        try {
            List<Favoris> favorisList = favorisService.recupererFavoris();

            for (Favoris favori : favorisList) {
                VBox favorisCard = new VBox(8);
                favorisCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4; -fx-border-radius: 10px;");
                favorisCard.setPrefSize(200, 250);

                Label idLabel = new Label("📌 ID Produit: " + favori.getIdProduit());
                Label userLabel = new Label("👤 ID Utilisateur: " + favori.getIdUser());
                Label dateLabel = new Label("📅 Date: " + favori.getDate());

                Button modifyButton = new Button("✏ Modifier");
                modifyButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 5px 10px;");
                modifyButton.setOnAction(event -> openModifierFavoris(favori));

                Button deleteButton = new Button("🗑 Supprimer");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 5px 10px;");
                deleteButton.setOnAction(event -> deleteFavoris(favori.getIdFavoris()));

                favorisCard.getChildren().addAll(idLabel, userLabel, dateLabel, modifyButton, deleteButton);
                favorisTilePane.getChildren().add(favorisCard);
            }

            scrollPane.setContent(favorisTilePane);
            System.out.println("✅ Favoris chargés avec succès !");

        } catch (Exception e) {
            System.out.println("❌ Erreur lors du chargement des favoris: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openModifierFavoris(Favoris favori) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierfavoris.fxml"));
            Parent root = loader.load();

            modifierfavoriscontroller controller = loader.getController();
            controller.initData(favori);

            Stage stage = new Stage();
            stage.setTitle("Modifier Favoris");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("❌ Erreur lors de l'ouverture de la fenêtre de modification : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteFavoris(int id_favoris) {
        try {
            favorisService.supprimerFavoris(id_favoris);
            System.out.println("🗑 Favori supprimé avec succès !");
            loadFavoris();
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la suppression du favori: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
