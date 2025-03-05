package tools;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioAPI {

    public TwilioAPI()
    {
        Twilio.init(
                "AC6be49cb91b9e3630bc17ef0093eb97b4",
                "3885aa0b6936f0c6cf784f4409573dff");
    }

    public void  sendSMS(String number,String message)
    {
        Message msg = Message.creator(
                        new PhoneNumber(number),
                        new PhoneNumber("+15632028564"),
                        message)
                .create();
    }
}