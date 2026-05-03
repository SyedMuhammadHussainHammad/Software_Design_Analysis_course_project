package model;

/**
 * Dispatcher extends User — demonstrates INHERITANCE.
 */
@SuppressWarnings("unused")
public class Dispatcher extends User {
    private String station;

    public Dispatcher(int id, String name, String email, String password, String station) {
        super(id, name, email, password);
        this.station = station;
    }

    @Override
    public String getRole() { return "Dispatcher"; }

    public String getStation() { return station; }
    public void setStation(String station) { this.station = station; }
}
