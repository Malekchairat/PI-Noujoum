package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import models.Produit;
import models.Produit.Categorie;
import services.ServicesCrud;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

public class updateproduitcontroller {

    @FXML
    private TextField idProduit;

    @FXML
    private TextField nom;

    @FXML
    private TextArea description;

    @FXML
    private ComboBox<Categorie> categorieComboBox;

    @FXML
    private TextField prix;

    @FXML
    private TextField disponibilite;

    @FXML
    private ImageView image;

    @FXML
    private Button browseImage;

    @FXML
    private Button updateButton;

    @FXML
    private Button affich;

    private File selectedImageFile;

    @FXML
    public void initialize() {
        categorieComboBox.getItems().addAll(Categorie.values());
    }

    @FXML
    void browseImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            Image img = new Image(selectedImageFile.toURI().toString());
            image.setImage(img);
        } else {
            showAlert("Erreur", "Aucune image sélectionnée", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void updateProduit(ActionEvent event) {
        try {
            int id = parseInteger(idProduit.getText(), "ID produit invalide");
            String nomProduit = nom.getText().trim();
            String desc = description.getText().trim();
            Categorie selectedCategorie = categorieComboBox.getValue();
            float productPrix = parseFloat(prix.getText(), "Prix invalide");
            int disponibiliteInt = parseInteger(disponibilite.getText(), "Disponibilité invalide");

            if (nomProduit.isEmpty() || desc.isEmpty() || selectedCategorie == null) {
                showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
                return;
            }

            Blob imageBlob = null;
            if (selectedImageFile != null) {
                try (InputStream inputStream = new FileInputStream(selectedImageFile)) {
                    byte[] imageBytes = inputStream.readAllBytes();
                    imageBlob = new SerialBlob(imageBytes);
                }
            }

            Produit produit = new Produit(id, nomProduit, desc, selectedCategorie, productPrix, disponibiliteInt, imageBlob);

            ServicesCrud service = new ServicesCrud();
            service.update(produit);
            showAlert("Succès", "Produit mis à jour avec succès!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Erreur", "Échec de la mise à jour : " + e.getMessage(), Alert.AlertType.ERROR);
        }
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

    private int parseInteger(String value, String errorMessage) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private float parseFloat(String value, String errorMessage) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
