package service;

import model.Reservation;
import model.Trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
    // Singleton Pattern - Lazy Initialization
    private static ReservationService instance;
    private List<Reservation> reservations;
    private int nextReservationId;
    private TripService tripService;

    // Private constructor to prevent instantiation
    private ReservationService() {
        this.reservations = new ArrayList<>();
        this.nextReservationId = 1;
        this.tripService = TripService.getInstance(); // Initialize tripService
    }

    // Singleton getInstance method
    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public String createReservation(String username, String tripId, int seatNumber) {
        String reservationId = "R" + String.format("%04d", nextReservationId++);
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Reservation reservation = new Reservation(reservationId, username, tripId, seatNumber, currentDateTime);
        reservations.add(reservation);

        return reservationId;
    }

    // Original method with 3 parameters
    public boolean cancelReservation(String username, String tripId, int seatNumber) {
        return reservations.removeIf(reservation -> reservation.getUsername().equals(username) &&
                reservation.getTripId().equals(tripId) &&
                reservation.getSeatNumber() == seatNumber);
    }

    // New overloaded method for canceling by reservation ID (needed by GUI)
    public boolean cancelReservation(String reservationId) {
        // Find the reservation by ID
        Reservation reservationToCancel = reservations.stream()
                .filter(reservation -> reservation.getReservationId().equals(reservationId))
                .findFirst()
                .orElse(null);

        if (reservationToCancel == null) {
            return false;
        }

        // Free up the seat in the trip
        Trip trip = tripService.findTripById(reservationToCancel.getTripId());
        if (trip != null) {
            trip.getSeats()[reservationToCancel.getSeatNumber()] = false;
        }

        // Remove the reservation
        return reservations.removeIf(reservation -> reservation.getReservationId().equals(reservationId));
    }

    public List<Reservation> getUserReservations(String username) {
        return reservations.stream()
                .filter(reservation -> reservation.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public List<Reservation> getTripReservations(String tripId) {
        return reservations.stream()
                .filter(reservation -> reservation.getTripId().equals(tripId))
                .collect(Collectors.toList());
    }

    public boolean isUserOwnerOfReservation(String username, String tripId, int seatNumber) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getUsername().equals(username) &&
                        reservation.getTripId().equals(tripId) &&
                        reservation.getSeatNumber() == seatNumber);
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public boolean processReservationPayment(String reservationId, String paymentMethod,
            double amount, String userEmail) {
        PaymentService paymentService = PaymentService.getInstance();
        return paymentService.processPayment(paymentMethod, amount, "USD", userEmail);
    }

    public boolean makeReservation(String username, String tripId, int seatNumber) {
        // Get tripService instance if not initialized
        if (tripService == null) {
            tripService = TripService.getInstance();
        }

        // Trip'i bul
        Trip trip = tripService.findTripById(tripId);
        if (trip == null) {
            return false;
        }

        // Koltuk durumunu kontrol et
        if (seatNumber < 0 || seatNumber >= trip.getSeats().length) {
            return false; // Geçersiz koltuk numarası
        }

        if (trip.getSeats()[seatNumber]) {
            return false; // Koltuk zaten dolu
        }

        // Rezervasyon ID'si oluştur
        String reservationId = "RES" + System.currentTimeMillis();

        // Rezervasyon tarihi oluştur
        String reservationDate = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Rezervasyon oluştur
        Reservation reservation = new Reservation(reservationId, username, tripId, seatNumber, reservationDate);
        reservations.add(reservation);

        // Trip'te koltuğu işaretle
        trip.getSeats()[seatNumber] = true;

        return true;
    }
}