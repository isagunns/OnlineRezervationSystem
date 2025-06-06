package model; //MVC Pattern

public class PlaneTrip extends Trip {
    public PlaneTrip(String tripId, String from, String to, String dateTime, int seatCount) {
        super(tripId, from, to, dateTime, seatCount);
    }

    @Override
    public String getTransportType() {// Template Method Pattern
        return "Plane";
    }
}
