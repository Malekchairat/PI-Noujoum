package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Evenement;
import models.Type_e;
import services.EvenementService;

import java.io.IOException;
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

    private EvenementService service;
    private Evenement selectedEvent;

    @FXML
    public void initialize() {
        service = new EvenementService();
        eventType.getItems().setAll(Type_e.values());
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

        // ✅ Vérification des champs obligatoires
        if (location.isEmpty() || artistName.isEmpty() || desc.isEmpty() || start == null || end == null
                || timeText.isEmpty() || priceText.isEmpty() || ticketCountText.isEmpty() || type == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // ✅ Vérification du format de l'heure (doit être un nombre à 4 chiffres)
        if (!timeText.matches("^\\d{4}$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'heure doit être au format HHMM (ex: 1400 pour 14h00)");
            return;
        }

        // ✅ Vérification du format du prix (nombre positif)
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

        // ✅ Vérification du nombre de tickets (entier positif)
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

        // ✅ Vérification que la date de début est avant la date de fin
        if (start.isAfter(end)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de début ne peut pas être après la date de fin !");
            return;
        }

        // ✅ Création de l'objet événement
        Evenement newEvent = new Evenement(location, artistName, desc,
                Date.valueOf(start), Date.valueOf(end),
                Integer.parseInt(timeText), priceValue, type, ticketCountValue);

        // ✅ Ajout de l'événement à la base de données avec gestion des exceptions
        try {
            service.ajouter(newEvent);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été ajouté avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de l'événement : " + e.getMessage());
        }
    }

    @FXML
    private void update(ActionEvent event) {
        // Logique pour mettre à jour un événement
    }

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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
