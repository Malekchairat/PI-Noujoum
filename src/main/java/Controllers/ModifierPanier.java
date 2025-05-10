package Controllers;

import models.Panier;
import services.PanierService;
import javafx.fxml.FXML;
<<<<<<< HEAD
import javafx.scene.control.Alert;
=======
>>>>>>> origin/integration-branch
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ModifierPanier {

    @FXML
    private TextField idpanier, idproduit, iduser, nbrproduit;

<<<<<<< HEAD
    private Panier panier;
    private final PanierService panierService = new PanierService();
    private AfficherPanier afficherPanierController;

    public void setPanier(Panier panier) {
        this.panier = panier;

=======
    private Panier panier; // Le panier à modifier
    private final PanierService panierService = new PanierService(); // Service pour interagir avec la base de données
    private AfficherPanier afficherPanierController; // Référence au contrôleur principal

    /**
     * Initialise les champs avec les données du panier.
     *
     * @param panier Le panier à modifier.
     */
    public void setPanier(Panier panier) {
        this.panier = panier;

        // Remplir les champs avec les données du panier
>>>>>>> origin/integration-branch
        idpanier.setText(String.valueOf(panier.getId_panier()));
        idproduit.setText(String.valueOf(panier.getId_produit()));
        iduser.setText(String.valueOf(panier.getId_user()));
        nbrproduit.setText(String.valueOf(panier.getNbr_produit()));
    }

<<<<<<< HEAD
=======
    /**
     * Définit le contrôleur AfficherPanier pour rafraîchir la liste après modification.
     *
     * @param afficherPanierController Le contrôleur AfficherPanier.
     */
>>>>>>> origin/integration-branch
    public void setAfficherPanierController(AfficherPanier afficherPanierController) {
        this.afficherPanierController = afficherPanierController;
    }

<<<<<<< HEAD
    @FXML
    private void enregistrerModification() {
        if (!validateAndConvertFields()) {
            return; // Arrêter si la validation échoue
        }

        try {
=======
    /**
     * Enregistre les modifications du panier dans la base de données.
     */
    @FXML
    private void enregistrerModification() {
        try {
            // Mettre à jour les attributs du panier
>>>>>>> origin/integration-branch
            panier.setId_produit(Integer.parseInt(idproduit.getText()));
            panier.setId_user(Integer.parseInt(iduser.getText()));
            panier.setNbr_produit(Integer.parseInt(nbrproduit.getText()));

<<<<<<< HEAD
            // Appel à la méthode modifier avec un seul paramètre panier
            panierService.modifier(panier);  // Suppression du paramètre "ignored_param"

=======
            // Mettre à jour le panier dans la base de données
            panierService.modifier(panier, "ignored_param"); // Utilisation du deuxième paramètre

            // Rafraîchir la liste des paniers dans AfficherPanier
>>>>>>> origin/integration-branch
            if (afficherPanierController != null) {
                afficherPanierController.refreshGrid();
            }

<<<<<<< HEAD
            showAlert("Succès", "Modification réussie !", Alert.AlertType.INFORMATION);

            Stage stage = (Stage) idpanier.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
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

=======
            // Fermer la fenêtre de modification
            Stage stage = (Stage) idpanier.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            System.err.println("Erreur de format : les champs doivent être des nombres valides.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    /**
     * Ferme la fenêtre de modification.
     */
>>>>>>> origin/integration-branch
    @FXML
    private void fermer() {
        Stage stage = (Stage) idpanier.getScene().getWindow();
        stage.close();
    }
<<<<<<< HEAD

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
=======
}
>>>>>>> origin/integration-branch
