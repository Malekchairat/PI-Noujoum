package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import models.User;
import services.UserService;

public class addfavoriscontroller implements Initializable {

    @FXML
    private Button id_ajoutfav;

    @FXML
    private TextField id_produit;
    @FXML
    private TextField id_user;
    @FXML
    private TextField id_date;


    @FXML
    private Button show;



    @FXML
    void addUser(ActionEvent event) {
        String first = id_produit.getText().trim();
        String emailInput = id_user.getText().trim();
        String last = id_date.getText().trim();

        String role = idrole.getValue(); // Récupérer la valeur sélectionnée

        if (first.isEmpty() || last.isEmpty() || emailInput.isEmpty() || passwordInput.isEmpty() || phone.isEmpty() || role == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis!", Alert.AlertType.ERROR);
            return;
        }

        if (!emailInput.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Erreur", "Format d'email invalide!", Alert.AlertType.ERROR);
            return;
        }

        if (passwordInput.length() < 8) {
            showAlert("Erreur", "Le mot de passe doit contenir au moins 8 caractères!", Alert.AlertType.ERROR);
            return;
        }

        if (!phone.matches("^\\d{8}$")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir 8 chiffres!", Alert.AlertType.ERROR);
            return;
        }

        User newUser = new User(1, first, last, emailInput, passwordInput, Integer.parseInt(phone), role, null);

        UserService userCrud = new UserService();
        try {
            userCrud.ajouter(newUser);
            showAlert("Succès", "Utilisateur ajouté avec succès!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Erreur", "Échec de l'ajout de l'utilisateur: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficheruser.fxml"));
            Parent root = loader.load();
            showusercontroller afficheruserController = loader.getController();
            afficheruserController.loadUsers();
            show.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
