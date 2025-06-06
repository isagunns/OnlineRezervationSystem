package model; //MVC Pattern

public abstract class Trip { // Template Method Pattern
    protected String tripId;
    protected String from;
    protected String to;
    protected String dateTime;
    protected boolean[] seats;

    public Trip(String tripId, String from, String to, String dateTime, int seatCount) {
        this.tripId = tripId;
        this.from = from;
        this.to = to;
        this.dateTime = dateTime;
        this.seats = new boolean[seatCount];
    }

    public String getTripId() {
        return tripId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean[] getSeats() {
        return seats;
    }

    public abstract String getTransportType(); // Template Method Pattern

    public boolean reserveSeat(int seatNumber) {
        if (seatNumber >= 0 && seatNumber < seats.length && !seats[seatNumber]) {
            seats[seatNumber] = true;
            return true;
        }
        return false;
    }

    public void cancelReservation(int seatNumber) {
        if (seatNumber >= 0 && seatNumber < seats.length) {
            seats[seatNumber] = false;
        }
    }

    public void printSeatStatus() {
        System.out.print("Seats: ");
        for (int i = 0; i < seats.length; i++) {
            System.out.print((seats[i] ? "[X]" : "[ ]") + " ");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return "[" + tripId + "] " + getTransportType() + " from " + from + " to " + to + " at " + dateTime;
    }
}
