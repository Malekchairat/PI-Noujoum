<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/Controllers/supprimerTicket.java
package Controllers;
=======
package controller;
>>>>>>> origin/integration-branch:src/main/java/controller/supprimerTicket.java
=======
package Controllers;
>>>>>>> origin/GestionCommande

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Ticket;
import services.TicketService;

public class supprimerTicket {

    @FXML
    private Label ticketInfoLabel;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private TicketService ticketService;
    private int ticketId;

    public void setTicket(int idTicket, String details) {
        this.ticketId = idTicket;
        this.ticketInfoLabel.setText(details);
    }

    @FXML
    public void initialize() {
        ticketService = new TicketService();

        confirmButton.setOnAction(event -> supprimerTicket());
        cancelButton.setOnAction(event -> fermerFenetre());
    }

    private void supprimerTicket() {
        try {
            ticketService.supprimer(ticketId);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le ticket a été supprimé avec succès !");
            fermerFenetre();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le ticket.");
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
