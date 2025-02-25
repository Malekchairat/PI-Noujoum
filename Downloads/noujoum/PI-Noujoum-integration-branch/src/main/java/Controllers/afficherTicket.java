package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Ticket;
import services.TicketService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class afficherTicket {

    public VBox ticketContainer; // Conteneur des tickets
    private final TicketService ticketService = new TicketService();

    public void initialize() {
        try {
            List<Ticket> tickets = ticketService.recuperer();
            ticketContainer.getChildren().clear(); // Nettoyer avant d'ajouter

            for (Ticket ticket : tickets) {
                // Récupérer les infos
                float prix = ticket.getPrix();
                float quantite = ticket.getQuantite();
                String methodePaiement = ticket.getMethodePaiement().name(); // Correction ici

                // Création des labels
                Label prixLabel = new Label("💰 Prix: " + prix + "€");
                Label quantiteLabel = new Label("📦 Quantité: " + quantite);
                Label paiementLabel = new Label("💳 Paiement: " + methodePaiement);

                // Boutons d'action
                Button deleteButton = new Button("Supprimer");
                Button updateButton = new Button("Modifier");

                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

                deleteButton.setOnAction(e -> supprimerTicket(ticket.getIdTicket()));
                updateButton.setOnAction(e -> modifierTicket(ticket));

                // Conteneur pour un ticket
                VBox ticketBox = new VBox(5);
                ticketBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-padding: 10px;");
                ticketBox.getChildren().addAll(prixLabel, quantiteLabel, paiementLabel, deleteButton, updateButton);

                // Ajouter le ticketBox à l'affichage
                ticketContainer.getChildren().add(ticketBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void supprimerTicket(int idTicket) {
        // Affichage d'une boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Voulez-vous vraiment supprimer ce ticket ?");
        confirmation.setContentText("Cette action est irréversible.");

        // Attendre la réponse de l'utilisateur
        Optional<ButtonType> result = confirmation.showAndWait();

        // Si l'utilisateur clique sur "OK", on supprime le ticket
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ticketService.supprimer(idTicket);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Ticket supprimé avec succès !");
                refreshScene(); // Rafraîchir la liste des tickets
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression du ticket.");
            }
        }
    }
    private void modifierTicket(Ticket ticketSelectionne) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierTicket.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur
            modifierTicket controller = loader.getController();

            // Passer le ticket sélectionné
            controller.setTicket(ticketSelectionne);

            // Ouvrir la fenêtre de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la modification.");
        }
    }

    private void refreshScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/afficherTicket.fxml"));
            Stage stage = (Stage) ticketContainer.getScene().getWindow();
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

    public void handleTestButton(ActionEvent actionEvent) {
        System.out.println("Bouton cliqué !");
    }
}
