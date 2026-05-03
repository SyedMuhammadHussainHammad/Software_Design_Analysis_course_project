package service;

import model.*;
import java.util.ArrayList;

/**
 * AuthService — Singleton. Manages user authentication.
 * Demonstrates Singleton design pattern.
 */
@SuppressWarnings("unused")
public class AuthService {
    private static AuthService instance;
    private final ArrayList<User> users;

    private AuthService() {
        users = new ArrayList<>();
        // Seed users: Admin, 2 Pilots, 1 Dispatcher
        users.add(new Admin(1, "System Admin", "admin@sky.com", "admin123", 1));
        users.add(new Pilot(2, "Capt. Hussain", "pilot@sky.com", "pilot123", "PK-4521", 3200));
        users.add(new Pilot(3, "Capt. Sara", "sara@sky.com", "sara123", "PK-3310", 2100));
        users.add(new Dispatcher(4, "Ali Khan", "dispatch@sky.com", "disp123", "KHI-Control"));
        users.add(new Passenger(5, "John Doe", "passenger@sky.com", "pass123", "AB1234567", "+923001234567"));
    }

    public static AuthService getInstance() {
        if (instance == null)
            instance = new AuthService();
        return instance;
    }

    /**
     * Returns the matching User or null if credentials are invalid.
     */
    public User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email.trim())
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    /** Returns only Pilots (for crew assignment dropdowns) */
    public ArrayList<Pilot> getPilots() {
        ArrayList<Pilot> pilots = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Pilot)
                pilots.add((Pilot) u);
        }
        return pilots;
    }

    /** Returns only Dispatchers */
    public ArrayList<Dispatcher> getDispatchers() {
        ArrayList<Dispatcher> dispatchers = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Dispatcher)
                dispatchers.add((Dispatcher) u);
        }
        return dispatchers;
    }

    /** Checks whether an email is already registered. */
    public boolean isEmailTaken(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email.trim()))
                return true;
        }
        return false;
    }

    /**
     * Registers a new user with sensible defaults for role-specific fields.
     * Returns the created User, or null if the email is already taken.
     */
    public User registerUser(String name, String email, String password, String role) {
        if (isEmailTaken(email)) return null;

        int newId = users.size() + 1;
        User newUser;
        switch (role) {
            case "Pilot" -> newUser = new Pilot(newId, name, email, password,
                    "PK-" + (1000 + newId), 0);
            case "Dispatcher" -> newUser = new Dispatcher(newId, name, email, password, "Unassigned");
            case "Passenger" -> newUser = new Passenger(newId, name, email, password, "Pending", "Pending");
            default -> newUser = new Admin(newId, name, email, password, 1);
        }
        users.add(newUser);
        return newUser;
    }

    /**
     * Deletes a user by ID. Prevents deletion of the last Admin account.
     * Returns true if successfully deleted, false otherwise.
     */
    public boolean deleteUser(int userId) {
        User toRemove = null;
        for (User u : users) {
            if (u.getId() == userId) {
                toRemove = u;
                break;
            }
        }
        if (toRemove == null) return false;

        // Guard: don't delete the last admin
        if (toRemove.getRole().equals("Admin")) {
            long adminCount = users.stream().filter(u -> u.getRole().equals("Admin")).count();
            if (adminCount <= 1) return false;
        }

        users.remove(toRemove);
        return true;
    }
}
