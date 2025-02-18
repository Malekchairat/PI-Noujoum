package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.Commande;
import services.CommandeService;
import services.UserService; // Assuming a UserService to check if idUser exists
import services.PanierService; // Assuming a PanierService to check if idPanier exists

public class AjouterCommande {

    @FXML
    private TextField idPanier, rue, ville, codePostal, etat, montantTotal, methodePaiment, idUser;

    @FXML
    private Button ajouterPanier;  // If needed for the "ajouterPanier" button, you can link it here

    // Méthode pour ajouter une commande
    @FXML
    void ajout(ActionEvent event) {
        // Valider les champs d'entrée avant de procéder
        if (!isValidInput()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
            return;
        }

        try {
            // Vérifier si idUser et idPanier existent dans la base de données
            int idPanierInt = Integer.parseInt(idPanier.getText());
            int idUserInt = Integer.parseInt(idUser.getText());

            PanierService panierService = new PanierService();
            UserService userService = new UserService();

            if (!panierService.exists(idPanierInt)) {
                showAlert("Erreur", "Le panier avec cet ID n'existe pas.", AlertType.ERROR);
                return;
            }

            if (!userService.exists(idUserInt)) {
                showAlert("Erreur", "L'utilisateur avec cet ID n'existe pas.", AlertType.ERROR);
                return;
            }

            // Convertir les champs en types corrects
            float montantTotalFloat = Float.parseFloat(montantTotal.getText());

            // Créer le service pour ajouter la commande
            CommandeService commandeCrud = new CommandeService();

            // Créer l'objet commande à insérer
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

            // Ajouter la commande dans la base de données
            commandeCrud.ajouter(commande);

            // Afficher un message de succès
            showAlert("Succès", "Commande ajoutée avec succès !", AlertType.INFORMATION);

            // Effacer les champs après l'ajout
            clearFields();
        } catch (NumberFormatException e) {
            // Gestion des erreurs de format de nombre
            showAlert("Erreur", "Format de numéro invalide.", AlertType.ERROR);
        } catch (Exception e) {
            // Gestion des autres exceptions
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de la commande.", AlertType.ERROR);
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour effacer les champs de saisie
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

    // Méthode pour valider les champs de saisie
    private boolean isValidInput() {
        return !idPanier.getText().isEmpty() &&
                !rue.getText().isEmpty() &&
                !ville.getText().isEmpty() &&
                !codePostal.getText().isEmpty() &&
                !etat.getText().isEmpty() &&
                !montantTotal.getText().isEmpty() &&
                !methodePaiment.getText().isEmpty() &&
                !idUser.getText().isEmpty();
    }

    // Méthode pour gérer le bouton "ajouterPanier" (si nécessaire)
    @FXML
    public void ajouterPanier(ActionEvent event) {
        // Implement the logic for adding a panier if needed
        System.out.println("Ajouter Panier clicked");
    }

    // Méthode pour gérer le bouton "afficherCommandes" (si nécessaire)
    @FXML
    public void afficherCommandes(ActionEvent event) {
        // Implement the logic to display orders if needed
        System.out.println("Afficher Commandes clicked");
    }
}