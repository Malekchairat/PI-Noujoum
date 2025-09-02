package Controllers;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.List;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import models.Produit;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import services.FavorisService;
import services.UserSession;


import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.List;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import models.Produit;
import services.UserSession;

public class showfavoriscontroller {

    @FXML
    private TilePane favorisTilePane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox menuLateral;

    private final FavorisService favorisService = new FavorisService();

    @FXML
    public void initialize() {
        System.out.println("🔄 Initialisation du contrôleur ShowFavorisController");

        if (favorisTilePane == null || scrollPane == null) {
            System.out.println("❌ ERREUR: Un élément est NULL ! Vérifie ton fichier FXML.");
            return;
        }

        setupScrollPane();
        setupTilePane();
        loadFavoris(); // Call without parameters
    }

    private void setupScrollPane() {
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: black; -fx-border-color: gold;");
    }

    private void setupTilePane() {
        favorisTilePane.setHgap(20);
        favorisTilePane.setVgap(20);
        favorisTilePane.setPrefColumns(3);
        favorisTilePane.setStyle("-fx-background-color: black; -fx-padding: 20px;");
    }

    public void loadFavoris() {
        int userId = UserSession.getUserId();  // Retrieve the user ID from the session-like context

        if (userId == 0) {  // If no user is logged in (user ID is 0), handle the error
            System.out.println("❌ Aucun utilisateur connecté !");
            return;
        }

        favorisTilePane.getChildren().clear();
        favorisTilePane.setHgap(30);
        favorisTilePane.setVgap(50);

        try {
            List<Produit> produits = favorisService.recupererFavoris(userId);

            for (Produit produit : produits) {
                VBox productCard = createProductCard(produit);
                favorisTilePane.getChildren().add(productCard);
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur lors du chargement des favoris : " + e.getMessage());
        }
    }

    private VBox createProductCard(Produit produit) {
        VBox productCard = new VBox(10);
        productCard.getStyleClass().add("product-card");
        productCard.setPrefSize(250, 350);

        ImageView imageView = createProductImageView(produit);

        Label title = new Label(produit.getNom());
        title.getStyleClass().add("product-title");

        Label priceLabel = new Label(produit.getPrix() + " €");
        priceLabel.getStyleClass().add("product-price");

        Label categoryLabel = new Label("Catégorie: " + produit.getCategorie());
        categoryLabel.getStyleClass().add("product-category");

        Label availabilityLabel = new Label("Disponibilité: " + produit.getDisponibilite());
        availabilityLabel.getStyleClass().add("product-availability");

        Button removeButton = new Button("🗑️ Supprimer des favoris");
        removeButton.getStyleClass().add("delete-button");
        removeButton.setOnAction(event -> {
            favorisService.supprimerFavori(UserSession.getUserId(), produit.getIdproduit());
            loadFavoris(); // Reload the favorites after removal
        });

        productCard.getChildren().addAll(imageView, title, priceLabel, categoryLabel, availabilityLabel, removeButton);
        return productCard;
    }

    private ImageView createProductImageView(Produit produit) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        imageView.getStyleClass().add("product-image");

        if (produit.getImage() != null) {
            try {
                byte[] imageData = produit.getImage().getBytes(1, (int) produit.getImage().length());
                Image image = new Image(new ByteArrayInputStream(imageData));
                imageView.setImage(image);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return imageView;
    }

    @FXML
    private void goToWishlist(ActionEvent event) throws IOException {
        navigateTo(event, "/afficherfavoris.fxml");
    }

    @FXML
    private void goToCart(ActionEvent event) throws IOException {
        navigateTo(event, "/AfficherPanier.fxml");
    }

    @FXML
    private void goToHome(ActionEvent event) throws IOException {
        navigateTo(event, "/frontoffice.fxml");
    }

    @FXML
    private void goToReclamations(ActionEvent event) {
        System.out.println("Aller à Réclamations");
    }

    @FXML
    private void goToProducts(ActionEvent event) throws IOException {
        navigateTo(event, "/afficheproduitf.fxml");
    }

    @FXML
    private void goToEvents(ActionEvent event) throws IOException {
        navigateTo(event, "/afficherEvenement.fxml");
    }

    private void navigateTo(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
