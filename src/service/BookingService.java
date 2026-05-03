package service;

import model.Booking;
import utils.DataStore;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Handles business logic for flight bookings.
 */
@SuppressWarnings("unused")
public class BookingService {
    private final DataStore store = DataStore.getInstance();

    public ArrayList<Booking> getBookingsForPassenger(int passengerId) {
        ArrayList<Booking> result = new ArrayList<>();
        for (Booking b : store.getBookings()) {
            if (b.getPassengerId() == passengerId) {
                result.add(b);
            }
        }
        return result;
    }

    public Booking createBooking(String flightId, int passengerId) {
        // Simple check to prevent double booking the same flight
        for (Booking b : store.getBookings()) {
            if (b.getFlightId().equals(flightId) && b.getPassengerId() == passengerId && b.getStatus().equals("Confirmed")) {
                return null; // Already booked
            }
        }
        
        String bookingId = "BKG-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Booking b = new Booking(bookingId, flightId, passengerId, "Confirmed");
        store.getBookings().add(b);
        return b;
    }

    public boolean cancelBooking(String bookingId) {
        for (Booking b : store.getBookings()) {
            if (b.getBookingId().equals(bookingId)) {
                b.setStatus("Cancelled");
                return true;
            }
        }
        return false;
    }
}
