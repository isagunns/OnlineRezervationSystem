package service;

import model.Trip;
import model.TripFactory;

import java.util.ArrayList;
import java.util.List;

public class TripService {
    // Singleton Pattern - Lazy Initialization
    private static TripService instance;
    private List<Trip> trips;

    // Private constructor to prevent instantiation
    private TripService() {
        this.trips = new ArrayList<>();

        // Örnek veriler (Factory deseni kullanılarak oluşturuluyor)
        trips.add(TripFactory.createTrip("T001", "İzmir", "İstanbul", "2025-06-01 10:00", "Bus", 10));
        trips.add(TripFactory.createTrip("T002", "Ankara", "Antalya", "2025-06-02 14:00", "Plane", 15));
        trips.add(TripFactory.createTrip("T003", "İstanbul", "Trabzon", "2025-06-03 08:30", "Plane", 12));
        trips.add(TripFactory.createTrip("T004", "Bursa", "Adana", "2025-06-04 16:45", "Bus", 20));
    }

    // Singleton getInstance method
    public static TripService getInstance() {
        if (instance == null) {
            instance = new TripService();
        }
        return instance;
    }

    public void addTrip(String id, String from, String to, String dateTime, String transportType, int seatCount) {
        Trip trip = TripFactory.createTrip(id, from, to, dateTime, transportType, seatCount);
        if (trip != null) {
            trips.add(trip);
        } else {
            System.out.println("Invalid transport type: " + transportType);
        }
    }

    public boolean removeTrip(String tripId) {
        return trips.removeIf(trip -> trip.getTripId().equalsIgnoreCase(tripId));
    }

    public List<Trip> getAllTrips() {
        return new ArrayList<>(trips); // Return a copy to prevent external modifications
    }

    public Trip findTripById(String tripId) {
        for (Trip trip : trips) {
            if (trip.getTripId().equalsIgnoreCase(tripId)) {
                return trip;
            }
        }
        return null;
    }

    public boolean reserveSeat(String tripId, int seatNumber) {
        Trip trip = findTripById(tripId);
        return trip != null && trip.reserveSeat(seatNumber);
    }

    public boolean cancelReservation(String tripId, int seatNumber) {
        Trip trip = findTripById(tripId);
        if (trip != null) {
            trip.cancelReservation(seatNumber);
            return true;
        }
        return false;
    }

    public void listTrips() {
        if (trips.isEmpty()) {
            System.out.println("No trips available.");
            return;
        }
        for (Trip trip : trips) {
            System.out.println(trip);
            trip.printSeatStatus();
            System.out.println();
        }
    }

    // GUI için ek metodlar
    public int getAvailableSeatCount(String tripId) {
        Trip trip = findTripById(tripId);
        if (trip == null)
            return 0;

        int available = 0;
        for (boolean seat : trip.getSeats()) {
            if (!seat)
                available++;
        }
        return available;
    }

    public boolean isSeatAvailable(String tripId, int seatNumber) {
        Trip trip = findTripById(tripId);
        if (trip == null || seatNumber < 0 || seatNumber >= trip.getSeats().length) {
            return false;
        }
        return !trip.getSeats()[seatNumber];
    }
}