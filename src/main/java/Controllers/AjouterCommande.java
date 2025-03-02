package Controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.Commande;
import models.Panier;
import models.Produit;
import services.CommandeService;
import java.util.List;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private TextArea produit;
    @FXML
    private ComboBox<String> methodePaiment;
    @FXML
    private Button email;
    @FXML
    public void initialize() {
        email.setDisable(true);
    }


    public void setIdPanier(int id_panier) {
        idPanier.setText(String.valueOf(id_panier));
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

        setMontantTotal(totalPrice);
        this.produit.setText(productNames.toString());
    }

    @FXML
    void ajout(ActionEvent event) throws Exception {
        try {
            CommandeService commandeCrud = new CommandeService();
            UserService userService = new UserService(); // Create an instance of UserService


            if (!isValidInput()) {
                return;
            }

            String idPanierText = idPanier.getText().trim();
            String montantTotalText = montantTotal.getText().trim().replace(",", ".");
            String idUserText = idUser.getText().trim();

            int idPanierInt = Integer.parseInt(idPanierText);
            float montantTotalFloat = Float.parseFloat(montantTotalText);
            int idUserInt = Integer.parseInt(idUserText);



            if (!commandeCrud.isPanierExists(idPanierInt)) {
                showAlert("Erreur", "Le panier avec l'ID " + idPanierInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

            if (!commandeCrud.isUserExists(idUserInt)) {
                showAlert("Erreur", "L'utilisateur avec l'ID " + idUserInt + " n'existe pas.", AlertType.ERROR);
                return;
            }

            Commande commande = new Commande(
                    0, idPanierInt, rue.getText(), ville.getText(), codePostal.getText(),
                    etat.getText(), montantTotalFloat, methodePaiment.getValue(),
                    idUserInt, produit.getText()
            );

            commandeCrud.ajouter(commande);
            showAlert("Succès", "Commande ajoutée avec succès !", AlertType.INFORMATION);

            // Activer le bouton après l'ajout
            email.setDisable(false);
            println("Bouton activé");

            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide pour les champs numériques. Veuillez entrer des valeurs valides.", AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur SQL", "Une erreur s'est produite : " + e.getMessage(), AlertType.ERROR);
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

    public void setMontantTotal(double totalPrice) {
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

}
