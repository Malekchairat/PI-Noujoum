package Controllers;

import API.PaymentService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import models.Commande;
import models.Panier;
import models.Produit;
import org.apache.commons.io.IOUtils;
import services.CommandeService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.awt.Desktop;
import java.io.*;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import services.UserService;
import utils.Mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static java.sql.DriverManager.println;


//import static utils.Mail.prepareMessage;

public class AjouterCommande {

    @FXML
    private TextField idPanier, rue, ville, codePostal, etat, montantTotal, idUser; // Keep all fields
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

    public void setIdPanier(int id_panier) {
        idPanier.setText(String.valueOf(id_panier));
    }
    public WebView getPaymentWebView() {
        return paymentWebView;
    }

    @FXML
    public void initialize() {
        email.setDisable(true);
        idPanier.setVisible(false);
        idPanier.setEditable(false);
// Set this after setting the value
    }





    public void setCartItems(List<Panier> paniers, List<Produit> produits) {
        double totalPrice = 0.0;
        StringBuilder productNames = new StringBuilder();

        for (int i = 0; i < paniers.size(); i++) {
            Panier panier = paniers.get(i);
            Produit produit = produits.get(i);
            totalPrice += panier.getNbr_produit() * produit.getPrix();
            productNames.append(produit.getNom()).append(" (Quantity: ").append(panier.getNbr_produit()).append(")\n");
        }
        setIdPanier(34);

        setMontantTotal(totalPrice);
        this.produit.setText(productNames.toString());
    }

