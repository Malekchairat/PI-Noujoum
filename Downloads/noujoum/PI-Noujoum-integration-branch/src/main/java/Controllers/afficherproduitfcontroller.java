package Controllers;

//import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
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
import javafx.scene.Node;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
//import javax.print.attribute.standard.Media;

public class afficherproduitfcontroller {

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



@FXML
    public void initialize() {
        setupMenu();

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
        List<Produit> produits = service.recuperer();

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


            Button deleteButton = new Button("add to cart");
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
    private void searchAction(ActionEvent event) {
        String searchText = searchField.getText();
        System.out.println("Recherche : " + searchText);
        searchProducts();
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


    @FXML
    public void loadProduits(String filter) {
        promoTilePane.getChildren().clear();
        promoTilePane.setHgap(30);
        promoTilePane.setVgap(50);

        try {
            List<Produit> produits = service.recuperer();
            if (!filter.isEmpty()) {
                produits = produits.stream()
                        .filter(p -> p.getNom().toLowerCase().contains(filter.toLowerCase()))
                        .collect(Collectors.toList());
            }

            for (Produit produit : produits) {
                VBox productCard = new VBox(10);
                productCard.getStyleClass().add("product-card");
                productCard.setPrefSize(250, 350);

                // Image, title, price, etc.
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

                // Add the Favoris (Favorite) button
                Button favorisButton = new Button("❤️Ajouter aux favoris");
                favorisButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white;");
                favorisButton.setOnAction(event -> {
                    // Add or remove from favorites
                    toggleFavorite(produit);
                });

                // Add all elements to productCard
                productCard.getChildren().addAll(imageView, title, priceContainer, category, availability, favorisButton);

                if (promoTag != null) {
                    productCard.getChildren().add(1, promoTag); // Display promo tag if available
                }

                promoTilePane.getChildren().add(productCard);
            }

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des produits : " + e.getMessage());
        }
    }

    // Example method to toggle favorite status (you can use a favorite list or database to store favorites)
    private void toggleFavorite(Produit produit) {
        // This is where you can store the favorite product, e.g., in a database or a list
        // For now, let's print a message to simulate adding to favorites
        System.out.println(produit.getNom() + " has been added to the favorites.");
        // You can add logic to handle favorites here, like updating the UI or saving to a list
    }

    private void playAudio(String audioUrl) {
        if (audioUrl == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Extrait non disponible");
            alert.setHeaderText(null);
            alert.setContentText("Désolé, aucun extrait audio n'est disponible pour cet album.");
            alert.showAndWait();
            return;
        }

        try {
            Media media = new Media(audioUrl);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la lecture du fichier audio.");
        }
    }
    private void openWebPage(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String extractArtistName(String description) {
        // Supposons que la description contient "Album de NomDeLArtiste"
        if (description != null && description.startsWith("Album de ")) {
            return description.substring("Album de ".length());
        }
        return null; // Retourne null si le format n'est pas respecté
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
            double prixOriginal = produit.getPrix();
            double prixRemise = prixOriginal - (prixOriginal * (promotion.getPourcentage() / 100.0));

            produit.setPrix((float) prixRemise);  // Met à jour le prix
            produit.setPromotion(promotion);  // Associe la promotion au produit

            System.out.println("Promotion appliquée : " + promotion.getCode() + " (" + promotion.getPourcentage() + "%)");
        }
    }



    private String getYouTubePreviewUrl(Produit produit) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Récupérer le nom de l'album et de l'artiste
            String albumName = produit.getNom();
            String artistName = extractArtistName(produit.getDescription());

            // Encoder les noms pour l'URL
            String query = URLEncoder.encode(albumName + " " + artistName, StandardCharsets.UTF_8.toString());

            // Votre clé API YouTube
            String apiKey = "AIzaSyDC5k1-zp1VZcNJ0jMr7n8HL1rHvZsI4Yw";

            // Faire une requête à l'API YouTube pour rechercher des vidéos
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + query + "&type=video&key=" + apiKey))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Afficher la réponse de l'API pour le débogage
            System.out.println("Réponse de l'API YouTube : " + response.body());

            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONArray items = jsonResponse.getJSONArray("items");

                if (items.length() > 0) {
                    // Prendre la première vidéo trouvée
                    String videoId = items.getJSONObject(0).getJSONObject("id").getString("videoId");
                    return "https://www.youtube.com/watch?v=" + videoId; // URL de la vidéo YouTube
                }
            } else {
                System.out.println("Erreur lors de la recherche YouTube : " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Aucune vidéo trouvée
    }



}



