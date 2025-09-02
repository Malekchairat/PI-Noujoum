package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import models.Type_e;
import services.EvenementService;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FRONTafficherEvenement {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private TextField searchField;

    // Boutons de filtrage
    @FXML
    private Button btnAll;

    @FXML
    private Button btnConcert;

    @FXML
    private Button btnFanmeet;

    // Bouton de retour
    @FXML
    private Button backButton;

    private EvenementService serviceEvenement;
    private List<Evenement> allEvenements;

    // currentFilter : null pour Tous, sinon le type sélectionné
    private Type_e currentFilter = null;

    @FXML
    public void initialize() {
        serviceEvenement = new EvenementService();

        // Configuration du ScrollPane
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(tilePane);

        // Récupérer tous les événements
        allEvenements = serviceEvenement.recuperer();

        // Affichage initial (tous les événements)
        filtrerEtAfficher();

        // Recherche avancée qui se déclenche dès la saisie
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            filtrerEtAfficher();
        });

        // Configuration des boutons de filtrage
        btnAll.setOnAction(e -> {
            currentFilter = null;
            updateButtonStyles();
            filtrerEtAfficher();
        });

        btnConcert.setOnAction(e -> {
            currentFilter = Type_e.CONCERT;
            updateButtonStyles();
            filtrerEtAfficher();
        });

        btnFanmeet.setOnAction(e -> {
            currentFilter = Type_e.FANMEET;
            updateButtonStyles();
            filtrerEtAfficher();
        });

        // Bouton de retour vers la page d'ajout d'événement
        backButton.setOnAction(e -> goBack());

        // Mise à jour initiale du style (par défaut, "Tous" est actif)
        updateButtonStyles();
    }

    // Met à jour le style des boutons pour indiquer le filtre actif
    private void updateButtonStyles() {
        String activeStyle = "-fx-background-color: pink; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 10;";
        String inactiveStyle = "-fx-background-color: lightgray; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;";

        if (currentFilter == null) {
            btnAll.setStyle(activeStyle);
            btnConcert.setStyle(inactiveStyle);
            btnFanmeet.setStyle(inactiveStyle);
        } else if (currentFilter == Type_e.CONCERT) {
            btnAll.setStyle(inactiveStyle);
            btnConcert.setStyle(activeStyle);
            btnFanmeet.setStyle(inactiveStyle);
        } else if (currentFilter == Type_e.FANMEET) {
            btnAll.setStyle(inactiveStyle);
            btnConcert.setStyle(inactiveStyle);
            btnFanmeet.setStyle(activeStyle);
        }
    }

    // Applique les filtres (recherche et type) et affiche la liste filtrée
    private void filtrerEtAfficher() {
        String query = searchField.getText().toLowerCase().trim();
        List<Evenement> filteredList = new ArrayList<>(allEvenements);

        if (!query.isEmpty()) {
            filteredList = filteredList.stream().filter(event ->
                    event.getLocation().toLowerCase().contains(query) ||
                            event.getArtist().toLowerCase().contains(query) ||
                            event.getDescription().toLowerCase().contains(query)
            ).collect(Collectors.toList());
        }

        if (currentFilter != null) {
            filteredList = filteredList.stream().filter(event -> event.getType() == currentFilter)
                    .collect(Collectors.toList());
        }

        afficherEvenements(filteredList);
    }

    // Affiche les événements dans le TilePane
    private void afficherEvenements(List<Evenement> evenements) {
        tilePane.getChildren().clear();

        for (Evenement evenement : evenements) {
            VBox card = createEventCard(evenement);
            tilePane.getChildren().add(card);
        }
    }

    // Création d'une carte pour un événement
    private VBox createEventCard(Evenement event) {
        VBox eventCard = new VBox();
        eventCard.setSpacing(12);
        eventCard.setPadding(new Insets(10));
        eventCard.setStyle("-fx-background-color: black; -fx-padding: 15; -fx-border-radius: 12; " +
                "-fx-border-color: gold; -fx-border-width: 2;");

        // Affichage de l'image
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

        // Détails de l'événement
        VBox eventDetails = new VBox(8);
        Label eventName = createStyledLabel("🎤 " + event.getArtist(), Color.GOLD, 20, true);
        Label eventDescription = createStyledLabel("📖 " + event.getDescription(), Color.WHITE, 14, false);
        Label eventLocation = createStyledLabel("📍 " + event.getLocation(), Color.WHITE, 14, false);
        Label eventPrice = createStyledLabel("💰 Price: $" + event.getPrice(), Color.GOLD, 16, true);

        eventDetails.getChildren().addAll(eventName, eventDescription, eventLocation, eventPrice);

        // Boutons d'actions (Modifier, Supprimer, Réserver)
        HBox buttonsContainer = new HBox(12);

        Button reserveButton = createStyledButton("Réserver", "green", "white");
        reserveButton.setOnAction(e -> handleReserverTicket(event));



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
        button.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-background-radius: 10;");
        return button;
    }




    private void handleReserverTicket(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterTicket.fxml"));
            Parent root = loader.load();
            ajouterTicket ticketController = loader.getController();
            ticketController.initData(evenement);

            Stage stage = new Stage();
            stage.setTitle("Réserver un Ticket");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de réservation de ticket.");
        }
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterEvenement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de revenir à la page d'ajout d'événement.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
