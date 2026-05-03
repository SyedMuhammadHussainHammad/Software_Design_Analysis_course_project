package service;

import model.Aircraft;
import model.Flight;
import utils.DataStore;
import java.util.ArrayList;

/**
 * FlightService — handles all CRUD operations on Flights and Aircraft.
 */
@SuppressWarnings("unused")
public class FlightService {
    private static int flightIdCounter = 200;
    private final DataStore store = DataStore.getInstance();

    public ArrayList<Flight> getAllFlights() {
        return store.getFlightSchedule().getAllFlights();
    }

    public ArrayList<Aircraft> getAllAircraft() {
        return store.getAircraft();
    }

    public void addFlight(Flight flight) {
        store.getFlightSchedule().addFlight(flight);
    }

    public boolean updateFlight(String flightId, String origin, String destination,
            String departure, String arrival, String status, Aircraft aircraft) {
        Flight f = store.getFlightSchedule().findById(flightId);
        if (f == null)
            return false;
        f.setOrigin(origin);
        f.setDestination(destination);
        f.setDepartureTime(departure);
        f.setArrivalTime(arrival);
        f.setStatus(status);
        f.setAircraft(aircraft);
        return true;
    }

    public boolean deleteFlight(String flightId) {
        return store.getFlightSchedule().removeFlight(flightId);
    }

    public Flight findById(String flightId) {
        return store.getFlightSchedule().findById(flightId);
    }

    public String generateFlightId() {
        return "SK-" + (++flightIdCounter);
    }
}
