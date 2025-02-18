package Controllers;

import models.Commande;
import services.CommandeService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ModifierCommande {

    @FXML

    private TextField txtIdCommande, txtIdUser, txtIdPanier, txtRue, txtVille, txtCodePostal, txtEtat, txtMontant, txtMethodePaiement;

    private Commande commande;
    private final CommandeService commandeService = new CommandeService();
    private AfficherCommande afficherCommandeController; // Référence au contrôleur principal

    public void setCommande(Commande commande) {
        this.commande = commande;
        txtRue.setText(commande.getRue());
        txtVille.setText(commande.getVille());
        txtCodePostal.setText(commande.getCode_postal());
        txtEtat.setText(commande.getEtat());
        txtMontant.setText(String.valueOf(commande.getMontant_total()));
        txtMethodePaiement.setText(commande.getMethodePaiment());
    }

    public void setAfficherCommandeController(AfficherCommande controller) {
        this.afficherCommandeController = controller;
    }

    @FXML
    private void enregistrerModification() {

            // Mise à jour des valeurs de l'objet Commande
            commande.setRue(txtRue.getText());
            commande.setVille(txtVille.getText());
            commande.setCode_postal(txtCodePostal.getText());
            commande.setEtat(txtEtat.getText());
            commande.setMontant_total(Float.parseFloat(txtMontant.getText()));
            commande.setMethodePaiment(txtMethodePaiement.getText());

            // Mise à jour dans la base de données
            commandeService.modifier(commande);

            // Rafraîchir la liste des commandes
            if (afficherCommandeController != null) {
                afficherCommandeController.refreshGrid();
            }

            // Fermer la fenêtre de modification
            Stage stage = (Stage) txtRue.getScene().getWindow();
            stage.close();


    }
    @FXML
    private void fermer() {
        Stage stage = (Stage) txtRue.getScene().getWindow();
        stage.close();
    }

}
