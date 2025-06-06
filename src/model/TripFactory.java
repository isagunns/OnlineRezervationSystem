package model; //MVC Pattern

public class TripFactory {// Factory Pattern

    public static Trip createTrip(String tripId, String from, String to, String dateTime, String transportType,
            int seatCount) {
        if (transportType == null) {// Trip trip = TripFactory.createTrip(...);
            return null;
        }

        switch (transportType.toLowerCase()) {
            case "bus":
                return new BusTrip(tripId, from, to, dateTime, seatCount);
            case "plane":
                return new PlaneTrip(tripId, from, to, dateTime, seatCount);
            default:
                return null; // Invalid transport type
        }
    }
}
