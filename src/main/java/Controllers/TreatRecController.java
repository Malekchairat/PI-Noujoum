package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import models.Reclamation;
import services.ReclamationService;

public class TreatRecController {

    @FXML
    private TextArea adminCommentField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button updateStatusButton;

    private Reclamation rec;
    private ReclamationService reclamationService = new ReclamationService();
    private DashAdminRecController parentController;


    public void setReclamation(Reclamation rec, DashAdminRecController parentController) {
        this.rec = rec;
        this.parentController = parentController;
        statusComboBox.getItems().addAll("Traitée", "En attente");
        statusComboBox.setValue(rec.getStatut());
    }

    @FXML
    void handleUpdateStatus(ActionEvent event) {
        String newStatus = statusComboBox.getValue();
        String adminComment = adminCommentField.getText();

        if (newStatus == null || newStatus.isEmpty()) {
            System.out.println("Veuillez sélectionner un statut valide.");
            return;
        }

        rec.setStatut(newStatus);
        rec.setAnswer(adminComment);
        reclamationService.modifier(rec, rec.getId());

        parentController.refreshListView();
        System.out.println("Réclamation mise à jour avec le statut: " + newStatus+ rec.getId());
    }
}