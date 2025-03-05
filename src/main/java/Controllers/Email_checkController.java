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
    private int code2;
    private String email_set;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic (if needed)
    }

    @FXML
    private void passwrd_reset(ActionEvent event) throws SQLException {
        String email = email_check.getText();

        if (us.existEmail(email)) {
            email_set = email;
            Random rand = new Random();
            code2 = rand.nextInt(9000) + 1000;  // Generate a 4-digit code

            // Send email
            mail.send(email, "Code de vérification", "Votre code de vérification est : " + code2);

            // Update UI
            email_check.setVisible(false);
            valbtn.setVisible(false);
            code.setVisible(true);
            valcodebtn.setVisible(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email inconnu");
            alert.setHeaderText(null);
            alert.setContentText("L'email fourni n'est pas enregistré.");
            alert.show();
        }
    }

    @FXML
    private void retour_login(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
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
            int enteredCode = Integer.parseInt(code.getText());

            if (enteredCode == code2) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangementMdp.fxml"));
                Parent root = loader.load();
                ChangementMdpController controller = loader.getController();
                controller.updateMdp(email_set);

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Changement de mot de passe");
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Code incorrect");
                alert.setHeaderText(null);
                alert.setContentText("Le code que vous avez entré est incorrect.");
                alert.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de format");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer un code numérique valide.");
            alert.show();
        }
    }
}
