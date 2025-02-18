package Controllers;

import models.Panier;
import services.PanierService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        try {
            panier.setId_produit(Integer.parseInt(idproduit.getText()));
            panier.setId_user(Integer.parseInt(iduser.getText()));
            panier.setNbr_produit(Integer.parseInt(nbrproduit.getText()));

            panierService.modifier(panier); // ✅ Correction ici !

            if (afficherPanierController != null) {
                afficherPanierController.refreshGrid();
            }

            Stage stage = (Stage) idpanier.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            System.err.println("Erreur de format : les champs doivent être des nombres valides.");
        }
    }

    @FXML
    private void fermer() {
        Stage stage = (Stage) idpanier.getScene().getWindow();
        stage.close();
    }
}
