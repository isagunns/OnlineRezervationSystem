/*
 * package gui;
 * 
 * import model.User;
 * import model.Reservation;
 * import service.UserService;
 * import service.TripService;
 * import service.ReservationService;
 * 
 * import java.util.Scanner;
 * import java.util.regex.Pattern;
 * import java.time.LocalDateTime;
 * import java.time.format.DateTimeFormatter;
 * import java.time.format.DateTimeParseException;
 * import java.util.List;
 * 
 * public class ReservationSystemApp {
 * private static Scanner scanner = new Scanner(System.in);
 * private static UserService userService = new UserService();
 * private static TripService tripService = new TripService();
 * private static ReservationService reservationService = new
 * ReservationService();
 * 
 * public static void main(String[] args) {
 * User currentUser = null;
 * 
 * System.out.println("=== Welcome to the Online Reservation System ===");
 * 
 * // Login or Register
 * while (currentUser == null) {
 * currentUser = handleAuthentication();
 * }
 * 
 * // Determine whether the user is an admin
 * if (currentUser.isAdmin()) {
 * adminMenu(currentUser);
 * } else {
 * userMenu(currentUser);
 * }
 * 
 * scanner.close();
 * }
 * 
 * private static User handleAuthentication() {
 * System.out.println("\nSelect an option:");
 * System.out.println("1. Log In");
 * System.out.println("2. Register");
 * System.out.println("3. Exit");
 * System.out.print("Your choice: ");
 * 
 * String choice = scanner.nextLine().trim();
 * 
 * switch (choice) {
 * case "1":
 * return handleLogin();
 * case "2":
 * handleRegistration();
 * return null; // Return to authentication menu after registration
 * case "3":
 * System.out.println("Thank you for using our service. Goodbye!");
 * System.exit(0);
 * return null;
 * default:
 * System.out.println("Invalid option. Please choose 1, 2, or 3.");
 * return null;
 * }
 * }
 * 
 * private static User handleLogin() {
 * System.out.print("Username: ");
 * String username = scanner.nextLine().trim();
 * System.out.print("Password: ");
 * String password = scanner.nextLine();
 * 
 * User user = userService.loginUser(username, password);
 * if (user == null) {
 * System.out.println("Invalid login credentials. Please try again.");
 * } else {
 * System.out.println("Welcome, " + user.getFullName() + "!");
 * }
 * return user;
 * }
 * 
 * private static void handleRegistration() {
 * System.out.print("Username: ");
 * String username = scanner.nextLine().trim();
 * 
 * if (username.isEmpty()) {
 * System.out.println("Username cannot be empty.");
 * return;
 * }
 * 
 * System.out.print("Password: ");
 * String password = scanner.nextLine();
 * 
 * if (password.length() < 6) {
 * System.out.println("Password must be at least 6 characters long.");
 * return;
 * }
 * 
 * System.out.print("Full Name: ");
 * String fullName = scanner.nextLine().trim();
 * 
 * if (fullName.isEmpty()) {
 * System.out.println("Full name cannot be empty.");
 * return;
 * }
 * 
 * System.out.print("Email: ");
 * String email = scanner.nextLine().trim();
 * 
 * if (!isValidEmail(email)) {
 * System.out.println("Please enter a valid email address.");
 * return;
 * }
 * 
 * boolean registered = userService.registerUser(username, password, fullName,
 * email);
 * if (registered) {
 * System.out.println("Registration successful! You can log in now.");
 * } else {
 * System.out.
 * println("Username already exists. Please choose a different username.");
 * }
 * }
 * 
 * private static boolean isValidEmail(String email) {
 * String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
 * Pattern pattern = Pattern.compile(emailRegex);
 * return pattern.matcher(email).matches();
 * }
 * 
 * private static void adminMenu(User currentUser) {
 * while (true) {
 * System.out.println("\n=== Admin Panel ===");
 * System.out.println("1. List All Trips");
 * System.out.println("2. Add New Trip");
 * System.out.println("3. Remove Trip");
 * System.out.println("4. View Trip Details");
 * System.out.println("5. View All Reservations");
 * System.out.println("6. Logout");
 * 
 * System.out.print("Your choice: ");
 * String choice = scanner.nextLine().trim();
 * 
 * switch (choice) {
 * case "1":
 * System.out.println("\n--- All Trips ---");
 * tripService.listTrips();
 * break;
 * case "2":
 * addNewTrip();
 * break;
 * case "3":
 * removeTrip();
 * break;
 * case "4":
 * viewTripDetails();
 * break;
 * case "5":
 * viewAllReservations();
 * break;
 * case "6":
 * System.out.println("Logging out...");
 * return;
 * default:
 * System.out.println("Invalid option. Please choose 1-6.");
 * }
 * }
 * }
 * 
 * private static void userMenu(User currentUser) {
 * while (true) {
 * System.out.println("\n=== User Panel ===");
 * System.out.println("1. View Available Trips");
 * System.out.println("2. Make a Reservation");
 * System.out.println("3. Cancel My Reservation");
 * System.out.println("4. View My Reservations");
 * System.out.println("5. View Trip Details");
 * System.out.println("6. Logout");
 * 
 * System.out.print("Your choice: ");
 * String choice = scanner.nextLine().trim();
 * 
 * switch (choice) {
 * case "1":
 * System.out.println("\n--- Available Trips ---");
 * tripService.listTrips();
 * break;
 * case "2":
 * makeReservation(currentUser);
 * break;
 * case "3":
 * cancelUserReservation(currentUser);
 * break;
 * case "4":
 * viewMyReservations(currentUser);
 * break;
 * case "5":
 * viewTripDetails();
 * break;
 * case "6":
 * System.out.println("Logging out...");
 * return;
 * default:
 * System.out.println("Invalid option. Please choose 1-6.");
 * }
 * }
 * }
 * 
 * private static void addNewTrip() {
 * try {
 * System.out.print("Trip ID: ");
 * String id = scanner.nextLine().trim();
 * 
 * if (id.isEmpty()) {
 * System.out.println("Trip ID cannot be empty.");
 * return;
 * }
 * 
 * if (tripService.findTripById(id) != null) {
 * System.out.println("Trip ID already exists. Please use a unique ID.");
 * return;
 * }
 * 
 * System.out.print("From: ");
 * String from = scanner.nextLine().trim();
 * 
 * if (from.isEmpty()) {
 * System.out.println("Origin city cannot be empty.");
 * return;
 * }
 * 
 * System.out.print("To: ");
 * String to = scanner.nextLine().trim();
 * 
 * if (to.isEmpty()) {
 * System.out.println("Destination city cannot be empty.");
 * return;
 * }
 * 
 * System.out.print("Date and Time (YYYY-MM-DD HH:MM): ");
 * String dateTime = scanner.nextLine().trim();
 * 
 * if (!isValidDateTime(dateTime)) {
 * System.out.
 * println("Invalid date format. Please use YYYY-MM-DD HH:MM format (e.g., 2025-12-25 14:30)."
 * );
 * return;
 * }
 * 
 * System.out.print("Transport Type (Bus/Plane): ");
 * String transportType = scanner.nextLine().trim();
 * 
 * if (!transportType.equalsIgnoreCase("bus") &&
 * !transportType.equalsIgnoreCase("plane")) {
 * System.out.println("Invalid transport type. Please enter 'Bus' or 'Plane'.");
 * return;
 * }
 * 
 * System.out.print("Total Number of Seats: ");
 * int totalSeats = getIntInput();
 * 
 * if (totalSeats <= 0) {
 * System.out.println("Number of seats must be positive.");
 * return;
 * }
 * 
 * tripService.addTrip(id, from, to, dateTime, transportType, totalSeats);
 * System.out.println("Trip added successfully!");
 * 
 * } catch (Exception e) {
 * System.out.println("Error adding trip. Please try again.");
 * }
 * }
 * 
 * private static void removeTrip() {
 * System.out.print("Enter Trip ID to remove: ");
 * String deleteId = scanner.nextLine().trim();
 * 
 * if (deleteId.isEmpty()) {
 * System.out.println("Trip ID cannot be empty.");
 * return;
 * }
 * 
 * if (tripService.removeTrip(deleteId)) {
 * System.out.println("Trip removed successfully!");
 * } else {
 * System.out.println("Trip not found. Please check the Trip ID.");
 * }
 * }
 * 
 * private static void makeReservation(User currentUser) {
 * System.out.print("Trip ID: ");
 * String tripId = scanner.nextLine().trim();
 * 
 * if (tripId.isEmpty()) {
 * System.out.println("Trip ID cannot be empty.");
 * return;
 * }
 * 
 * var trip = tripService.findTripById(tripId);
 * if (trip == null) {
 * System.out.println("Trip not found. Please check the Trip ID.");
 * return;
 * }
 * 
 * System.out.println("\nCurrent seat status for " + trip.toString() + ":");
 * trip.printSeatStatus();
 * System.out.println("Available seats: 0 to " + (trip.getSeats().length - 1));
 * 
 * System.out.print("Seat number (0-" + (trip.getSeats().length - 1) + "): ");
 * int seatNumber = getIntInput();
 * 
 * if (!isValidSeatNumber(seatNumber, trip.getSeats().length)) {
 * System.out
 * .println("Invalid seat number. Please choose between 0 and " +
 * (trip.getSeats().length - 1) + ".");
 * return;
 * }
 * 
 * if (trip.getSeats()[seatNumber]) {
 * System.out.println("Seat " + seatNumber +
 * " is already reserved. Please choose another seat.");
 * return;
 * }
 * 
 * if (tripService.reserveSeat(tripId, seatNumber)) {
 * String reservationId =
 * reservationService.createReservation(currentUser.getUsername(), tripId,
 * seatNumber);
 * System.out.println("Reservation successful! Seat " + seatNumber +
 * " has been reserved.");
 * System.out.println("Your reservation ID: " + reservationId);
 * } else {
 * System.out.println("Reservation failed. Please try again.");
 * }
 * }
 * 
 * private static void cancelUserReservation(User currentUser) {
 * List<Reservation> userReservations =
 * reservationService.getUserReservations(currentUser.getUsername());
 * 
 * if (userReservations.isEmpty()) {
 * System.out.println("You have no reservations to cancel.");
 * return;
 * }
 * 
 * System.out.println("\n--- Your Reservations ---");
 * for (int i = 0; i < userReservations.size(); i++) {
 * Reservation res = userReservations.get(i);
 * var trip = tripService.findTripById(res.getTripId());
 * System.out.println((i + 1) + ". " + res.getReservationId() + " - " +
 * res.getTripId() + " (Seat " + res.getSeatNumber() + ") - " +
 * (trip != null ? trip.getFrom() + " to " + trip.getTo() : "Unknown trip"));
 * }
 * 
 * System.out.print("Select reservation to cancel (1-" + userReservations.size()
 * + "): ");
 * int choice = getIntInput();
 * 
 * if (choice < 1 || choice > userReservations.size()) {
 * System.out.println("Invalid selection.");
 * return;
 * }
 * 
 * Reservation selectedRes = userReservations.get(choice - 1);
 * 
 * if (reservationService.cancelReservation(currentUser.getUsername(),
 * selectedRes.getTripId(), selectedRes.getSeatNumber()) &&
 * tripService.cancelReservation(selectedRes.getTripId(),
 * selectedRes.getSeatNumber())) {
 * System.out.println("Reservation cancelled successfully!");
 * } else {
 * System.out.println("Cancellation failed. Please try again.");
 * }
 * }
 * 
 * private static void viewMyReservations(User currentUser) {
 * List<Reservation> userReservations =
 * reservationService.getUserReservations(currentUser.getUsername());
 * 
 * if (userReservations.isEmpty()) {
 * System.out.println("You have no reservations.");
 * return;
 * }
 * 
 * System.out.println("\n--- Your Reservations ---");
 * for (Reservation res : userReservations) {
 * var trip = tripService.findTripById(res.getTripId());
 * System.out.println("Reservation ID: " + res.getReservationId());
 * System.out.println("Trip: " + res.getTripId() + " - Seat " +
 * res.getSeatNumber());
 * if (trip != null) {
 * System.out.println("Route: " + trip.getFrom() + " to " + trip.getTo());
 * System.out.println("Date & Time: " + trip.getDateTime());
 * }
 * System.out.println("Reserved on: " + res.getReservationDate());
 * System.out.println("---");
 * }
 * }
 * 
 * private static void viewAllReservations() {
 * List<Reservation> allReservations = reservationService.getAllReservations();
 * 
 * if (allReservations.isEmpty()) {
 * System.out.println("No reservations found.");
 * return;
 * }
 * 
 * System.out.println("\n--- All Reservations ---");
 * for (Reservation res : allReservations) {
 * var trip = tripService.findTripById(res.getTripId());
 * System.out.println("Reservation ID: " + res.getReservationId());
 * System.out.println("User: " + res.getUsername());
 * System.out.println("Trip: " + res.getTripId() + " - Seat " +
 * res.getSeatNumber());
 * if (trip != null) {
 * System.out.println("Route: " + trip.getFrom() + " to " + trip.getTo());
 * }
 * System.out.println("Reserved on: " + res.getReservationDate());
 * System.out.println("---");
 * }
 * }
 * 
 * private static void viewTripDetails() {
 * System.out.print("Enter Trip ID: ");
 * String tripId = scanner.nextLine().trim();
 * 
 * if (tripId.isEmpty()) {
 * System.out.println("Trip ID cannot be empty.");
 * return;
 * }
 * 
 * var trip = tripService.findTripById(tripId);
 * if (trip != null) {
 * System.out.println("\n--- Trip Details ---");
 * System.out.println(trip);
 * trip.printSeatStatus();
 * 
 * // Show reservations for this trip
 * List<Reservation> tripReservations =
 * reservationService.getTripReservations(tripId);
 * if (!tripReservations.isEmpty()) {
 * System.out.println("\nReservations for this trip:");
 * for (Reservation res : tripReservations) {
 * System.out.println("- Seat " + res.getSeatNumber() + ": " + res.getUsername()
 * +
 * " (Reserved: " + res.getReservationDate() + ")");
 * }
 * }
 * } else {
 * System.out.println("Trip not found. Please check the Trip ID.");
 * }
 * }
 * 
 * private static int getIntInput() {
 * while (true) {
 * try {
 * String input = scanner.nextLine().trim();
 * return Integer.parseInt(input);
 * } catch (NumberFormatException e) {
 * System.out.print("Please enter a valid number: ");
 * }
 * }
 * }
 * 
 * private static boolean isValidDateTime(String dateTime) {
 * try {
 * DateTimeFormatter formatter =
 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
 * LocalDateTime.parse(dateTime, formatter);
 * return true;
 * } catch (DateTimeParseException e) {
 * return false;
 * }
 * }
 * 
 * private static boolean isValidSeatNumber(int seatNumber, int totalSeats) {
 * return seatNumber >= 0 && seatNumber < totalSeats;
 * }
 * }
 */