package controllers;

import controller.ServicesCrud;
import entity.Produit;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

public class afficheproduitcontroller {

    @FXML
    private Button update;

    @FXML
    private Button ajout;

    @FXML
    private TilePane promoTilePane;

    @FXML
    private ScrollPane scrollPane;

    private ServicesCrud service = new ServicesCrud();

    @FXML
    public void loadProduits() {
        promoTilePane.getChildren().clear();
        promoTilePane.setHgap(50);
        promoTilePane.setVgap(120);

        try {
            List<Produit> produits = service.findAll();
            if (produits.isEmpty()) {
                System.out.println("Aucun produit trouvé.");
                return;
            }

            for (Produit produit : produits) {
                VBox productCard = new VBox(5);
                productCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4; -fx-border-radius: 10px;");
                productCard.setPrefSize(200, 300);

                // Affichage de l'image
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
                        new Label("ID: " + produit.getIdproduit()),
                        new Label("Nom: " + produit.getNom()),
                        new Label("Description: " + produit.getDescription()),
                        new Label("Catégorie: " + produit.getCategorie()),
                        new Label("Prix: " + produit.getPrix() + " €"),
                        new Label("Disponibilité: " + produit.getDisponibilite())
                );

                // Bouton de suppression
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    service.delete(produit);
                    loadProduits();
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
    void ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addProduit.fxml"));
            Parent root = loader.load();
            ajout.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'interface d'ajout : " + e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateProduit.fxml"));
            Parent root = loader.load();
            update.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'interface de mise à jour : " + e.getMessage());
        }
    }

}
