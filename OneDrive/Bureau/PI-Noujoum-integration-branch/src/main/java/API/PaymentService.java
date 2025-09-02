package API;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class PaymentService {
    // Set your Stripe Secret Key (Replace with your actual key)
    private static final String STRIPE_SECRET_KEY = "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";

    public static String createPaymentIntent(long amount, String currency) {
        Stripe.apiKey = STRIPE_SECRET_KEY;

        try {
            System.out.println("Initializing Payment Intent...");
            System.out.println("Amount: " + amount + ", Currency: " + currency);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount) // Amount in cents (e.g., 1000 = $10.00)
                    .setCurrency(currency)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            System.out.println("PaymentIntent Created: " + intent.getId());

            return intent.getClientSecret(); // Return the client secret to the frontend
        } catch (StripeException e) {
            System.err.println("Stripe API Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            return null;
        }
    }
}