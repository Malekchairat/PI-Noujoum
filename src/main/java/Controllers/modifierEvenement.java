package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import models.Evenement;
import models.Type_e;
import services.EvenementService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
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
    @FXML private Label imageLabel; // Label pour afficher le nom de l'image sélectionnée

    private EvenementService service;
    private int eventId;
    private Blob eventImage; // Attribut pour stocker l'image du Blob

    @FXML
    public void initialize() {
        service = new EvenementService();
        eventTypeComboBox.getItems().setAll(Type_e.values());
    }

    public void loadEvent(int eventId) {
        this.eventId = eventId;
        System.out.println("🔍 Chargement de l'événement ID : " + eventId);

        try {
            Evenement event = service.recupererParId(eventId);
            if (event != null) {
                locationField.setText(event.getLocation());
                artistField.setText(event.getArtist());
                descriptionField.setText(event.getDescription());

                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();

                startDatePicker.setValue(startDate);
                endDatePicker.setValue(endDate);

                timeField.setText(String.valueOf(event.getTime()));
                priceField.setText(String.valueOf(event.getPrice()));
                ticketCountField.setText(String.valueOf(event.getTicketCount()));
                eventTypeComboBox.setValue(event.getType());

                eventImage = event.getImage(); // Assurez-vous que cette méthode existe dans Evenement
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
            String location = locationField.getText().trim();
            String artist = artistField.getText().trim();
            String description = descriptionField.getText().trim();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            int time = Integer.parseInt(timeField.getText().trim());
            float price = Float.parseFloat(priceField.getText().trim());
            int ticketCount = Integer.parseInt(ticketCountField.getText().trim());
            Type_e type = eventTypeComboBox.getValue();

            if (location.isEmpty() || artist.isEmpty() || description.isEmpty() ||
                    startDate == null || endDate == null || type == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis!");
                return;
            }

            if (endDate.isBefore(startDate)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La date de fin ne peut pas être avant la date de début!");
                return;
            }

            Evenement updatedEvent = new Evenement(
                    eventId,
                    location,
                    artist,
                    description,
                    Date.valueOf(startDate),
                    Date.valueOf(endDate),
                    time,
                    price,
                    type,
                    ticketCount,
                    eventImage // Ajout de l'image (Blob) à l'événement
            );

            System.out.println("📝 Modification de l'événement ID : " + eventId);
            service.modifier(updatedEvent, eventId);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été modifié avec succès.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir des valeurs numériques valides.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la modification de l'événement.");
        }
    }
    public void setEvenementData(Evenement evenement) {
        if (evenement != null) {
            locationField.setText(evenement.getLocation());
            artistField.setText(evenement.getArtist());
            descriptionField.setText(evenement.getDescription());

            // Assuming startDate and endDate are of type LocalDate
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();


            timeField.setText(String.valueOf(evenement.getTime()));
            priceField.setText(String.valueOf(evenement.getPrice()));
            ticketCountField.setText(String.valueOf(evenement.getTicketCount()));
            eventTypeComboBox.setValue(evenement.getType());

            eventImage = evenement.getImage(); // Store the image blob if available
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
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
                eventImage = new SerialBlob(imageBytes); // Conversion en Blob
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'image.");
            }
        }
    }
}
