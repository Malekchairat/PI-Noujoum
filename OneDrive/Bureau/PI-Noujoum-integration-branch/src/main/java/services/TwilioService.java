package services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {
    // Replace these with your Twilio credentials (preferably fetched from environment variables)
    public static final String ACCOUNT_SID = System.getenv("AC1d02c0ceb31798c328d3b0bfb56d07e4");
    public static final String AUTH_TOKEN = System.getenv("ba88c252349cc60d92c767ed871a8981");

    public static final String TWILIO_PHONE_NUMBER = "+19289284584"; // Your Twilio phone number

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    public static void sendSms(String userPhoneNumber, String code) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                        new PhoneNumber(userPhoneNumber),  // Receiver (Your Tunisian number)
                        new PhoneNumber(TWILIO_PHONE_NUMBER),  // Your Twilio number
                        "Your password reset code is: " + code)
                .create();

        System.out.println("SMS sent: " + message.getSid());
    }

    public static void main(String[] args) {
        // Test sending SMS to your Tunisian number
        sendSms("+216XXXXXXXX", "123456");
        System.out.println("Account SID: " + ACCOUNT_SID);
        System.out.println("Auth Token: " + AUTH_TOKEN);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);// Replace with your actual Tunisian number
    }


}

