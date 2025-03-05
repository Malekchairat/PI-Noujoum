package Controllers;

<<<<<<< HEAD
import javafx.collections.FXCollections;
=======
>>>>>>> origin/integration-branch
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
<<<<<<< HEAD
import javafx.scene.control.ComboBox;
=======
>>>>>>> origin/integration-branch
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Commande;
import services.CommandeService;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterCommande {

    @FXML
<<<<<<< HEAD
    private TextField idPanier, rue, ville, codePostal, etat, montantTotal, idUser;

    @FXML
    private ComboBox<String> methodePaiment;
=======
    private TextField idPanier, rue, ville, codePostal, etat, montantTotal, methodePaiment, idUser;
>>>>>>> origin/integration-branch

    @FXML
    void ajout(ActionEvent event) {
        try {
            CommandeService commandeCrud = new CommandeService();

<<<<<<< HEAD
            if (!isValidInput()) {
=======
            if (idPanier.getText().isEmpty() || rue.getText().isEmpty() || ville.getText().isEmpty() ||
                    codePostal.getText().isEmpty() || etat.getText().isEmpty() || montantTotal.getText().isEmpty() ||
                    methodePaiment.getText().isEmpty() || idUser.getText().isEmpty()) {

                showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
>>>>>>> origin/integration-branch
                return;
            }

            int idPanierInt = Integer.parseInt(idPanier.getText());
            float montantTotalFloat = Float.parseFloat(montantTotal.getText());
            int idUserInt = Integer.parseInt(idUser.getText());

<<<<<<< HEAD
            // Check if the Panier and User exist
            if (!commandeCrud.isPanierExists(idPanierInt)) {
                showAlert("Erreur", "Le panier avec l'ID " + idPanierInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

            if (!commandeCrud.isUserExists(idUserInt)) {
                showAlert("Erreur", "L'utilisateur avec l'ID " + idUserInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

            // Proceed with adding the order
            Commande commande = new Commande(
                    0, // ID auto-généré
                    idPanierInt,
                    rue.getText(),
                    ville.getText(),
                    codePostal.getText(),
                    etat.getText(),
                    montantTotalFloat,
                    methodePaiment.getValue(), // Récupère la valeur sélectionnée du ComboBox
                    idUserInt
            );

            commandeCrud.ajouter(commande);
            showAlert("Succès", "Commande ajoutée avec succès !", AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide pour les champs numériques.", AlertType.ERROR);
        } catch (Exception e) {
=======
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
        } catch (SQLException e) {
>>>>>>> origin/integration-branch
            showAlert("Erreur SQL", "Une erreur s'est produite : " + e.getMessage(), AlertType.ERROR);
        }
    }

<<<<<<< HEAD

    private boolean isValidInput() {
        if (idPanier.getText().isEmpty() || rue.getText().isEmpty() || ville.getText().isEmpty() ||
                codePostal.getText().isEmpty() || etat.getText().isEmpty() || montantTotal.getText().isEmpty() ||
                methodePaiment.getValue() == null || idUser.getText().isEmpty()) { // Vérifie si une méthode de paiement est sélectionnée
            showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
            return false;
        }

        if (!codePostal.getText().matches("\\d{4,5}")) {
            showAlert("Erreur", "Le code postal doit être composé de 4 ou 5 chiffres.", AlertType.ERROR);
            return false;
        }

        if (!isAlphabetic(rue.getText()) || !isAlphabetic(ville.getText()) || !isAlphabetic(etat.getText())) {
            showAlert("Erreur", "Les champs rue, ville et état ne doivent contenir que des lettres.", AlertType.ERROR);
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
        methodePaiment.getSelectionModel().clearSelection(); // Efface la sélection du ComboBox
        idUser.clear();
    }

=======
>>>>>>> origin/integration-branch
    @FXML
    void afficherCommandes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
<<<<<<< HEAD
            stage.setTitle("Liste des Commandes");
            stage.show();
        } catch (IOException e) {
=======
            stage.setTitle("Liste   des Commandes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
>>>>>>> origin/integration-branch
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
<<<<<<< HEAD

    @FXML
    private void ajouterPanier(ActionEvent event) {
        try {
=======
    @FXML
    private void ajouterPanier(ActionEvent event) {
        try {
            // Charger la fenêtre AjouterPanier
>>>>>>> origin/integration-branch
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPanier.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Ajouter un Panier");
<<<<<<< HEAD
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AjouterPanier.", Alert.AlertType.ERROR);
        }
    }
}
=======

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            // Afficher une alerte si l'ouverture de la fenêtre échoue
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AjouterPanier.", Alert.AlertType.ERROR);
        }
    }

}
>>>>>>> origin/integration-branch
