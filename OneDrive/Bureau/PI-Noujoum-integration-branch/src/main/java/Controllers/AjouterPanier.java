package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Panier;
import services.PanierService;

import java.io.IOException;

public class AjouterPanier {

    @FXML
    private TextField idProduit, nbrProduit, idUser;

    @FXML
    private void ajout(ActionEvent event) {
        if (!validateAndConvertFields()) {
            return; // Stopper si la validation échoue
        }

        try {
            int idProduitInt = Integer.parseInt(idProduit.getText());
            int nbrProduitInt = Integer.parseInt(nbrProduit.getText());
            int idUserInt = Integer.parseInt(idUser.getText());

            PanierService panierService = new PanierService();

            // Debugging: Afficher les valeurs récupérées
            System.out.println("ID Utilisateur saisi : " + idUserInt);
            System.out.println("ID Produit saisi : " + idProduitInt);

            // Vérification si l'utilisateur et le produit existent
            if (!panierService.userExists(idUserInt)) {
                System.out.println("Utilisateur non trouvé !");
                showAlert("Erreur", "L'utilisateur avec ID " + idUserInt + " n'existe pas.", Alert.AlertType.ERROR);
                return;
            } else {
                System.out.println("Utilisateur trouvé !");
            }

            if (!panierService.produitExists(idProduitInt)) {
                System.out.println("Produit non trouvé !");
                showAlert("Erreur", "Le produit avec ID " + idProduitInt + " n'existe pas.", Alert.AlertType.ERROR);
                return;
            } else {
                System.out.println("Produit trouvé !");
            }

            // Ajout du panier
            Panier panier = new Panier(idProduitInt, idUserInt, nbrProduitInt);
            panierService.ajouter(panier);
            showAlert("Succès", "Panier ajouté avec succès !", Alert.AlertType.INFORMATION);

            // Effacer les champs après ajout
            idProduit.clear();
            nbrProduit.clear();
            idUser.clear();

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateAndConvertFields() {
        if (idProduit.getText().isEmpty() || nbrProduit.getText().isEmpty() || idUser.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            int idProduitInt = Integer.parseInt(idProduit.getText());
            int nbrProduitInt = Integer.parseInt(nbrProduit.getText());
            int idUserInt = Integer.parseInt(idUser.getText());

            if (idProduitInt <= 0 || nbrProduitInt <= 0 || idUserInt <= 0) {
                showAlert("Erreur", "Les valeurs numériques doivent être strictement positives.", Alert.AlertType.ERROR);
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Tous les champs doivent contenir uniquement des nombres entiers valides.", Alert.AlertType.ERROR);
            return false;
        }
    }

    @FXML
    void afficherPaniers(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPanier.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Liste des Paniers");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AfficherPanier.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void revenirAjouterCommande(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCommande.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Ajouter Commande");
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) idProduit.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AjouterCommande.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
