package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;

public class ProfileController {

    @FXML
    private Label userNameLabel, userEmailLabel, userPhoneLabel;
    @FXML
    private ImageView profileImage;

    private User user;
    @FXML
    private VBox menuLateral;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void goToWishlist(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/afficherfavoris.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToCart(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/AfficherPanier.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontoffice.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToReclamations(ActionEvent event) {
        System.out.println("Aller à Réclamations");
    }
    @FXML
    private void goToProducts(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficheproduitf.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to navigate to the Profile page
    @FXML
    private void goToProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
        Parent root = loader.load();

        // Pass the logged-in user to the profile page
        ProfileController profileController = loader.getController();
        profileController.setUser(LoginController.UserConnected); // Pass the logged-in user

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Profile");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToEvents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontafficherEvenement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to set the logged-in user
    public void setUser(User user) {
        this.user = user;
        updateProfile();
    }

    // Update the profile UI with user details
    private void updateProfile() {
        if (user != null) {
            userNameLabel.setText("Name: " + user.getNom());
            userEmailLabel.setText("Email: " + user.getEmail());
            userPhoneLabel.setText("Phone: " + user.getTel());

            // Set profile image if it exists
            if (user.getImage() != null) {
                Blob imageBlob = user.getImage();
                try {
                    byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                    Image image = new Image(byteArrayInputStream);
                    profileImage.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Logout functionality (optional)
    @FXML
    private void logout() {
        // Clear the user session and navigate to login screen
        LoginController.UserConnected = null; // Clear the session
        showAlert(AlertType.INFORMATION, "Logged out", "You have been logged out successfully.");
        // You can switch back to the login screen or home screen here
    }

    // Helper method for showing alerts
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void logout(ActionEvent event) {
        try {
            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent loginRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Print the error in case of failure
        }
    }
}
