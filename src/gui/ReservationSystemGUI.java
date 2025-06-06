package gui;

import model.*;
import model.Feedback.FeedbackStatus;
import service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class ReservationSystemGUI extends JFrame {
    // Singleton Pattern kullanımı - Service sınıflarından tek instance alınıyor
    private UserService userService;
    private TripService tripService;
    private ReservationService reservationService;
    private PaymentService paymentService; // New payment service
    private User currentUser;

    // Login components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    // Main panel components
    private JTabbedPane tabbedPane;

    private FeedbackService feedbackService;

    public ReservationSystemGUI() {
        // Singleton Pattern - getInstance() metodları kullanılıyor
        userService = UserService.getInstance();
        tripService = TripService.getInstance();
        reservationService = ReservationService.getInstance();
        paymentService = PaymentService.getInstance();
        feedbackService = FeedbackService.getInstance(); // Initialize payment service

        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Online Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        showLoginScreen();
    }

    private void showLoginScreen() {
        getContentPane().removeAll();

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();

        // Title
        JLabel titleLabel = new JLabel("Online Reservation System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 30, 0);
        loginPanel.add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> handleLogin());

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> showRegistrationDialog());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        loginPanel.add(buttonPanel, gbc);

        add(loginPanel);
        revalidate();
        repaint();
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = userService.loginUser(username, password);
        if (user != null) {
            currentUser = user;
            showMainScreen();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "Register New User", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField regUsernameField = new JTextField(15);
        JPasswordField regPasswordField = new JPasswordField(15);
        JTextField fullNameField = new JTextField(15);
        JTextField emailField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(regUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(regPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        panel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> {
            String username = regUsernameField.getText().trim();
            String password = new String(regPasswordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();

            if (validateRegistration(username, password, fullName, email)) {
                if (userService.registerUser(username, password, fullName, email)) {
                    JOptionPane.showMessageDialog(dialog, "Registration successful!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Username already exists!");
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private boolean validateRegistration(String username, String password, String fullName, String email) {
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!");
            return false;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!");
            return false;
        }
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name cannot be empty!");
            return false;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email!");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void showMainScreen() {
        getContentPane().removeAll();

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            currentUser.setLoggedIn(false);
            currentUser = null;
            showLoginScreen();
        });

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Tabbed pane
        tabbedPane = new JTabbedPane();

        if (currentUser.isAdmin()) {
            createAdminTabs();
        } else {
            createUserTabs();
        }

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    // New method for payment testing panel (Admin only)
    private JPanel createPaymentTestPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Başlık
        JLabel titleLabel = new JLabel("Payment System Test & Debug Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(220, 20, 60)); // Kırmızı - test ortamı olduğunu belirtir
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Uyarı mesajı
        JLabel warningLabel = new JLabel("⚠️ Test Environment - No Real Transactions");
        warningLabel.setForeground(Color.RED);
        warningLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gbc.gridy = 1;
        panel.add(warningLabel, gbc);

        // Test senaryoları
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Test Scenario:"), gbc);

        JComboBox<String> scenarioCombo = new JComboBox<>(new String[] {
                "Normal Payment",
                "Insufficient Funds",
                "Network Error",
                "Invalid Card",
                "Expired Card"
        });
        gbc.gridx = 1;
        panel.add(scenarioCombo, gbc);

        // Amount field
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Test Amount:"), gbc);

        JTextField amountField = new JTextField("50.0", 10);
        gbc.gridx = 1;
        panel.add(amountField, gbc);

        // Currency selection
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Currency:"), gbc);

        JComboBox<String> currencyCombo = new JComboBox<>(new String[] { "USD", "EUR", "GBP", "TRY" });
        gbc.gridx = 1;
        panel.add(currencyCombo, gbc);

        // Payment method selection
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Payment Method:"), gbc);

        JComboBox<String> paymentCombo = new JComboBox<>(
                new String[] { "Bank Transfer", "Visa", "MasterCard", "PayPal" });
        gbc.gridx = 1;
        panel.add(paymentCombo, gbc);

        // Email for PayPal
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Email (PayPal):"), gbc);

        JTextField emailField = new JTextField("test@example.com", 15);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Test buttons
        JPanel buttonPanel = new JPanel();

        JButton testButton = new JButton("🧪 Run Test");
        testButton.setBackground(new Color(60, 179, 113));
        testButton.setForeground(Color.WHITE);

        JButton stressTestButton = new JButton("⚡ Stress Test");
        stressTestButton.setBackground(new Color(255, 140, 0));
        stressTestButton.setForeground(Color.WHITE);

        buttonPanel.add(testButton);
        buttonPanel.add(stressTestButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(buttonPanel, gbc);

        // Test sonuçları için log alanı
        JTextArea logArea = new JTextArea(12, 40);
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.GREEN);
        logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        logArea.setText("=== Payment Test Console ===\nReady for testing...\n");

        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Test Results & Logs"));
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(logScrollPane, gbc);

        // Test button action
        testButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String currency = (String) currencyCombo.getSelectedItem();
                String paymentMethod = (String) paymentCombo.getSelectedItem();
                String scenario = (String) scenarioCombo.getSelectedItem();
                String email = emailField.getText().trim();

                logArea.append("\n" + java.time.LocalDateTime.now() + " - Testing: " + scenario);
                logArea.append("\nMethod: " + paymentMethod + " | Amount: " + amount + " " + currency);

                // Test senaryosuna göre sonuç
                boolean result;
                switch (scenario) {
                    case "Insufficient Funds":
                        result = false;
                        logArea.append("\n❌ FAILED: Insufficient funds");
                        break;
                    case "Network Error":
                        result = false;
                        logArea.append("\n❌ FAILED: Network timeout");
                        break;
                    case "Invalid Card":
                        result = false;
                        logArea.append("\n❌ FAILED: Invalid card number");
                        break;
                    case "Expired Card":
                        result = false;
                        logArea.append("\n❌ FAILED: Card expired");
                        break;
                    default:
                        result = paymentService.processPayment(paymentMethod, amount, currency, email);
                        logArea.append(result ? "\n✅ SUCCESS: Payment processed" : "\n❌ FAILED: Payment rejected");
                }

                logArea.append("\n" + "=".repeat(50));
                logArea.setCaretPosition(logArea.getDocument().getLength());

            } catch (NumberFormatException ex) {
                logArea.append("\n❌ ERROR: Invalid amount format");
            }
        });

        // Stress test button action
        stressTestButton.addActionListener(e -> {
            logArea.append("\n🚀 Starting stress test with 10 concurrent payments...");

            // Basit stress test simulation
            SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                @Override
                protected Void doInBackground() throws Exception {
                    for (int i = 1; i <= 10; i++) {
                        publish("Test " + i + "/10: Processing...");
                        Thread.sleep(500); // Simulate processing time

                        boolean result = Math.random() > 0.2; // %80 success rate
                        publish("Test " + i + "/10: " + (result ? "✅ SUCCESS" : "❌ FAILED"));
                    }
                    return null;
                }

                @Override
                protected void process(List<String> chunks) {
                    for (String message : chunks) {
                        logArea.append("\n" + message);
                        logArea.setCaretPosition(logArea.getDocument().getLength());
                    }
                }

                @Override
                protected void done() {
                    logArea.append("\n🏁 Stress test completed!");
                    logArea.append("\n" + "=".repeat(50));
                }
            };
            worker.execute();
        });

        return panel;
    }

    private JPanel createTripsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = { "Trip ID", "Type", "From", "To", "Date & Time", "Available Seats" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshTripsTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTripsTable(model));
        buttonPanel.add(refreshButton);

        if (currentUser.isAdmin()) {
            JButton removeButton = new JButton("Remove Selected Trip");
            removeButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String tripId = (String) model.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to remove trip " + tripId + "?",
                            "Confirm Removal", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (tripService.removeTrip(tripId)) {
                            JOptionPane.showMessageDialog(this, "Trip removed successfully!");
                            refreshTripsTable(model);
                        }
                    }
                }
            });
            buttonPanel.add(removeButton);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshTripsTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Trip> trips = tripService.getAllTrips();

        for (Trip trip : trips) {
            int availableSeats = 0;
            for (boolean seat : trip.getSeats()) {
                if (!seat)
                    availableSeats++;
            }

            model.addRow(new Object[] {
                    trip.getTripId(),
                    trip.getTransportType(),
                    trip.getFrom(),
                    trip.getTo(),
                    trip.getDateTime(),
                    availableSeats + "/" + trip.getSeats().length
            });
        }
    }

    private JPanel createAddTripPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField tripIdField = new JTextField(15);
        JTextField fromField = new JTextField(15);
        JTextField toField = new JTextField(15);
        JTextField dateTimeField = new JTextField(15);
        JComboBox<String> transportCombo = new JComboBox<>(new String[] { "Bus", "Plane" });
        JSpinner seatSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Trip ID:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(tripIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("From:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(fromField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("To:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(toField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Date & Time (YYYY-MM-DD HH:MM):"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(dateTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Transport Type:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(transportCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Number of Seats:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(seatSpinner, gbc);

        JButton addButton = new JButton("Add Trip");
        addButton.setBackground(new Color(60, 179, 113));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            String tripId = tripIdField.getText().trim();
            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            String dateTime = dateTimeField.getText().trim();
            String transport = (String) transportCombo.getSelectedItem();
            int seats = (Integer) seatSpinner.getValue();

            if (validateTripData(tripId, from, to, dateTime)) {
                if (tripService.findTripById(tripId) == null) {
                    tripService.addTrip(tripId, from, to, dateTime, transport, seats);
                    JOptionPane.showMessageDialog(this, "Trip added successfully!");
                    // Clear fields
                    tripIdField.setText("");
                    fromField.setText("");
                    toField.setText("");
                    dateTimeField.setText("");
                    seatSpinner.setValue(10);
                } else {
                    JOptionPane.showMessageDialog(this, "Trip ID already exists!");
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(addButton, gbc);

        return panel;
    }

    private boolean validateTripData(String tripId, String from, String to, String dateTime) {
        if (tripId.isEmpty() || from.isEmpty() || to.isEmpty() || dateTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!");
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD HH:MM");
            return false;
        }

        return true;
    }

    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Trip selection
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Trip:"));
        JComboBox<String> tripCombo = new JComboBox<>();
        refreshTripCombo(tripCombo);
        topPanel.add(tripCombo);

        JButton refreshTripsBtn = new JButton("Refresh Trips");
        refreshTripsBtn.addActionListener(e -> refreshTripCombo(tripCombo));
        topPanel.add(refreshTripsBtn);

        panel.add(topPanel, BorderLayout.NORTH);

        // Seat selection panel
        JPanel seatPanel = new JPanel();
        seatPanel.setBorder(BorderFactory.createTitledBorder("Select Seat"));
        panel.add(seatPanel, BorderLayout.CENTER);

        // Update seat display when trip is selected
        tripCombo.addActionListener(e -> {
            String selectedTrip = (String) tripCombo.getSelectedItem();
            if (selectedTrip != null && !selectedTrip.equals("No trips available")) {
                String tripId = selectedTrip.split(" - ")[0];
                updateSeatDisplay(seatPanel, tripId);
            }
        });

        // Initial seat display
        if (tripCombo.getItemCount() > 0 && !tripCombo.getItemAt(0).equals("No trips available")) {
            String firstTrip = tripCombo.getItemAt(0);
            String tripId = firstTrip.split(" - ")[0];
            updateSeatDisplay(seatPanel, tripId);
        }

        return panel;
    }

    private void refreshTripCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        List<Trip> trips = tripService.getAllTrips();

        if (trips.isEmpty()) {
            combo.addItem("No trips available");
        } else {
            for (Trip trip : trips) {
                combo.addItem(trip.getTripId() + " - " + trip.getFrom() + " to " + trip.getTo() + " ("
                        + trip.getDateTime() + ")");
            }
        }
    }

    private void updateSeatDisplay(JPanel seatPanel, String tripId) {
        seatPanel.removeAll();

        Trip trip = tripService.findTripById(tripId);
        if (trip == null)
            return;

        seatPanel.setLayout(new GridLayout(0, 5, 5, 5));

        for (int i = 0; i < trip.getSeats().length; i++) {
            final int seatNum = i;
            JButton seatButton = new JButton("Seat " + i);

            if (trip.getSeats()[i]) {
                seatButton.setBackground(Color.RED);
                seatButton.setForeground(Color.WHITE);
                seatButton.setText("Seat " + i + " (Taken)");
                seatButton.setEnabled(false);
            } else {
                seatButton.setBackground(Color.GREEN);
                seatButton.setForeground(Color.WHITE);
                seatButton.addActionListener(e -> {
                    // Show payment dialog before confirming reservation
                    showPaymentDialog(tripId, seatNum, trip);
                });
            }
            seatPanel.add(seatButton);
        }

        seatPanel.revalidate();
        seatPanel.repaint();
    }

    // New method to show payment dialog
    private void showPaymentDialog(String tripId, int seatNum, Trip trip) {
        JDialog paymentDialog = new JDialog(this, "Payment Required", true);
        paymentDialog.setSize(450, 400);
        paymentDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Trip info
        JLabel tripInfoLabel = new JLabel("<html><b>Trip:</b> " + trip.getFrom() + " → " + trip.getTo() +
                "<br><b>Date:</b> " + trip.getDateTime() +
                "<br><b>Seat:</b> " + seatNum + "</html>");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(tripInfoLabel, gbc);

        // Amount (you can calculate this based on trip type, distance, etc.)
        double ticketPrice = calculateTicketPrice(trip);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Amount:"), gbc);

        JTextField amountField = new JTextField(String.valueOf(ticketPrice), 10);
        amountField.setEditable(false);
        gbc.gridx = 1;
        panel.add(amountField, gbc);

        // Currency
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Currency:"), gbc);

        JComboBox<String> currencyCombo = new JComboBox<>(new String[] { "USD", "EUR", "GBP" });
        gbc.gridx = 1;
        panel.add(currencyCombo, gbc);

        // Payment method
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Payment Method:"), gbc);

        JComboBox<String> paymentCombo = new JComboBox<>(
                new String[] { "Bank Transfer", "Visa", "MasterCard", "PayPal" });
        gbc.gridx = 1;
        panel.add(paymentCombo, gbc);

        // Email for PayPal (conditionally visible)
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(currentUser.getEmail(), 15);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Initially hide email field
        emailLabel.setVisible(false);
        emailField.setVisible(false);

        // Show/hide email field based on payment method
        paymentCombo.addActionListener(e -> {
            boolean isPayPal = "PayPal".equals(paymentCombo.getSelectedItem());
            emailLabel.setVisible(isPayPal);
            emailField.setVisible(isPayPal);
            paymentDialog.revalidate();
        });

        // Buttons
        JPanel buttonPanel = new JPanel();

        JButton payButton = new JButton("Pay & Reserve");
        payButton.setBackground(new Color(60, 179, 113));
        payButton.setForeground(Color.WHITE);
        payButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String currency = (String) currencyCombo.getSelectedItem();
                String paymentMethod = (String) paymentCombo.getSelectedItem();
                String email = emailField.getText().trim();

                // Process payment
                boolean paymentResult = paymentService.processPayment(paymentMethod, amount, currency, email);

                if (paymentResult) {
                    // Payment successful, make reservation
                    if (reservationService.makeReservation(currentUser.getUsername(), tripId, seatNum)) {
                        JOptionPane.showMessageDialog(paymentDialog,
                                "Payment successful!\nReservation confirmed for seat " + seatNum,
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        paymentDialog.dispose();
                        updateSeatDisplay((JPanel) ((JPanel) tabbedPane.getSelectedComponent()).getComponent(1),
                                tripId);
                    } else {
                        JOptionPane.showMessageDialog(paymentDialog,
                                "Payment processed but reservation failed!\nPlease contact support.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(paymentDialog,
                            "Payment failed!\nPlease try again with different payment method.",
                            "Payment Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(paymentDialog, "Please enter a valid amount!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> paymentDialog.dispose());

        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(buttonPanel, gbc);

        paymentDialog.add(panel);
        paymentDialog.setVisible(true);
    }

    // Helper method to calculate ticket price based on trip
    private double calculateTicketPrice(Trip trip) {
        // Simple pricing logic - you can make this more sophisticated
        double basePrice = 50.0;

        if ("Plane".equals(trip.getTransportType())) {
            basePrice = 150.0;
        } else if ("Bus".equals(trip.getTransportType())) {
            basePrice = 30.0;
        }

        // You could add more logic here based on distance, time, etc.
        return basePrice;
    }

    private JPanel createMyReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = { "Reservation ID", "Trip ID", "From", "To", "Date & Time", "Seat", "Reservation Date" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshMyReservationsTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshMyReservationsTable(model));
        buttonPanel.add(refreshButton);

        JButton cancelButton = new JButton("Cancel Selected Reservation");
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String reservationId = (String) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to cancel reservation " + reservationId + "?",
                        "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (reservationService.cancelReservation(reservationId)) {
                        JOptionPane.showMessageDialog(this, "Reservation cancelled successfully!");
                        refreshMyReservationsTable(model);
                        // Refresh trips table if it's visible
                        if (tabbedPane.getSelectedIndex() == 0) {
                            JPanel tripsPanel = (JPanel) tabbedPane.getComponentAt(0);
                            JScrollPane tripsScrollPane = (JScrollPane) tripsPanel.getComponent(0);
                            JTable tripsTable = (JTable) tripsScrollPane.getViewport().getView();
                            DefaultTableModel tripsModel = (DefaultTableModel) tripsTable.getModel();
                            refreshTripsTable(tripsModel);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel reservation!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a reservation to cancel!");
            }
        });
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshMyReservationsTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Reservation> userReservations = reservationService.getUserReservations(currentUser.getUsername());

        for (Reservation reservation : userReservations) {
            Trip trip = tripService.findTripById(reservation.getTripId());
            if (trip != null) {
                model.addRow(new Object[] {
                        reservation.getReservationId(),
                        reservation.getTripId(),
                        trip.getFrom(),
                        trip.getTo(),
                        trip.getDateTime(),
                        reservation.getSeatNumber(),
                        reservation.getReservationDate()
                });
            }
        }
    }

    private JPanel createAllReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = { "Reservation ID", "Username", "Trip ID", "From", "To", "Date & Time", "Seat",
                "Reservation Date" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshAllReservationsTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshAllReservationsTable(model));
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshAllReservationsTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Reservation> allReservations = reservationService.getAllReservations();

        for (Reservation reservation : allReservations) {
            Trip trip = tripService.findTripById(reservation.getTripId());
            if (trip != null) {
                model.addRow(new Object[] {
                        reservation.getReservationId(),
                        reservation.getUsername(),
                        reservation.getTripId(),
                        trip.getFrom(),
                        trip.getTo(),
                        trip.getDateTime(),
                        reservation.getSeatNumber(),
                        reservation.getReservationDate()
                });
            }
        }
    }

    // createUserTabs() metodunu güncelleyin:
    private void createUserTabs() {
        tabbedPane.addTab("Available Trips", createTripsPanel());
        tabbedPane.addTab("Make Reservation", createReservationPanel());
        tabbedPane.addTab("My Reservations", createMyReservationsPanel());
        tabbedPane.addTab("Send Feedback", createFeedbackPanel()); // YENİ
        tabbedPane.addTab("My Feedbacks", createMyFeedbacksPanel()); // YENİ
    }

    // createAdminTabs() metodunu güncelleyin:
    private void createAdminTabs() {
        tabbedPane.addTab("All Trips", createTripsPanel());
        tabbedPane.addTab("Add Trip", createAddTripPanel());
        tabbedPane.addTab("All Reservations", createAllReservationsPanel());
        tabbedPane.addTab("User Feedbacks", createAdminFeedbackPanel()); // YENİ
        tabbedPane.addTab("Payment Test", createPaymentTestPanel());
    }

    // Kullanıcı feedback gönderme paneli
    private JPanel createFeedbackPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Başlık
        JLabel titleLabel = new JLabel("Send Feedback or Trip Request");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Feedback başlığı
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Title:"), gbc);

        JTextField titleField = new JTextField(30);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(titleField, gbc);

        // Önceden tanımlı başlık seçenekleri
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Quick Select:"), gbc);

        JComboBox<String> quickTitles = new JComboBox<>(new String[] {
                "-- Select a common request --",
                "New Route Request",
                "Service Quality",
                "Pricing Feedback",
                "Schedule Suggestion",
                "Technical Issue",
                "General Feedback"
        });

        quickTitles.addActionListener(e -> {
            String selected = (String) quickTitles.getSelectedItem();
            if (!selected.startsWith("--")) {
                titleField.setText(selected);
            }
        });

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(quickTitles, gbc);

        // Mesaj alanı
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        panel.add(new JLabel("Message:"), gbc);

        JTextArea messageArea = new JTextArea(8, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createLoweredBevelBorder());

        JScrollPane messageScroll = new JScrollPane(messageArea);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(messageScroll, gbc);

        // Örnek mesajlar butonu
        JButton exampleButton = new JButton("Example Messages");
        exampleButton.addActionListener(e -> showExampleMessages(messageArea));

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        panel.add(exampleButton, gbc);

        // Gönder butonu
        JButton submitButton = new JButton("Send Feedback");
        submitButton.setBackground(new Color(60, 179, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));

        submitButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String message = messageArea.getText().trim();

            if (title.isEmpty() || message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in both title and message!",
                        "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (message.length() < 10) {
                JOptionPane.showMessageDialog(this, "Message should be at least 10 characters long!",
                        "Message Too Short", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String feedbackId = feedbackService.submitFeedback(currentUser.getUsername(), title, message);

            JOptionPane.showMessageDialog(this,
                    "Feedback submitted successfully!\nFeedback ID: " + feedbackId +
                            "\n\nThank you for your feedback. Admin will review it soon.",
                    "Feedback Sent", JOptionPane.INFORMATION_MESSAGE);

            // Formu temizle
            titleField.setText("");
            messageArea.setText("");
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(submitButton, gbc);

        return panel;
    }

    // Örnek mesajları gösteren dialog
    private void showExampleMessages(JTextArea messageArea) {
        String[] examples = {
                "I would like to request a new flight route from Ankara to Erzurum. This route would be very convenient for business travelers and students.",
                "The bus service from Izmir to Istanbul was excellent. The seats were comfortable and the journey was smooth. Thank you!",
                "Could you please consider adding more departure times for the Istanbul-Trabzon route? Current schedule is limited.",
                "I think the prices for plane tickets are a bit high compared to other companies. Could you review them?",
                "The reservation system is very user-friendly. Great job on the interface design!"
        };

        JDialog dialog = new JDialog(this, "Example Messages", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String example : examples) {
            listModel.addElement(example);
        }

        JList<String> exampleList = new JList<>(listModel);
        exampleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(exampleList);

        JButton useButton = new JButton("Use This Message");
        useButton.addActionListener(e -> {
            String selected = exampleList.getSelectedValue();
            if (selected != null) {
                messageArea.setText(selected);
                dialog.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(useButton);

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Kullanıcının gönderdiği feedback'leri görüntüleme paneli
    private JPanel createMyFeedbacksPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = { "Feedback ID", "Title", "Status", "Date", "Admin Response" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Sütun genişliklerini ayarla
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        refreshMyFeedbacksTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshMyFeedbacksTable(model));
        buttonPanel.add(refreshButton);

        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String feedbackId = (String) model.getValueAt(selectedRow, 0);
                showFeedbackDetails(feedbackId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a feedback to view!");
            }
        });
        buttonPanel.add(viewButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Admin feedback yönetim paneli
    private JPanel createAdminFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Üst panel - filtreler
        JPanel topPanel = new JPanel(new FlowLayout());

        JLabel filterLabel = new JLabel("Filter by Status:");
        JComboBox<String> statusFilter = new JComboBox<>(new String[] {
                "All", "PENDING", "REVIEWED", "RESOLVED"
        });

        topPanel.add(filterLabel);
        topPanel.add(statusFilter);

        // Pending feedback sayısını göster
        JLabel pendingLabel = new JLabel();
        updatePendingCount(pendingLabel);
        pendingLabel.setForeground(Color.RED);
        pendingLabel.setFont(new Font("Arial", Font.BOLD, 12));
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(pendingLabel);

        panel.add(topPanel, BorderLayout.NORTH);

        // Tablo
        String[] columns = { "Feedback ID", "User", "Title", "Status", "Date", "Message Preview" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Sütun genişliklerini ayarla
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);
        table.getColumnModel().getColumn(5).setPreferredWidth(200);

        refreshAdminFeedbacksTable(model, "All");

        // Filtre değiştiğinde tabloyu güncelle
        statusFilter.addActionListener(e -> {
            String selectedStatus = (String) statusFilter.getSelectedItem();
            refreshAdminFeedbacksTable(model, selectedStatus);
            updatePendingCount(pendingLabel);
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            String selectedStatus = (String) statusFilter.getSelectedItem();
            refreshAdminFeedbacksTable(model, selectedStatus);
            updatePendingCount(pendingLabel);
        });
        buttonPanel.add(refreshButton);

        JButton viewButton = new JButton("View & Respond");
        viewButton.setBackground(new Color(70, 130, 180));
        viewButton.setForeground(Color.WHITE);
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String feedbackId = (String) model.getValueAt(selectedRow, 0);
                showAdminFeedbackDialog(feedbackId, model, statusFilter, pendingLabel);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a feedback to respond!");
            }
        });
        buttonPanel.add(viewButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Pending feedback sayısını güncelle
    private void updatePendingCount(JLabel label) {
        int pendingCount = feedbackService.getPendingFeedbackCount();
        label.setText("Pending Feedbacks: " + pendingCount);
    }

    // Kullanıcı feedback tablosunu yenile
    private void refreshMyFeedbacksTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Feedback> userFeedbacks = feedbackService.getUserFeedbacks(currentUser.getUsername());

        for (Feedback feedback : userFeedbacks) {
            model.addRow(new Object[] {
                    feedback.getFeedbackId(),
                    feedback.getTitle(),
                    feedback.getStatus(),
                    feedback.getFeedbackDate(),
                    feedback.getAdminResponse().isEmpty() ? "No response yet" : feedback.getAdminResponse()
            });
        }
    }

    // Admin feedback tablosunu yenile
    private void refreshAdminFeedbacksTable(DefaultTableModel model, String statusFilter) {
        model.setRowCount(0);
        List<Feedback> feedbacks;

        if ("All".equals(statusFilter)) {
            feedbacks = feedbackService.getAllFeedbacks();
        } else {
            Feedback.FeedbackStatus status = Feedback.FeedbackStatus.valueOf(statusFilter);
            feedbacks = feedbackService.getFeedbacksByStatus(status);
        }

        for (Feedback feedback : feedbacks) {
            String messagePreview = feedback.getMessage().length() > 50 ? feedback.getMessage().substring(0, 47) + "..."
                    : feedback.getMessage();

            model.addRow(new Object[] {
                    feedback.getFeedbackId(),
                    feedback.getUsername(),
                    feedback.getTitle(),
                    feedback.getStatus(),
                    feedback.getFeedbackDate(),
                    messagePreview
            });
        }
    }

    // Feedback detaylarını gösteren dialog
    private void showFeedbackDetails(String feedbackId) {
        Feedback feedback = feedbackService.findFeedbackById(feedbackId);
        if (feedback == null)
            return;

        JDialog dialog = new JDialog(this, "Feedback Details", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Feedback bilgileri
        addInfoRow(panel, gbc, 0, "Feedback ID:", feedback.getFeedbackId());
        addInfoRow(panel, gbc, 1, "Title:", feedback.getTitle());
        addInfoRow(panel, gbc, 2, "Status:", feedback.getStatus().toString());
        addInfoRow(panel, gbc, 3, "Date:", feedback.getFeedbackDate());

        // Mesaj
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Message:"), gbc);

        JTextArea messageArea = new JTextArea(feedback.getMessage(), 6, 30);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JScrollPane messageScroll = new JScrollPane(messageArea);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        panel.add(messageScroll, gbc);

        // Admin yanıtı
        if (!feedback.getAdminResponse().isEmpty()) {
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            gbc.weighty = 0;
            panel.add(new JLabel("Admin Response:"), gbc);

            JTextArea responseArea = new JTextArea(feedback.getAdminResponse(), 4, 30);
            responseArea.setEditable(false);
            responseArea.setLineWrap(true);
            responseArea.setWrapStyleWord(true);
            responseArea.setBackground(new Color(240, 248, 255));

            JScrollPane responseScroll = new JScrollPane(responseArea);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 0.3;
            panel.add(responseScroll, gbc);
        }

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Admin feedback yanıt dialogu
    private void showAdminFeedbackDialog(String feedbackId, DefaultTableModel model,
            JComboBox<String> statusFilter, JLabel pendingLabel) {
        Feedback feedback = feedbackService.findFeedbackById(feedbackId);
        if (feedback == null)
            return;

        JDialog dialog = new JDialog(this, "Admin Response - " + feedbackId, true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Feedback bilgileri (üst kısım)
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints infoGbc = new GridBagConstraints();
        infoGbc.insets = new Insets(2, 5, 2, 5);
        infoGbc.anchor = GridBagConstraints.WEST;

        addInfoRow(infoPanel, infoGbc, 0, "User:", feedback.getUsername());
        addInfoRow(infoPanel, infoGbc, 1, "Title:", feedback.getTitle());
        addInfoRow(infoPanel, infoGbc, 2, "Date:", feedback.getFeedbackDate());
        addInfoRow(infoPanel, infoGbc, 3, "Current Status:", feedback.getStatus().toString());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(infoPanel, gbc);

        // Kullanıcı mesajı
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("User Message:"), gbc);

        JTextArea userMessageArea = new JTextArea(feedback.getMessage(), 5, 30);
        userMessageArea.setEditable(false);
        userMessageArea.setLineWrap(true);
        userMessageArea.setWrapStyleWord(true);
        userMessageArea.setBackground(new Color(248, 248, 248));

        JScrollPane userMessageScroll = new JScrollPane(userMessageArea);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        panel.add(userMessageScroll, gbc);

        // Status değiştirme
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("New Status:"), gbc);

        JComboBox<Feedback.FeedbackStatus> statusCombo = new JComboBox<>(Feedback.FeedbackStatus.values());
        statusCombo.setSelectedItem(feedback.getStatus());
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);

        // Admin yanıtı
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Admin Response:"), gbc);

        JTextArea responseArea = new JTextArea(feedback.getAdminResponse(), 6, 30);
        responseArea.setLineWrap(true);
        responseArea.setWrapStyleWord(true);

        JScrollPane responseScroll = new JScrollPane(responseArea);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        panel.add(responseScroll, gbc);

        // Hızlı yanıt butonları
        JPanel quickResponsePanel = new JPanel(new FlowLayout());

        JButton thankButton = new JButton("Thank User");
        thankButton
                .addActionListener(e -> responseArea.setText("Thank you for your feedback! We appreciate your input."));

        JButton reviewButton = new JButton("Under Review");
        reviewButton.addActionListener(e -> responseArea.setText(
                "Thank you for your suggestion. We are reviewing this request and will consider it for future implementations."));

        JButton resolvedButton = new JButton("Request Added");
        resolvedButton.addActionListener(e -> responseArea.setText(
                "Great news! We have added the route you requested. You can now find it in our trip listings. Thank you for the suggestion!"));

        quickResponsePanel.add(thankButton);
        quickResponsePanel.add(reviewButton);
        quickResponsePanel.add(resolvedButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        panel.add(quickResponsePanel, gbc);

        // Buton paneli
        // Buton paneli (showAdminFeedbackDialog metodunun devamı)
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton updateButton = new JButton("Update Response");
        updateButton.setBackground(new Color(60, 179, 113));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Arial", Font.BOLD, 12));

        updateButton.addActionListener(e -> {
            FeedbackStatus newStatus = (FeedbackStatus) statusCombo.getSelectedItem();
            String response = responseArea.getText().trim();

            if (feedbackService.updateFeedbackStatus(feedbackId, newStatus, response)) {
                JOptionPane.showMessageDialog(dialog,
                        "Feedback updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Tabloyu güncelle
                String selectedStatus = (String) statusFilter.getSelectedItem();
                refreshAdminFeedbacksTable(model, selectedStatus);
                updatePendingCount(pendingLabel);

                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Failed to update feedback!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        panel.add(buttonPanel, gbc);

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    // Bilgi satırı ekleme yardımcı metodu
    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(5, 15, 5, 5);
        JLabel valueComponent = new JLabel(value);
        panel.add(valueComponent, gbc);

        gbc.insets = new Insets(5, 5, 5, 5); // Reset insets
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel
            }
            new ReservationSystemGUI().setVisible(true);
        });
    }
}