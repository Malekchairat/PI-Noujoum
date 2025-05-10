package Controllers;

<<<<<<< HEAD
<<<<<<< HEAD
import javafx.collections.FXCollections;
=======
>>>>>>> origin/integration-branch
=======
import API.EventLocation;
import API.PaymentService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
>>>>>>> origin/GestionCommande
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
<<<<<<< HEAD
<<<<<<< HEAD
import javafx.scene.control.ComboBox;
=======
>>>>>>> origin/integration-branch
import javafx.scene.control.TextField;
=======
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
>>>>>>> origin/GestionCommande
import javafx.stage.Stage;
import models.Commande;
import models.Panier;
import models.Produit;
import models.User;
import org.apache.commons.io.IOUtils;
import services.CommandeService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.scene.control.Button;

import java.awt.Desktop;
import java.io.*;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.UserService;
import utils.Mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.stream.Location;

public class AjouterCommande {

    @FXML
<<<<<<< HEAD
<<<<<<< HEAD
    private TextField idPanier, rue, ville, codePostal, etat, montantTotal, idUser;

    @FXML
    private ComboBox<String> methodePaiment;
=======
    private TextField idPanier, rue, ville, codePostal, etat, montantTotal, methodePaiment, idUser;
>>>>>>> origin/integration-branch
=======
    private TextField idPanier, rue, ville, codePostal, etat, montantTotal, idUser;
    @FXML
    private WebView paymentWebView;
    @FXML
    private TextArea produit;
    @FXML
    private ComboBox<String> methodePaiment;
    @FXML
    private Button email;
    @FXML
    private Button ajout;
    @FXML
    private Button pdf;
    @FXML
    private Button maps;

    public void setIdPanier(int id_panier) {
        idPanier.setText(String.valueOf(id_panier));
    }

    public WebView getPaymentWebView() {
        return paymentWebView;
    }
>>>>>>> origin/GestionCommande

    @FXML
    public void initialize() {
        email.setDisable(true);
        idPanier.setVisible(false);
        idPanier.setEditable(false);
        // Set idUser always to 1 and make it non-editable.
        idUser.setText("1");
        idUser.setEditable(false);
        idUser.setVisible(false);
    }

    public void setCartItems(List<Panier> paniers, List<Produit> produits) {
        double totalPrice = 0.0;
        StringBuilder productNames = new StringBuilder();

        for (int i = 0; i < paniers.size(); i++) {
            Panier panier = paniers.get(i);
            Produit produitObj = produits.get(i);
            totalPrice += panier.getNbr_produit() * produitObj.getPrix();
            productNames.append(produitObj.getNom())
                    .append(" (Quantity: ")
                    .append(panier.getNbr_produit())
                    .append(")\n");
        }
        setIdPanier(34);
        setMontantTotal(totalPrice);
        this.produit.setText(productNames.toString());
    }

