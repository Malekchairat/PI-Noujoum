package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import models.Evenement;
import services.EvenementService;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.List;

public class afficherEvenement {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    private EvenementService serviceEvenement;

    @FXML
    public void initialize() {
        // Initialisation de l'objet serviceEvenement
        serviceEvenement = new EvenementService();

        // Configuration du ScrollPane
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(tilePane);

        // Charger les événements
        afficherEvenements();
    }

    private void afficherEvenements() {
        tilePane.getChildren().clear(); // Clear any existing cards

        List<Evenement> evenements = serviceEvenement.recuperer();
        for (Evenement evenement : evenements) {
            VBox card = createEventCard(evenement);
            tilePane.getChildren().add(card);
        }
    }

    private VBox createEventCard(Evenement event) {
        // Create the container for the event card (using VBox)
        VBox eventCard = new VBox();
        eventCard.setSpacing(15);
        eventCard.setStyle("-fx-background-color: black; -fx-padding: 15; -fx-border-radius: 10; -fx-border-color: gold; -fx-border-width: 2;");

        // Create the image view for the event's image
        ImageView eventImageView = new ImageView();
        eventImageView.setFitWidth(300);
        eventImageView.setFitHeight(200);
        eventImageView.setPreserveRatio(true);

        // Get the image from the Evenement entity
        Blob imageBlob = event.getImageE();
        if (imageBlob != null) {
            try {
                // Convert Blob to byte array
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                Image image = new Image(byteArrayInputStream);
                eventImageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                // If there's an error, set a placeholder image
                eventImageView.setImage(new Image("file:path/to/placeholder/image.jpg"));
            }
        } else {
            // If the image is null, set a placeholder image
            eventImageView.setImage(new Image("file:path/to/placeholder/image.jpg"));
        }

        // Set up event details (name, description, etc.)
        VBox eventDetails = new VBox();
        eventDetails.setSpacing(10);

        // Event name with emoji and gold styling
        Label eventName = new Label("🎤 " + event.getArtist());
        eventName.setTextFill(Color.GOLD);
        eventName.setFont(Font.font("Arial", 20));
        eventName.setStyle("-fx-font-weight: bold;");

        // Event description with emoji
        Label eventDescription = new Label("📖 " + event.getDescription());
        eventDescription.setTextFill(Color.WHITE);
        eventDescription.setFont(Font.font("Arial", 14));

        // Event location with emoji
        Label eventLocation = new Label("📍 " + event.getLocation());
        eventLocation.setTextFill(Color.WHITE);
        eventLocation.setFont(Font.font("Arial", 14));

        // Event price with emoji
        Label eventPrice = new Label("💰 Price: $" + event.getPrice());
        eventPrice.setTextFill(Color.GOLD);
        eventPrice.setFont(Font.font("Arial", 16));

        // Add details to eventDetails container
        eventDetails.getChildren().addAll(eventName, eventDescription, eventLocation, eventPrice);

        // Create buttons for the event card
        HBox buttonsContainer = new HBox(10);
        buttonsContainer.setSpacing(10);

        // Button to modify event
        Button modifyButton = new Button("Modifier");
        modifyButton.setStyle("-fx-background-color: gold; -fx-text-fill: black; -fx-font-weight: bold;");
        modifyButton.setOnAction(e -> handleModifyEvent(event.getIdEvenement()));

        // Button to delete event
        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setOnAction(e -> handleDeleteEvent(event.getIdEvenement()));

        // Button to reserve ticket for the event
        Button reserveButton = new Button("Réserver");
        reserveButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold;");
        reserveButton.setOnAction(e -> handleReserverTicket(event));

        // Add buttons to the buttons container
        buttonsContainer.getChildren().addAll(modifyButton, deleteButton, reserveButton);

        // Add the image, details, and buttons to the event card
        eventCard.getChildren().addAll(eventImageView, eventDetails, buttonsContainer);

        // Add some padding inside the event card to make it more spacious
        eventCard.setPadding(new Insets(10));

        return eventCard;
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
        EvenementService service = new EvenementService();
        service.supprimer(eventId);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été supprimé avec succès.");
        afficherEvenements();
    }

    private void handleReserverTicket(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterTicket.fxml"));
            Parent root = loader.load();
            Controllers.ajouterTicket controller = loader.getController();
            controller.initData(evenement);

            Stage stage = new Stage();
            stage.setTitle("Réserver un Ticket");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de réservation.");
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
