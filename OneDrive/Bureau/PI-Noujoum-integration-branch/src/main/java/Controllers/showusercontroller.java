package Controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.UserService;
import models.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;
import java.util.stream.Collectors;

public class showusercontroller {

    @FXML
    private TilePane userTilePane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button sortButton;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> roleFilter;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        loadUsers();

        // Populate role filter dropdown
        roleFilter.getItems().addAll("Tous", "Admin", "Fan");
        roleFilter.setOnAction(event -> advancedSearch());

        // Call advancedSearch whenever the user types in the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> advancedSearch());

        sortButton.setOnAction(event -> sortUsers());
    }

    private void loadUsers() {
        userTilePane.getChildren().clear();
        userTilePane.setHgap(20);
        userTilePane.setVgap(20);
        userTilePane.setPrefColumns(2);

        HBox container = new HBox(userTilePane);
        container.setAlignment(Pos.CENTER);
        scrollPane.setContent(container);

        try {
            List<User> users = userService.recuperer();
            displayUsers(users);
        } catch (Exception e) {
            System.out.println("❌ Erreur lors du chargement des utilisateurs : " + e.getMessage());
        }
    }

    private void displayUsers(List<User> users) {
        for (User user : users) {
            VBox userCard = new VBox(10);
            userCard.setStyle("-fx-border-color: gold; -fx-border-width: 2px; -fx-padding: 15px; -fx-background-color: black; -fx-border-radius: 15px; -fx-alignment: center;");
            userCard.setPrefSize(250, 300);

            Label nameLabel = new Label("\uD83D\uDC64 Nom: " + user.getNom());
            nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            Label emailLabel = new Label("✉️ Email: " + user.getEmail());
            emailLabel.setStyle("-fx-text-fill: white;");

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
    }

    @FXML
    private void advancedSearch() {
        try {
            List<User> users = userService.recuperer();
            String searchText = searchField.getText().toLowerCase();
            String selectedRole = roleFilter.getValue();

            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getNom().toLowerCase().contains(searchText) ||
                            user.getEmail().toLowerCase().contains(searchText))
                    .filter(user -> selectedRole == null || selectedRole.equals("Tous") || user.getRole().equalsIgnoreCase(selectedRole))
                    .collect(Collectors.toList());

            userTilePane.getChildren().clear();
            displayUsers(filteredUsers);

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la recherche avancée : " + e.getMessage());
        }
    }

    private void generateUserPDF(User user) {
        try {
            String filePath = "user_" + user.getId() + ".pdf";
            File file = new File(filePath);
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Détails de l'utilisateur"));
            document.add(new Paragraph("Nom: " + user.getNom()));
            document.add(new Paragraph("Email: " + user.getEmail()));
            document.add(new Paragraph("Rôle: " + user.getRole()));

            document.close();

            System.out.println("📄 PDF généré : " + file.getAbsolutePath());

            // Ouvrir le fichier après la création
            java.awt.Desktop.getDesktop().open(file);

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    @FXML
    private Button downloadPdfButton;

    @FXML
    private void downloadUserListPdf() {
        String dest = "Liste_Utilisateurs.pdf";
        try {
            PdfWriter writer = new PdfWriter(new File(dest));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Liste des Utilisateurs\n\n"));

            // Création du tableau avec 2 colonnes (Nom, Email)
            float[] columnWidths = {200F, 200F};
            Table table = new Table(columnWidths);
            table.addCell("Nom");
            table.addCell("Email");

            // Remplir le tableau avec les données des utilisateurs
            List<User> users = userService.recuperer();
            for (User user : users) {
                table.addCell(user.getNom());
                table.addCell(user.getEmail());
            }

            document.add(table);
            document.close();
            System.out.println("✅ PDF généré avec succès !");
        } catch (FileNotFoundException e) {
            System.out.println("❌ Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    @FXML
    private void sortUsers() {
        try {
            List<User> users = userService.recuperer();
            users.sort((u1, u2) -> u1.getNom().compareToIgnoreCase(u2.getNom()));

            userTilePane.getChildren().clear();
            displayUsers(users);
        } catch (Exception e) {
            System.out.println("❌ Erreur lors du tri des utilisateurs : " + e.getMessage());
        }
    }

    private void openUpdateUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifieruser.fxml"));
            Parent root = loader.load();
            updateusercontroller controller = loader.getController(); // Make sure the controller is of the correct type
            controller.setUserData(user);  // Set the user data in the controller

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
}
