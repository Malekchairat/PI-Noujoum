package controller;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import models.Reclamation;
import models.User;
import services.ReclamationService;
import services.UserService;
import tools.GeminiAPI;
import tools.TwilioAPI;

import java.util.Date;
import java.util.regex.Pattern;

public class AddRecController {

    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private Button submitButton;

    @FXML
    private Label titreError;
    @FXML
    private Label descriptionError;

    private ReclamationService reclamationService = new ReclamationService();
    private UserService us = new UserService();
    private final User logged_in_User = us.recupererParId(2);

    @FXML
    public void initialize() {
        // Validate title input in real-time
        titreField.textProperty().addListener((observable, oldValue, newValue) -> validateTitre(newValue));

        // Validate description input in real-time
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> validateDescription(newValue));
    }

    @FXML
    private void handleSubmit() {
        TwilioAPI tp = new TwilioAPI();
        String titre = titreField.getText().trim();
        String description = descriptionField.getText().trim();

        // Check for validation errors before submitting
        boolean validTitre = validateTitre(titre);
        boolean validDescription = validateDescription(description);

        if (!validTitre || !validDescription) {
            showAlert("Erreur", "Veuillez corriger les erreurs avant de soumettre.");
            return;
        }

        // Get priority from Gemini API
        String priorite = getPriorityFromGemini(description);
        System.out.println("description "+description);
        System.out.println("Priorité: " + priorite);

        // Create a new Reclamation object (Agent is assigned automatically)
        Reclamation reclamation = new Reclamation(
                0,logged_in_User, titre, description, new Date(), "En attente", priorite
        );

        // Add reclamation to database
        reclamationService.ajouter(reclamation);
        showAlert("Succès", "Réclamation ajoutée avec succès !");
        tp.sendSMS("+21654410619","l'utilisateur "+logged_in_User.getNom()+"à desposé une reclamation avec une priorité "+priorite);
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
     * 🔹 Get priority level from Gemini AI based on description
     */
    private String getPriorityFromGemini(String description) {
        try {
            String prompt = "Classify the priority of this complaint as 'Basse', or 'Haute'. Respond with only the priority level:\n" + description;
            System.out.println("Prompt: " + prompt);
            JsonObject response = GeminiAPI.getGeminiResponse(prompt);

            String priority = response.get("candidates").getAsJsonArray().get(0).getAsJsonObject().get("content").getAsJsonObject().get("parts").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString();

            return priority;

        } catch (Exception e) {
            System.err.println("Erreur API Gemini: " + e.getMessage());
            return "Inconnu"; // Or a more suitable default
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
