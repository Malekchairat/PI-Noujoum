package Controllers;
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

public class updateproduitcontroller {

    @FXML
    private TextField idProduit, nom, prix, disponibilite;

    // Correction : utiliser TextArea au lieu de TextField
    @FXML
    private TextArea description;

    @FXML
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
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Si nécessaire, implémentez cette méthode
    @FXML
    private void browseImageAction(ActionEvent event) {
        // Implémentez cette méthode si vous souhaitez modifier l'image
    }
}


