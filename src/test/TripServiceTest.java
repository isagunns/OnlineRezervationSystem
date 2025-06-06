package test;

import model.Trip;
import service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class TripServiceTest {

    private TripService tripService;

    @BeforeEach
    void setUp() {
        tripService = TripService.getInstance();
    }

    @Test
    @DisplayName("Singleton pattern test - should return same instance")
    void testSingletonPattern() {
        TripService instance1 = TripService.getInstance();
        TripService instance2 = TripService.getInstance();

        assertSame(instance1, instance2, "Both instances should be the same (Singleton pattern)");
    }

    @Test
    @DisplayName("Initial sample data should be loaded")
    void testInitialData() {
        List<Trip> trips = tripService.getAllTrips();

        assertFalse(trips.isEmpty(), "Trip list should not be empty initially");

        assertTrue(trips.size() >= 1, "At least one trip should exist");

        Trip trip = tripService.findTripById("T001");
        if (trip != null) {
            assertEquals("T001", trip.getTripId(), "T001 trip ID should be correct");
        }
    }

    @Test
    @DisplayName("Add new trip - valid transport type")
    void testAddTripValid() {
        int initialSize = tripService.getAllTrips().size();

        tripService.addTrip("T005", "Kayseri", "Samsun", "2025-06-05 12:00", "Bus", 25);

        List<Trip> trips = tripService.getAllTrips();
        assertEquals(initialSize + 1, trips.size(), "Trip count should increase by one");

        Trip addedTrip = tripService.findTripById("T005");
        assertNotNull(addedTrip, "Added trip should be found");
        assertEquals("Kayseri", addedTrip.getFrom(), "Departure point should be correct");
        assertEquals("Samsun", addedTrip.getTo(), "Destination should be correct");
    }

    @Test
    @DisplayName("Add new trip - invalid transport type")
    void testAddTripInvalidTransportType() {
        int initialSize = tripService.getAllTrips().size();

        tripService.addTrip("T006", "Konya", "Gaziantep", "2025-06-06 15:00", "Ship", 30);

        assertEquals(initialSize, tripService.getAllTrips().size(),
                "Trip should not be added with invalid transport type");
        assertNull(tripService.findTripById("T006"), "Trip should not be found with invalid trip ID");
    }

    @Test
    @DisplayName("Remove trip - existing trip")
    void testRemoveTripExists() {
        assertNotNull(tripService.findTripById("T001"), "T001 trip should exist initially");

        boolean removed = tripService.removeTrip("T001");

        assertTrue(removed, "Existing trip should be removable");
        assertNull(tripService.findTripById("T001"), "Removed trip should no longer be found");
    }

    @Test
    @DisplayName("Remove trip - non-existing trip")
    void testRemoveTripNotExists() {
        boolean removed = tripService.removeTrip("T999");

        assertFalse(removed, "Non-existing trip cannot be removed");
    }

    @Test
    @DisplayName("Find trip by ID - existing trip")
    void testFindTripByIdExists() {
        Trip trip = tripService.findTripById("T002");

        assertNotNull(trip, "Trip should be found with existing trip ID");
        assertEquals("T002", trip.getTripId(), "Found trip ID should be correct");
        assertEquals("Ankara", trip.getFrom(), "Found trip departure point should be correct");
    }

    @Test
    @DisplayName("Find trip by ID - non-existing trip")
    void testFindTripByIdNotExists() {
        Trip trip = tripService.findTripById("T999");

        assertNull(trip, "Should return null for non-existing trip ID");
    }

    @Test
    @DisplayName("Find trip by ID - case insensitive")
    void testFindTripByIdCaseInsensitive() {
        Trip trip1 = tripService.findTripById("t001");
        Trip trip2 = tripService.findTripById("T001");

        assertNotNull(trip1, "Trip should be found with lowercase ID");
        assertNotNull(trip2, "Trip should be found with uppercase ID");
        assertEquals(trip1.getTripId(), trip2.getTripId(), "Both searches should find the same trip");
    }

    @Test
    @DisplayName("Seat reservation - valid")
    void testReserveSeatValid() {
        List<Trip> trips = tripService.getAllTrips();
        assertFalse(trips.isEmpty(), "At least one trip should exist");

        Trip trip = trips.get(0);
        String tripId = trip.getTripId();

        int availableSeat = -1;
        for (int i = 0; i < trip.getSeats().length; i++) {
            if (tripService.isSeatAvailable(tripId, i)) {
                availableSeat = i;
                break;
            }
        }

        if (availableSeat != -1) {
            assertTrue(tripService.isSeatAvailable(tripId, availableSeat),
                    "Seat should be available initially");

            boolean reserved = tripService.reserveSeat(tripId, availableSeat);

            assertTrue(reserved, "Valid seat should be reservable");
            assertFalse(tripService.isSeatAvailable(tripId, availableSeat),
                    "Reserved seat should no longer be available");
        }
    }

    @Test
    @DisplayName("Seat reservation - non-existing trip")
    void testReserveSeatInvalidTrip() {
        boolean reserved = tripService.reserveSeat("T999", 0);

        assertFalse(reserved, "Seat should not be reservable for non-existing trip");
    }

    @Test
    @DisplayName("Cancel reservation - valid")
    void testCancelReservationValid() {
        tripService.reserveSeat("T001", 1);
        assertFalse(tripService.isSeatAvailable("T001", 1), "Seat should be reserved");

        boolean cancelled = tripService.cancelReservation("T001", 1);

        assertTrue(cancelled, "Reservation should be cancellable");
        assertTrue(tripService.isSeatAvailable("T001", 1), "Cancelled seat should be available again");
    }

    @Test
    @DisplayName("Cancel reservation - non-existing trip")
    void testCancelReservationInvalidTrip() {
        boolean cancelled = tripService.cancelReservation("T999", 0);

        assertFalse(cancelled, "Reservation should not be cancellable for non-existing trip");
    }

    @Test
    @DisplayName("Available seat count - not fully booked trip")
    void testGetAvailableSeatCount() {
        String tripId = "T001";
        Trip trip = tripService.findTripById(tripId);

        if (trip == null) {
            List<Trip> trips = tripService.getAllTrips();
            assertFalse(trips.isEmpty(), "At least one trip should exist");
            trip = trips.get(0);
            tripId = trip.getTripId();
        }

        int totalSeats = trip.getSeats().length;
        int initialAvailable = tripService.getAvailableSeatCount(tripId);

        assertTrue(initialAvailable >= 0 && initialAvailable <= totalSeats,
                "Available seat count should be within valid range");

        boolean seatReserved = false;
        for (int i = 0; i < totalSeats; i++) {
            if (tripService.isSeatAvailable(tripId, i)) {
                tripService.reserveSeat(tripId, i);
                seatReserved = true;
                break;
            }
        }

        if (seatReserved) {
            int afterReservation = tripService.getAvailableSeatCount(tripId);
            assertEquals(initialAvailable - 1, afterReservation,
                    "Available seat count should decrease by one after reservation");
        }
    }

    @Test
    @DisplayName("Available seat count - non-existing trip")
    void testGetAvailableSeatCountInvalidTrip() {
        int availableSeats = tripService.getAvailableSeatCount("T999");

        assertEquals(0, availableSeats, "Available seat count should be 0 for non-existing trip");
    }

    @Test
    @DisplayName("Seat availability check - valid seat")
    void testIsSeatAvailableValid() {
        List<Trip> trips = tripService.getAllTrips();
        assertFalse(trips.isEmpty(), "At least one trip should exist");

        Trip trip = trips.get(0);
        String tripId = trip.getTripId();

        int availableSeat = -1;
        for (int i = 0; i < trip.getSeats().length; i++) {
            if (tripService.isSeatAvailable(tripId, i)) {
                availableSeat = i;
                break;
            }
        }

        if (availableSeat != -1) {
            assertTrue(tripService.isSeatAvailable(tripId, availableSeat),
                    "Found seat should be available");

            tripService.reserveSeat(tripId, availableSeat);
            assertFalse(tripService.isSeatAvailable(tripId, availableSeat),
                    "Reserved seat should not be available");
        }
    }

    @Test
    @DisplayName("Seat availability check - invalid seat number")
    void testIsSeatAvailableInvalidSeatNumber() {
        String tripId = "T001";

        assertFalse(tripService.isSeatAvailable(tripId, -1), "Should return false for negative seat number");
        assertFalse(tripService.isSeatAvailable(tripId, 1000), "Should return false for too large seat number");
    }

    @Test
    @DisplayName("Seat availability check - non-existing trip")
    void testIsSeatAvailableInvalidTrip() {
        assertFalse(tripService.isSeatAvailable("T999", 0),
                "Seat availability check should return false for non-existing trip");
    }

    @Test
    @DisplayName("Get all trips")
    void testGetAllTrips() {
        List<Trip> trips = tripService.getAllTrips();

        assertNotNull(trips, "Trip list should not be null");
        assertFalse(trips.isEmpty(), "Trip list should not be empty");

        int originalSize = trips.size();
        trips.clear();

        List<Trip> tripsAgain = tripService.getAllTrips();
        assertEquals(originalSize, tripsAgain.size(),
                "getAllTrips() should return a copy, original list should not be modified");
    }

    @Test
    @DisplayName("Trip data integrity")
    void testTripDataIntegrity() {
        Trip trip = tripService.findTripById("T003");

        assertNotNull(trip, "T003 trip should be found");
        assertEquals("İstanbul", trip.getFrom(), "Departure point should be correct");
        assertEquals("Trabzon", trip.getTo(), "Destination should be correct");
        assertEquals("2025-06-03 08:30", trip.getDateTime(), "Date/time should be correct");
        assertEquals(12, trip.getSeats().length, "Seat count should be correct");
    }
}