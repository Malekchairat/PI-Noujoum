package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import models.Produit;
import services.ServicesCrud;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class afficheproduitcontroller implements Initializable {

    @FXML
    private TilePane productTilePane;  // Doit correspondre à fx:id="productTilePane" dans le FXML

    // Boutons additionnels si vous les utilisez dans le FXML (facultatif)
    @FXML
    private Button ajout;
    @FXML
    private Button update;

    private ServicesCrud service = new ServicesCrud();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (productTilePane == null) {
            System.out.println("⚠️ ERREUR: productTilePane est null ! Vérifiez votre FXML.");
        } else {
            System.out.println("✅ productTilePane correctement injecté.");
            loadProduits();
        }
    }

    // Charge la liste des produits depuis la base de données et les affiche
    public void loadProduits() {
        productTilePane.getChildren().clear();
        List<Produit> produits = service.recuperer();
        for (Produit produit : produits) {
            productTilePane.getChildren().add(createProductCard(produit));
        }
    }

    // Crée une "carte" de produit (VBox) pour l'affichage
    private VBox createProductCard(Produit produit) {
        VBox productCard = new VBox();
        productCard.setSpacing(10);
        productCard.setStyle("-fx-border-color: #FFD700; -fx-border-radius: 15px; -fx-padding: 20px; -fx-background-color: #333333;");
        productCard.setPrefSize(250, 380);

        // Affichage de l'image du produit
        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        if (produit.getImage() != null) {
            try {
                byte[] imageData = produit.getImage().getBytes(1, (int) produit.getImage().length());
                Image img = new Image(new ByteArrayInputStream(imageData));
                imageView.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur chargement image pour " + produit.getNom() + " : " + e.getMessage());
            }
        }

        // Label affichant le nom du produit
        Label nomLabel = new Label("🛍️ " + produit.getNom());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Bouton Supprimer
        Button deleteButton = new Button("❌ Supprimer");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 10px;");
        deleteButton.setOnAction(event -> {
            service.supprimer(produit.getIdproduit());
            loadProduits();
        });

        // Bouton Ajouter aux Favoris
        Button favButton = new Button();
        ImageView heartIcon = new ImageView(new Image(getClass().getResourceAsStream("/heart.png")));
        heartIcon.setFitWidth(20);
        heartIcon.setFitHeight(20);
        favButton.setGraphic(heartIcon);
        favButton.setStyle("-fx-background-color: transparent;");
        favButton.setOnAction(event -> {
            Produit.setProduitId(produit.getIdproduit());
            navigateToAjouterFavoris();
        });

        // Bouton Modifier
        Button updateButton = new Button("✏️ Modifier");
        updateButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-background-radius: 10px;");
        updateButton.setOnAction(event -> {
            Produit.setProduitId(produit.getIdproduit());
            navigateToUpdateProduit();
        });

        // Ajout des éléments dans la carte
        productCard.getChildren().addAll(imageView, nomLabel, favButton, updateButton, deleteButton);
        return productCard;
    }

    // Navigation vers la page Ajouter Favoris
    private void navigateToAjouterFavoris() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterfavoris.fxml"));
            Parent root = loader.load();
            productTilePane.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur navigation vers Ajouter Favoris : " + e.getMessage());
        }
    }

    // Navigation vers la page Modifier Produit
    private void navigateToUpdateProduit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateProduit.fxml"));
            Parent root = loader.load();
            productTilePane.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur navigation vers Modifier Produit : " + e.getMessage());
        }
    }

    // Méthodes pour les boutons "ajout" et "update" du FXML, si utilisés
    public void ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addProduit.fxml"));
            Parent root = loader.load();
            ajout.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur navigation vers Add Produit : " + e.getMessage());
        }
    }

    public void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateProduit.fxml"));
            Parent root = loader.load();
            update.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur navigation vers Update Produit : " + e.getMessage());
        }
    }
}