package controller;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Reclamation;
import services.ReclamationService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import tools.GeminiAPI;

public class UpdateRecController {

    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField dateField;
    @FXML
    private Button updateButton;

    @FXML
    private Label titreError;
    @FXML
    private Label descriptionError;

    private ReclamationService reclamationService = new ReclamationService();
    private Reclamation reclamation; // To store the selected reclamation

    @FXML
    public void initialize() {
        // Validate title input in real-time
        titreField.textProperty().addListener((observable, oldValue, newValue) -> validateTitre(newValue));

        // Validate description input in real-time
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> validateDescription(newValue));
    }

    /**
     * ✅ Load existing reclamation data into the form
     */
    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;

        titreField.setText(reclamation.getTitre());
        descriptionField.setText(reclamation.getDescription());
        dateField.setText(reclamation.getDateCreation().toString());

        // Ensure the fields are validated correctly when data is loaded
        validateTitre(titreField.getText());
        validateDescription(descriptionField.getText());
    }

    /**
     * ✅ Handle the update action
     */
    @FXML
    private void handleUpdate() {
        String titre = titreField.getText().trim();
        String description = descriptionField.getText().trim();

        // Check for validation errors before submitting
        boolean validTitre = validateTitre(titre);
        boolean validDescription = validateDescription(description);

        if (!validTitre || !validDescription) {
            showAlert("Erreur", "Veuillez corriger les erreurs avant de soumettre.");
            return;
        }

        // Get the new priority from Gemini API based on the updated description
        String newPriorite = getPriorityFromGemini(description);

        // Update the existing reclamation object
        reclamation.setTitre(titre);
        reclamation.setDescription(description);
        reclamation.setPriorite(newPriorite);

        // Update reclamation in database
        reclamationService.modifier(reclamation, reclamation.getId());
        showAlert("Succès", "Réclamation mise à jour avec succès !");
    }

    /**
     * 🔹 Get priority level from Gemini AI based on description
     */
    private String getPriorityFromGemini(String description) {
        try {
            String prompt = "Classify the priority of this complaint as 'Basse', 'Moyenne', or 'Haute':\n" + description;
            JsonObject response = GeminiAPI.getGeminiResponse(prompt);
            return response.get("priority").getAsString();
        } catch (Exception e) {
            System.err.println("Erreur API Gemini: " + e.getMessage());
            return "Moyenne"; // Default priority
        }
    }


    /**
     * ✅ Validate "Titre" field in real-time
     */
    private boolean validateTitre(String titre) {
        if (titre.length() < 3 || titre.length() > 50) {
            titreError.setText("Le titre doit contenir entre 3 et 50 caractères.");
            titreField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            return false;
        } else {
            titreError.setText("");
            titreField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
            return true;
        }
    }

    /**
     * ✅ Validate "Description" field in real-time
     */
    private boolean validateDescription(String description) {
        if (description.length() < 10) {
            descriptionError.setText("La description doit contenir au moins 10 caractères.");
            descriptionField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            return false;
        } else {
            descriptionError.setText("");
            descriptionField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
            return true;
        }
    }

    /**
     * ✅ Show alert messages
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
