package service.adapters;

import external.BankPaymentSystem;
import service.PaymentProcessor;

public class BankPaymentAdapter implements PaymentProcessor {
    private BankPaymentSystem bankSystem;

    public BankPaymentAdapter(BankPaymentSystem bankSystem) {
        this.bankSystem = bankSystem;
    }

    @Override
    public boolean processPayment(double amount, String currency) {
        // Convert currency if needed (simplified)
        double convertedAmount = convertCurrency(amount, currency);
        return bankSystem.makePayment(convertedAmount);
    }

    @Override
    public String getPaymentMethod() {
        return "Bank Transfer (" + bankSystem.getBankName() + ")";
    }

    private double convertCurrency(double amount, String currency) {
        // Simplified currency conversion
        switch (currency.toUpperCase()) {
            case "EUR":
                return amount * 1.1; // EUR to USD
            case "GBP":
                return amount * 1.3; // GBP to USD
            default:
                return amount; // Assume USD
        }
    }
}