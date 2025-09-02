package Controllers;

import models.mail;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.UserService;

public class Email_checkController implements Initializable {

    @FXML
    private TextField email_check;
    @FXML
    private Button valbtn;
    @FXML
    private ImageView goback;
    @FXML
    private TextField code;
    @FXML
    private Button valcodebtn;

    private UserService us = new UserService();
    private static int code2; // Doit être statique pour conserver la valeur
    private static String email_set; // Stocker l'email pour la vérification

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation si nécessaire
    }

    @FXML
    private void passwrd_reset(ActionEvent event) throws SQLException {
        String email = email_check.getText().trim();
        System.out.println("Email entered: " + email);  // Debug line

        if (us.existEmail(email)) {
            email_set = email;
            Random rand = new Random();
            code2 = rand.nextInt(9000) + 1000; // Generate 4-digit code
            System.out.println("Code généré : " + code2);

            // Sending email with code
            mail.send(email, "Code de vérification", "Votre code de vérification est : " + code2);

            // Mise à jour de l'UI
            email_check.setVisible(false);
            valbtn.setVisible(false);
            code.setVisible(true);
            valcodebtn.setVisible(true);
        } else {
            showAlert("Email inconnu", "L'email fourni n'est pas enregistré.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void retour_login(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void verif_code(ActionEvent event) throws IOException {
        try {
            int enteredCode = Integer.parseInt(code.getText().trim());
            System.out.println("Code entré : " + enteredCode + " | Code attendu : " + code2);

            if (enteredCode == code2) {
                // Chargement de la page de changement de mot de passe
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangementMdp.fxml"));
                Parent root = loader.load();

                ChangementMdpController controller = loader.getController();
                controller.updateMdp(email_set); // Passer l'email

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Changement de mot de passe");
                stage.setScene(scene);
                stage.show();
            } else {
                showAlert("Code incorrect", "Le code saisi est incorrect. Veuillez réessayer.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un code valide.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
