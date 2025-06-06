package service.adapters;

import external.CreditCardSystem;
import service.PaymentProcessor;

public class CreditCardAdapter implements PaymentProcessor {
    private CreditCardSystem creditCardSystem;
    private String cardType;

    public CreditCardAdapter(CreditCardSystem creditCardSystem, String cardType) {
        this.creditCardSystem = creditCardSystem;
        this.cardType = cardType;
    }

    @Override
    public boolean processPayment(double amount, String currency) {
        // Convert currency if needed
        double convertedAmount = convertCurrency(amount, currency);
        return creditCardSystem.chargeCreditCard(convertedAmount, cardType);
    }

    @Override
    public String getPaymentMethod() {
        return "Credit Card (" + cardType + ")";
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

    public String[] getSupportedCardTypes() {
        return creditCardSystem.getSupportedCardTypes();
    }
}
