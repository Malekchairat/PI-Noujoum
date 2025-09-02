package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.io.IOException;

public class FrontOfficeController {



    @FXML
    private TilePane promoTilePane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchField;

    @FXML
    private Button homeBtn, productsBtn, claimsBtn, eventsBtn;

    @FXML
    private ImageView logo;


    @FXML
    private VBox menuLateral;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void goToWishlist(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/afficherfavoris.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToCart(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/AfficherPanier.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontoffice.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToReclamations(ActionEvent event) {
        System.out.println("Aller à Réclamations");
    }
    @FXML
    private void goToProducts(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficheproduitf.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToEvents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherEvenement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setupNavigationBar() {
        homeBtn.setOnAction(event -> navigateTo("/home.fxml"));
        productsBtn.setOnAction(event -> navigateTo("/afficheproduit.fxml"));
        claimsBtn.setOnAction(event -> navigateTo("/reclamations.fxml"));
        eventsBtn.setOnAction(event -> navigateTo("/evenements.fxml"));
    }

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            homeBtn.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur de navigation : " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Load images safely
        loadImage(logo, "login.png");  // Remove leading slash

    }

    private void loadImage(ImageView imageView, String path) {
        try {
            // Assuming images are inside the resources folder (e.g., src/main/resources)
            URL imageUrl = getClass().getResource("/" + path);
            if (imageUrl != null) {
                imageView.setImage(new Image(imageUrl.toString()));
            } else {
                throw new IOException("Image not found at path: " + path);
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    private void handlePreOrder() {
        System.out.println("Pre-Order button clicked! Implement your logic here.");
    }

    public void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) logo.getScene().getWindow(); // Use logoImage to get scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading page: " + fxmlFile);
        }
    }

    private void showError(String message) {
        System.err.println(message); // You may want to replace this with an alert or dialog box
    }

    @FXML
    public void accueil() {
        loadPage("/frontoffice.fxml");
    }

    @FXML
    public void produits() {
        loadPage("/afficheProduitf.fxml");
    }

    @FXML
    public void events() {
        loadPage("/afficheEvenement.fxml");
    }

    @FXML
    public void panier() {
        loadPage("/AjouterPanier.fxml");
    }
}
