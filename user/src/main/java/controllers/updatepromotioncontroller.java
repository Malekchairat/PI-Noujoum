package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import entity.Promotion;
import controller.PromotionCrud;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class updatepromotioncontroller {

    @FXML
    private TextField id;

    @FXML
    private TextField code;


    @FXML
    private TextField pourcentage;

    @FXML
    private TextField expiration;

    @FXML
    private Button show;

    //@FXML
    //private Button ajout;

    @FXML
    void updatec(ActionEvent event) throws SQLException {
        // Trim input values
        String couponidStr = id.getText().trim();
        String couponpromo = code.getText().trim();
        String coupondiscountStr = pourcentage.getText().trim();
        String couponexpiry = expiration.getText().trim();

        // Validate empty fields
        if (couponidStr.isEmpty() || couponpromo.isEmpty() || coupondiscountStr.isEmpty() || couponexpiry.isEmpty()) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        // Validate coupon ID (convert to int)
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

        // Validate promo code length (must be exactly 6 characters)
        if (couponpromo.length() != 6) {
            showAlert("Error", "Promo code must be exactly 6 characters long!", Alert.AlertType.ERROR);
            return;
        }

        // Validate discount percentage (convert to float)
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

        // Validate expiry date format
        LocalDate inputDate;
        try {
            // Ensure format (YYYY-MM-DD)
            if (!couponexpiry.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) {
                showAlert("Error", "Invalid date format! Use YYYY-MM-DD.", Alert.AlertType.ERROR);
                return;
            }

            // Prevent past expiry dates
            inputDate = LocalDate.parse(couponexpiry);
            if (inputDate.isBefore(LocalDate.now())) {
                showAlert("Error", "Expiry date cannot be in the past!", Alert.AlertType.ERROR);
                return;
            }

        } catch (Exception e) {
            showAlert("Error", "Invalid date input!", Alert.AlertType.ERROR);
            return;
        }

        // Create Coupon object with correct data types
        Promotion c = new Promotion(couponid, couponpromo, coupondiscount, couponexpiry);
        PromotionCrud cc = new PromotionCrud();
        cc.update(c);

        showAlert("Success", "Coupon updated successfully!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichePromotion.fxml"));
            Parent root = loader.load();
            afficherpromotioncontroller affichercouponController = loader.getController();
            affichercouponController.loadUsers();
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
