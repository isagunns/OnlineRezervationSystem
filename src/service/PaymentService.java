package service;

import external.BankPaymentSystem;
import external.CreditCardSystem;
import external.PayPalSystem;
import service.adapters.BankPaymentAdapter;
import service.adapters.CreditCardAdapter;
import service.adapters.PayPalAdapter;

import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    // Singleton Pattern
    private static PaymentService instance;
    private List<PaymentProcessor> availableProcessors;

    private PaymentService() {
        initializePaymentProcessors();
    }

    public static PaymentService getInstance() {
        if (instance == null) {
            instance = new PaymentService();
        }
        return instance;
    }

    private void initializePaymentProcessors() {
        availableProcessors = new ArrayList<>();

        // Initialize external systems and create adapters
        BankPaymentSystem bankSystem = new BankPaymentSystem();
        CreditCardSystem creditCardSystem = new CreditCardSystem();

        // Create adapters for different payment methods
        availableProcessors.add(new BankPaymentAdapter(bankSystem));
        availableProcessors.add(new CreditCardAdapter(creditCardSystem, "Visa"));
        availableProcessors.add(new CreditCardAdapter(creditCardSystem, "MasterCard"));
    }

    public List<PaymentProcessor> getAvailableProcessors() {
        return new ArrayList<>(availableProcessors);
    }

    public boolean processPayment(String paymentMethod, double amount, String currency, String userEmail) {
        PaymentProcessor processor = null;

        // Find the appropriate processor based on payment method
        for (PaymentProcessor p : availableProcessors) {
            if (p.getPaymentMethod().toLowerCase().contains(paymentMethod.toLowerCase())) {
                processor = p;
                break;
            }
        }

        // If PayPal is selected, create a new adapter with user email
        if (paymentMethod.toLowerCase().contains("paypal") && userEmail != null) {
            PayPalSystem payPalSystem = new PayPalSystem();
            processor = new PayPalAdapter(payPalSystem, userEmail);
        }

        if (processor != null) {
            System.out.println("Processing payment using: " + processor.getPaymentMethod());
            return processor.processPayment(amount, currency);
        }

        System.out.println("Payment method not supported: " + paymentMethod);
        return false;
    }

    public void addPaymentProcessor(PaymentProcessor processor) {
        availableProcessors.add(processor);
    }

    public void removePaymentProcessor(PaymentProcessor processor) {
        availableProcessors.remove(processor);
    }
}