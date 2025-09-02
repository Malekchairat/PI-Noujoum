package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.FavorisService;
import models.Favoris;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.List;

public class showfavoriscontroller {

    @FXML
    private TilePane favorisTilePane;

    @FXML
    private ScrollPane scrollPane;

    private final FavorisService favorisService = new FavorisService();

    @FXML
    public void initialize() {
        System.out.println("🔄 Initialisation du contrôleur ShowFavorisController");
        if (favorisTilePane == null || scrollPane == null) {
            System.out.println("❌ ERREUR: Un élément est NULL ! Vérifie ton fichier FXML.");
            return;
        }

        // Configuration du ScrollPane pour permettre le défilement vertical
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: black; -fx-border-color: gold;");

        // Configuration du TilePane
        favorisTilePane.setHgap(20);
        favorisTilePane.setVgap(20);
        favorisTilePane.setPrefColumns(3);
        favorisTilePane.setStyle("-fx-background-color: black; -fx-padding: 20px;");

        // 🔥 Fix: Faire en sorte que le TilePane s'ajuste dynamiquement
        favorisTilePane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
        favorisTilePane.setPrefHeight(Region.USE_COMPUTED_SIZE);

        loadFavoris();
    }

    public void loadFavoris() {
        favorisTilePane.getChildren().clear();

        try {
            List<Favoris> favorisList = favorisService.recuperer();
            System.out.println("🔍 Nombre de favoris récupérés: " + favorisList.size());

            if (favorisList.isEmpty()) {
                Label emptyMessage = new Label("⚠ Aucun favori trouvé.");
                emptyMessage.setStyle("-fx-text-fill: gold; -fx-font-size: 16px;");
                favorisTilePane.getChildren().add(emptyMessage);
                return;
            }

            for (Favoris favori : favorisList) {
                VBox favorisCard = new VBox(10);
                favorisCard.setAlignment(Pos.CENTER);
                favorisCard.setStyle(
                        "-fx-border-color: gold; " +
                                "-fx-background-color: black; " +
                                "-fx-text-fill: white; " +
                                "-fx-border-radius: 10px; " +
                                "-fx-padding: 15px; " +
                                "-fx-background-radius: 10px; " +
                                "-fx-effect: dropshadow(three-pass-box, gold, 5, 0, 0, 0);"
                );
                favorisCard.setMinWidth(200);
                favorisCard.setMaxWidth(250);

                Label idLabel = new Label("📌 ID Produit: " + favori.getIdProduit());
                idLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                Label userLabel = new Label("👤 ID Utilisateur: " + favori.getIdUser());
                userLabel.setStyle("-fx-text-fill: white;");

                Label dateLabel = new Label("📅 Date: " + favori.getDate());
                dateLabel.setStyle("-fx-text-fill: white;");

                Button modifyButton = new Button("✏ Modifier");
                modifyButton.setOnAction(event -> openModifierFavoris(favori));
                modifyButton.setStyle("-fx-background-color: gold; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 5px 10px;");

                Button deleteButton = new Button("🗑 Supprimer");
                deleteButton.setOnAction(event -> confirmDeleteFavoris(favori.getIdFavoris()));
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5px 10px;");

                favorisCard.getChildren().addAll(idLabel, userLabel, dateLabel, modifyButton, deleteButton);
                favorisTilePane.getChildren().add(favorisCard);
            }

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
            System.out.println("❌ Erreur lors de l'ouverture de la fenêtre de modification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void confirmDeleteFavoris(int id_favoris) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce favori ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                deleteFavoris(id_favoris);
            }
        });
    }

    private void deleteFavoris(int id_favoris) {
        try {
            favorisService.supprimer(id_favoris);
            System.out.println("🗑 Favori supprimé avec succès !");
            loadFavoris();
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la suppression du favori: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
