package Controllers;
<<<<<<< HEAD
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import models.Produit;
import services.ServicesCrud;
import java.io.ByteArrayInputStream;
import java.io.IOException;
=======

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
>>>>>>> origin/GestionCommande

public class updateproduitcontroller {

    @FXML
<<<<<<< HEAD
    private TextField idProduit, nom, prix, disponibilite;

    // Correction : utiliser TextArea au lieu de TextField
=======
    private TextField idProduit;

    @FXML
    private TextField nom;

>>>>>>> origin/GestionCommande
    @FXML
    private TextArea description;

    @FXML
<<<<<<< HEAD
    private ImageView image;

    // Optionnel si vous l'utilisez pour la catégorie
    @FXML
    private ComboBox<Produit.Categorie> categorieComboBox;

    @FXML
    private Button update;  // Bouton "Mettre à jour"

    @FXML
    private Button affich;  // Bouton "Afficher"

    @FXML
    public void initialize() {
        int produitId = Produit.getProduitId();
        if (produitId != 0) {
            idProduit.setText(String.valueOf(produitId));
            idProduit.setEditable(false);

            ServicesCrud service = new ServicesCrud();
            Produit produit = service.recupererParId(produitId);

            if (produit != null) {
                nom.setText(produit.getNom());
                description.setText(produit.getDescription());
                prix.setText(String.valueOf(produit.getPrix()));
                disponibilite.setText(String.valueOf(produit.getDisponibilite()));

                // Optionnel : mettre à jour la ComboBox si besoin
                // categorieComboBox.setValue(produit.getCategorie());

                if (produit.getImage() != null) {
                    try {
                        byte[] imageData = produit.getImage().getBytes(1, (int) produit.getImage().length());
                        Image img = new Image(new ByteArrayInputStream(imageData));
                        image.setImage(img);
                    } catch (Exception e) {
                        System.out.println("Erreur chargement image: " + e.getMessage());
                    }
                }
            }
        }
    }

    // Méthode appelée par onAction="#updateProduit" dans le FXML
    @FXML
    private void updateProduit(ActionEvent event) {
        try {
            int id = Integer.parseInt(idProduit.getText());
            String nomProduit = nom.getText().trim();
            String desc = description.getText().trim();
            float prixValue = Float.parseFloat(prix.getText().trim());
            int dispo = Integer.parseInt(disponibilite.getText().trim());

            ServicesCrud service = new ServicesCrud();
            Produit existingProduit = service.recupererParId(id);
            if (existingProduit == null) {
                showAlert("Erreur", "Produit non trouvé.", Alert.AlertType.ERROR);
                return;
            }

            Produit updatedProduit = new Produit(
                    id,
                    nomProduit,
                    desc,
                    existingProduit.getCategorie().name(),
                    prixValue,
                    dispo,
                    existingProduit.getImage()
            );

            service.modifier(updatedProduit);
            showAlert("Succès", "Produit mis à jour avec succès!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la mise à jour : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Méthode appelée par onAction="#Afficher" dans le FXML
    @FXML
    private void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficheProduit.fxml"));
            Parent root = loader.load();
            affich.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "Erreur navigation vers Afficher Produit : " + e.getMessage(), Alert.AlertType.ERROR);
=======
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

            // Convert Categorie to String
            Produit produit = new Produit(id, nomProduit, desc, selectedCategorie.toString(), productPrix, disponibiliteInt, imageBlob);

            ServicesCrud service = new ServicesCrud();
            service.modifier(produit);
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
>>>>>>> origin/GestionCommande
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
<<<<<<< HEAD

    // Si nécessaire, implémentez cette méthode
    @FXML
    private void browseImageAction(ActionEvent event) {
        // Implémentez cette méthode si vous souhaitez modifier l'image
    }
}


=======
}
>>>>>>> origin/GestionCommande
