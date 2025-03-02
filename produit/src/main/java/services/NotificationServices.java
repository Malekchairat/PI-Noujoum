package services;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class NotificationServices {

    // Vos identifiants Twilio (récupérés depuis votre compte Twilio)
    public static final String ACCOUNT_SID = "AC3b74a9a1cdc84d0001f6c6e49ea011ec";  // Remplacez par votre SID
    public static final String AUTH_TOKEN = "891ffb9806f3b93a12616b8193f65cd4";    // Remplacez par votre Auth Token

    // Numéro Twilio d'envoi (obtenu lors de l'inscription à Twilio)
    public static final String FROM_PHONE_NUMBER = "+12702791467";  // Remplacez par votre numéro Twilio

    public NotificationServices() {
        // Initialiser Twilio avec les identifiants
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // Méthode pour envoyer un SMS via Twilio
    public static void envoyerSMS(String messageContent, String toPhoneNumber) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            // Envoyer le SMS via l'API Twilio
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),  // Numéro destinataire
                    new PhoneNumber(FROM_PHONE_NUMBER),  // Numéro Twilio d'envoi
                    messageContent  // Contenu du message
            ).create();

            System.out.println("Message envoyé avec SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du message : " + e.getMessage());
        }
    }

    // Méthode pour afficher la notification (optionnel)
    public void afficherNotificationJavaFX(String message) {
        // Logique pour afficher la notification dans JavaFX (par exemple avec un Toast ou un Alert)
        System.out.println("Notification JavaFX : " + message);
    }
}
