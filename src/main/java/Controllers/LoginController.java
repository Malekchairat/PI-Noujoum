package Controllers;

import models.User;
import services.UserService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;

public class LoginController implements Initializable {

    @FXML
    private Button cnbt, cnbt1, icibt;

    @FXML
    private TextField email;

    @FXML
    private PasswordField mdp;

    @FXML
    private ImageView logo;

    @FXML
    private Text slogan, bien;

    private UserService us = new UserService();
    public static User UserConnected;

    @Override
    public void initialize(URL url, java.util.ResourceBundle rb) {
        // Check if 'slogan' is initialized before calling animateUI
        if (slogan != null) {
            animateUI();
        }
        slogan.setText("Briller Vos Nuits!");  // Set initial text for slogan
        bien.setText("Your gateway to your idols!");  // Set initial text for bien
    }

    private void animateUI() {
        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), bien);
        rotate.setByAngle(360);
        rotate.setCycleCount(3);
        rotate.play();

        TranslateTransition translate = new TranslateTransition(Duration.seconds(5), slogan);
        translate.setFromX(-slogan.getBoundsInParent().getWidth());
        translate.setToX(logo.getBoundsInParent().getWidth());
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.play();
    }

    @FXML
    private void connect(ActionEvent event) throws SQLException, IOException {
        String userEmail = email.getText().trim();
        String userPassword = mdp.getText().trim();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Champ Vide", "Veuillez remplir tous les champs.");
            return;
        }

        if (!userEmail.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert(AlertType.ERROR, "Email Non Valide", "Format email incorrect !");
            return;
        }

        List<User> users = us.recuperer();
        UserConnected = users.stream()
                .filter(user -> user.getEmail().equals(userEmail) && user.getMdp().equals(userPassword))
                .findFirst()
                .orElse(null);

        if (UserConnected != null) {
            showAlert(AlertType.INFORMATION, "Bienvenue", "Welcome " + UserConnected.getNom());
            slogan.setText("Welcome back, " + UserConnected.getNom() + "!");
            bien.setText("Let's get started with Noujoum!");

            if ("Fan".equals(UserConnected.getRole())) {
                switchToScene(event, "/acceuil.fxml", "Noujoum");
            } else if ("Admin".equals(UserConnected.getRole())) {
                switchToScene(event, "/backoffice.fxml", "Back Office");
            } else {
                showAlert(AlertType.ERROR, "Accès Refusé", "Votre rôle ne vous permet pas d'accéder à cette application.");
            }
        } else {
            showAlert(AlertType.ERROR, "Erreur", "Utilisateur inexistant !");
            slogan.setText("Invalid credentials, try again!");
        }
    }

    @FXML
    private void inscrire(ActionEvent event) throws IOException {
        switchToScene(event, "/ajouteruser.fxml", "Inscription");
    }

    @FXML
    private void passwrd(ActionEvent event) throws IOException {
        switchToScene(event, "/email_check.fxml", "Changement de mot de passe");
    }

    private void switchToScene(ActionEvent event, String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void mdp_ob(ActionEvent event) throws SQLException, IOException {
        String userEmail = email.getText().trim();

        if (userEmail.isEmpty()) {
            showAlert(AlertType.ERROR, "Champ Vide", "Veuillez entrer votre email.");
            return;
        }

        if (!userEmail.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert(AlertType.ERROR, "Email Non Valide", "Format email incorrect !");
            return;
        }

        System.out.println("Sending verification code to: " + userEmail);
        showAlert(AlertType.INFORMATION, "Code Envoyé", "Un code de vérification a été envoyé à " + userEmail);
    }
}
