package models;

import java.util.Properties;
import javafx.scene.control.Alert;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeMessage;

public class mail {
    public static void send(String to, String sub, String msg) {
        // Securely get email credentials from environment variables
        String user = System.getenv("EMAIL_USER");
        String pass = System.getenv("EMAIL_PASSWORD");

        if (user == null || pass == null) {
            System.out.println("Error: Email credentials not set in environment variables.");
            return;
        }

        // Validate email format
        if (!to.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("L'email fourni est invalide.");
            alert.setHeaderText(null);
            alert.show();
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(sub);
            message.setText(msg);

            Transport.send(message);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Email envoyé avec succès !");
            alert.setHeaderText(null);
            alert.show();
        } catch (MessagingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Échec de l'envoi de l'email.");
            alert.setHeaderText("Erreur : " + e.getMessage());
            alert.show();
            e.printStackTrace();
        }
    }
}
