package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Commande;
import services.CommandeService;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterCommande {

    @FXML
    private TextField idPanier, rue, ville, codePostal, etat, montantTotal, methodePaiment, idUser;

    @FXML
    void ajout(ActionEvent event) {
        try {
            CommandeService commandeCrud = new CommandeService();

            if (idPanier.getText().isEmpty() || rue.getText().isEmpty() || ville.getText().isEmpty() ||
                    codePostal.getText().isEmpty() || etat.getText().isEmpty() || montantTotal.getText().isEmpty() ||
                    methodePaiment.getText().isEmpty() || idUser.getText().isEmpty()) {

                showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
                return;
            }

            int idPanierInt = Integer.parseInt(idPanier.getText());
            float montantTotalFloat = Float.parseFloat(montantTotal.getText());
            int idUserInt = Integer.parseInt(idUser.getText());

            Commande commande = new Commande(
                    0, // ID auto-généré (car souvent géré par la BD)
                    Integer.parseInt(idPanier.getText()), // id_panier
                    rue.getText(),                         // rue
                    ville.getText(),                       // ville
                    codePostal.getText(),                   // code_postal
                    etat.getText(),                         // etat
                    Float.parseFloat(montantTotal.getText()), // montant_total
                    methodePaiment.getText(),               // methodePaiement
                    Integer.parseInt(idUser.getText())       // id_user
            );


            commandeCrud.ajouter(commande);

            showAlert("Succès", "Commande ajoutée avec succès !", AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide pour les champs numériques : " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    void afficherCommandes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Liste   des Commandes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AfficherCommande.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void ajouterPanier(ActionEvent event) {
        try {
            // Charger la fenêtre AjouterPanier
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPanier.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Ajouter un Panier");

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Afficher une alerte si l'ouverture de la fenêtre échoue
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AjouterPanier.", Alert.AlertType.ERROR);
        }
    }

}
