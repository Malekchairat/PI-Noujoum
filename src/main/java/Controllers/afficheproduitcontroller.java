package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
<<<<<<< HEAD
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
=======
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
>>>>>>> origin/GestionCommande
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
<<<<<<< HEAD
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
=======
import javafx.stage.Stage;
import models.Panier;
import models.Produit;
import services.PanierService;
import services.ServicesCrud;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class afficheproduitcontroller {
    @FXML
    private Button panier;
    @FXML
    private Button update;

    @FXML
    private Button ajout;

    @FXML
    private TilePane promoTilePane;

    @FXML
    private ScrollPane scrollPane;

    private ServicesCrud service = new ServicesCrud();
    private static Set<Integer> addedProducts = new HashSet<>(); // To track added products

    @FXML
    public void initialize() {
        // Configuration du ScrollPane
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(promoTilePane);

        loadProduits();
    }


    @FXML
    public void loadProduits() {
        promoTilePane.getChildren().clear();
        promoTilePane.setHgap(20);  // Espacement horizontal
        promoTilePane.setVgap(20);  // Espacement vertical
        promoTilePane.setPrefColumns(3); // 3 cartes par ligne
        promoTilePane.setStyle("-fx-background-color: #121212;");  // Fond sombre pour toute la page

        try {
            List<Produit> produits = service.recuperer();
            if (produits.isEmpty()) {
                System.out.println("Aucun produit trouvé.");
                return;
            }

            for (Produit produit : produits) {
                VBox productCard = new VBox(10);
                productCard.setStyle("-fx-border-color: #FFD700; -fx-border-radius: 15px; -fx-padding: 20px; -fx-background-color: #333333;");
                productCard.setPrefSize(250, 350);
                productCard.setOnMouseEntered(event -> productCard.setStyle("-fx-border-color: #FFD700; -fx-border-radius: 15px; -fx-padding: 20px; -fx-background-color: #444444;")); // Effet hover
                productCard.setOnMouseExited(event -> productCard.setStyle("-fx-border-color: #FFD700; -fx-border-radius: 15px; -fx-padding: 20px; -fx-background-color: #333333;"));

                // Affichage de l'image
                ImageView imageView = new ImageView();
                imageView.setFitHeight(120);
                imageView.setFitWidth(120);
                try {
                    if (produit.getImage() != null) {
                        byte[] imageData = produit.getImage().getBytes(1, (int) produit.getImage().length());
                        Image image = new Image(new ByteArrayInputStream(imageData));
                        imageView.setImage(image);
                    }
                } catch (SQLException e) {
                    System.out.println("Erreur lors du chargement de l'image pour le produit " + produit.getNom() + " : " + e.getMessage());
                }

                // Ajouter l'image et les labels avec texte blanc
                imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255, 255, 255, 0.5), 10, 0.0, 0.0, 0.0);");
                productCard.getChildren().add(imageView);
                productCard.getChildren().add(new Label("🔢 ID: " + produit.getIdproduit()));
                productCard.getChildren().add(new Label("🛍️ Nom: " + produit.getNom()));
                productCard.getChildren().add(new Label("📜 Description: " + produit.getDescription()));
                productCard.getChildren().add(new Label("💡 Catégorie: " + produit.getCategorie()));
                productCard.getChildren().add(new Label("💵 Prix: " + produit.getPrix() + " DT"));
                productCard.getChildren().add(new Label("📅 Disponibilité: " + produit.getDisponibilite()));

                // Style pour les Labels
                for (javafx.scene.Node node : productCard.getChildren()) {
                    if (node instanceof Label) {
                        Label label = (Label) node;
                        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Arial';");
                    }
                }

                // Bouton de suppression
                Button deleteButton = new Button("❌ Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10px;");
                deleteButton.setOnAction(event -> {
                    service.supprimer(produit.getIdproduit());
                    loadProduits(); // Rechargement après suppression
                });

                // Bouton "Ajouter au panier"
                Button addToCartButton = new Button("🛒 Ajouter au panier");
                addToCartButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10px;");
                addToCartButton.setOnAction(event -> addToCart(produit));


                productCard.getChildren().addAll(deleteButton, addToCartButton);

                // Ajouter la carte dans le TilePane
                promoTilePane.getChildren().add(productCard);
            }

            promoTilePane.requestLayout();
            scrollPane.requestLayout();

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des produits : " + e.getMessage());
        }
    }

    @FXML
    void ajout(ActionEvent event) {
>>>>>>> origin/GestionCommande
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addProduit.fxml"));
            Parent root = loader.load();
            ajout.getScene().setRoot(root);
        } catch (IOException e) {
<<<<<<< HEAD
            System.out.println("Erreur navigation vers Add Produit : " + e.getMessage());
        }
    }

    public void update(ActionEvent event) {
=======
            System.out.println("Erreur lors du chargement de l'interface d'ajout : " + e.getMessage());
        }
    }
    private void addToCart(Produit produit) {
        PanierService panierCrud = new PanierService();
        Panier Panier = new Panier(); // Create an instance of PanierCrud
        panierCrud.addOrUpdateCartItem(produit.getIdproduit(), 1, 1); // Call the method on the instance
        System.out.println("Product added to cart: " + produit.getNom());
    }

    @FXML
    void update(ActionEvent event) {
>>>>>>> origin/GestionCommande
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateProduit.fxml"));
            Parent root = loader.load();
            update.getScene().setRoot(root);
        } catch (IOException e) {
<<<<<<< HEAD
            System.out.println("Erreur navigation vers Update Produit : " + e.getMessage());
        }
    }
=======
            System.out.println("Erreur lors du chargement de l'interface de mise à jour : " + e.getMessage());
        }
    }


    @FXML
    void panier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPanier.fxml"));
            Parent root = loader.load();
            AfficherPanier affichercController = loader.getController();
            if (affichercController != null) {
                affichercController.loadUsers();
            } else {
                System.out.println("Controller for AfficherPanier not loaded properly.");
            }
            panier.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error loading the FXML: " + e.getMessage());
        }

    }
>>>>>>> origin/GestionCommande
}