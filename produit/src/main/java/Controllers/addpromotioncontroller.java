package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import models.Promotion;
import models.Produit;
import services.PromotionCrud;
import services.ServicesCrud;

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
    private ComboBox<String> produitComboBox;

    private final PromotionCrud promotionCrud = new PromotionCrud();
    private final ServicesCrud produitCrud = new ServicesCrud();

    @FXML
    public void initialize() {
        loadProduits();
    }

    @FXML
    void ajout(ActionEvent event) {
        String promo = code.getText().trim();
        String per = pourcentage.getText().trim();
        String date = expiration.getText().trim();
        String produitSelectionne = produitComboBox.getValue();

        if (promo.isEmpty() || per.isEmpty() || date.isEmpty() || produitSelectionne == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis!", AlertType.ERROR);
            return;
        }

        if (promo.length() != 6) {
            showAlert("Erreur", "Le code promo doit contenir exactement 6 caractères!", AlertType.ERROR);
            return;
        }

        float percent;
        try {
            percent = Float.parseFloat(per);
            if (percent < 0 || percent > 100) {
                showAlert("Erreur", "Le pourcentage doit être entre 0 et 100!", AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le pourcentage doit être un nombre valide!", AlertType.ERROR);
            return;
        }

        try {
            LocalDate inputDateObj = LocalDate.parse(date);
            if (inputDateObj.isBefore(LocalDate.now())) {
                showAlert("Erreur", "La date d'expiration ne peut pas être dans le passé!", AlertType.ERROR);
                return;
            }
        } catch (Exception e) {
            showAlert("Erreur", "Format de date invalide! Utilisez YYYY-MM-DD.", AlertType.ERROR);
            return;
        }

        int produitId = Integer.parseInt(produitSelectionne.split(":")[0].trim());

        try {
            Promotion promotion = new Promotion(0, promo, percent, date, produitId);
            promotionCrud.ajouter(promotion);
            showAlert("Succès", "Promotion ajoutée avec succès!", AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout de la promotion: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichePromotion.fxml"));
            Parent root = loader.load();
            afficherpromotioncontroller controller = loader.getController();
            controller.loadPromotions();
            affich.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de l'affichage des promotions: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadProduits() {
        produitComboBox.getItems().clear();
        List<Produit> produits = produitCrud.recupererTous();
        for (Produit produit : produits) {
            produitComboBox.getItems().add(produit.getIdproduit() + ": " + produit.getNom());
        }
    }
}