    @FXML
    void ajout(ActionEvent event) throws Exception {
        try {
            CommandeService commandeCrud = new CommandeService();
            UserService userService = new UserService();

<<<<<<< HEAD
<<<<<<< HEAD
            if (!isValidInput()) {
=======
            if (idPanier.getText().isEmpty() || rue.getText().isEmpty() || ville.getText().isEmpty() ||
                    codePostal.getText().isEmpty() || etat.getText().isEmpty() || montantTotal.getText().isEmpty() ||
                    methodePaiment.getText().isEmpty() || idUser.getText().isEmpty()) {

=======
            if (!isValidInput()) {
>>>>>>> origin/GestionCommande
                showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
>>>>>>> origin/integration-branch
                return;
            }

            String montantTotalText = montantTotal.getText().trim().replace(",", ".");
            // We ignore any input from idUser and always use value 1.
            int idPanierInt = Integer.parseInt(idPanier.getText().trim());
            float montantTotalFloat = Float.parseFloat(montantTotalText);
            int idUserInt = 1;  // Force the user ID to be 1

            if (!commandeCrud.isPanierExists(idPanierInt)) {
                showAlert("Erreur", "Le panier avec l'ID " + idPanierInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

            if (!commandeCrud.isUserExists(idUserInt)) {
                showAlert("Erreur", "L'utilisateur avec l'ID " + idUserInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

<<<<<<< HEAD
            // Check if the Panier and User exist
            if (!commandeCrud.isPanierExists(idPanierInt)) {
                showAlert("Erreur", "Le panier avec l'ID " + idPanierInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

            if (!commandeCrud.isUserExists(idUserInt)) {
                showAlert("Erreur", "L'utilisateur avec l'ID " + idUserInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

            // Proceed with adding the order
            Commande commande = new Commande(
                    0, // ID auto-généré
                    idPanierInt,
                    rue.getText(),
                    ville.getText(),
                    codePostal.getText(),
                    etat.getText(),
                    montantTotalFloat,
                    methodePaiment.getValue(), // Récupère la valeur sélectionnée du ComboBox
                    idUserInt
            );

            commandeCrud.ajouter(commande);
            showAlert("Succès", "Commande ajoutée avec succès !", AlertType.INFORMATION);
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide pour les champs numériques.", AlertType.ERROR);
        } catch (Exception e) {
=======
            Commande commande = new Commande(
                    0, idPanierInt, rue.getText(), ville.getText(), codePostal.getText(),
                    etat.getText(), montantTotalFloat, methodePaiment.getValue(),
                    idUserInt, produit.getText()
            );

            commandeCrud.ajouter(commande);
            showAlert("Succès", "Commande ajoutée avec succès !", AlertType.INFORMATION);

            email.setDisable(false);

            initiatePayment();

            clearFields();

        } catch (NumberFormatException e) {
<<<<<<< HEAD
            showAlert("Erreur", "Format invalide pour les champs numériques : " + e.getMessage(), AlertType.ERROR);
        } catch (SQLException e) {
>>>>>>> origin/integration-branch
            showAlert("Erreur SQL", "Une erreur s'est produite : " + e.getMessage(), AlertType.ERROR);
        }
    }

<<<<<<< HEAD

    private boolean isValidInput() {
        if (idPanier.getText().isEmpty() || rue.getText().isEmpty() || ville.getText().isEmpty() ||
                codePostal.getText().isEmpty() || etat.getText().isEmpty() || montantTotal.getText().isEmpty() ||
                methodePaiment.getValue() == null || idUser.getText().isEmpty()) { // Vérifie si une méthode de paiement est sélectionnée
=======
            showAlert("Erreur", "Format invalide pour les champs numériques. Veuillez entrer des valeurs valides.", AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage(), AlertType.ERROR);
        }
    }

    private boolean isValidInput() {
        if (idPanier.getText().isEmpty() || rue.getText().isEmpty() || ville.getText().isEmpty() ||
                codePostal.getText().isEmpty() || etat.getText().isEmpty() || montantTotal.getText().isEmpty() ||
                methodePaiment.getValue() == null) {  // Removed idUser check since it's always set to 1.
>>>>>>> origin/GestionCommande
            showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
            return false;
        }

        if (!codePostal.getText().matches("\\d{4,5}")) {
            showAlert("Erreur", "Le code postal doit être composé de 4 ou 5 chiffres.", AlertType.ERROR);
            return false;
        }

        if (!isAlphabetic(rue.getText()) || !isAlphabetic(ville.getText()) || !isAlphabetic(etat.getText())) {
            showAlert("Erreur", "Les champs rue, ville et état ne doivent contenir que des lettres.", AlertType.ERROR);
            return false;
        }
<<<<<<< HEAD

        try {
            Integer.parseInt(idPanier.getText());
            Float.parseFloat(montantTotal.getText());
            Integer.parseInt(idUser.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les champs ID Panier, Montant Total et ID User doivent être numériques.", AlertType.ERROR);
            return false;
        }

=======
>>>>>>> origin/GestionCommande
        return true;
    }

    private boolean isAlphabetic(String text) {
<<<<<<< HEAD
        return text.matches("[a-zA-Z\u00C0-\u017F ]+"); // Accepte les lettres + accents + espaces
=======
        return text.matches("[a-zA-Z\u00C0-\u017F ]+");
>>>>>>> origin/GestionCommande
    }

    private void clearFields() {
        idPanier.clear();
        rue.clear();
        ville.clear();
        codePostal.clear();
        etat.clear();
<<<<<<< HEAD
        montantTotal.clear();
        methodePaiment.getSelectionModel().clearSelection(); // Efface la sélection du ComboBox
        idUser.clear();
    }

=======
>>>>>>> origin/integration-branch
=======
        methodePaiment.getSelectionModel().clearSelection();
        // Reset idUser to 1 after clearing fields.
        idUser.setText("1");
    }

>>>>>>> origin/GestionCommande
    @FXML
    void afficherCommandes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
<<<<<<< HEAD
<<<<<<< HEAD
            stage.setTitle("Liste des Commandes");
            stage.show();
        } catch (IOException e) {
=======
            stage.setTitle("Liste   des Commandes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
>>>>>>> origin/integration-branch
=======
            stage.setTitle("Liste des Commandes");
            stage.show();
        } catch (IOException e) {
>>>>>>> origin/GestionCommande
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AfficherCommande.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
<<<<<<< HEAD
<<<<<<< HEAD

    @FXML
    private void ajouterPanier(ActionEvent event) {
        try {
=======
    @FXML
    private void ajouterPanier(ActionEvent event) {
        try {
            // Charger la fenêtre AjouterPanier
>>>>>>> origin/integration-branch
=======

    private double totalPrice;

    public void setMontantTotal(double totalPrice) {
        this.totalPrice = totalPrice;
        montantTotal.setText(String.format("%.2f", totalPrice));
        montantTotal.setEditable(false);
    }

    @FXML
    private void ajouterPanier(ActionEvent event) {
        try {
>>>>>>> origin/GestionCommande
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPanier.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Ajouter un Panier");
<<<<<<< HEAD
<<<<<<< HEAD
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AjouterPanier.", Alert.AlertType.ERROR);
        }
    }
}
=======

            // Afficher la fenêtre
=======
>>>>>>> origin/GestionCommande
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AjouterPanier.", AlertType.ERROR);
        }
    }

    @FXML
    void sendBtnOnAction(ActionEvent event) throws MessagingException {
        UserService userService = new UserService();
        // Retrieve the user with id 1
        User utilisateur = userService.getById(1);

        // Check if the user and email are valid
        if (utilisateur == null || utilisateur.getEmail() == null || utilisateur.getEmail().isEmpty()) {
            showAlert("Erreur", "L'utilisateur ou son adresse email n'a pas été trouvé.", Alert.AlertType.ERROR);
            return;
        }

        // Use the email from the user
        String recipientEmail = utilisateur.getEmail();
        sendEmail(recipientEmail);

        produit.clear();
        montantTotal.clear();
    }


    private void sendEmail(String recipientEmail) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "*");

        String myEmail = "hedifridhy@gmail.com";
        String myPassword = "xrfq cctt mibr upru";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, myPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Votre commande NOUJOUM est en route… et une surprise vous attend !");
            message.setText("Bonjour star de NOUJOUM !\n\n" +
                    "Votre commande a été ajoutée avec succès !\n\n" +
                    "Voici votre liste de produits : \n" +
                    "- " + produit.getText().replace(", ", "\n- ") + "\n\n" +
                    "Montant total : " + montantTotal.getText() + " DT\n\n" +
                    "Nous espérons que ces articles illumineront votre quotidien. Et psst… une surprise vous attend peut-être sur votre prochaine commande !\n\n" +
                    "Restez à l'affût et continuez à briller avec NOUJOUM !\n\n" +
                    "À très bientôt,\n" +
                    "L’équipe NOUJOUM");
            Transport.send(message);
            new Alert(Alert.AlertType.INFORMATION, "Email envoyé avec succès !").show();
        } catch (MessagingException e) {
            new Alert(Alert.AlertType.ERROR, "Échec de l'envoi de l'email : " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private Message prepareMessage(Session session, String myEmail, String recipientEmail) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Commande ajoutée");
            message.setText("Une nouvelle commande a été ajoutée.");
            return message;
        } catch (MessagingException e) {
            Logger.getLogger(AjouterCommande.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @FXML
    private void initiatePayment() {
        if (paymentWebView == null) {
            System.out.println("WebView is not initialized in the main controller.");
        }
        setMontantTotal(totalPrice);
        System.out.println("Initiating payment...");
        long amount = (long) (totalPrice * 100); // Convert to cents for Stripe
        String currency = "usd";
        System.setProperty("java.net.useSystemProxies", "true");

        String clientSecret = PaymentService.createPaymentIntent(amount, currency);

        if (clientSecret != null) {
            String paymentIntentId = clientSecret.split("_secret")[0];
            String stripeUrl = "https://dashboard.stripe.com/test/payments/" + paymentIntentId;
            loadStripePaymentPage(stripeUrl);
        } else {
            showAlert("Payment Error", "Failed to create Payment Intent.", AlertType.ERROR);
        }
    }

    private void loadStripePaymentPage(String stripeUrl) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paymentview.fxml"));
            AnchorPane root = loader.load();

            WebView webView = (WebView) root.lookup("#paymentWebView");
            if (webView == null) {
                AjouterCommande controller = loader.getController();
                webView = controller.getPaymentWebView();
            }
            if (webView == null) {
                System.out.println("WebView is not initialized in the loaded FXML.");
                return;
            }
            webView.getEngine().load(stripeUrl);

            Stage paymentStage = new Stage();
            paymentStage.setTitle("Stripe Payment");
            Scene paymentScene = new Scene(root, 800, 800);
            paymentStage.setScene(paymentScene);
            paymentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page de paiement.", AlertType.ERROR);
        }
    }

    private void showAlert(AlertType alertType, String erreur, String s) {
        // Overloaded method (empty implementation if needed)
    }

    @FXML
    private void generateWelcomePdf(ActionEvent event) {
        String userName = "Hedy Fridhi";
        String street = rue.getText();
        String city = ville.getText();
        String postalCode = codePostal.getText();
        String state = etat.getText();
        String totalAmount = montantTotal.getText().replace(",", ".").replaceAll("[^0-9.]", "");

        List<String> productList = Arrays.asList(produit.getText().split("\n"));

        String userHome = System.getProperty("user.home");
        String filePath = userHome + "/Downloads/Facture_NOUJOUM_" + userName + ".pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            InputStream logoStream = getClass().getResourceAsStream("/images/logonjm.jpg");
            if (logoStream != null) {
                Image logo = Image.getInstance(IOUtils.toByteArray(logoStream));
                logo.scaleToFit(100, 100);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            } else {
                System.out.println("Erreur : Image non trouvée !");
            }

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0, 102, 204));
            Paragraph title = new Paragraph("Facture NOUJOUM", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            Font userInfoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            document.add(new Paragraph("👤 Client : " + userName, userInfoFont));
            document.add(new Paragraph("📍 Adresse : " + street + ", " + city + " " + postalCode + ", " + state, userInfoFont));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell productHeader = new PdfPCell(new Phrase("Produit", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
            PdfPCell priceHeader = new PdfPCell(new Phrase("", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
            productHeader.setBackgroundColor(new BaseColor(50, 50, 50));
            priceHeader.setBackgroundColor(new BaseColor(50, 50, 50));
            productHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            priceHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(productHeader);
            table.addCell(priceHeader);

            for (String item : productList) {
                PdfPCell productName = new PdfPCell(new Phrase(item));
                productName.setHorizontalAlignment(Element.ALIGN_LEFT);
                productName.setColspan(2);
                table.addCell(productName);
            }
            System.out.println("Valeur de totalAmount avant formatage : " + totalAmount);

            PdfPCell totalLabel = new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE)));
            PdfPCell totalValue = new PdfPCell(new Phrase("Total: " + totalAmount + "DT"));
            totalLabel.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabel.setBackgroundColor(new BaseColor(230, 230, 250));
            table.addCell(totalLabel);
            table.addCell(totalValue);

            document.add(table);

            Font italicFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, new BaseColor(80, 80, 80));
            document.add(new Paragraph("\nMerci pour votre confiance !", italicFont));
            document.add(new Paragraph("L’équipe NOUJOUM vous souhaite une excellente journée ! 🚀", italicFont));

            document.close();
            document.close();

            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("Erreur : le fichier PDF n'a pas été trouvé.");
            }

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }





    @FXML
    private void showCityOnMap() {
        // Get the city name from the TextField named 'ville'
        String city = ville.getText(); // This gets the text content of the 'ville' TextField

        // Debug: Check if the 'ville' field is empty
        if (city.isEmpty()) {
            System.out.println("Error: City name is empty.");
            return;
        }

        // Create an instance of the EventLocation class to retrieve latitude and longitude
        EventLocation location = new EventLocation();

        // Debug: Log the city name
        System.out.println("City Name: " + city);

        double lat = location.getLatitude(city);  // Pass the city name (String)
        double lng = location.getLongitude(city); // Pass the city name (String)

        // Debug: Log the coordinates
        System.out.println("Latitude: " + lat);
        System.out.println("Longitude: " + lng);

        // If the coordinates are valid (not 0, 0), show the city on the map
        if (lat != 0 && lng != 0) {
            // Construct the HTML for the map with the coordinates of the selected city
            String html = String.format(Locale.US, """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8" />
            <title>City Map</title>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
        </head>
        <body>
            <div id="map" style="width: 100%%; height: 600px;"></div>
            <script>
                var map = L.map('map').setView([%.6f, %.6f], 13);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                }).addTo(map);
                
                // Add a marker at the city coordinates
                L.marker([%.6f, %.6f]).addTo(map)
                    .bindPopup("<b>%s</b>")
                    .openPopup();
            </script>
        </body>
        </html>
        """, lat, lng, lat, lng, city);

            // Debugging: Log the generated HTML
            System.out.println("Generated HTML:\n" + html);

            // Create a WebView to show the map
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.setJavaScriptEnabled(true);

            // Load the HTML content directly
            webEngine.loadContent(html);

            // Show the map in a new stage
            Stage mapStage = new Stage();
            mapStage.initModality(Modality.APPLICATION_MODAL);
            mapStage.setTitle("City Map");
            mapStage.setScene(new Scene(webView, 800, 600));
            mapStage.show();
        } else {
            // If coordinates are invalid (0, 0), show an error
            System.out.println("Error: Invalid coordinates for the city.");
        }
    }



}
>>>>>>> origin/integration-branch
