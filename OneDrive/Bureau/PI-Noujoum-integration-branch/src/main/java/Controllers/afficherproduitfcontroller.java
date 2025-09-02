package Controllers;
import services.UserService;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.util.Duration;
import models.Promotion;
import org.json.JSONArray;
import services.NewsItem;
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
import javafx.scene.input.MouseEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
import models.User;
// Imports for favorites
import models.Favoris;
import services.FavorisService;
import services.UserSession;

import java.time.LocalDate;

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
    private ListView<NewsItem> newsListView; // Utilisez NewsItem au lieu de String


    @FXML
    private TextArea iaResponseArea;

    @FXML
    private VBox newsSection;  // Ou StackPane selon votre choix

    @FXML
    private Button homeBtn, productsBtn, claimsBtn, eventsBtn;
    @FXML
    private ImageView logo;
    @FXML
    private TextField questionField;

    @FXML
    private VBox menuLateral;

    // IA section container (from FXML)
    @FXML
    private VBox iaSection;

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









    // Instance of favorites service
    private FavorisService favorisService = new FavorisService();
    private ServicesCrud service = new ServicesCrud();

    // Navigation methods

    @FXML
    private void handleNewsButtonClick(ActionEvent event) {
        System.out.println("Bouton cliqué !"); // Pour vérifier que l'événement est déclenché
        // Appeler la méthode pour afficher les actualités
        showNewsForArtist("Nom de l'artiste"); // Remplacez par le nom de l'artiste dynamique
        showNewsSection();
    }
    @FXML
    private void goToProfile() {
        int userId = UserSession.getUserId();
        User currentUser = UserService.findById(userId);

        if (currentUser == null) {
            System.out.println("No user logged in!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            AnchorPane profilePane = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setUser(currentUser); // Pass the user to profile

            Stage stage = new Stage();
            stage.setScene(new Scene(profilePane));
            stage.setTitle("Profil");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void showNewsForArtist(String artistName) {
        newsListView.getItems().clear();  // Vider les anciens articles

        new Thread(() -> {
            try {
                String apiKey = "d406712af6ca4b2b9cccc205192638fa";  // Remplacez par votre clé API
                String urlString = "https://newsapi.org/v2/everything?q=" + URLEncoder.encode(artistName, "UTF-8")
                        + "+musique&apiKey=" + apiKey + "&language=fr&pageSize=5&sortBy=relevancy";
                System.out.println("Request URL: " + urlString);  // Vérifiez l'URL générée

                // Connexion et récupération des données
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Debug: Affiche la réponse de l'API
                System.out.println("API Response: " + response.toString());

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray articles = jsonResponse.getJSONArray("articles");

                Platform.runLater(() -> {
                    if (articles.length() == 0) {
                        newsListView.getItems().add(new NewsItem("Aucune actualité musicale disponible pour cet artiste.", ""));
                    } else {
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject article = articles.getJSONObject(i);
                            String title = article.getString("title");
                            String description = article.optString("description", "");  // Description de l'article

                            // Filtrer les articles pour ne garder que ceux liés à la musique
                            if (title.toLowerCase().contains("musique") || description.toLowerCase().contains("musique") ||
                                    title.toLowerCase().contains("album") || description.toLowerCase().contains("album") ||
                                    title.toLowerCase().contains("concert") || description.toLowerCase().contains("concert") ||
                                    title.toLowerCase().contains("chanson") || description.toLowerCase().contains("chanson")) {
                                // Ajoutez les articles sous forme d'objets NewsItem
                                newsListView.getItems().add(new NewsItem(title, description));
                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    newsListView.getItems().add(new NewsItem("Erreur lors du chargement des actualités.", ""));
                });
            }
        }).start();
    }


    private void hideNewsSection() {
        newsSection.setVisible(false);  // Cache la section des actualités
        newsListView.getItems().clear();  // Efface les anciens articles
    }





    private void showNewsSection() {
        newsSection.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newsSection);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
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
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
            loadProduits(newValue.trim());
        });
    }

    private void setupMenu() {
        menuLateral.getChildren().clear();
        menuLateral.setStyle("-fx-background-color: #1E1E1E; -fx-padding: 15; -fx-background-radius: 10;");
        Label title = new Label("Catégories");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        // The menu buttons in FXML call handleMenuClick, so we use that handler.
        Button albumsBtn = new Button("•  Albums");
        albumsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 14px; -fx-font-style: italic;");
        albumsBtn.setOnAction(this::handleMenuClick);
        Button vetementsBtn = new Button("•  Vêtements");
        vetementsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 14px; -fx-font-style: italic;");
        vetementsBtn.setOnAction(this::handleMenuClick);
        Button accessoiresBtn = new Button("•  Accessoires");
        accessoiresBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 14px; -fx-font-style: italic;");
        accessoiresBtn.setOnAction(this::handleMenuClick);
        Button lightsticksBtn = new Button("•  Lightsticks");
        lightsticksBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B0B0; -fx-font-size: 14px; -fx-font-style: italic;");
        lightsticksBtn.setOnAction(this::handleMenuClick);
        menuLateral.getChildren().addAll(title, albumsBtn, vetementsBtn, accessoiresBtn, lightsticksBtn);
    }

    // Handler for menu button clicks
    @FXML
    private void handleMenuClick(ActionEvent event) {
        Button btn = (Button) event.getSource();
        // Remove the bullet "•" and trim the text
        String category = btn.getText().replace("•", "").trim();
        loadProduitsByCategory(category);
    }

    private void loadProduitsByCategory(String category) {
        promoTilePane.getChildren().clear();
        List<Produit> produits = service.recuperer();
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

            // Create a normal button with text "add to favorite"
            Button favorisButton = createFavorisButton(produit);
            Button addcart = new Button("➕ To Cart");
            addcart.getStyleClass().add("add-button");
            addcart.setOnMouseEntered(event -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(200), addcart);
                st.setToX(1.05);
                st.setToY(1.05);
                st.play();
            });

            addcart.setOnMouseExited(event -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(200), addcart);
                st.setToX(1);
                st.setToY(1);
                st.play();
            });
            // Ajouter le bouton pour afficher les actualités
            Button newsButton = new Button("Actualités de l'artiste");
            newsButton.getStyleClass().add("news-button");
            newsButton.setId("newsButton");
            newsButton.setOnMouseClicked(event -> {
                String artistName = produit.getDescription(); // Assure-toi que ton Produit a bien cet attribut
                showNewsForArtist(artistName); // Appelle ta méthode existante
                showNewsSection(); // Pour rendre visible la section news
            });






            productCard.getChildren().addAll(imageView, title, priceContainer, categoryLabel, availabilityLabel, favorisButton,addcart,newsButton);
            if (promoTag != null) {
                productCard.getChildren().add(1, promoTag);
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

                // Create a normal button with text "add to favorite"
                Button favorisButton = createFavorisButton(produit);

                productCard.getChildren().addAll(imageView, title, priceContainer, category, availability, favorisButton);
                if (promoTag != null) {
                    productCard.getChildren().add(1, promoTag);
                }
                promoTilePane.getChildren().add(productCard);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des produits : " + e.getMessage());
        }
    }

    // Method to create a normal "add to favorite" button
    private Button createFavorisButton(Produit produit) {
        Button favorisButton = new Button("add to favorite");
        favorisButton.setOnAction(event -> toggleFavorite(produit));
        return favorisButton;
    }

    // Method to add a product to favorites (the favorites ID is auto-incremented in the database)
    private void toggleFavorite(Produit produit) {
        try {
            if (LoginController.UserConnected == null) {
                throw new Exception("Aucun utilisateur connecté. Veuillez vous connecter.");
            }
            int idUtilisateur = LoginController.UserConnected.getId();
            int idProduit = produit.getIdproduit();
            if (favorisService.favoriExiste(idUtilisateur, idProduit)) {
                System.out.println("Ce favori existe déjà!");
                return;
            }
            Favoris newFavoris = new Favoris(0, idProduit, idUtilisateur, LocalDate.now());
            favorisService.ajouter(newFavoris);
            System.out.println(produit.getNom() + " a été ajouté aux favoris.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout aux favoris: " + e.getMessage());
        }
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
        if (description != null && description.startsWith("Album de ")) {
            return description.substring("Album de ".length());
        }
        return null;
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
                    .version(HttpClient.Version.HTTP_2)
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:11435/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            new JSONObject()
                                    .put("model", "gemma:2b")
                                    .put("prompt", question)
                                    .put("max_tokens", 100)
                                    .toString()
                    ))
                    .build();
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                StringBuilder responseText = new StringBuilder();
                int tokenCounter = 0;
                while (true) {
                    String line = reader.readLine();
                    if (line == null) break;
                    JSONObject json = new JSONObject(line);
                    if (json.has("response")) {
                        responseText.append(json.getString("response"));
                        tokenCounter++;
                        if (tokenCounter % 5 == 0) {
                            String finalText = responseText.toString();
                            javafx.application.Platform.runLater(() -> iaResponseArea.setText(finalText));
                        }
                    }
                }
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
            produit.setPrix((float) prixRemise);
            produit.setPromotion(promotion);
            System.out.println("Promotion appliquée : " + promotion.getCode() + " (" + promotion.getPourcentage() + "%)");
        }
    }

    private String getYouTubePreviewUrl(Produit produit) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String albumName = produit.getNom();
            String artistName = extractArtistName(produit.getDescription());
            String query = URLEncoder.encode(albumName + " " + artistName, StandardCharsets.UTF_8.toString());
            String apiKey = "AIzaSyDC5k1-zp1VZcNJ0jMr7n8HL1rHvZsI4Yw";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + query + "&type=video&key=" + apiKey))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Réponse de l'API YouTube : " + response.body());
            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONArray items = jsonResponse.getJSONArray("items");
                if (items.length() > 0) {
                    String videoId = items.getJSONObject(0).getJSONObject("id").getString("videoId");
                    return "https://www.youtube.com/watch?v=" + videoId;
                }
            } else {
                System.out.println("Erreur lors de la recherche YouTube : " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ----- IA Icon Handlers -----
    @FXML
    private void toggleIaSection(ActionEvent event) {
        // Toggle the visibility of the IA section
        if (iaSection.isVisible()) {
            iaSection.setVisible(false);
            iaSection.setManaged(false);
        } else {
            iaSection.setVisible(true);
            iaSection.setManaged(true);
        }
    }

    @FXML
    private void handleIconPress(MouseEvent event) {
        // Optionally record initial position for dragging
        System.out.println("IA Icon pressed.");
    }

    @FXML
    private void handleIconDrag(MouseEvent event) {
        // Optionally update the position of the IA container
        System.out.println("IA Icon is being dragged.");
    }

    @FXML
    private void handleIconRelease(MouseEvent event) {
        // Optionally finalize the drag action
        System.out.println("IA Icon drag released.");
    }
}
