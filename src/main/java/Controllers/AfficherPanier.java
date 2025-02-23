package Controllers;

import models.Panier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.PanierService;
import javafx.stage.Stage;
import models.Produit;
import services.ServicesCrud;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AfficherPanier implements Initializable {

    @FXML
    private ListView<HBox> panierListView;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> searchCriteriaComboBox;
    @FXML
    private Button close;
    @FXML
    private Button home;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label price;

    private PanierService panierCrud = new PanierService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchCriteriaComboBox.getItems().addAll("Nom", "Catégorie");
        searchCriteriaComboBox.setValue("Nom");

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchPaniers());

        loadUsers();
    }

    @FXML
    public void loadUsers() {
        panierListView.getItems().clear();
        List<Panier> paniers = panierCrud.getCartItems();

        for (Panier panier : paniers) {
            Produit produit = panierCrud.getProductById(panier.getId_produit());
            if (produit == null) {
                System.out.println("Product not found for ID: " + panier.getId_produit());
                continue;
            }
            panierListView.getItems().add(createPanierItem(panier, produit));
        }
        updateTotalPrice();
    }

    @FXML
    private void searchPaniers() {
        String searchTerm = searchTextField.getText().toLowerCase();
        String filter = searchCriteriaComboBox.getValue();

        panierListView.getItems().clear();
        List<Panier> allPaniers = panierCrud.getCartItems();
        List<Panier> filteredPaniers = allPaniers.stream()
                .filter(panier -> {
                    Produit produit = panierCrud.getProductById(panier.getId_produit());
                    if (produit == null) return false;

                    if ("Nom".equals(filter)) {
                        return produit.getNom().toLowerCase().contains(searchTerm);
                    } else if ("Catégorie".equals(filter)) {
                        return produit.getCategorie().name().toLowerCase().contains(searchTerm);
                    }
                    return false;
                })
                .collect(Collectors.toList());

        for (Panier panier : filteredPaniers) {
            Produit produit = panierCrud.getProductById(panier.getId_produit());
            panierListView.getItems().add(createPanierItem(panier, produit));
        }
    }

    private HBox createPanierItem(Panier panier, Produit produit) {
        HBox itemContainer = new HBox(20);
        itemContainer.setStyle("-fx-border-bottom-color: black; -fx-border-bottom-width: 1px; " +
                "-fx-border-style: solid; -fx-padding: 10px; -fx-background-color: #f4f4f4;");
        itemContainer.setPrefSize(600, 100);

        VBox infoContainer = new VBox(5);
        Label nameLabel = new Label("Name: " + produit.getNom());
        Label descriptionLabel = new Label("Description: " + produit.getDescription());
        Label typeLabel = new Label("Type: " + produit.getCategorie().name());
        Label priceLabel = new Label("Price: $" + produit.getPrix());
        Label quantityLabel = new Label("Quantity: " + panier.getNbr_produit());

        ImageView imageView = new ImageView();
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        if (produit.getImage() != null) {
            imageView.setImage(convertBlobToImage(produit.getImage()));
        }

        HBox buttonContainer = new HBox(10);
        Button increaseQtyButton = new Button("+");
        increaseQtyButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        increaseQtyButton.setOnAction(event -> {
            try {
                updateQuantity(panier, 1, quantityLabel);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Button decreaseQtyButton = new Button("-");
        decreaseQtyButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        decreaseQtyButton.setOnAction(event -> {
            try {
                updateQuantity(panier, -1, quantityLabel);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(event -> deletePanier(panier));

        buttonContainer.getChildren().addAll(decreaseQtyButton, increaseQtyButton, deleteButton);
        VBox labelsBox = new VBox(5, nameLabel, descriptionLabel, typeLabel, priceLabel, quantityLabel);
        itemContainer.getChildren().addAll(imageView, labelsBox, buttonContainer);
        return itemContainer;
    }

    private Image convertBlobToImage(Blob blob) {
        try {
            if (blob != null) {
                InputStream inputStream = blob.getBinaryStream();
                return new Image(inputStream);
            }
        } catch (SQLException e) {
            System.out.println("Error converting BLOB to Image: " + e.getMessage());
        }
        return null;
    }

    private void updateTotalPrice() {
        double totalPrice = panierCrud.getCartItems().stream()
                .mapToDouble(panier -> panierCrud.getProductPrice(panier.getId_produit()) * panier.getNbr_produit())
                .sum();
        price.setText(String.format("$%.2f", totalPrice));
    }

    private void deletePanier(Panier panier) {
        panierCrud.removeProductFromCart(panier.getId_produit());
        loadUsers();
    }

    private void updateQuantity(Panier panier, int change, Label quantityLabel) throws SQLException {
        int newQuantity = panier.getNbr_produit() + change;
        if (newQuantity <= 0) {
            deletePanier(panier);
        } else {
            panierCrud.updateCartQuantity(panier.getId_produit(), newQuantity);
            panier.setNbr_produit(newQuantity);
            quantityLabel.setText(String.valueOf(newQuantity));
        }
        updateTotalPrice();
    }
}
