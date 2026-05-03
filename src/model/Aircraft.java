package model;

/**
 * Represents an Aircraft.
 * Associated with Flight (Flight HAS-A Aircraft).
 */
public class Aircraft {
    private String aircraftId;
    private String model;
    private int capacity;
    private String status; // "Active", "Maintenance", "Retired"

    public Aircraft(String aircraftId, String model, int capacity, String status) {
        this.aircraftId = aircraftId;
        this.model = model;
        this.capacity = capacity;
        this.status = status;
    }

    public String getAircraftId() { return aircraftId; }
    public void setAircraftId(String aircraftId) { this.aircraftId = aircraftId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() { return aircraftId + " — " + model; }
}
