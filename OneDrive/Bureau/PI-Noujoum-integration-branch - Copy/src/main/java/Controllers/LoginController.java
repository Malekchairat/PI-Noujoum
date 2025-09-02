package Controllers;
import com.google.api.services.oauth2.model.Userinfo;
import com.twilio.Twilio;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import services.GoogleAuthService;
import services.UserSession;
import models.PasswordUtils;
import services.TwilioService;
import models.User;
import services.UserService;
import com.twilio.rest.api.v2010.account.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
// Google OAuth 2.0 Imports
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.util.Optional;


public class LoginController implements Initializable {
    public static final String ACCOUNT_SID = "AC1d02c0ceb31798c328d3b0bfb56d07e4";
    public static final String AUTH_TOKEN = "ba88c252349cc60d92c767ed871a8981";
    private static final String TWILIO_PHONE_NUMBER = "+19289284584";


    @FXML
    private Button cnbt, cnbt1, icibt, googleLoginButton;

    @FXML
    private TextField email;

    @FXML
    private PasswordField mdp;

    @FXML
    private ImageView logo;

    @FXML
    private Text slogan, bien;

    private final UserService us = new UserService();
    public static User UserConnected;

    private static final String CLIENT_SECRET_FILE = "/client_secret.json"; // Store in `resources/`
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList("profile");
    @Override
    public void initialize(URL url, java.util.ResourceBundle rb) {
        if (slogan != null) {
            animateUI();
        }
        slogan.setText("Briller Vos Nuits!");
        bien.setText("Your gateway to your idols!");

    }

