package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.time.LocalDate;


import entity.Promotion;
import controller.PromotionCrud;

public class addpromotioncontroller {

    @FXML
    private Button update;
    @FXML
    private Button affich;


    @FXML
    private TextField code;

    @FXML
    private TextField pourcentage;

    @FXML
    private TextField expiration;


    @FXML
    void ajout(ActionEvent event) {
        // Trim input values to avoid unnecessary spaces
        String promo = code.getText().trim();
        String per = pourcentage.getText().trim();
        String date = expiration.getText().trim();

        // Check if fields are empty
        if (promo.isEmpty() || per.isEmpty() || date.isEmpty()) {
            showAlert("Error", "All fields must be filled!", AlertType.ERROR);
            return;
        }

        // Fix: Ensure promo code is exactly 6 characters long
        if (promo.length() != 6) {
            showAlert("Error", "Promo code must be exactly 6 characters long!", AlertType.ERROR);
            return;
        }

        // Validate percentage input
        float percent;
        try {
            percent = Float.parseFloat(per);
            if (percent < 0 || percent > 100) {
                showAlert("Error", "Promo percentage must be between 0 and 100!", AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Promo percentage must be a valid number!", AlertType.ERROR);
            return;
        }

        // Validate expiry date
        LocalDate inputDate; // Declare outside try-catch to ensure scope
        try {
            // Check format (YYYY-MM-DD)
            if (!date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) {
                showAlert("Error", "Invalid date format! Use YYYY-MM-DD.", AlertType.ERROR);
                return;
            }

            // Prevent past dates
            LocalDate inputDateObj = LocalDate.parse(date);
            if (inputDateObj.isBefore(LocalDate.now())) {
                showAlert("Error", "Expiry date cannot be in the past!", AlertType.ERROR);
                return;
            }

        } catch (Exception e) {
            showAlert("Error", "Invalid date input!", AlertType.ERROR);
            return;
        }

        // Now everything is validated, create the Coupon object with String date
        Promotion c = new Promotion(1,promo, percent, date); // Pass `date` as String

        PromotionCrud cc = new PromotionCrud();
        cc.add(c);

        showAlert("Success", "Coupon added successfully!", AlertType.INFORMATION);

    }


    @FXML
    void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichePromotion.fxml"));
            Parent root = loader.load();
            afficherpromotioncontroller controller = loader.getController();
            controller.loadUsers();
            affich.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur chargement afficherpromotion.fxml : " + e.getMessage());
        }
    }

    /*
    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/update_coupon.fxml"));
            Parent root = loader.load();
            update.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error loading the FXML: " + e.getMessage());
        }*/




    // Helper method to show alerts
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}