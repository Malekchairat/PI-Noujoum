package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import models.Favoris;
import services.FavorisService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class addfavoriscontroller {

    @FXML
    private Button id_ajoutfav;
    @FXML
    private TextField id_produit;
    @FXML
    private TextField id_user;
    @FXML
    private TextField id_date;

    private final FavorisService favorisService = new FavorisService();

    @FXML
    void addFavoris(ActionEvent event) {
        String productText = id_produit.getText().trim();
        String userText = id_user.getText().trim();
        String dateText = id_date.getText().trim();

        if (productText.isEmpty() || userText.isEmpty() || dateText.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis!", Alert.AlertType.ERROR);
            return;
        }

        try {
            int product = Integer.parseInt(productText);
            int user = Integer.parseInt(userText);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(dateText, formatter);

            Favoris newFavoris = new Favoris(product, user, date);
            favorisService.ajouterFavoris(newFavoris);

            showAlert("Succès", "Favoris ajouté avec succès!", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID produit et l'ID utilisateur doivent être des nombres!", Alert.AlertType.ERROR);
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Le format de la date doit être JJ/MM/AAAA!", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Échec de l'ajout du Favoris: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void showFavoris() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherfavoris.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher Favoris");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de afficherfavoris.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
