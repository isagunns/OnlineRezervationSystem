package model; //MVC Pattern

public class BusTrip extends Trip {
    public BusTrip(String tripId, String from, String to, String dateTime, int seatCount) {
        super(tripId, from, to, dateTime, seatCount);
    }

    @Override
    public String getTransportType() {// Template Method Pattern
        return "Bus";
    }
}
