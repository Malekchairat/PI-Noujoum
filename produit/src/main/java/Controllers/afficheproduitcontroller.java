package Controllers;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
//import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import models.Promotion;
import org.json.JSONArray;
import services.ServicesCrud;
import models.Produit;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
//import javax.print.attribute.standard.Media;

public class afficheproduitcontroller {

    @FXML
    private Button update, ajout;

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
    private TextField questionField;

    @FXML
    private TextArea iaResponseArea;

    private ServicesCrud service = new ServicesCrud();
    @FXML
    private VBox menuLateral;
    private static final String ACCOUNT_SID = "AC3b74a9a1cdc84d0001f6c6e49ea011ec";
    private static final String AUTH_TOKEN = "891ffb9806f3b93a12616b8193f65cd4";
    private static final String TWILIO_PHONE_NUMBER = "+12702791467"; // Ton numéro Twilio
    private static final String TON_NUMERO = "+21652164756"; // Ton numéro de téléphone
    @FXML
    public void initialize() {
        setupMenu();
        setupNavigationBar();
        loadProduits("");
        Image logoImage = new Image(getClass().getResource("/images/njm.png").toExternalForm());
        logo.setImage(logoImage);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadProduits(newValue.trim()); // Met à jour la liste des produits avec le filtre
        });

    }




    private void setupMenu() {
        menuLateral.getChildren().clear();
        menuLateral.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 15; -fx-background-radius: 10;");

        Label title = new Label("Catégories");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button albumsBtn = createCategoryButton("Albums", "Albums");
        Button vetementsBtn = createCategoryButton("Vêtements", "Vêtements");
        Button accessoiresBtn = createCategoryButton("Accessoires", "Accessoires");
        Button lightsticksBtn = createCategoryButton("Lightsticks", "Lightsticks");

        menuLateral.getChildren().addAll(title, albumsBtn, vetementsBtn, accessoiresBtn, lightsticksBtn);
    }

    private Button createCategoryButton(String text, String category) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;");
        button.setOnAction(event -> loadProduitsByCategory(category));
        return button;
    }

    private void loadProduitsByCategory(String category) {
        promoTilePane.getChildren().clear();
        List<Produit> produits = service.recupererTous();

        // Filtrer les produits par catégorie
        List<Produit> filteredProduits = produits.stream()
                .filter(p -> p.getCategorie() != null && p.getCategorie().name().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        for (Produit produit : filteredProduits) {
            VBox productCard = new VBox(10);
            productCard.getStyleClass().add("product-card");
            productCard.setPrefSize(250, 350);


            ImageView imageView = new ImageView();
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            imageView.getStyleClass().add("product-image");

            try {
                if (produit.getImage() != null) {
                    byte[] imageData = produit.getImage().getBytes(1, (int) produit.getImage().length());
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    imageView.setImage(image);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            Label title = new Label(produit.getNom());
            title.getStyleClass().add("product-title");


            HBox priceContainer = new HBox(5);
            priceContainer.setAlignment(Pos.CENTER_LEFT);

            Label originalPrice = new Label(produit.getPrix() + " €");
            originalPrice.getStyleClass().add("product-price");

            Label promoTag = null;
            Label discountedPrice = null;

            if (produit.getPromotion() != null) {
                double prixOriginal = produit.getPrix();
                double prixRemise = prixOriginal - (prixOriginal * (produit.getPromotion().getPourcentage() / 100.0));

                discountedPrice = new Label(String.format("%.2f €", prixRemise));
                discountedPrice.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red;");

                promoTag = new Label("-" + produit.getPromotion().getPourcentage() + "%");
                promoTag.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 5px; -fx-font-weight: bold;");

                originalPrice.setStyle("-fx-strikethrough: true; -fx-text-fill: gray;");

                priceContainer.getChildren().addAll(originalPrice, discountedPrice);
            } else {
                priceContainer.getChildren().add(originalPrice);
            }


            Label categoryLabel = new Label("Catégorie: " + produit.getCategorie());
            categoryLabel.getStyleClass().add("product-category");

            Label availabilityLabel = new Label("Disponibilité: " + produit.getDisponibilite());
            availabilityLabel.getStyleClass().add("product-availability");


            Button deleteButton = new Button("🗑️ Supprimer");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setOnAction(event -> {
                service.supprimer(produit.getIdproduit());
                loadProduitsByCategory(category); // Recharger après suppression
            });


            productCard.getChildren().addAll(imageView, title, priceContainer, categoryLabel, availabilityLabel, deleteButton);

            if (promoTag != null) {
                productCard.getChildren().add(1, promoTag); // Met la promo en haut si existante
            }

            promoTilePane.getChildren().add(productCard);
        }
    }

    @FXML
    private void goToHome(ActionEvent event) {
        System.out.println("Aller à Accueil");
    }

    @FXML
    private void goToProducts(ActionEvent event) {
        System.out.println("Aller à Produits");
    }

    @FXML
    private void goToReclamations(ActionEvent event) {
        System.out.println("Aller à Réclamations");
    }

    @FXML
    private void goToEvents(ActionEvent event) {
        System.out.println("Aller à Événements");
    }

    @FXML
    private void searchAction(ActionEvent event) {
        String searchText = searchField.getText();
        System.out.println("Recherche : " + searchText);
        searchProducts();
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
    public void searchProducts() {
        loadProduits(searchField.getText().trim());
    }

    public void loadProduits(String filter) {
        promoTilePane.getChildren().clear();
        promoTilePane.setHgap(30);
        promoTilePane.setVgap(50);

        try {
            List<Produit> produits = service.recupererTous();
            if (!filter.isEmpty()) {
                produits = produits.stream()
                        .filter(p -> p.getNom().toLowerCase().contains(filter.toLowerCase()))
                        .collect(Collectors.toList());
            }

            for (Produit produit : produits) {
                VBox productCard = new VBox(10);
                productCard.getStyleClass().add("product-card");
                productCard.setPrefSize(250, 350);

                // Ajouter l'image, le titre, le prix, etc.
                ImageView imageView = new ImageView();
                imageView.setFitHeight(200);
                imageView.setFitWidth(200);
                imageView.getStyleClass().add("product-image");

                try {
                    if (produit.getImage() != null) {
                        byte[] imageData = produit.getImage().getBytes(1, (int) produit.getImage().length());
                        Image image = new Image(new ByteArrayInputStream(imageData));
                        imageView.setImage(image);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Label title = new Label(produit.getNom());
                title.getStyleClass().add("product-title");

                HBox priceContainer = new HBox(5);
                priceContainer.setAlignment(Pos.CENTER_LEFT);

                Label originalPrice = new Label(produit.getPrix() + " €");
                originalPrice.getStyleClass().add("product-price");

                Label promoTag = null;
                Label discountedPrice = null;

                if (produit.getPromotion() != null) {
                    double prixOriginal = produit.getPrix();
                    double prixRemise = prixOriginal - (prixOriginal * (produit.getPromotion().getPourcentage() / 100.0));

                    discountedPrice = new Label(String.format("%.2f €", prixRemise));
                    discountedPrice.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red;");

                    promoTag = new Label("-" + produit.getPromotion().getPourcentage() + "%");
                    promoTag.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 5px; -fx-font-weight: bold;");

                    originalPrice.setStyle("-fx-strikethrough: true; -fx-text-fill: gray;");

                    priceContainer.getChildren().addAll(originalPrice, discountedPrice);
                } else {
                    priceContainer.getChildren().add(originalPrice);
                }

                Label category = new Label("Catégorie: " + produit.getCategorie());
                category.getStyleClass().add("product-category");

                Label availability = new Label("Disponibilité: " + produit.getDisponibilite());
                availability.getStyleClass().add("product-availability");



                Button deleteButton = new Button("🗑️ Supprimer");
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> {
                    service.supprimer(produit.getIdproduit());
                    loadProduits("");
                });

                productCard.getChildren().addAll(imageView, title, priceContainer, category, availability, deleteButton);

                if (promoTag != null) {
                    productCard.getChildren().add(1, promoTag); // Affiche la promotion en haut
                }

                promoTilePane.getChildren().add(productCard);
            }

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des produits : " + e.getMessage());
        }
    }

    // Méthode pour afficher une notification dans l'interface
    private void afficherNotificationJavaFX(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Nouvelle Promotion");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void ajout(ActionEvent event) {
        navigateTo("/addProduit.fxml");
    }

    @FXML
    void update(ActionEvent event) {
        navigateTo("/updateProduit.fxml");
    }

    public void loadProduits() {
        loadProduits("");
    }

    // Méthode pour gérer les questions de l'utilisateur
    @FXML
    private void handleQuestion(ActionEvent event) {
        String question = questionField.getText();
        if (!question.isEmpty()) {
            iaResponseArea.setText("Chargement...");

            Task<Void> aiTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    getAIResponseStreaming(question);
                    return null;
                }
            };

            aiTask.setOnFailed(e -> iaResponseArea.setText("Erreur de l'IA"));
            new Thread(aiTask).start();
        } else {
            iaResponseArea.setText("Veuillez entrer une question.");
        }
    }

    private void getAIResponseStreaming(String question) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2) // Utilisation de HTTP/2 pour accélérer
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:11435/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            new JSONObject()
                                    .put("model", "gemma:2b")
                                    .put("prompt", question)
                                    .put("max_tokens", 100) // Limite de tokens générés pour accélérer
                                    .toString()
                    ))
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                StringBuilder responseText = new StringBuilder();
                int tokenCounter = 0; // Compteur de tokens reçus

                while (true) {
                    String line = reader.readLine();
                    if (line == null) break; // Fin du flux

                    JSONObject json = new JSONObject(line);
                    if (json.has("response")) {
                        responseText.append(json.getString("response"));
                        tokenCounter++;

                        // Affichage tous les 5 tokens pour améliorer la fluidité
                        if (tokenCounter % 5 == 0) {
                            String finalText = responseText.toString();
                            javafx.application.Platform.runLater(() -> iaResponseArea.setText(finalText));
                        }
                    }
                }

                // Mise à jour finale après réception complète
                javafx.application.Platform.runLater(() -> iaResponseArea.setText(responseText.toString()));

            }
        } catch (Exception e) {
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> iaResponseArea.setText("Erreur lors de la communication avec l'IA"));
        }
    }

    public void appliquerPromotion(Produit produit, Promotion promotion) {
        if (produit != null && promotion != null && promotion.getPourcentage() > 0) {
            // Appliquer la promotion et obtenir le nouveau prix
            produit.setPrix(produit.getPrixAvecPromo()); // Prix après application de la promo
            produit.setPromotion(promotion);

            // Créer le message de notification
            String message = "Nouvelle promotion : " + produit.getNom() + " - " + promotion.getPourcentage() + "% de réduction ! Nouveau prix : " + produit.getPrix();

            // Afficher la notification dans l'interface JavaFX
            afficherNotificationJavaFX(message);

            // Envoyer un SMS
            envoyerSMS(message); // Assurez-vous que cette méthode est bien implémentée pour envoyer le SMS
        } else {
            System.err.println("Erreur : Produit ou promotion invalide.");
        }
    }

    private void envoyerSMS(String message) {
        try {
            System.out.println("Initialisation de Twilio...");
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            System.out.println("Envoi du SMS...");
            Message sms = Message.creator(
                    new PhoneNumber(TON_NUMERO), // Numéro de destination
                    new PhoneNumber(TWILIO_PHONE_NUMBER), // Numéro Twilio
                    message // Message à envoyer
            ).create();

            System.out.println("SMS envoyé avec succès ! ID : " + sms.getSid());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
            e.printStackTrace();
        }
    }
}