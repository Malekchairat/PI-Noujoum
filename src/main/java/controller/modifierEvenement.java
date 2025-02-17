package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Evenement;
import models.Type_e;
import services.EvenementService;

import java.time.LocalDate;

public class modifierEvenement {

    @FXML private TextField locationField;
    @FXML private TextField artistField;
    @FXML private TextField descriptionField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField timeField;
    @FXML private TextField priceField;
    @FXML private TextField ticketCountField;
    @FXML private ComboBox<Type_e> eventTypeComboBox;

    private EvenementService service;
    private int eventId;

    @FXML
    public void initialize() {
        service = new EvenementService();
        eventTypeComboBox.getItems().setAll(Type_e.values());
    }

    public void loadEvent(int eventId) {
        this.eventId = eventId;
        try {
            Evenement event = service.recupererParId(eventId); // Fetch event by ID
            if (event != null) {
                locationField.setText(event.getLocation());
                artistField.setText(event.getArtist());
                descriptionField.setText(event.getDescription());
                startDatePicker.setValue(event.getStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                endDatePicker.setValue(event.getEndDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                timeField.setText(String.valueOf(event.getTime()));
                priceField.setText(String.valueOf(event.getPrice()));
                ticketCountField.setText(String.valueOf(event.getTicketCount()));
                eventTypeComboBox.setValue(event.getType());
            } else {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun événement trouvé avec cet ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'événement.");
        }
    }

    @FXML
    private void saveEvent(ActionEvent event) {
        try {
            String location = locationField.getText();
            String artist = artistField.getText();
            String description = descriptionField.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            int time = Integer.parseInt(timeField.getText());
            float price = Float.parseFloat(priceField.getText());
            int ticketCount = Integer.parseInt(ticketCountField.getText());
            Type_e type = eventTypeComboBox.getValue();

            // Créer un nouvel événement avec les champs mis à jour
            Evenement updatedEvent = new Evenement(
                    eventId,
                    location,
                    artist,
                    description,
                    java.sql.Date.valueOf(startDate),
                    java.sql.Date.valueOf(endDate),
                    time,
                    price,
                    type,
                    ticketCount
            );

            // Appeler la méthode "modifier" en passant l'ancien ID
            service.modifier(updatedEvent, eventId);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été modifié avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la modification de l'événement.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
