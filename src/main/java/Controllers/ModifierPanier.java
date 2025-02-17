package Controllers;

import models.Panier;
import services.PanierService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ModifierPanier {

    @FXML
    private TextField idpanier, idproduit, iduser, nbrproduit;

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
        idpanier.setText(String.valueOf(panier.getId_panier()));
        idproduit.setText(String.valueOf(panier.getId_produit()));
        iduser.setText(String.valueOf(panier.getId_user()));
        nbrproduit.setText(String.valueOf(panier.getNbr_produit()));
    }

    /**
     * Définit le contrôleur AfficherPanier pour rafraîchir la liste après modification.
     *
     * @param afficherPanierController Le contrôleur AfficherPanier.
     */
    public void setAfficherPanierController(AfficherPanier afficherPanierController) {
        this.afficherPanierController = afficherPanierController;
    }

    /**
     * Enregistre les modifications du panier dans la base de données.
     */
    @FXML
    private void enregistrerModification() {
        try {
            // Mettre à jour les attributs du panier
            panier.setId_produit(Integer.parseInt(idproduit.getText()));
            panier.setId_user(Integer.parseInt(iduser.getText()));
            panier.setNbr_produit(Integer.parseInt(nbrproduit.getText()));

            // Mettre à jour le panier dans la base de données
            panierService.modifier(panier, "ignored_param"); // Utilisation du deuxième paramètre

            // Rafraîchir la liste des paniers dans AfficherPanier
            if (afficherPanierController != null) {
                afficherPanierController.refreshGrid();
            }

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
    @FXML
    private void fermer() {
        Stage stage = (Stage) idpanier.getScene().getWindow();
        stage.close();
    }
}