package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import services.UserService;

public class ChangementMdpController implements Initializable {

    @FXML
    private Button valider;
    @FXML
    private PasswordField mdp1;
    @FXML
    private PasswordField mdp2;

    private String email2;
    private final UserService us = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation si nécessaire
    }

    public void updateMdp(String email) {
        this.email2 = email;
        System.out.println("Email reçu pour la mise à jour du mot de passe : " + email2);
    }

    @FXML
    private void Update_password(ActionEvent event) throws SQLException, IOException {
        String newPassword = mdp1.getText();
        String confirmPassword = mdp2.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Les champs ne doivent pas être vides.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Les mots de passe ne sont pas identiques.");
            return;
        }

        // Mise à jour du mot de passe

        us.modifMDP(email2, newPassword);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Votre mot de passe a été changé avec succès.");

        // Redirection vers la page de connexion
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
