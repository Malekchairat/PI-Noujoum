package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Evenement;
import models.Type_e;
import services.EvenementService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class ajouterEvenement {

    @FXML private TextField locationField;
    @FXML private TextField artist;
    @FXML private TextField description;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextField time;
    @FXML private TextField price;
    @FXML private TextField ticketCount;
    @FXML private ComboBox<Type_e> eventType;
    @FXML private Label imageLabel; // Affiche le nom de l'image sélectionnée
    private Blob eventImage; // Stocke l'image au format Blob

    private EvenementService service;

    @FXML
    public void initialize() {
        service = new EvenementService();
        eventType.getItems().setAll(Type_e.values());
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imageLabel.setText(selectedFile.getName());
            try (FileInputStream fileInputStream = new FileInputStream(selectedFile)) {
                byte[] imageBytes = fileInputStream.readAllBytes();
                eventImage = new SerialBlob(imageBytes);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'image.");
            }
        }
    }

    @FXML
    private void ajout(ActionEvent event) {
        String location = locationField.getText().trim();
        String artistName = artist.getText().trim();
        String desc = description.getText().trim();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        String timeText = time.getText().trim();
        String priceText = price.getText().trim();
        String ticketCountText = ticketCount.getText().trim();
        Type_e type = eventType.getValue();

        if (location.isEmpty() || artistName.isEmpty() || desc.isEmpty() || start == null || end == null
                || timeText.isEmpty() || priceText.isEmpty() || ticketCountText.isEmpty() || type == null || eventImage == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        if (!timeText.matches("^\\d{4}$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'heure doit être au format HHMM (ex: 1400 pour 14h00)");
            return;
        }

        float priceValue;
        try {
            priceValue = Float.parseFloat(priceText);
            if (priceValue <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre valide !");
            return;
        }

        int ticketCountValue;
        try {
            ticketCountValue = Integer.parseInt(ticketCountText);
            if (ticketCountValue <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nombre de tickets doit être un entier positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nombre de tickets doit être un entier valide !");
            return;
        }

        if (start.isAfter(end)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de début ne peut pas être après la date de fin !");
            return;
        }

        Evenement newEvent = new Evenement(location, artistName, desc,
                Date.valueOf(start), Date.valueOf(end),
                Integer.parseInt(timeText), priceValue, type, ticketCountValue, eventImage);

        try {
            service.ajouter(newEvent);
            CalenderEventAdder.addEvent(newEvent); // Synchronisation avec Google Calendar
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été ajouté avec succès !");
        } catch (IOException | GeneralSecurityException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de l'événement : " + ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de l'événement en base : " + e.getMessage());
        }
    }

    @FXML
    private void update(ActionEvent event) {}

    @FXML
    private void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherEvenement.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Liste des événements");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'affichage des événements.");
        }
    }

    @FXML
    private void afficherTickets(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherTicket.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Liste des Tickets");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'affichage des tickets.");
        }
    }

    @FXML
    private void newButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/voirTicket.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Voir Tickets");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}