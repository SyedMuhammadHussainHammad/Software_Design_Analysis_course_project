package service;

import model.IncidentReport;
import utils.DataStore;
import java.util.ArrayList;

/**
 * IncidentService — manages IncidentReport CRUD.
 */
@SuppressWarnings("unused")
public class IncidentService {
    private static int idCounter = 10;
    private final DataStore store = DataStore.getInstance();

    public ArrayList<IncidentReport> getAllReports() {
        return store.getIncidentReports();
    }

    public void addReport(IncidentReport report) {
        store.getIncidentReports().add(report);
    }

    public boolean deleteReport(int reportId) {
        return store.getIncidentReports().removeIf(r -> r.getReportId() == reportId);
    }

    public boolean resolveReport(int reportId) {
        for (IncidentReport r : store.getIncidentReports()) {
            if (r.getReportId() == reportId) {
                r.setSeverity("Resolved");
                return true;
            }
        }
        return false;
    }

    public int generateId() {
        return ++idCounter;
    }
}
