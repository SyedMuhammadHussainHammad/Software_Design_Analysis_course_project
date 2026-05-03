package model;

/**
 * CrewAssignment — demonstrates COMPOSITION with Flight.
 * A CrewAssignment cannot exist without a Flight.
 */
@SuppressWarnings("unused")
public class CrewAssignment {
    private int assignmentId;
    private String flightId;
    private String pilotName;
    private String dispatcherName;
    private String notes;

    public CrewAssignment(int assignmentId, String flightId,
            String pilotName, String dispatcherName, String notes) {
        this.assignmentId = assignmentId;
        this.flightId = flightId;
        this.pilotName = pilotName;
        this.dispatcherName = dispatcherName;
        this.notes = notes;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }

    public String getDispatcherName() {
        return dispatcherName;
    }

    public void setDispatcherName(String dispatcherName) {
        this.dispatcherName = dispatcherName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