    @FXML
    void ajout(ActionEvent event) throws Exception {
        try {
            CommandeService commandeCrud = new CommandeService();
            UserService userService = new UserService(); // Create an instance of UserService

            if (!isValidInput()) {
                showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
                return;
            }

            String montantTotalText = montantTotal.getText().trim().replace(",", ".");
            String idUserText = idUser.getText().trim();

            // Parsing the input values
            int idPanierInt = Integer.parseInt(idPanier.getText().trim());
            float montantTotalFloat = Float.parseFloat(montantTotalText);
            int idUserInt = Integer.parseInt(idUserText);

            // Check if the Panier exists
            if (!commandeCrud.isPanierExists(idPanierInt)) {
                showAlert("Erreur", "Le panier avec l'ID " + idPanierInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

            // Check if the User exists
            if (!commandeCrud.isUserExists(idUserInt)) {
                showAlert("Erreur", "L'utilisateur avec l'ID " + idUserInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

            // Create the Commande object
            Commande commande = new Commande(
                    0, idPanierInt, rue.getText(), ville.getText(), codePostal.getText(),
                    etat.getText(), montantTotalFloat, methodePaiment.getValue(),
                    idUserInt, produit.getText()
            );

            // Add the Commande to the database
            commandeCrud.ajouter(commande);
            showAlert("Succès", "Commande ajoutée avec succès !", AlertType.INFORMATION);

            // Enable the email button after successful addition
            email.setDisable(false);

            initiatePayment();


            // Clear fields after submission
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide pour les champs numériques. Veuillez entrer des valeurs valides.", AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage(), AlertType.ERROR);
        }
    }





    private boolean isValidInput() {
        if (idPanier.getText().isEmpty() || rue.getText().isEmpty() || ville.getText().isEmpty() ||
                codePostal.getText().isEmpty() || etat.getText().isEmpty() || montantTotal.getText().isEmpty() ||
                methodePaiment.getValue() == null || idUser.getText().isEmpty()) {
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

        // Numeric validation for idUser only
        try {
            Integer.parseInt(idUser.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le champ ID User doit être numérique.", AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isAlphabetic(String text) {
        return text.matches("[a-zA-Z\u00C0-\u017F ]+");
    }

    private void clearFields() {
        idPanier.clear();
        rue.clear();
        ville.clear();
        codePostal.clear();
        etat.clear();
        montantTotal.clear();
        methodePaiment.getSelectionModel().clearSelection();
        idUser.clear();
    }

    @FXML
    void afficherCommandes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Liste des Commandes");
            stage.show();
        } catch (IOException e) {
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

    private double totalPrice;

    public void setMontantTotal(double totalPrice) {
        this.totalPrice = totalPrice; // Set the totalPrice field
        montantTotal.setText(String.format("%.2f", totalPrice));
        montantTotal.setEditable(false); // Keep this field non-editable
    }
    @FXML
    private void ajouterPanier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPanier.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Ajouter un Panier");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre AjouterPanier.", AlertType.ERROR);
        }
    }
    @FXML
    void sendBtnOnAction(ActionEvent event) throws MessagingException {
        String recipientEmail = "hedifridhy@gmail.com";
        sendEmail(recipientEmail);
        produit.clear();

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
                    "- " + produit.getText().replace(", ", "\n- ") + "\n\n" +  // Affichage en liste
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
        // Do not reassign the button's action here.
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
            // Extract Payment Intent ID and build the Stripe URL
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

            // Try to get the WebView from the loaded FXML by lookup.
            WebView webView = (WebView) root.lookup("#paymentWebView");
            if (webView == null) {
                // Alternatively, if the controller is set in paymentview.fxml,
                // get the controller and then the WebView.
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
    }


    @FXML
    private void generateWelcomePdf(ActionEvent event) {
        // Récupération des valeurs des champs de texte
        String userName = "Hedy Fridhi";
        String street = rue.getText();
        String city = ville.getText();
        String postalCode = codePostal.getText();
        String state = etat.getText();
        String totalAmount = montantTotal.getText().replace(",", ".").replaceAll("[^0-9.]", ""); // Supprime tout sauf les chiffres et le point

// Convertir la chaîne de produits en une liste (séparée par des sauts de ligne)
        List<String> productList = Arrays.asList(produit.getText().split("\n"));

        String userHome = System.getProperty("user.home");
        String filePath = userHome + "/Downloads/Facture_NOUJOUM_" + userName + ".pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Ajout d'un logo (remplace "logo.png" par ton fichier réel)
            InputStream logoStream = getClass().getResourceAsStream("/images/logonjm.jpg");
            if (logoStream != null) {
                Image logo = Image.getInstance(IOUtils.toByteArray(logoStream));
                logo.scaleToFit(100, 100);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            } else {
                System.out.println("Erreur : Image non trouvée !");
            }


            // Ajouter le titre du document
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0, 102, 204));
            Paragraph title = new Paragraph("Facture NOUJOUM", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Ajouter les informations de l'utilisateur
            Font userInfoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            document.add(new Paragraph("👤 Client : " + userName, userInfoFont));
            document.add(new Paragraph("📍 Adresse : " + street + ", " + city + " " + postalCode + ", " + state, userInfoFont));
            document.add(new Paragraph("\n"));

            // Ajouter la liste des produits sous forme de tableau
            PdfPTable table = new PdfPTable(2); // 2 colonnes : Produit - Prix
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Style des cellules d'en-tête
            PdfPCell productHeader = new PdfPCell(new Phrase("Produit", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
            PdfPCell priceHeader = new PdfPCell(new Phrase("", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
            productHeader.setBackgroundColor(new BaseColor(50, 50, 50));
            priceHeader.setBackgroundColor(new BaseColor(50, 50, 50));
            productHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            priceHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(productHeader);
            table.addCell(priceHeader);

            // Ajouter les produits commandés
            for (String item : productList) {
                PdfPCell productName = new PdfPCell(new Phrase(item)); // Produit sans prix
                productName.setHorizontalAlignment(Element.ALIGN_LEFT);
                productName.setColspan(2); // Étend sur les 2 colonnes pour éviter d'afficher un prix inutile
                table.addCell(productName);
            }
            System.out.println("Valeur de totalAmount avant formatage : " + totalAmount);

            // Ajouter le total
            PdfPCell totalLabel = new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE)));
            PdfPCell totalValue = new PdfPCell(new Phrase("Total: " + totalAmount + "DT"));
            totalLabel.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabel.setBackgroundColor(new BaseColor(230, 230, 250));
            table.addCell(totalLabel);
            table.addCell(totalValue);

            document.add(table);

            // Message de remerciement
            Font italicFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, new BaseColor(80, 80, 80));
            document.add(new Paragraph("\nMerci pour votre confiance !", italicFont));
            document.add(new Paragraph("L’équipe NOUJOUM vous souhaite une excellente journée ! 🚀", italicFont));

            document.close();
            document.close();


            // Ouvre automatiquement le fichier après création
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

}


