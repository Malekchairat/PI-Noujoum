package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class updateusercontroller {

    @FXML
    private TextField id;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField email;

    @FXML
    private TextField mdp;

    @FXML
    private TextField tel;

    @FXML
    private ComboBox<String> rolee;


    @FXML
    private Button show;

    @FXML
    private Button ajout;


    @FXML
    public void initialize() {
        rolee.getItems().addAll("Admin", "Fan"); // Ajoute les rôles possibles
    }

    @FXML
    void updateUser(ActionEvent event) throws SQLException {
        // Trim input values
        String idUserStr = id.getText().trim();
        String userNom = nom.getText().trim();
        String userPrenom = prenom.getText().trim();
        String userEmail = email.getText().trim();
        String userMdp = mdp.getText().trim();
        String userTel = tel.getText().trim();
        String userRole = rolee.getValue();
        if (userRole == null) {
            showAlert("Erreur", "Veuillez sélectionner un rôle !", Alert.AlertType.ERROR);
            return;
        }


        // Validate empty fields
        if (idUserStr.isEmpty() || userNom.isEmpty() || userPrenom.isEmpty() ||
                userEmail.isEmpty() || userMdp.isEmpty() || userTel.isEmpty() || userRole.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !", Alert.AlertType.ERROR);
            return;
        }

        // Validate ID
        int userId;
        try {
            userId = Integer.parseInt(idUserStr);
            if (userId < 0) {
                showAlert("Erreur", "L'ID utilisateur doit être un nombre positif !", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID utilisateur doit être un nombre valide !", Alert.AlertType.ERROR);
            return;
        }

        // Validate email format
        if (!userEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Erreur", "Adresse e-mail invalide !", Alert.AlertType.ERROR);
            return;
        }

        // Validate phone number (10 chiffres)
        if (!userTel.matches("^\\d{8}$")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir 8 chiffres !", Alert.AlertType.ERROR);
            return;
        }

        // Validate password length
        if (userMdp.length() < 6) {
            showAlert("Erreur", "Le mot de passe doit contenir au moins 6 caractères !", Alert.AlertType.ERROR);
            return;
        }

        // Create User object
        User user = new User(userId, userNom, userPrenom, userEmail, userMdp, Integer.parseInt(userTel), userRole,null);
        UserService userService = new UserService();
        userService.modifier(user);

        showAlert("Succès", "Utilisateur mis à jour avec succès !", Alert.AlertType.INFORMATION);
    }

    @FXML
    void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficheruser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) show.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de show_user.fxml : " + e.getMessage());
        }
    }
    public void setUserData(User user) {
        id.setText(String.valueOf(user.getId())); // Stocke l'ID (même s'il est invisible)
        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        email.setText(user.getEmail());
        mdp.setText(user.getMdp());
        tel.setText(String.valueOf(user.getTel()));
        rolee.setValue(user.getRole());
    }


    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
