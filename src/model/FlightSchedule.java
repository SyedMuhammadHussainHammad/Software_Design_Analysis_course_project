package model;

import java.util.ArrayList;

/**
 * FlightSchedule — demonstrates AGGREGATION.
 * Contains a collection of Flights that can exist independently.
 */
public class FlightSchedule {
    private String scheduleId;
    private ArrayList<Flight> flights;

    public FlightSchedule(String scheduleId) {
        this.scheduleId = scheduleId;
        this.flights = new ArrayList<>();
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public boolean removeFlight(String flightId) {
        return flights.removeIf(f -> f.getFlightId().equals(flightId));
    }

    public Flight findById(String flightId) {
        for (Flight f : flights) {
            if (f.getFlightId().equals(flightId))
                return f;
        }
        return null;
    }

    public ArrayList<Flight> getAllFlights() {
        return flights;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
}
