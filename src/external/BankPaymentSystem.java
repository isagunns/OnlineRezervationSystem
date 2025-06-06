package external;

public class BankPaymentSystem {
    public boolean makePayment(double amount) {
        // Simulate bank payment processing
        System.out.println("Processing bank payment of $" + amount);
        // Simulate some processing time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return amount > 0 && amount <= 10000; // Simulate payment limits
    }

    public String getBankName() {
        return "National Bank";
    }
}