package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class BackofficeController {

    @FXML
    private VBox mainContent;  // This is where we will load other FXML pages

    // Load Dashboard
    @FXML
    private void handleDashboard() throws IOException {
        loadPage("/dashboard.fxml");
    }

    // Load Users (Utilisateurs)
    @FXML
    private void handleAfficherUser() throws IOException {
        loadPage("/afficheruser.fxml");
    }

    // Load Events (Événements)
    @FXML
    private void handleAjouterEvenement() throws IOException {
        loadPage("/ajouterEvenement.fxml");
    }

    // Load Products (Produits)
    @FXML
    private void handleAjouterProduit() throws IOException {
        loadPage("/afficheProduit.fxml");
    }

    // Method to dynamically replace the content inside mainContent
    private void loadPage(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent newContent = loader.load();
        mainContent.getChildren().setAll(newContent);  // Replace content inside VBox
    }
}
