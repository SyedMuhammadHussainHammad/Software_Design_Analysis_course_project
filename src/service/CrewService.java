package service;

import model.CrewAssignment;
import utils.DataStore;
import java.util.ArrayList;

/**
 * CrewService — manages CrewAssignment CRUD.
 */
@SuppressWarnings("unused")
public class CrewService {
    private static int idCounter = 10;
    private final DataStore store = DataStore.getInstance();

    public ArrayList<CrewAssignment> getAllAssignments() {
        return store.getCrewAssignments();
    }

    public void addAssignment(CrewAssignment ca) {
        store.getCrewAssignments().add(ca);
    }

    public boolean deleteAssignment(int assignmentId) {
        return store.getCrewAssignments().removeIf(ca -> ca.getAssignmentId() == assignmentId);
    }

    public CrewAssignment findByFlightId(String flightId) {
        for (CrewAssignment ca : store.getCrewAssignments()) {
            if (ca.getFlightId().equals(flightId))
                return ca;
        }
        return null;
    }

    public int generateId() {
        return ++idCounter;
    }
}
