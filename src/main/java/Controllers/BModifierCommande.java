package Controllers;

import models.Commande;
import services.CommandeService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox; // Importer ComboBox


import java.sql.SQLException;

public class BModifierCommande {

    @FXML
    private TextField txtIdCommande, txtIdUser, txtIdPanier, txtRue, txtVille, txtCodePostal, txtEtat, txtMontant;

    @FXML
    private ComboBox<String> txtMethodePaiement; // Changer ici

    private Commande commande;
    private final CommandeService commandeService = new CommandeService();
    private BAfficherCommande BafficherCommandeController; // Référence au contrôleur principal

    public void setCommande(Commande commande) {
        this.commande = commande;
        txtIdCommande.setVisible(false); // Cacher le champ idCommande
        txtIdUser.setVisible(false); // Cacher le champ idUser
        txtIdPanier.setVisible(false); // Cacher le champ idPanier
        txtMontant.setDisable(true); // Rendre le montant non modifiable


        txtRue.setText(commande.getRue());
        txtVille.setText(commande.getVille());
        txtCodePostal.setText(commande.getCode_postal());
        txtEtat.setText(commande.getEtat());
        txtMontant.setText(String.valueOf(commande.getMontant_total()));
        txtMethodePaiement.setValue(commande.getMethodePaiment()); // Modifier ici pour utiliser ComboBox
    }

    public void setBAfficherCommandeController(BAfficherCommande controller) {
        this.BafficherCommandeController = controller;
    }

    @FXML
    private void enregistrerModification() {
        try {
            // Vérification des champs obligatoires
            if (txtRue.getText().isEmpty() || txtVille.getText().isEmpty() || txtCodePostal.getText().isEmpty() ||
                    txtEtat.getText().isEmpty() || txtMontant.getText().isEmpty() || txtMethodePaiement.getValue() == null) { // Modifier ici
                showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
                return;
            }

            // Vérification du code postal (4 ou 5 chiffres)
            if (!txtCodePostal.getText().matches("\\d{4,5}")) {
                showAlert("Erreur", "Le code postal doit contenir 4 ou 5 chiffres.", Alert.AlertType.ERROR);
                return;
            }

            // Vérification des champs texte (seulement des lettres et espaces)
            if (!txtRue.getText().matches("[a-zA-ZÀ-ÿ\\s]+") || !txtVille.getText().matches("[a-zA-ZÀ-ÿ\\s]+") ||
                    !txtEtat.getText().matches("[a-zA-ZÀ-ÿ\\s]+")) { // Supprimer txtMethodePaiement de la vérification
                showAlert("Erreur", "Les champs Rue, Ville et État ne doivent contenir que des lettres.", Alert.AlertType.ERROR);
                return;
            }

            // Vérification du montant total (nombre positif)
            float montantTotal;
            try {
                montantTotal = Float.parseFloat(txtMontant.getText());
                if (montantTotal <= 0) {
                    showAlert("Erreur", "Le montant total doit être un nombre positif.", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le montant total doit être un nombre valide.", Alert.AlertType.ERROR);
                return;
            }

            // Mise à jour des valeurs de l'objet Commande
            commande.setRue(txtRue.getText());
            commande.setVille(txtVille.getText());
            commande.setCode_postal(txtCodePostal.getText());
            commande.setEtat(txtEtat.getText());
            commande.setMontant_total(montantTotal);
            commande.setMethodePaiment(txtMethodePaiement.getValue()); // Modifier ici pour utiliser ComboBox

            // Mise à jour dans la base de données
            commandeService.modifier(commande);

            // Rafraîchir la liste des commandes
            if (BafficherCommandeController != null) {
                BafficherCommandeController.refreshGrid();
            }

            // Fermer la fenêtre de modification
            Stage stage = (Stage) txtRue.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert("Erreur SQL", "Une erreur s'est produite : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void fermer() {
        Stage stage = (Stage) txtRue.getScene().getWindow();
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