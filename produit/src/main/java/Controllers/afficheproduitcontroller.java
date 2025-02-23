package Controllers;

import services.ServicesCrud;
import models.Produit;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class afficheproduitcontroller {

    @FXML
    private Button update, ajout;

    @FXML
    private TilePane promoTilePane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchField;

    @FXML
    private Button homeBtn, productsBtn, claimsBtn, eventsBtn;

    private ServicesCrud service = new ServicesCrud();
    @FXML
    private ImageView logo;


    @FXML
    public void initialize() {
        setupNavigationBar();
        loadProduits("");
        Image logoImage;
        logoImage = new Image(getClass().getResource("/images/njm.png").toExternalForm());

        logo.setImage(logoImage);
    }

    @FXML
    private void goToHome(ActionEvent event) {
        // Charger la page d'accueil
        System.out.println("Aller à Accueil");
    }

    @FXML
    private void goToProducts(ActionEvent event) {
        // Charger la page des produits
        System.out.println("Aller à Produits");
    }

    @FXML
    private void goToReclamations(ActionEvent event) {
        // Charger la page des réclamations
        System.out.println("Aller à Réclamations");
    }

    @FXML
    private void goToEvents(ActionEvent event) {
        // Charger la page des événements
        System.out.println("Aller à Événements");
    }

    @FXML
    private void searchAction(ActionEvent event) {
        String searchText = searchField.getText();
        System.out.println("Recherche : " + searchText);
    }

    private void setupNavigationBar() {
        homeBtn.setOnAction(event -> navigateTo("/home.fxml"));
        productsBtn.setOnAction(event -> navigateTo("/afficheproduit.fxml"));
        claimsBtn.setOnAction(event -> navigateTo("/reclamations.fxml"));
        eventsBtn.setOnAction(event -> navigateTo("/evenements.fxml"));
    }

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            homeBtn.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur de navigation : " + e.getMessage());
        }
    }

    @FXML
    public void searchProducts() {
        loadProduits(searchField.getText().trim());
    }

    public void loadProduits(String filter) {
        promoTilePane.getChildren().clear();
        promoTilePane.setHgap(50);
        promoTilePane.setVgap(120);

        try {
            List<Produit> produits = service.recupererTous();
            if (!filter.isEmpty()) {
                produits = produits.stream()
                        .filter(p -> p.getNom().toLowerCase().contains(filter.toLowerCase()))
                        .collect(Collectors.toList());
            }

            for (Produit produit : produits) {
                VBox productCard = new VBox(5);
                productCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4; -fx-border-radius: 10px;");
                productCard.setPrefSize(200, 300);

                ImageView imageView = new ImageView();
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                try {
                    if (produit.getImage() != null) {
                        byte[] imageData = produit.getImage().getBytes(1, (int) produit.getImage().length());
                        Image image = new Image(new ByteArrayInputStream(imageData));
                        imageView.setImage(image);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                productCard.getChildren().addAll(
                        imageView,
                        new Label("Nom: " + produit.getNom()),
                        new Label("Prix: " + produit.getPrix() + " €"),
                        new Label("Catégorie: " + produit.getCategorie()),
                        new Label("Disponibilité: " + produit.getDisponibilite())
                );

                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    service.supprimer(produit.getIdproduit());
                    loadProduits(filter);
                });

                productCard.getChildren().add(deleteButton);
                promoTilePane.getChildren().add(productCard);
            }
            scrollPane.setContent(promoTilePane);
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des produits : " + e.getMessage());
        }
    }

    @FXML
    void ajout(ActionEvent event) { navigateTo("/addProduit.fxml"); }

    @FXML
    void update(ActionEvent event) { navigateTo("/updateProduit.fxml"); }

    public void loadProduits() {
        loadProduits("");
    }
}
