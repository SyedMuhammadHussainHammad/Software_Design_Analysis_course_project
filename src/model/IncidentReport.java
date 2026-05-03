package model;

/**
 * Represents an incident report submitted by a Pilot.
 */
public class IncidentReport {
    private int reportId;
    private String flightId;
    private String reportedBy; // Pilot name
    private String description;
    private String severity; // "Low", "Medium", "High"
    private String date;

    public IncidentReport(int reportId, String flightId, String reportedBy,
            String description, String severity, String date) {
        this.reportId = reportId;
        this.flightId = flightId;
        this.reportedBy = reportedBy;
        this.description = description;
        this.severity = severity;
        this.date = date;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
