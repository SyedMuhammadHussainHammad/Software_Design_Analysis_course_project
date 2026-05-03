package model;

import java.time.LocalDateTime;

/**
 * Represents a flight reservation made by a Passenger.
 */
public class Booking {
    private String bookingId;
    private String flightId;
    private int passengerId;
    private String bookingDate;
    private String status; // e.g., "Confirmed", "Cancelled"

    public Booking(String bookingId, String flightId, int passengerId, String status) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.status = status;
        this.bookingDate = LocalDateTime.now().toString();
    }

    public String getBookingId() { return bookingId; }
    public String getFlightId() { return flightId; }
    public int getPassengerId() { return passengerId; }
    public String getBookingDate() { return bookingDate; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
