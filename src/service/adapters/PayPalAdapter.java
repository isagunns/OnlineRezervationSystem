package service.adapters;

import external.PayPalSystem;
import service.PaymentProcessor;

public class PayPalAdapter implements PaymentProcessor {
    private PayPalSystem payPalSystem;
    private String userEmail;

    public PayPalAdapter(PayPalSystem payPalSystem, String userEmail) {
        this.payPalSystem = payPalSystem;
        this.userEmail = userEmail;
    }

    @Override
    public boolean processPayment(double amount, String currency) {
        // Verify account first
        if (!payPalSystem.verifyAccount(userEmail)) {
            System.out.println("PayPal account verification failed for: " + userEmail);
            return false;
        }

        // Convert currency if needed
        double convertedAmount = convertCurrency(amount, currency);
        return payPalSystem.executePayment(convertedAmount, userEmail);
    }

    @Override
    public String getPaymentMethod() {
        return "PayPal (" + userEmail + ")";
    }

    private double convertCurrency(double amount, String currency) {
        // Simplified currency conversion
        switch (currency.toUpperCase()) {
            case "EUR":
                return amount * 1.1;
            case "GBP":
                return amount * 1.3;
            default:
                return amount;
        }
    }
}
