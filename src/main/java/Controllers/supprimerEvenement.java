package Controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import services.EvenementService;

public class supprimerEvenement {
    public void handleDeleteEvent(int eventId, Button sourceButton) {

        try {
            // Confirmer la suppression
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Êtes-vous sûr de vouloir supprimer cet événement ?",
                    ButtonType.YES, ButtonType.NO);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.showAndWait();

            if (confirmationAlert.getResult() == ButtonType.YES) {
                // Supprimer l'événement
                EvenementService service = new EvenementService();
                service.supprimer(eventId);

                // Afficher un message de succès
                showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été supprimé avec succès.");

                // Recharger afficherEvenement.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/afficherEvenement.fxml"));
                Parent root = loader.load();

                // Obtenir la scène à partir du bouton source
                Stage stage = (Stage) sourceButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression de l'événement.");
        }

    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
    private int eventId;
    // Méthode pour définir l'ID de l'événement
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
