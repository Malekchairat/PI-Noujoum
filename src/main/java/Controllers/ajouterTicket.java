<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/Controllers/ajouterTicket.java
package Controllers;

import API.PaymentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import models.Evenement;
import models.QRCodeGenerator;
import models.Ticket;
import models.Type_P;
import services.TicketService;
import java.io.IOException;
import java.sql.SQLException;
import models.User;
import com.google.zxing.WriterException;
=======
package controller;

=======
package Controllers;

>>>>>>> origin/GestionCommande
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Evenement;
import models.Ticket;
import models.Type_P;
import services.TicketService;
<<<<<<< HEAD
>>>>>>> origin/integration-branch:src/main/java/controller/ajouterTicket.java
=======
>>>>>>> origin/GestionCommande

public class ajouterTicket {

    @FXML private Label lblEventName;
    @FXML private TextField prixField;
    @FXML private TextField quantiteField;
    @FXML private ComboBox<String> paiementCombo;
<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/Controllers/ajouterTicket.java
    @FXML private ImageView qrCodeImageView;
    // The paymentWebView defined in the FXML for the main view may not be used here.
    // We'll load the payment view separately.
    @FXML private Button ajout;

    private final TicketService ticketService = new TicketService();
    private Evenement evenement;
    private Ticket ticketEnCours; // This ticket is created using the user's input
=======

    private final TicketService ticketService = new TicketService();
    private Evenement evenement; // L'événement sélectionné
>>>>>>> origin/integration-branch:src/main/java/controller/ajouterTicket.java
=======

    private final TicketService ticketService = new TicketService();
    private Evenement evenement; // L'événement sélectionné
>>>>>>> origin/GestionCommande

    public void initData(Evenement evenement) {
        this.evenement = evenement;
        lblEventName.setText("Réservation pour : " + evenement.getArtist());

<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/Controllers/ajouterTicket.java
        ajout.setOnAction(event -> ajouterTicket(event)); // Associate the button with the reservation

        // (Optional) Warn if the main view's WebView is not injected
        // (We will load a separate payment view FXML for payment)
        // if (paymentWebView == null) { System.out.println("WebView is not initialized"); }

        prixField.setEditable(false); // Disable manual editing of price

        // Populate the ComboBox with payment method types
        paiementCombo.getItems().clear();
        for (Type_P type : Type_P.values()) {
            paiementCombo.getItems().add(type.getTypeString());
        }
        paiementCombo.setValue(Type_P.CASH.getTypeString());

        // Listener to recalculate total price when quantity changes
        quantiteField.textProperty().addListener((observable, oldValue, newValue) -> calculerPrixTotal());
    }

    private void calculerPrixTotal() {
        try {
            int quantite = Integer.parseInt(quantiteField.getText().trim());
            if (quantite > 0) {
                float prixTotal = evenement.getPrice() * quantite; // Unit price * quantity
                prixField.setText(String.format("%.2f", prixTotal)); // Display with 2 decimals
            } else {
                prixField.setText("0.00");
            }
        } catch (NumberFormatException e) {
            prixField.setText("0.00");
        }
=======
=======
>>>>>>> origin/GestionCommande
        // Ajouter les valeurs de l'énumération Type_P dans la ComboBox
        for (Type_P type : Type_P.values()) {
            paiementCombo.getItems().add(type.getTypeString());
        }

        paiementCombo.setValue(Type_P.CASH.getTypeString()); // Valeur par défaut
<<<<<<< HEAD
>>>>>>> origin/integration-branch:src/main/java/controller/ajouterTicket.java
=======
>>>>>>> origin/GestionCommande
    }

