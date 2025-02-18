package Controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.event.ActionEvent;

public class Acceuil {
    public void handleAjouterUser() throws IOException {
        loadPage("/ajouteruser.fxml");
    }

    // Load AjouterEvenement.fxml
    public void handleAjouterEvenement() throws IOException {
        loadPage("/ajouterEvenement.fxml");
    }

    // Load AjouterCommande.fxml
    public void handleAjouterPanier() throws IOException {
        loadPage("/AjouterCommande.fxml");
    }

    // Method to load an FXML file in a new window
    private void loadPage(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
