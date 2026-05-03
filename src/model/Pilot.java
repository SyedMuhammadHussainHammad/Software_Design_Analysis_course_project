package model;

/**
 * Pilot extends User — demonstrates INHERITANCE.
 */
@SuppressWarnings("unused")
public class Pilot extends User {
    private String licenseNumber;
    private int flightHours;

    public Pilot(int id, String name, String email, String password,
                 String licenseNumber, int flightHours) {
        super(id, name, email, password);
        this.licenseNumber = licenseNumber;
        this.flightHours = flightHours;
    }

    @Override
    public String getRole() { return "Pilot"; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public int getFlightHours() { return flightHours; }
    public void setFlightHours(int flightHours) { this.flightHours = flightHours; }
}
