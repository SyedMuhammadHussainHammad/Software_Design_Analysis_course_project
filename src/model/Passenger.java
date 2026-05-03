package model;

/**
 * Passenger extends User.
 * Represents a customer who can book flights on the SkyStream platform.
 */
public class Passenger extends User {
    private String passportNumber;
    private String phoneNumber;

    public Passenger(int id, String name, String email, String password, String passportNumber, String phoneNumber) {
        super(id, name, email, password);
        this.passportNumber = passportNumber;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getRole() {
        return "Passenger";
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
