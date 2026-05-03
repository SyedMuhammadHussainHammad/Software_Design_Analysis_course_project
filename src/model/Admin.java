package model;

/**
 * Admin extends User — demonstrates INHERITANCE.
 */
@SuppressWarnings("unused")
public class Admin extends User {
    private int adminLevel;

    public Admin(int id, String name, String email, String password, int adminLevel) {
        super(id, name, email, password);
        this.adminLevel = adminLevel;
    }

    @Override
    public String getRole() { return "Admin"; }

    public int getAdminLevel() { return adminLevel; }
    public void setAdminLevel(int adminLevel) { this.adminLevel = adminLevel; }
}
