package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.UserService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;

public class addusercontroller implements Initializable {

    @FXML
    private Button goToAddFavoris;
    @FXML
    private Button id_ajout;
    @FXML
    private TextField id_nom;
    @FXML
    private TextField id_prenom;
    @FXML
    private TextField id_mail;
    @FXML
    private TextField id_mdp;
    @FXML
    private TextField id_tel;
    @FXML
    private ComboBox<String> idrole;
    @FXML
    private Button show;
    @FXML
    private Button browseImage;
    @FXML
    private ImageView profileImage;

    private File selectedImageFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idrole.getItems().addAll("Fan", "Admin");
    }

    @FXML
    void browseImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            Image img = new Image(selectedImageFile.toURI().toString());
            profileImage.setImage(img);
        } else {
            showAlert("Erreur", "Aucune image sélectionnée", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void addUser(ActionEvent event) {
        String first = id_nom.getText().trim();
        String last = id_prenom.getText().trim();
        String emailInput = id_mail.getText().trim();
        String passwordInput = id_mdp.getText().trim();
        String phone = id_tel.getText().trim();
        String role = idrole.getValue();

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

        Blob imageBlob = null;
        if (selectedImageFile != null) {
            try (InputStream inputStream = new FileInputStream(selectedImageFile)) {
                byte[] imageBytes = inputStream.readAllBytes();
                imageBlob = new SerialBlob(imageBytes);
            } catch (IOException | SQLException e) {
                showAlert("Erreur", "Problème lors de la conversion de l'image.", Alert.AlertType.ERROR);
                return;
            }
        }

        User newUser = new User(1, first, last, emailInput, passwordInput, Integer.parseInt(phone), role, imageBlob);

        UserService userCrud = new UserService();
        userCrud.ajouter(newUser);
        showAlert("Succès", "Utilisateur ajouté avec succès!", Alert.AlertType.INFORMATION);

        resetFields();
    }

    private void resetFields() {
        id_nom.clear();
        id_prenom.clear();
        id_mail.clear();
        id_mdp.clear();
        id_tel.clear();
        idrole.getSelectionModel().clearSelection();
        profileImage.setImage(null);
        selectedImageFile = null;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficheruser.fxml"));
            Parent root = loader.load();
            show.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void goToAddFavoris(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterfavoris.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Favoris");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page Ajouter Favoris: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
