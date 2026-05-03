package utils;

import model.*;
import java.util.ArrayList;

/**
 * DataStore — Singleton holding all in-memory application data.
 * Shared across all service classes.
 */
public class DataStore {
    private static DataStore instance;

    private final FlightSchedule flightSchedule;
    private final ArrayList<Aircraft> aircraft;
    private final ArrayList<CrewAssignment> crewAssignments;
    private final ArrayList<IncidentReport> incidentReports;
    private final ArrayList<Booking> bookings;

    private DataStore() {
        flightSchedule = new FlightSchedule("SS-2026");
        aircraft = new ArrayList<>();
        crewAssignments = new ArrayList<>();
        incidentReports = new ArrayList<>();
        bookings = new ArrayList<>();
        seedData();
    }

    public static DataStore getInstance() {
        if (instance == null)
            instance = new DataStore();
        return instance;
    }

    // ─── Seed data ──────────────────────────────────────────────────────────
    private void seedData() {
        // Aircraft
        Aircraft a1 = new Aircraft("AC-001", "Boeing 737", 180, "Active");
        Aircraft a2 = new Aircraft("AC-002", "Airbus A320", 160, "Active");
        Aircraft a3 = new Aircraft("AC-003", "Boeing 777", 350, "Maintenance");
        aircraft.add(a1);
        aircraft.add(a2);
        aircraft.add(a3);

        // Flights (One-way Domestic Pakistani Flights)
        Flight f1 = new Flight("PK-302", "Karachi (KHI)", "Lahore (LHE)", "08:00", "09:45", "Scheduled", a1);
        Flight f2 = new Flight("PK-303", "Lahore (LHE)", "Islamabad (ISB)", "11:00", "11:50", "Scheduled", a2);
        Flight f3 = new Flight("PK-304", "Islamabad (ISB)", "Quetta (UET)", "14:30", "16:10", "Scheduled", a1);
        Flight f4 = new Flight("PK-305", "Karachi (KHI)", "Peshawar (PEW)", "18:00", "20:00", "Scheduled", a2);
        Flight f5 = new Flight("PK-306", "Multan (MUX)", "Karachi (KHI)", "21:00", "22:30", "Departed", a1);
        
        flightSchedule.addFlight(f1);
        flightSchedule.addFlight(f2);
        flightSchedule.addFlight(f3);
        flightSchedule.addFlight(f4);
        flightSchedule.addFlight(f5);

        // Crew Assignments
        CrewAssignment ca1 = new CrewAssignment(1, "PK-302", "Capt. Hussain", "Dispatcher Ali", "Main crew");
        crewAssignments.add(ca1);

        // Incident Reports
        IncidentReport ir1 = new IncidentReport(1, "PK-306", "Capt. Hussain",
                "Minor turbulence over Sindh", "Low", "2026-05-01");
        incidentReports.add(ir1);
    }

    // ─── Accessors ──────────────────────────────────────────────────────────
    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public ArrayList<Aircraft> getAircraft() {
        return aircraft;
    }

    public ArrayList<CrewAssignment> getCrewAssignments() {
        return crewAssignments;
    }

    public ArrayList<IncidentReport> getIncidentReports() {
        return incidentReports;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }
}
