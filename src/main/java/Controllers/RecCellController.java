package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Reclamation;
import services.ReclamationService;

import java.io.IOException;

public class RecCellController {

    @FXML
    private Label titreLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label statutLabel;
    @FXML
    private Label prioriteLabel;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button feedbackButton;

    private Reclamation reclamation;
    private ReclamationService reclamationService = new ReclamationService();
    private ListView<Reclamation> parentListView;

    public void setReclamation(Reclamation reclamation, ListView<Reclamation> parentListView) {
        this.reclamation = reclamation;
        this.parentListView = parentListView;

        titreLabel.setText(reclamation.getTitre());
        descriptionLabel.setText(reclamation.getDescription());
        dateLabel.setText("Créée le: " + reclamation.getDateCreation());
        statutLabel.setText("Statut: " + reclamation.getStatut());
        prioriteLabel.setText("Priorité: " + reclamation.getPriorite());

        if (reclamation.getAnswer() == null || reclamation.getAnswer().isEmpty()) {
            updateButton.setVisible(true);
            feedbackButton.setVisible(false);
        } else {
            updateButton.setVisible(false);
            feedbackButton.setVisible(true);
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateRec.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier Réclamation");
            stage.setScene(new Scene(loader.load()));

            UpdateRecController updateRecController = loader.getController();
            updateRecController.setReclamation(reclamation);

            stage.showAndWait();
            parentListView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");
        confirmationAlert.setContentText("Cette action est irréversible.");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                reclamationService.supprimer(reclamation.getId());
                parentListView.getItems().remove(reclamation);
            }
        });
    }

    @FXML
    private void handleFeedback() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserFeedback.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Donner un Feedback");
            stage.setScene(new Scene(loader.load()));

            UserFeedbackController feedbackController = loader.getController();
            feedbackController.setReclamation(reclamation);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}