    @FXML
    public void ajouterTicket(ActionEvent event) {
        try {
<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/Controllers/ajouterTicket.java
            if (quantiteField.getText().trim().isEmpty() || paiementCombo.getValue() == null) {
                throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires.");
            }

            int quantite = Integer.parseInt(quantiteField.getText().trim());
            if (quantite <= 0) {
                throw new IllegalArgumentException("La quantité doit être un nombre positif.");
            }

=======
=======
>>>>>>> origin/GestionCommande
            // Validation des champs
            if (quantiteField.getText().trim().isEmpty() || prixField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis.");
            }

            int quantite = Integer.parseInt(quantiteField.getText().trim());
            float prix = Float.parseFloat(prixField.getText().trim());

            if (quantite <= 0 || prix <= 0) {
                throw new IllegalArgumentException("La quantité et le prix doivent être positifs.");
            }

            // Vérification de l'événement
<<<<<<< HEAD
>>>>>>> origin/integration-branch:src/main/java/controller/ajouterTicket.java
=======
>>>>>>> origin/GestionCommande
            if (evenement == null) {
                throw new NullPointerException("Aucun événement sélectionné.");
            }

<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/Controllers/ajouterTicket.java
            if (LoginController.UserConnected == null) {
                throw new Exception("Aucun utilisateur connecté. Veuillez vous connecter.");
            }

            int idUtilisateur = LoginController.UserConnected.getId();
            System.out.println("ID utilisateur récupéré : " + idUtilisateur);

            // Verify that the user exists in the database
            if (!ticketService.utilisateurExiste(idUtilisateur)) {
                throw new Exception("Utilisateur invalide. Veuillez vous reconnecter.");
            }

            float prixTotal = evenement.getPrice() * quantite; // Calculate total price
            Type_P typePaiement = Type_P.valueOf(paiementCombo.getValue().toUpperCase());

            String qrCodeData = "Ticket | Événement: " + evenement.getIdEvenement() +
                    " | Utilisateur: " + idUtilisateur +
                    " | Prix: " + prixTotal + " TND " +
                    " | Quantité: " + quantite;

            String qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(qrCodeData, 150, 150);
            // Create the ticket using the constructor with the collected information
            ticketEnCours = new Ticket(0, evenement.getIdEvenement(), idUtilisateur, prixTotal, quantite, qrCodeBase64, typePaiement);

            ticketService.ajouter(ticketEnCours); // Save the ticket

            Image qrImage = new Image("data:image/png;base64," + qrCodeBase64);
            if (qrCodeImageView != null) {
                qrCodeImageView.setImage(qrImage);
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ticket réservé avec succès ! QR Code généré.");
            // Instead of closing the stage, initiate payment now
            initiatePayment();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la génération du QR Code.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer une quantité valide.");
=======
            // Création du ticket avec un id_utilisateur fictif (Remplacez par un utilisateur réel)
            int idUtilisateur = 0;
=======
            // Création du ticket avec un id_utilisateur fictif (Remplacez par un utilisateur réel)
            int idUtilisateur = 27;
>>>>>>> origin/GestionCommande
            Type_P typePaiement = Type_P.valueOf(paiementCombo.getValue().toUpperCase());

            Ticket ticket = new Ticket(0, evenement.getIdEvenement(), idUtilisateur, prix, quantite, null, typePaiement);

<<<<<<< HEAD
            // Ajout via le service
            ticketService.ajouter(evenement, ticket);
=======
            // Utilisation de la bonne méthode d'ajout
            ticketService.ajouterAvecEvenement(evenement, ticket);
>>>>>>> origin/GestionCommande

            // Succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ticket réservé avec succès !");

            // Fermer la fenêtre
            ((Stage) prixField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer des valeurs numériques valides.");
<<<<<<< HEAD
>>>>>>> origin/integration-branch:src/main/java/controller/ajouterTicket.java
=======
>>>>>>> origin/GestionCommande
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la réservation : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/Controllers/ajouterTicket.java

    @FXML
    private void initiatePayment() {
        if (ticketEnCours == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun ticket enregistré pour le paiement.");
            return;
        }

        // Use the ticket instance's getPrix() method
        float totalPrice = ticketEnCours.getPrix();

        long amount = (long) (totalPrice * 10); // Convert to cents for Stripe
        String currency = "usd";

        System.out.println("Initiating payment...");
        String clientSecret = PaymentService.createPaymentIntent(amount, currency);

        if (clientSecret != null) {
            // Extract Payment Intent ID
            String paymentIntentId = clientSecret.split("_secret")[0];
            String stripeUrl = "https://dashboard.stripe.com/test/payments/" + paymentIntentId;
            loadStripePaymentPage(stripeUrl);
        } else {
            showAlert(Alert.AlertType.ERROR, "Payment Error", "Failed to create Payment Intent.");
        }
    }

    /**
     * Instead of using the (possibly null) injected WebView,
     * we load the payment view from its FXML file to ensure the WebView is initialized.
     */
    private void loadStripePaymentPage(String stripeUrl) {
        try {
            // Adjust the path if needed – here it's assumed to be in the Controllers package
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paymentview.fxml"));
            AnchorPane root = loader.load();
            // Get the WebView from the loaded FXML by its fx:id
            WebView webView = (WebView) root.lookup("#paymentWebView");
            if (webView == null) {
                System.out.println("WebView is not initialized in the loaded FXML.");
                return;
            }
            webView.getEngine().load(stripeUrl);

            Stage paymentStage = new Stage();
            paymentStage.setTitle("Stripe Payment");

            Scene paymentScene = new Scene(root, 800, 800); // Adjust size as needed
            paymentStage.setScene(paymentScene);
            paymentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la page de paiement.");
        }
    }
}
=======
}
>>>>>>> origin/integration-branch:src/main/java/controller/ajouterTicket.java
=======
}
>>>>>>> origin/GestionCommande
