package model;

/**
 * Flight — demonstrates ASSOCIATION (has-a Aircraft)
 * and COMPOSITION (owns a CrewAssignment).
 */
public class Flight {
    private String flightId;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private String status; // "Scheduled", "Departed", "Arrived", "Cancelled"

    // ASSOCIATION: Flight references Aircraft
    private Aircraft aircraft;

    // COMPOSITION: CrewAssignment is created/destroyed with the Flight
    private CrewAssignment crewAssignment;

    public Flight(String flightId, String origin, String destination,
            String departureTime, String arrivalTime,
            String status, Aircraft aircraft) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
        this.aircraft = aircraft;
    }

    // Getters & Setters
    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public CrewAssignment getCrewAssignment() {
        return crewAssignment;
    }

    public void setCrewAssignment(CrewAssignment crewAssignment) {
        this.crewAssignment = crewAssignment;
    }

    @Override
    public String toString() {
        return flightId + ": " + origin + " → " + destination + " [" + status + "]";
    }
}
