package Controllers;

import models.Panier;
import services.PanierService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ModifierPanier {

    @FXML
    private TextField idpanier, idproduit, iduser, nbrproduit;

    private Panier panier;
    private final PanierService panierService = new PanierService();
    private AfficherPanier afficherPanierController;

    public void setPanier(Panier panier) {
        this.panier = panier;

        idpanier.setText(String.valueOf(panier.getId_panier()));
        idproduit.setText(String.valueOf(panier.getId_produit()));
        iduser.setText(String.valueOf(panier.getId_user()));
        nbrproduit.setText(String.valueOf(panier.getNbr_produit()));
    }

    public void setAfficherPanierController(AfficherPanier afficherPanierController) {
        this.afficherPanierController = afficherPanierController;
    }

    @FXML
    private void enregistrerModification() {
        if (!validateAndConvertFields()) {
            return; // Arrêter si la validation échoue
        }

        try {
            panier.setId_produit(Integer.parseInt(idproduit.getText()));
            panier.setId_user(Integer.parseInt(iduser.getText()));
            panier.setNbr_produit(Integer.parseInt(nbrproduit.getText()));

            panierService.modifier(panier, "ignored_param");

            if (afficherPanierController != null) {
                afficherPanierController.refreshGrid();
            }

            showAlert("Succès", "Modification réussie !", Alert.AlertType.INFORMATION);

            Stage stage = (Stage) idpanier.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de la mise à jour : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateAndConvertFields() {
        if (idproduit.getText().isEmpty() || iduser.getText().isEmpty() || nbrproduit.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            int idProduitInt = Integer.parseInt(idproduit.getText());
            int idUserInt = Integer.parseInt(iduser.getText());
            int nbrProduitInt = Integer.parseInt(nbrproduit.getText());

            if (idProduitInt <= 0 || idUserInt <= 0 || nbrProduitInt <= 0) {
                showAlert("Erreur", "Les valeurs numériques doivent être strictement positives.", Alert.AlertType.ERROR);
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les champs doivent contenir uniquement des nombres valides.", Alert.AlertType.ERROR);
            return false;
        }
    }

    @FXML
    private void fermer() {
        Stage stage = (Stage) idpanier.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
