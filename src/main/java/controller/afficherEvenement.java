package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Evenement;
import services.EvenementService;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.List;

public class afficherEvenement {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    private EvenementService serviceEvenement;

    @FXML
    public void initialize() {
        serviceEvenement = new EvenementService();
        afficherEvenements();
    }

    private void afficherEvenements() {
        tilePane.getChildren().clear(); // Nettoyer avant d'afficher

        List<Evenement> evenements;
        try {
            evenements = serviceEvenement.recuperer(); // Récupérer les événements
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
            return;
        }

        for (Evenement evenement : evenements) {
            VBox card = createEventCard(evenement);
            tilePane.getChildren().add(card);
        }
    }

    private VBox createEventCard(Evenement evenement) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: #222; -fx-padding: 10px; -fx-spacing: 5px; -fx-border-radius: 8px; -fx-border-color: gold; -fx-border-width: 2px;");

        Label lblLocation = new Label("📍 " + evenement.getLocation());
        Label lblArtist = new Label("🎤 " + evenement.getArtist());
        Label lblDate = new Label("📅 " + evenement.getStartDate().toString() + " - " + evenement.getEndDate().toString());
        Label lblPrice = new Label("💰 " + evenement.getPrice() + " TND");
        Label lblType = new Label("🎭 Type: " + evenement.getType());

        lblLocation.setStyle("-fx-text-fill: white;");
        lblArtist.setStyle("-fx-text-fill: white;");
        lblDate.setStyle("-fx-text-fill: white;");
        lblPrice.setStyle("-fx-text-fill: white;");
        lblType.setStyle("-fx-text-fill: white;");

        // Boutons : Modifier, Supprimer et Réserver Ticket
        Button btnModify = new Button("Modifier");
        Button btnDelete = new Button("Supprimer");
        Button btnReserver = new Button("Réserver Ticket");

        btnModify.setOnAction(e -> handleModifyEvent(evenement.getIdEvenement()));
        btnDelete.setOnAction(e -> handleDeleteEvent(evenement.getIdEvenement()));
        btnReserver.setOnAction(e -> handleReserverTicket(evenement));

        HBox buttonBox = new HBox(10, btnModify, btnDelete, btnReserver);
        buttonBox.setStyle("-fx-alignment: center;");

        card.getChildren().addAll(lblLocation, lblArtist, lblDate, lblPrice, lblType, buttonBox);
        return card;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optionnel : pas de texte d'en-tête
        alert.showAndWait();
    }

    private void handleModifyEvent(int eventId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierEvenement.fxml"));
            Parent root = loader.load();

            modifierEvenement controller = loader.getController();
            controller.loadEvent(eventId);

            Stage stage = new Stage();
            stage.setTitle("Modifier Événement");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }


    private void handleDeleteEvent(int eventId) {
        try {
            System.out.println("Supprimer clicked for Event ID: " + eventId);

            EvenementService service = new EvenementService();
            service.supprimer(eventId);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "L'événement a été supprimé avec succès.");
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.showAndWait();

            afficherEvenements(); // Rafraîchir la liste
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression de l'événement.");
        }
    }

    private void handleReserverTicket(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterTicket.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et vérifier l'objet événement
            ajouterTicket controller = loader.getController();
            if (evenement != null) {
                controller.initData(evenement);
            } else {
                throw new NullPointerException("L'objet Evenement est null.");
            }

            // Ouvrir la fenêtre
            Stage stage = new Stage();
            stage.setTitle("Réserver un Ticket");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de réservation.");
        }
    }

}
