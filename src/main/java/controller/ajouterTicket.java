package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Evenement;
import models.Ticket;
import models.Type_P;
import services.TicketService;

public class ajouterTicket {

    @FXML private Label lblEventName;
    @FXML private TextField prixField;
    @FXML private TextField quantiteField;
    @FXML private ComboBox<String> paiementCombo;

    private final TicketService ticketService = new TicketService();
    private Evenement evenement; // L'événement sélectionné

    public void initData(Evenement evenement) {
        this.evenement = evenement;
        lblEventName.setText("Réservation pour : " + evenement.getArtist());

        // Ajouter les valeurs de l'énumération Type_P dans la ComboBox
        for (Type_P type : Type_P.values()) {
            paiementCombo.getItems().add(type.getTypeString());
        }

        paiementCombo.setValue(Type_P.CASH.getTypeString()); // Valeur par défaut
    }

    @FXML
    public void ajouterTicket(ActionEvent event) {
        try {
            // Validation des champs
            if (quantiteField.getText().trim().isEmpty() || prixField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis.");
            }

            int quantite = Integer.parseInt(quantiteField.getText().trim());
            float prix = Float.parseFloat(prixField.getText().trim());

            if (quantite <= 0 || prix <= 0) {
                throw new IllegalArgumentException("La quantité et le prix doivent être positifs.");
            }

            // Vérification de l'événement
            if (evenement == null) {
                throw new NullPointerException("Aucun événement sélectionné.");
            }

            // Création du ticket avec un id_utilisateur fictif (Remplacez par un utilisateur réel)
            int idUtilisateur = 0;
            Type_P typePaiement = Type_P.valueOf(paiementCombo.getValue().toUpperCase());

            Ticket ticket = new Ticket(0, evenement.getIdEvenement(), idUtilisateur, prix, quantite, null, typePaiement);

            // Ajout via le service
            ticketService.ajouter(evenement, ticket);

            // Succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ticket réservé avec succès !");

            // Fermer la fenêtre
            ((Stage) prixField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer des valeurs numériques valides.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la réservation : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
