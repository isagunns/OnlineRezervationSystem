package external;

public class PayPalSystem {
    public boolean executePayment(double amount, String email) {
        // Simulate PayPal payment processing
        System.out.println("Processing PayPal payment of $" + amount + " for " + email);
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return amount > 0 && amount <= 2000; // PayPal limits
    }

    public boolean verifyAccount(String email) {
        // Simple email validation
        return email != null && email.contains("@");
    }
}