package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Reclamation;
import services.ReclamationService;

import java.io.IOException;

public class RecCellAdminController {
    private Reclamation rec;
    private ReclamationService reclamationService = new ReclamationService();
    private DashAdminRecController parentController;

    public void setRec(Reclamation rec, DashAdminRecController parentController) {
        this.rec = rec;
        this.parentController = parentController;
        titreLabel.setText(rec.getTitre());
        descriptionLabel.setText(rec.getDescription());
        dateLabel.setText(rec.getDateCreation().toString());
        statutLabel.setText(rec.getStatut());
        prioriteLabel.setText(rec.getPriorite());
    }

    @FXML
    private Label dateLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label prioriteLabel;

    @FXML
    private Label statutLabel;

    @FXML
    private Label titreLabel;

    @FXML
    private Button treatButton;




    @FXML
    void handleDelete(ActionEvent event) {
        reclamationService.supprimer(rec.getId());
        parentController.refreshListView();
        System.out.println("Réclamation supprimée avec succès.");
    }

    @FXML
    void handleTreat(ActionEvent event) {
        if ("Traitée".equalsIgnoreCase(rec.getStatut())) {
            // Afficher une alerte si la réclamation est déjà traitée
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Réclamation déjà traitée");
            alert.setHeaderText(null);
            alert.setContentText("Cette réclamation a déjà été traitée !");
            alert.showAndWait();
        } else {
            try {
                // Charger la vue TreatRec.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/TreatRec.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur et lui passer la réclamation sélectionnée
                TreatRecController controller = loader.getController();
                controller.setReclamation(rec, this.parentController);

                // Ouvrir une nouvelle fenêtre (stage) pour traiter la réclamation
                Stage stage = new Stage();
                stage.setTitle("Traitement de la Réclamation");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de la fenêtre TreatRec.");
            }
    }
}
}