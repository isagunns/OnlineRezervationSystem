package service;

public interface PaymentProcessor {
    boolean processPayment(double amount, String currency);

    String getPaymentMethod();
}
