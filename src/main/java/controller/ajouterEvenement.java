package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Evenement;
import models.Type_e;
import tools.MyDataBase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;

public class ajouterEvenement {

    @FXML private TextField locationField;
    @FXML private TextField artist;
    @FXML private TextField description;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextField time;
    @FXML private TextField price;
    @FXML private TextField ticketCount;
    @FXML private ComboBox<Type_e> eventType;

    @FXML
    public void initialize() {
        eventType.getItems().setAll(Type_e.values());
    }

    @FXML
    public void ajout(ActionEvent actionEvent) {
        String location = locationField.getText();
        String artistName = artist.getText();
        String desc = description.getText();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        int eventTime = Integer.parseInt(time.getText());
        float eventPrice = Float.parseFloat(price.getText());
        int tickets = Integer.parseInt(ticketCount.getText());
        Type_e type = eventType.getValue();

        try (Connection cnx = MyDataBase.getInstance().getCnx()) {
            String sql = "INSERT INTO evenement (location, artist, description, StartDate, EndDate, time, price, type, ticketCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, location);
            stmt.setString(2, artistName);
            stmt.setString(3, desc);
            stmt.setDate(4, Date.valueOf(start));
            stmt.setDate(5, Date.valueOf(end));
            stmt.setInt(6, eventTime);
            stmt.setFloat(7, eventPrice);
            stmt.setString(8, type.name());
            stmt.setInt(9, tickets);
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Événement ajouté avec succès !", ButtonType.OK);
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout de l'événement.", ButtonType.OK);
            alert.showAndWait();
        }


    }
    @FXML
    private void Afficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherEvenement.fxml"));
            Parent root = loader.load();

            // Optionnel : Récupérer le contrôleur et passer des données si nécessaire
            afficherEvenement controller = loader.getController();
            // controller.setSomeData(someData); // Si tu veux envoyer des données

            // Récupérer la scène actuelle et la changer
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de afficherEvenement.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(ActionEvent actionEvent) {
    }
}
