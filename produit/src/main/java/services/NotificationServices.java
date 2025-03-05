package services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class NotificationServices {

    // Vos identifiants Twilio
    public static final String ACCOUNT_SID = "AC3b74a9a1cdc84d0001f6c6e49ea011ec";
    public static final String AUTH_TOKEN = "bfc3644ac19e7fb08dae7679f97757be";
    public static final String FROM_PHONE_NUMBER = "+12702791467";

    public NotificationServices() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void envoyerSMS(String messageContent, String toPhoneNumber) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                    new PhoneNumber("+21652164756"),
                    new PhoneNumber(FROM_PHONE_NUMBER),
                    messageContent
            ).create();
            System.out.println("Message envoyé avec SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du message : " + e.getMessage());
        }
    }
}