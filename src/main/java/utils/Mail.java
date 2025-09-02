package utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Properties;
import Controllers.AjouterCommande;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;

public class Mail {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "hedifridhy@gmail.com";
    private static final String PASSWORD = "utqw lriw qvgj wtxv.";
    private static final String RECIPIENT_EMAIL = "hedifridhy@gmail.com"; // Replace with the recipient's email


    public static void envoyer(String email) throws Exception{
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "*");

        Session session = Session.getInstance(props,new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(RECIPIENT_EMAIL));
            message.setSubject("Commande ajoutée");
            message.setText("Une nouvelle commande a été ajoutée.");

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
        } }

    private static Message prepareMessage(Session session, String email){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("confiramation d'inscription");
            message.setText("Bonjour ,\n\nVotre inscription a bien été prise en compte .\n\nCordialement,\nL'équipe Creativa ");
            return message;
        } catch (MessagingException e) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE,null,e);
        }
        return null;
    }
}
