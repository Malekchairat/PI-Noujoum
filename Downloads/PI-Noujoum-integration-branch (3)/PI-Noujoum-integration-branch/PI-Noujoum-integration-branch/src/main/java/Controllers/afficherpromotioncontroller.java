package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import models.Promotion;
import services.PromotionCrud;

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
    public void initialize() {
        // Configuration du ScrollPane
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Associer le contenu du ScrollPane à promoTilePane
        scrollPane.setContent(promoTilePane);

        loadPromotions();
    }

    @FXML
    public void loadPromotions() {
        promoTilePane.getChildren().clear();
        promoTilePane.setHgap(50);
        promoTilePane.setVgap(20);
        promoTilePane.setPrefColumns(3); // Afficher 3 colonnes maximum

        try {
            List<Promotion> promotions = cc.recuperer();
            if (promotions.isEmpty()) {
                showAlert("Information", "Aucune promotion trouvée.", AlertType.INFORMATION);
                return;
            }

            for (Promotion promo : promotions) {
                VBox couponCard = new VBox(5);
                couponCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4; -fx-border-radius: 10px;");
                couponCard.setPrefSize(200, 250);

                // Ajouter les informations de la promotion et le produit associé
                couponCard.getChildren().addAll(
                        new Label("ID: " + promo.getIdpromotion()),
                        new Label("Code: " + promo.getCode()),
                        new Label("Réduction: " + promo.getPourcentage() + "%"),
                        new Label("Expiration: " + promo.getExpiration()),
                        new Label("Produit: " + promo.getProduit())
                );

                // Bouton de suppression avec confirmation
                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> showConfirmationAlert(promo));

                couponCard.getChildren().add(deleteButton);
                promoTilePane.getChildren().add(couponCard);
            }

            // Mise à jour du layout
            promoTilePane.requestLayout();
            scrollPane.requestLayout();

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des promotions : " + e.getMessage());
            showAlert("Erreur", "Une erreur est survenue lors du chargement des promotions.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmationAlert(Promotion promo) {
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette promotion ?");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteCoupon(promo);
            }
        });
    }

    private void deleteCoupon(Promotion promo) {
        try {
            cc.supprimer(promo.getIdpromotion());
            loadPromotions();
            showAlert("Succès", "Promotion supprimée avec succès.", AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la suppression de la promotion.", AlertType.ERROR);
        }
    }

    @FXML
    void ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPromotion.fxml"));
            Parent root = loader.load();
            ajout.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de l'interface d'ajout.", AlertType.ERROR);
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatePromotion.fxml"));
            Parent root = loader.load();
            update.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de l'interface de mise à jour.", AlertType.ERROR);
        }
    }
}
