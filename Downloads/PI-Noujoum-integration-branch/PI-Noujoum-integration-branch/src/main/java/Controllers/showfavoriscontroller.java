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
        loadFavoris(); // Charger les favoris au démarrage
    }

    public void loadFavoris() {
        if (favorisTilePane == null || scrollPane == null) {
            System.out.println("❌ ERREUR: Un composant FXML est NULL !");
            return;
        }

        favorisTilePane.getChildren().clear();
        favorisTilePane.setHgap(30);
        favorisTilePane.setVgap(30);

        try {
            List<Favoris> favorisList = favorisService.recuperer(); // Utilisation de la bonne méthode

            for (Favoris favori : favorisList) {
                VBox favorisCard = new VBox(8);
                favorisCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4; -fx-border-radius: 10px;");
                favorisCard.setMaxWidth(250); // Allow dynamic resizing

                Label idLabel = new Label("📌 ID Produit: " + favori.getIdProduit());
                Label userLabel = new Label("👤 ID Utilisateur: " + favori.getIdUser());
                Label dateLabel = new Label("📅 Date: " + favori.getDate());

                Button modifyButton = new Button("✏ Modifier");
                modifyButton.getStyleClass().add("modify-button"); // CSS class
                modifyButton.setOnAction(event -> openModifierFavoris(favori));

                Button deleteButton = new Button("🗑 Supprimer");
                deleteButton.getStyleClass().add("delete-button"); // CSS class
                deleteButton.setOnAction(event -> confirmDeleteFavoris(favori.getIdFavoris()));

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

            // Récupérer le contrôleur et initialiser les données
            Controllers.modifierfavoriscontroller controller = loader.getController();
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

    private void confirmDeleteFavoris(int id_favoris) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce favori ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                deleteFavoris(id_favoris);
            }
        });
    }

    private void deleteFavoris(int id_favoris) {
        try {
            favorisService.supprimer(id_favoris); // Suppression via service
            System.out.println("🗑 Favori supprimé avec succès !");
            loadFavoris(); // Reload favoris list
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la suppression du favori: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
