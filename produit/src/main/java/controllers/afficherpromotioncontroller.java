package controllers;
import controller.PromotionCrud;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import entity.Promotion;

import java.io.IOException;
import java.util.List;

public class afficherpromotioncontroller {

    @FXML
    private Button update;

    @FXML
    private Button ajout;

    @FXML
    private TilePane promoTilePane;

    @FXML
    private ScrollPane scrollPane;

    private PromotionCrud cc = new PromotionCrud();

    @FXML
    public void loadUsers() {
        promoTilePane.getChildren().clear();
        promoTilePane.setHgap(50);
        promoTilePane.setVgap(120);

        try {
            List<Promotion> promotions = cc.findAll();
            if (promotions.isEmpty()) {
                System.out.println("Aucune promotion trouvée.");
                return;
            }

            for (Promotion promo : promotions) {
                VBox couponCard = new VBox(5);
                couponCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4; -fx-border-radius: 10px;");
                couponCard.setPrefSize(200, 250);

                couponCard.getChildren().addAll(
                        new Label("ID: " + promo.getIdpromotion()),
                        new Label("Code: " + promo.getCode()),
                        new Label("Réduction: " + promo.getPourcentage() + "%"),
                        new Label("Expiration: " + promo.getExpiration())
                );

                // Bouton de suppression
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    cc.delete(promo.getIdpromotion());
                    loadUsers();
                });

                couponCard.getChildren().add(deleteButton);
                promoTilePane.getChildren().add(couponCard);
            }

            scrollPane.setContent(promoTilePane);

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des promotions : " + e.getMessage());
        }
    }



    private void deleteCoupon(Promotion prom) {
        PromotionCrud cc = new PromotionCrud(); // Create an instance of CouponCrud
        cc.delete(prom.getIdpromotion()); // Call the delete method on the instance
        loadUsers(); // Refresh the data after deletion
    }

    @FXML
    void ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPromotion.fxml"));
            Parent root = loader.load();
            ajout.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error loading the FXML: " + e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatePromotion.fxml"));
            Parent root = loader.load();
            update.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error loading the FXML: " + e.getMessage());
        }

}}
