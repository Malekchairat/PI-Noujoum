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

            if (!isValidInput()) {
                return;
            }

            int idPanierInt = Integer.parseInt(idPanier.getText());
            float montantTotalFloat = Float.parseFloat(montantTotal.getText());
            int idUserInt = Integer.parseInt(idUser.getText());

            Commande commande = new Commande(
                    0, // ID auto-généré
                    idPanierInt,
                    rue.getText(),
                    ville.getText(),
                    codePostal.getText(),
                    etat.getText(),
                    montantTotalFloat,
                    methodePaiment.getText(),
                    idUserInt
            );

            commandeCrud.ajouter(commande);
            showAlert("Succès", "Commande ajoutée avec succès !", AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide pour les champs numériques.", AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur SQL", "Une erreur s'est produite : " + e.getMessage(), AlertType.ERROR);
        }
    }

    private boolean isValidInput() {
        if (idPanier.getText().isEmpty() || rue.getText().isEmpty() || ville.getText().isEmpty() ||
                codePostal.getText().isEmpty() || etat.getText().isEmpty() || montantTotal.getText().isEmpty() ||
                methodePaiment.getText().isEmpty() || idUser.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
            return false;
        }

        if (!codePostal.getText().matches("\\d{4,5}")) {
            showAlert("Erreur", "Le code postal doit être composé de 4 ou 5 chiffres.", AlertType.ERROR);
            return false;
        }

        if (!isAlphabetic(rue.getText()) || !isAlphabetic(ville.getText()) || !isAlphabetic(etat.getText()) || !isAlphabetic(methodePaiment.getText())) {
            showAlert("Erreur", "Les champs rue, ville, état et méthode de paiement ne doivent contenir que des lettres.", AlertType.ERROR);
            return false;
        }

        try {
            Integer.parseInt(idPanier.getText());
            Float.parseFloat(montantTotal.getText());
            Integer.parseInt(idUser.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les champs ID Panier, Montant Total et ID User doivent être numériques.", AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isAlphabetic(String text) {
        return text.matches("[a-zA-Z\u00C0-\u017F ]+"); // Accepte les lettres + accents + espaces
    }

    private void clearFields() {
        idPanier.clear();
        rue.clear();
        ville.clear();
        codePostal.clear();
        etat.clear();
        montantTotal.clear();
        methodePaiment.clear();
        idUser.clear();
    }

    @FXML
    void afficherCommandes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Liste des Commandes");
            stage.show();
        } catch (IOException e) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPanier.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Ajouter un Panier");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AjouterPanier.", Alert.AlertType.ERROR);
        } } }
