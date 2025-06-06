package external;

public class CreditCardSystem {
    public boolean chargeCreditCard(double amount, String cardType) {
        // Simulate credit card processing
        System.out.println("Charging " + cardType + " credit card: $" + amount);
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return amount > 0 && amount <= 5000; // Different payment limits
    }

    public String[] getSupportedCardTypes() {
        return new String[] { "Visa", "MasterCard", "American Express" };
    }
}