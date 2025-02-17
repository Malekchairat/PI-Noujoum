package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Ticket;
import models.Type_P;
import services.TicketService;

import java.io.IOException;

public class modifierTicket {

    @FXML
    private TextField prixField;

    @FXML
    private ComboBox<Type_P> methodePaiementField;

    @FXML
    private Button modifierButton;

    @FXML
    private Button annulerButton;

    private Ticket ticket;
    private final TicketService ticketService = new TicketService();

    @FXML
    public void initialize() {
        System.out.println("ModifierTicket controller initialisé !");
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        prixField.setText(String.valueOf(ticket.getPrix()));

        methodePaiementField.setItems(FXCollections.observableArrayList(Type_P.values()));
        methodePaiementField.setValue(ticket.getMethodePaiement());
    }

    @FXML
    public void modifier(ActionEvent actionEvent) {
        try {
            // Vérifier si un ticket est bien chargé
            if (ticket == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun ticket sélectionné !");
                return;
            }

            // Mise à jour du ticket
            ticket.setPrix(Float.parseFloat(prixField.getText()));
            ticket.setMethodePaiement(methodePaiementField.getValue());

            // Enregistrement dans la base de données
            ticketService.modifier(ticket);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ticket modifié avec succès !");

            // Retour à l'affichage des tickets
            retourAfficherTickets();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un prix valide.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de modifier le ticket.");
        }
    }

    @FXML
    public void annuler(ActionEvent actionEvent) {
        retourAfficherTickets();
    }

    private void retourAfficherTickets() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherTicket.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) prixField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
