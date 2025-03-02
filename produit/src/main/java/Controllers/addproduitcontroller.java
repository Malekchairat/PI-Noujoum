package Controllers;

import models.Produit;
import models.Produit.Categorie;
import services.ServicesCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

public class addproduitcontroller {

    @FXML
    private TextField nom;

    @FXML
    private TextArea description;

    @FXML
    private ComboBox<Categorie> categorieComboBox;

    @FXML
    private TextField disponibilite;

    @FXML
    private TextField prix;

    @FXML
    private ImageView image;

    @FXML
    private Button ajout;
    @FXML
    private Button affich;
    @FXML
    private Button ajoutPromotion;
    @FXML
    private Button browseImage;

    private File selectedImageFile;

    @FXML
    public void initialize() {
        // Remplir la ComboBox avec les valeurs de l'énumération Categorie
        categorieComboBox.getItems().addAll(Categorie.values());
    }

    @FXML
    void browseImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            Image img = new Image(selectedImageFile.toURI().toString());
            image.setImage(img);
        } else {
            showAlert("Erreur", "Aucune image sélectionnée", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void ajout(ActionEvent event) {
        try {
            // Validation des champs
            String nomProduit = nom.getText().trim();
            String desc = description.getText().trim();
            Categorie selectedCategorie = categorieComboBox.getValue();
            String disp = disponibilite.getText().trim();
            String prixText = prix.getText().trim();

            if (nomProduit.isEmpty() || desc.isEmpty() || selectedCategorie == null || disp.isEmpty() || prixText.isEmpty() || selectedImageFile == null) {
                showAlert("Erreur", "Tous les champs et une image sont requis.", Alert.AlertType.ERROR);
                return;
            }

            int disponibiliteInt;
            try {
                disponibiliteInt = Integer.parseInt(disp);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La disponibilité doit être un entier.", Alert.AlertType.ERROR);
                return;
            }

            float prixValue;
            try {
                prixValue = Float.parseFloat(prixText);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le prix doit être un nombre valide.", Alert.AlertType.ERROR);
                return;
            }

            // Lecture et conversion de l'image en Blob
            Blob imageBlob;
            try (InputStream inputStream = new FileInputStream(selectedImageFile)) {
                byte[] imageBytes = inputStream.readAllBytes();
                imageBlob = new SerialBlob(imageBytes);
            }

            // Création du produit et ajout
            Produit produit = new Produit(
                    0,
                    nomProduit,
                    desc,
                    selectedCategorie,
                    prixValue,
                    disponibiliteInt,
                    imageBlob
            );

            ServicesCrud service = new ServicesCrud();
            service.ajouter(produit);
            showAlert("Succès", "Produit ajouté avec succès!", Alert.AlertType.INFORMATION);

            // Réinitialiser les champs
            resetFields();

        } catch (SQLException | IllegalArgumentException e) {
            showAlert("Erreur", "Échec de l'ajout : " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue est survenue.", Alert.AlertType.ERROR);
        }
    }

    private void resetFields() {
        nom.clear();
        description.clear();
        categorieComboBox.getSelectionModel().clearSelection();
        disponibilite.clear();
        prix.clear();
        image.setImage(null);
        selectedImageFile = null;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficheProduit.fxml"));
            Parent root = loader.load();
            afficheproduitcontroller controller = loader.getController();
            controller.loadProduits();
            affich.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur chargement afficherpromotion.fxml : " + e.getMessage());
        }
    }
    @FXML
    void ajoutPromotion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPromotion.fxml"));
            Parent root = loader.load();
            ajoutPromotion.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur chargement addPromotion.fxml : " + e.getMessage());
        }
    }


}
