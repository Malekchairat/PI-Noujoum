package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.Produit;
import models.Promotion;
import services.PromotionCrud;
import services.ServicesCrud;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class updatepromotioncontroller {

    @FXML
    private Button show;
    @FXML
    private TextField id;

    @FXML
    private TextField code;

    @FXML
    private TextField pourcentage;

    @FXML
    private TextField expiration;

    @FXML
    private ComboBox<Produit> produitComboBox;  // ComboBox pour sélectionner le produit

    private ServicesCrud serviceCrud = new ServicesCrud();

    @FXML
    public void initialize() {
        loadProducts();
    }

    private void loadProducts() {
        List<Produit> produits = serviceCrud.recuperer();
        System.out.println("Produits récupérés : " + produits.size());

        for (Produit produit : produits) {
            System.out.println("Produit: " + produit.getNom());
            produitComboBox.getItems().add(produit);
        }
    }
    @FXML
    void updatec(ActionEvent event) throws SQLException {
        // Récupérer les données des champs
        String couponidStr = id.getText().trim();
        String couponpromo = code.getText().trim();
        String coupondiscountStr = pourcentage.getText().trim();
        String couponexpiry = expiration.getText().trim();

        // Valider les champs vides
        if (couponidStr.isEmpty() || couponpromo.isEmpty() || coupondiscountStr.isEmpty() || couponexpiry.isEmpty()) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        // Convertir le couponid en entier
        int couponid;
        try {
            couponid = Integer.parseInt(couponidStr);
            if (couponid < 0 || couponid > 99999) {
                showAlert("Error", "Coupon's ID must be between 0 and 99999!", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Coupon's ID must be a valid number!", Alert.AlertType.ERROR);
            return;
        }

        // Valider le code promo
        if (couponpromo.length() != 6) {
            showAlert("Error", "Promo code must be exactly 6 characters long!", Alert.AlertType.ERROR);
            return;
        }

        // Valider le pourcentage de réduction
        float coupondiscount;
        try {
            coupondiscount = Float.parseFloat(coupondiscountStr);
            if (coupondiscount < 0 || coupondiscount > 100) {
                showAlert("Error", "Promo percentage must be between 0 and 100!", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Promo percentage must be a valid number!", Alert.AlertType.ERROR);
            return;
        }

        // Valider la date d'expiration
        LocalDate inputDate;
        try {
            if (!couponexpiry.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) {
                showAlert("Error", "Invalid date format! Use YYYY-MM-DD.", Alert.AlertType.ERROR);
                return;
            }

            inputDate = LocalDate.parse(couponexpiry);
            if (inputDate.isBefore(LocalDate.now())) {
                showAlert("Error", "Expiry date cannot be in the past!", Alert.AlertType.ERROR);
                return;
            }

        } catch (Exception e) {
            showAlert("Error", "Invalid date input!", Alert.AlertType.ERROR);
            return;
        }

        // Récupérer l'ID du produit sélectionné dans le ComboBox
        Produit selectedProduit = produitComboBox.getValue();
        int produitid = (selectedProduit != null) ? selectedProduit.getIdproduit() : -1;  // -1 si aucun produit sélectionné

        // Si aucun produit n'est sélectionné, vous pouvez afficher un message d'erreur
        if (produitid == -1) {
            showAlert("Error", "Please select a product!", Alert.AlertType.ERROR);
            return;
        }

        // Créer un objet Promotion avec les données
        Promotion updatedPromotion = new Promotion(couponid, couponpromo, coupondiscount, couponexpiry, produitid);
        PromotionCrud cc = new PromotionCrud();
        cc.modifier(updatedPromotion);

        /*if (updateSuccess) {
            showAlert("Success", "Coupon updated successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Error updating promotion.", Alert.AlertType.ERROR);
        }*/
    }
    @FXML
    void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichePromotion.fxml"));
            Parent root = loader.load();
            afficherpromotioncontroller affichercouponController = loader.getController();
            affichercouponController.loadPromotions();
            show.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
