package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Feedback;
import models.Reclamation;
import models.User;
import services.FeedbackService;
import services.UserService;
import java.util.Date;

public class UserFeedbackController {
    private Reclamation reclamation;
    private FeedbackService feedbackService = new FeedbackService();
    private UserService userService = new UserService();
    private User currentUser; // Assume this is set when the user logs in

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;

    }

    @FXML
    private TextArea commentaireField;

    @FXML
    private ComboBox<Integer> noteComboBox;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        noteComboBox.getItems().addAll(1, 2, 3, 4, 5);
    }

    @FXML
    void submitFeedback(ActionEvent event) {
        if (noteComboBox.getValue() == null || commentaireField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        Feedback feedback = new Feedback(0, reclamation, currentUser, noteComboBox.getValue(), commentaireField.getText(), new Date());
        feedbackService.ajouter(feedback);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Succès");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Feedback soumis avec succès.");
        successAlert.showAndWait();

        // Close the feedback window
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}