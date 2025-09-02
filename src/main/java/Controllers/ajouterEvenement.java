package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
<<<<<<< HEAD
import javafx.scene.Parent;
=======
>>>>>>> origin/GestionCommande
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
<<<<<<< HEAD
import java.security.GeneralSecurityException;
=======
>>>>>>> origin/GestionCommande
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
<<<<<<< HEAD
    @FXML private Label imageLabel; // Affiche le nom de l'image sélectionnée
    private Blob eventImage; // Stocke l'image au format Blob
=======
    @FXML private Label imageLabel; // Label pour afficher le nom de l'image sélectionnée
    private Blob eventImage; // Attribut pour stocker l'image au format Blob
>>>>>>> origin/GestionCommande

    private EvenementService service;

    @FXML
    public void initialize() {
        service = new EvenementService();
        eventType.getItems().setAll(Type_e.values());
    }

    @FXML
    private void uploadImage(ActionEvent event) {
<<<<<<< HEAD
=======
        // Ouvrir un FileChooser pour permettre à l'utilisateur de sélectionner une image
>>>>>>> origin/GestionCommande
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
<<<<<<< HEAD
            imageLabel.setText(selectedFile.getName());
            try (FileInputStream fileInputStream = new FileInputStream(selectedFile)) {
                byte[] imageBytes = fileInputStream.readAllBytes();
=======
            imageLabel.setText(selectedFile.getName()); // Afficher le nom de l'image sélectionnée
            try (FileInputStream fileInputStream = new FileInputStream(selectedFile)) {
                byte[] imageBytes = fileInputStream.readAllBytes();
                // Créer un SerialBlob à partir du tableau de bytes
>>>>>>> origin/GestionCommande
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

<<<<<<< HEAD
=======
        // ✅ Vérification des champs obligatoires
>>>>>>> origin/GestionCommande
        if (location.isEmpty() || artistName.isEmpty() || desc.isEmpty() || start == null || end == null
                || timeText.isEmpty() || priceText.isEmpty() || ticketCountText.isEmpty() || type == null || eventImage == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return;
        }

<<<<<<< HEAD
=======
        // ✅ Vérification du format de l'heure (doit être un nombre à 4 chiffres)
>>>>>>> origin/GestionCommande
        if (!timeText.matches("^\\d{4}$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'heure doit être au format HHMM (ex: 1400 pour 14h00)");
            return;
        }

<<<<<<< HEAD
=======
        // ✅ Vérification du format du prix (nombre positif)
>>>>>>> origin/GestionCommande
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

<<<<<<< HEAD
=======
        // ✅ Vérification du nombre de tickets (entier positif)
>>>>>>> origin/GestionCommande
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

<<<<<<< HEAD
=======
        // ✅ Vérification que la date de début est avant la date de fin
>>>>>>> origin/GestionCommande
        if (start.isAfter(end)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de début ne peut pas être après la date de fin !");
            return;
        }

<<<<<<< HEAD
        Evenement newEvent = new Evenement(location, artistName, desc,
                Date.valueOf(start), Date.valueOf(end),
                Integer.parseInt(timeText), priceValue, type, ticketCountValue, eventImage);
// i said push and commit 2
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
=======
        // ✅ Création de l'objet événement avec l'image au format Blob
        Evenement newEvent = new Evenement(location, artistName, desc,
                Date.valueOf(start), Date.valueOf(end),
                Integer.parseInt(timeText), priceValue, type, ticketCountValue, eventImage);

        // ✅ Ajout de l'événement à la base de données avec gestion des exceptions
        try {
            service.ajouter(newEvent);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été ajouté avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de l'événement : " + e.getMessage());
>>>>>>> origin/GestionCommande
        }
    }

    @FXML
<<<<<<< HEAD
    private void update(ActionEvent event) {}
=======
    private void update(ActionEvent event) {
        // Logique pour mettre à jour un événement (si nécessaire)
    }
>>>>>>> origin/GestionCommande

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

<<<<<<< HEAD
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

=======
>>>>>>> origin/GestionCommande
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/GestionCommande
