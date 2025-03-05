package Controllers;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.UserService;
import models.User;

import java.io.IOException;
import java.sql.Blob;
import java.io.InputStream;
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

        // Centrage des cartes
        HBox container = new HBox(userTilePane);
        container.setAlignment(Pos.CENTER);
        scrollPane.setContent(container);

        try {
            List<User> users = userService.recuperer();

            for (User user : users) {
                VBox userCard = new VBox(10);
                userCard.setStyle("-fx-border-color: gold; -fx-border-width: 2px; -fx-padding: 15px; -fx-background-color: black; -fx-border-radius: 15px; -fx-alignment: center;");
                userCard.setPrefSize(250, 300);

                Label nameLabel = new Label("\uD83D\uDC64 Nom: " + user.getNom());
                nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                Label emailLabel = new Label("✉️ Email: " + user.getEmail());
                emailLabel.setStyle("-fx-text-fill: white;");

                // Gestion de l'image
                Blob imageBlob = user.getImage();
                ImageView profileImageView = new ImageView();

                if (imageBlob != null) {
                    try (InputStream inputStream = imageBlob.getBinaryStream()) {
                        Image profileImage = new Image(inputStream);
                        profileImageView.setImage(profileImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Image placeholder = new Image(getClass().getResourceAsStream("/images/default_user.png"));
                    profileImageView.setImage(placeholder);
                }
                profileImageView.setFitWidth(100);
                profileImageView.setFitHeight(100);
                profileImageView.setStyle("-fx-border-radius: 50px; -fx-background-color: white;");

                Button editButton = new Button("✏️ Modifier");
                editButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 5px 10px;");
                editButton.setOnAction(event -> openUpdateUser(user));

                Button deleteButton = new Button("🗑️ Supprimer");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5px 10px;");
                deleteButton.setOnAction(event -> deleteUser(user));

                userCard.getChildren().addAll(profileImageView, nameLabel, emailLabel, editButton, deleteButton);
                userTilePane.getChildren().add(userCard);
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur lors du chargement des utilisateurs : " + e.getMessage());
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
            System.out.println("❌ Erreur lors du chargement de modifieruser.fxml : " + e.getMessage());
        }
    }

    private void deleteUser(User user) {
        try {
            userService.supprimer(user.getId());
            loadUsers();
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    void ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouteruser.fxml"));
            Parent root = loader.load();
            ajout.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("❌ Erreur lors du chargement : " + e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifieruser.fxml"));
            Parent root = loader.load();
            update.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("❌ Erreur lors du chargement : " + e.getMessage());
        }
    }
}
