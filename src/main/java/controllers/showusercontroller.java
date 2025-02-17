package controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import services.UserService;
import models.User;

import java.io.IOException;
import java.util.List;

public class showusercontroller {

    @FXML
    private Button update;

    @FXML
    private Button ajout;

    @FXML
    private TilePane userTilePane;

    @FXML
    private ScrollPane scrollPane;

    private UserService userService = new UserService();

    @FXML
    public void loadUsers() {
        userTilePane.getChildren().clear();
        userTilePane.setHgap(50);
        userTilePane.setVgap(120);

        try {
            List<User> users = userService.recuperer();

            for (User user : users) {
                VBox userCard = new VBox(5);
                userCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f4f4f4; -fx-border-radius: 10px;");
                userCard.setPrefSize(200, 250);

                Label idLabel = new Label("ID: " + user.getId());
                Label emailLabel = new Label("Email: " + user.getEmail());
                Label passwordLabel = new Label("Password: " + user.getMdp());
                Label nameLabel = new Label("Name: " + user.getNom());

                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 5px 10px; -fx-font-size: 12px;");
                deleteButton.setOnAction(event -> deleteUser(user));

                userCard.getChildren().addAll(idLabel, emailLabel, passwordLabel, nameLabel, deleteButton);
                userTilePane.getChildren().add(userCard);
            }

            // Force UI refresh
            scrollPane.setContent(userTilePane);

        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void deleteUser(User user) {
        try {
            userService.supprimer(user); // Call the delete method on the instance
            loadUsers(); // Refresh the data after deletion
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    @FXML
    void ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouteruser.fxml"));
            Parent root = loader.load();
            ajout.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error loading the FXML: " + e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifieruser.fxml"));
            Parent root = loader.load();
            update.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error loading the FXML: " + e.getMessage());
        }
    }


}