    private void animateUI() {
        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), bien);
        rotate.setByAngle(360);
        rotate.setCycleCount(3);
        rotate.play();

        TranslateTransition translate = new TranslateTransition(Duration.seconds(5), slogan);
        translate.setFromX(-slogan.getBoundsInParent().getWidth());
        translate.setToX(logo.getBoundsInParent().getWidth());
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.play();
    }

    @FXML
    private void googleLogin() {
        try {
            // Authenticate with Google
            Userinfo userinfo = GoogleAuthService.authenticate();
            System.out.println("Name: " + userinfo.getName());
            System.out.println("Email: " + userinfo.getEmail());
            System.out.println("Picture: " + userinfo.getPicture());

            // Check if user exists in the database
            User user = UserService.findByEmail(userinfo.getEmail());

            if (user != null) {
                // ✅ User exists → Proceed with login
                UserConnected = user;
                UserSession.setUserId(user.getId());
                showAlert(Alert.AlertType.INFORMATION, "Bienvenue", "Welcome " + user.getNom());
                slogan.setText("Welcome back, " + user.getNom() + "!");
                bien.setText("Let's get started with Noujoum!");

                Stage stage = (Stage) googleLoginButton.getScene().getWindow();
                if ("Fan".equals(user.getRole())) {
                    switchToScene1(stage, "/frontoffice.fxml", "Noujoum");
                } else if ("Admin".equals(user.getRole())) {
                    switchToScene1(stage, "/backoffice.fxml", "Back Office");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Accès Refusé", "Votre rôle ne vous permet pas d'accéder à cette application.");
                }
            } else {
                // ❌ User does NOT exist → Prompt user for account creation
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Complete Account Setup");
                dialog.setHeaderText("Enter password and phone number to create your account");

                ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);

                PasswordField passwordField = new PasswordField();
                passwordField.setPromptText("Password");

                TextField phoneField = new TextField();
                phoneField.setPromptText("Phone Number");

                grid.add(new Label("Password:"), 0, 0);
                grid.add(passwordField, 1, 0);
                grid.add(new Label("Phone Number:"), 0, 1);
                grid.add(phoneField, 1, 1);

                dialog.getDialogPane().setContent(grid);

                Platform.runLater(passwordField::requestFocus);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == registerButtonType) {
                        return new Pair<>(passwordField.getText(), phoneField.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                if (result.isPresent()) {
                    String password = result.get().getKey().trim();
                    String phone = result.get().getValue().trim();

                    if (password.length() < 6) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 6 characters long!");
                        return;
                    }

                    if (!phone.matches("\\d{8,15}")) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Phone number must be digits only and between 8-15 characters!");
                        return;
                    }

                    // ✅ Hash the password before storing
                    String hashedPassword = PasswordUtils.hashPassword(password);

                    Blob imgBlob = null;
                    if (userinfo.getPicture() != null) {
                        try (InputStream in = new URL(userinfo.getPicture()).openStream()) {
                            imgBlob = new SerialBlob(in.readAllBytes());
                        } catch (IOException | SQLException e) {
                            e.printStackTrace();
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to download profile picture!");
                        }
                    }

                    // Create the user
                    User newUser = new User(
                            0, // Auto-generated ID
                            userinfo.getName(),  // Name
                            "", // Set prenom as empty
                            userinfo.getEmail(),
                            hashedPassword, // Store hashed password
                            Integer.parseInt(phone), // Phone number
                            "Fan", // Default role
                            imgBlob // Image
                    );

                    // Add the new user to the database
                    UserService userService = new UserService();
                    userService.ajouter(newUser);

                    // Log in the newly created user
                    user = userService.findByEmail(userinfo.getEmail());

                    // Set the user session data
                    UserSession.setUserId(user.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Account created and logged in with Google!");

                    Stage stage = (Stage) googleLoginButton.getScene().getWindow();
                    switchToScene1(stage, "/frontoffice.fxml", "Noujoum");

                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Account setup canceled!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la connexion Google.");
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }



    @FXML
    private void connect(ActionEvent event) throws SQLException, IOException {
        String userEmail = email.getText().trim();
        String userPassword = mdp.getText().trim();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champ Vide", "Veuillez remplir tous les champs.");
            return;
        }

        if (!userEmail.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert(Alert.AlertType.ERROR, "Email Non Valide", "Format email incorrect !");
            return;
        }

        List<User> users = us.recuperer();
        UserConnected = users.stream()
                .filter(user -> user.getEmail().equals(userEmail) && user.getMdp().equals(userPassword))
                .findFirst()
                .orElse(null);

        if (UserConnected != null) {
            UserSession.setUserId(UserConnected.getId());

            showAlert(Alert.AlertType.INFORMATION, "Bienvenue", "Welcome " + UserConnected.getNom());
            slogan.setText("Welcome back, " + UserConnected.getNom() + "!");
            bien.setText("Let's get started with Noujoum!");
            sendSmsNotification(UserConnected.getNom(), "");

            if ("Fan".equals(UserConnected.getRole())) {
                switchToScene(event, "/frontoffice.fxml", "Noujoum");
            } else if ("Admin".equals(UserConnected.getRole())) {
                switchToScene(event, "/backoffice.fxml", "Back Office");
            } else {
                showAlert(Alert.AlertType.ERROR, "Accès Refusé", "Votre rôle ne vous permet pas d'accéder à cette application.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur inexistant !");
        }
    }




    @FXML
    private void inscrire(ActionEvent event) throws IOException {
        switchToScene(event, "/ajouteruser.fxml", "Inscription");
    }

    @FXML
    private void passwrd(ActionEvent event) throws IOException {
        switchToScene(event, "/email_check.fxml", "Changement de mot de passe");
    }

    private void switchToScene(ActionEvent event, String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    private void switchToScene1(Stage stage, String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void mdp_ob(ActionEvent event) throws SQLException, IOException {
        String userEmail = email.getText().trim();

        if (userEmail.isEmpty()) {
            showAlert(AlertType.ERROR, "Champ Vide", "Veuillez entrer votre email.");
            return;
        }

        if (!userEmail.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert(AlertType.ERROR, "Email Non Valide", "Format email incorrect !");
            return;
        }

        System.out.println("Sending verification code to: " + userEmail);
        showAlert(AlertType.INFORMATION, "Code Envoyé", "Un code de vérification a été envoyé à " + userEmail);
    }


    private void sendSmsNotification(String productName, String msg) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            String messageBody = "Hi " + productName + ", Someone's trying to log in with your account" + msg;

            Message message = Message.creator(
                    new PhoneNumber("+21656921682"), // Replace with recipient's phone number
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    messageBody
            ).create();

            System.out.println("SMS sent: " + message.getSid());
        } catch (com.twilio.exception.ApiException apiEx) {
            System.out.println("Twilio API Exception: " + apiEx.getMessage());
            System.out.println("Twilio Error Code: " + apiEx.getCode());
            showAlert(Alert.AlertType.ERROR, "Error", "Twilio API failed: " + apiEx.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to send SMS: " + e.getMessage());
        }
    }



}
