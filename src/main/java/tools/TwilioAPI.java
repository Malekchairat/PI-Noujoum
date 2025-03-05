package tools;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioAPI {

    public TwilioAPI()
    {
        Twilio.init(
                "ACe437f34e4b77f7971f1b55029db46fc5",
                "953c43ff17734856540dc62a6f039093");
    }

    public void  sendSMS(String number,String message)
    {
        Message msg = Message.creator(
                        new PhoneNumber(number),
                        new PhoneNumber("+13177432896"),
                        message)
                .create();
    }
}