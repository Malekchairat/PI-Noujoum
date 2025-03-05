package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
        serviceEvenement = new EvenementService();

        // Configure ScrollPane for better usability
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(tilePane);

        // Load and display events
        afficherEvenements();
    }

    private void afficherEvenements() {
        tilePane.getChildren().clear();

        List<Evenement> evenements = serviceEvenement.recuperer();
        for (Evenement evenement : evenements) {
            VBox card = createEventCard(evenement);
            tilePane.getChildren().add(card);
        }
    }

    private VBox createEventCard(Evenement event) {
        // Create event card container
        VBox eventCard = new VBox();
        eventCard.setSpacing(12);
        eventCard.setPadding(new Insets(10));
        eventCard.setStyle("-fx-background-color: black; -fx-padding: 15; -fx-border-radius: 12; " +
                "-fx-border-color: gold; -fx-border-width: 2;");

        // Image display
        ImageView eventImageView = new ImageView();
        eventImageView.setFitWidth(280);
        eventImageView.setFitHeight(180);
        eventImageView.setPreserveRatio(true);

        Blob imageBlob = event.getImageE();
        if (imageBlob != null) {
            try {
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                Image image = new Image(byteArrayInputStream);
                eventImageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                eventImageView.setImage(new Image("file:path/to/placeholder/image.jpg"));
            }
        } else {
            eventImageView.setImage(new Image("file:path/to/placeholder/image.jpg"));
        }

        // Event details
        VBox eventDetails = new VBox(8);
        Label eventName = createStyledLabel("🎤 " + event.getArtist(), Color.GOLD, 20, true);
        Label eventDescription = createStyledLabel("📖 " + event.getDescription(), Color.WHITE, 14, false);
        Label eventLocation = createStyledLabel("📍 " + event.getLocation(), Color.WHITE, 14, false);
        Label eventPrice = createStyledLabel("💰 Price: $" + event.getPrice(), Color.GOLD, 16, true);

        eventDetails.getChildren().addAll(eventName, eventDescription, eventLocation, eventPrice);

        // Buttons
        HBox buttonsContainer = new HBox(12);
        Button modifyButton = createStyledButton("Modifier", "gold", "black");
        modifyButton.setOnAction(e -> handleModifyEvent(event.getIdEvenement()));

        Button deleteButton = createStyledButton("Supprimer", "red", "white");
        deleteButton.setOnAction(e -> handleDeleteEvent(event.getIdEvenement()));

        Button reserveButton = createStyledButton("Réserver", "green", "white");
        reserveButton.setOnAction(e -> handleReserverTicket(event));

        buttonsContainer.getChildren().addAll(modifyButton, deleteButton, reserveButton);

        // Assemble card
        eventCard.getChildren().addAll(eventImageView, eventDetails, buttonsContainer);
        return eventCard;
    }

    private Label createStyledLabel(String text, Color color, int fontSize, boolean bold) {
        Label label = new Label(text);
        label.setTextFill(color);
        label.setFont(Font.font("Arial", fontSize));
        if (bold) {
            label.setStyle("-fx-font-weight: bold;");
        }
        return label;
    }

    private Button createStyledButton(String text, String bgColor, String textColor) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-font-weight: bold;");
        return button;
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
        new EvenementService().supprimer(eventId);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été supprimé avec succès.");
        afficherEvenements();
    }

    private void handleReserverTicket(Evenement evenement) {
        // Reservation logic here
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}