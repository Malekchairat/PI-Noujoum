package Controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        userTilePane.setHgap(20);
        userTilePane.setVgap(20);
        userTilePane.setPrefColumns(2);

        try {
            List<User> users = userService.recuperer();

            for (User user : users) {
                VBox userCard = new VBox(10);
                userCard.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: white; -fx-border-radius: 10px; -fx-alignment: center;");
                userCard.setPrefSize(220, 250);

                Label emailLabel = new Label("Email: " + user.getEmail());
                Label nameLabel = new Label("Nom: " + user.getNom());

                Button editButton = new Button("Modifier");
                editButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-padding: 5px 10px;");
                editButton.setOnAction(event -> openUpdateUser(user));

                Button deleteButton = new Button("Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 5px 10px;");
                deleteButton.setOnAction(event -> deleteUser(user));

                userCard.getChildren().addAll(emailLabel, nameLabel, editButton, deleteButton);
                userTilePane.getChildren().add(userCard);
            }

            scrollPane.setContent(userTilePane);

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des utilisateurs : " + e.getMessage());
        }
    }

    private void openUpdateUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifieruser.fxml"));
            Parent root = loader.load();

            updateusercontroller controller = loader.getController();
            controller.setUserData(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Utilisateur");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de modifieruser.fxml : " + e.getMessage());
        }
    }

    private void deleteUser(User user) {
        try {
            userService.supprimer(user.getId());
            loadUsers();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    void ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouteruser.fxml"));
            Parent root = loader.load();
            ajout.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement : " + e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifieruser.fxml"));
            Parent root = loader.load();
            update.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement : " + e.getMessage());
        }
    }
}
