package model; //MVC Pattern

public class Reservation {
    private String reservationId;
    private String username;
    private String tripId;
    private int seatNumber;
    private String reservationDate;

    public Reservation(String reservationId, String username, String tripId, int seatNumber, String reservationDate) {
        this.reservationId = reservationId;
        this.username = username;
        this.tripId = tripId;
        this.seatNumber = seatNumber;
        this.reservationDate = reservationDate;
    }

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public String getUsername() {
        return username;
    }

    public String getTripId() {
        return tripId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", username='" + username + '\'' +
                ", tripId='" + tripId + '\'' +
                ", seatNumber=" + seatNumber +
                ", reservationDate='" + reservationDate + '\'' +
                '}';
    }
}