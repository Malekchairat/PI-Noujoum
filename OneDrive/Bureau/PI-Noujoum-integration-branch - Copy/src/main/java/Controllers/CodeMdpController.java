package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CodeMdpController implements Initializable {

    @FXML
    private TextField code;

    @FXML
    private Button validerBtn;  // Make sure this is linked to the FXML file

    private final String verificationCode = "123456"; // Replace this with actual logic

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Any necessary initialization
    }

    public void setCode(int c) {
        code.setText(String.valueOf(c));
    }

    @FXML
    private void verifierCode(ActionEvent event) {
        String enteredCode = code.getText().trim();

        if (enteredCode.isEmpty()) {
            showAlert("Erreur", "Le champ est vide!", Alert.AlertType.ERROR);
            return;
        }

        if (enteredCode.equals(verificationCode)) {
            showAlert("Succès", "Code de confirmation valide!", Alert.AlertType.INFORMATION);
            // Navigate to password change screen (if required)
        } else {
            showAlert("Erreur", "Code incorrect. Veuillez réessayer!", Alert.AlertType.WARNING);
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
