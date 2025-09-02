package Controllers;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
//import javafx.scene.media.MediaPlayer;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.Promotion;
import org.json.JSONArray;
import javafx.scene.input.MouseEvent;

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
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private Pane iaContainer;
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
    private StackPane newsFloatingPane;

    @FXML
    private ListView<NewsItem> newsListView; // Utilisez NewsItem au lieu de String


    @FXML
    private TextArea iaResponseArea;

    @FXML
    private VBox newsSection;  // Ou StackPane selon votre choix


    @FXML
    private VBox iaSection;

    @FXML
    private Button iaIcon;

    @FXML
    private Label newsContent;  // Contenu des actualités (ex: description de l'artiste)

    private boolean isNewsVisible = false;

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private void handleIconPress(MouseEvent event) {
        xOffset = event.getSceneX() - iaContainer.getLayoutX();
        yOffset = event.getSceneY() - iaContainer.getLayoutY();
    }




    @FXML
    private void handleIconDrag(MouseEvent event) {
        double newX = event.getSceneX() - xOffset;
        double newY = event.getSceneY() - yOffset;

        iaContainer.setLayoutX(newX);
        iaContainer.setLayoutY(newY);
    }

    @FXML
    private void toggleIaSection() {
        if (iaSection.isVisible()) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), iaSection);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> iaSection.setVisible(false));
            fadeOut.play();
        } else {
            iaSection.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), iaSection);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
    }
    @FXML
    private void handleMenuClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        DropShadow glow = new DropShadow();
        glow.setColor(Color.WHITE);
        glow.setRadius(0); // Démarre sans rayon
        clickedButton.setEffect(glow);

        Timeline glowAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(glow.radiusProperty(), 0)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(glow.radiusProperty(), 30)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(glow.radiusProperty(), 0)
                )
        );
        glowAnimation.setOnFinished(e -> clickedButton.setEffect(null));
        glowAnimation.play();
    }


    @FXML
    private void handleIconRelease(MouseEvent event) {
        // Réinitialiser l'effet visuel lorsque l'icône est relâchée
        iaIcon.setStyle("-fx-text-fill: #FDD700; -fx-font-size: 24px;");
        DropShadow shadow = new DropShadow(10, Color.GOLD);
        iaIcon.setEffect(shadow);
    }

    private ServicesCrud service = new ServicesCrud();
    @FXML
    private VBox menuLateral;
    private static final String ACCOUNT_SID = "AC3b74a9a1cdc84d0001f6c6e49ea011ec";
    private static final String AUTH_TOKEN = "bfc3644ac19e7fb08dae7679f97757be";
    private static final String TWILIO_PHONE_NUMBER = "+12702791467"; // Ton numéro Twilio
    private static final String TON_NUMERO = "+21652164756"; // Ton numéro de téléphone
    @FXML
    public void initialize() {
        setupMenu();
        setupNavigationBar();
        loadProduits("");
        iaIcon.setOnMousePressed(event -> {
            xOffset = event.getSceneX() - iaIcon.getLayoutX();
            yOffset = event.getSceneY() - iaIcon.getLayoutY();
        });
        Image logoImage = new Image(getClass().getResource("/images/njm.png").toExternalForm());
        Button searchButton = new Button("🔍");
        setupButtonHoverEffect(searchButton);
        logo.setImage(logoImage);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadProduits(newValue.trim()); // Met à jour la liste des produits avec le filtre
        });
        newsListView.setCellFactory(param -> new ListCell<NewsItem>() {
            @Override
            protected void updateItem(NewsItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox();
                    Label titleLabel = new Label(item.getTitle());
                    Label descriptionLabel = new Label(item.getDescription());

                    titleLabel.setWrapText(true);
                    descriptionLabel.setWrapText(true);

                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    descriptionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

                    vbox.getChildren().addAll(titleLabel, descriptionLabel);

                    setGraphic(vbox);

                    // Limite la hauteur de chaque cellule
                    setPrefHeight(100);  // Ajuste la hauteur selon vos besoins
                }
            }
        });

    }

    private void setupButtonHoverEffect(Button button) {
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            button.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 10;");
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            button.setStyle("-fx-background-color: #FDD700; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 10;");
        });
    }


    private void animateProductCard(VBox productCard) {
        // Animation de fondu
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), productCard);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        // Animation de zoom
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), productCard);
        scaleTransition.setFromX(0.9);
        scaleTransition.setFromY(0.9);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();
    }
    // Votre méthode showNewsForArtist ici
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

                // Ajouter les effets d'animation à l'étiquette de promotion
                ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), promoTag);
                scaleTransition.setFromX(1);
                scaleTransition.setFromY(1);
                scaleTransition.setToX(1.2);
                scaleTransition.setToY(1.2);
                scaleTransition.setAutoReverse(true);
                scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
                scaleTransition.play();

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), promoTag);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.3);
                fadeTransition.setAutoReverse(true);
                fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
                fadeTransition.play();

            } else {
                priceContainer.getChildren().add(originalPrice);
            }

            Label categoryLabel = new Label("Catégorie: " + produit.getCategorie());
            categoryLabel.getStyleClass().add("product-category");

            Label availabilityLabel = new Label("Disponibilité: " + produit.getDisponibilite());
            availabilityLabel.getStyleClass().add("product-availability");



            Button addcart = new Button("➕ To Cart");
            addcart.getStyleClass().add("add-button");

            Button deleteButton = new Button("🗑️ Supprimer");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setOnAction(event -> {
                service.supprimer(produit.getIdproduit());
                loadProduitsByCategory(category); // Recharger après suppression
            });
            // Ajouter le bouton pour afficher les actualités
            Button newsButton = new Button("Actualités de l'artiste");
            newsButton.getStyleClass().add("news-button");
            newsButton.setId("newsButton");
            productCard.getChildren().addAll(imageView, title, priceContainer, categoryLabel, availabilityLabel, deleteButton,addcart,newsButton);

            if (promoTag != null) {
                productCard.getChildren().add(1, promoTag); // Met la promo en haut si existante
            }

            promoTilePane.getChildren().add(productCard);
        }
    }
    @FXML
    private void goToHome(ActionEvent event) {
        handleMenuClick(event);
        goToHomer(event);


    }
    private void showNewsSection() {
        newsSection.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newsSection);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }


    @FXML
    private void goToHomer(ActionEvent event) {
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
    private void handleNewsButtonClick(ActionEvent event) {
        System.out.println("Bouton cliqué !"); // Pour vérifier que l'événement est déclenché
        // Appeler la méthode pour afficher les actualités
        showNewsForArtist("Nom de l'artiste"); // Remplacez par le nom de l'artiste dynamique
        showNewsSection();
    }

    @FXML
    public void searchProducts() {
        loadProduits(searchField.getText().trim());
    }

    private void loadProduits(String filter) {
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
                animateProductCard(productCard);
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




                productCard.getChildren().addAll(imageView, title, priceContainer, category, availability, deleteButton, addcart,newsButton);

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

        // List of music-related keywords
        String[] musicKeywords = {"album", "song", "artist", "band", "track", "music", "concert", "lyrics"};

        // Check if the question contains any music-related keyword
        boolean isMusicQuestion = false;
        for (String keyword : musicKeywords) {
            if (question.toLowerCase().contains(keyword)) {
                isMusicQuestion = true;
                break;
            }
        }

        if (!question.isEmpty() && isMusicQuestion) {
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
            // Customized message for non-music-related questions
            String message = "Désolé, je ne peux répondre qu'aux questions sur la musique.\n";
            message += "Essayez de poser une question comme :\n";
            message += "- Quelle est la dernière chanson d'Artiste X ?\n";
            message += "- Qui est l'artiste derrière l'album Y ?\n";
            message += "- Quelle est la signification de cette chanson ?";

            iaResponseArea.setText(message);
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
                    new PhoneNumber("+21652164756"), // Numéro de destination
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