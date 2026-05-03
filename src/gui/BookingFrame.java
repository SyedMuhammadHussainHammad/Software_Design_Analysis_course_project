package gui;

import model.Booking;
import model.Flight;
import model.Passenger;
import service.BookingService;
import service.FlightService;
import utils.AppColors;
import utils.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Passenger dashboard allowing flight search, booking, and viewing reservations.
 * Designed to mimic the sky-stream-airplane-management web interface.
 */
public class BookingFrame extends JPanel {

    private final Passenger currentUser;
    private final FlightService flightService = new FlightService();
    private final BookingService bookingService = new BookingService();

    private DefaultTableModel searchTableModel;
    private JTable searchTable;

    private DefaultTableModel myBookingsTableModel;
    private JTable myBookingsTable;

    private JComboBox<String> originCb;
    private JComboBox<String> destCb;

    public BookingFrame(Passenger user) {
        this.currentUser = user;
        setLayout(new BorderLayout(0, 20));
        setBackground(AppColors.BG_DARK);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initComponents();
    }

    private void initComponents() {
        // ── Top: Search Form ────────────────────────────────────────────────
        JPanel searchPanel = new JPanel(new BorderLayout(0, 10));
        searchPanel.setOpaque(false);

        JLabel title = UIFactory.createTitleLabel("Search & Book Flights");
        title.setForeground(AppColors.ACCENT_BLUE);
        searchPanel.add(title, BorderLayout.NORTH);

        JPanel formCard = UIFactory.createCardPanel();
        formCard.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        // Get unique airports
        ArrayList<Flight> allFlights = flightService.getAllFlights();
        ArrayList<String> origins = new ArrayList<>();
        ArrayList<String> dests = new ArrayList<>();
        origins.add("Any");
        dests.add("Any");
        for (Flight f : allFlights) {
            if (!origins.contains(f.getOrigin())) origins.add(f.getOrigin());
            if (!dests.contains(f.getDestination())) dests.add(f.getDestination());
        }

        originCb = UIFactory.createComboBox(origins.toArray(new String[0]));
        destCb = UIFactory.createComboBox(dests.toArray(new String[0]));

        formCard.add(UIFactory.createFieldLabel("From:"));
        formCard.add(originCb);
        formCard.add(UIFactory.createFieldLabel("To:"));
        formCard.add(destCb);

        JButton searchBtn = UIFactory.createPrimaryButton("Search Flights");
        searchBtn.addActionListener(ignored -> doSearch());
        formCard.add(searchBtn);

        searchPanel.add(formCard, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // ── Middle: Search Results ──────────────────────────────────────────
        JPanel resultsPanel = new JPanel(new BorderLayout(0, 10));
        resultsPanel.setOpaque(false);

        JLabel resTitle = UIFactory.createFieldLabel("Available Flights (One-Way)");
        resTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultsPanel.add(resTitle, BorderLayout.NORTH);

        String[] searchCols = {"Flight ID", "Origin", "Destination", "Departs", "Arrives", "Status"};
        searchTableModel = new DefaultTableModel(searchCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        searchTable = new JTable(searchTableModel);
        UIFactory.styleTable(searchTable);

        resultsPanel.add(UIFactory.createStyledScrollPane(searchTable), BorderLayout.CENTER);

        JButton bookBtn = UIFactory.createPrimaryButton("Book Selected Flight");
        bookBtn.setBackground(AppColors.ACCENT_TEAL); // Gold highlight
        bookBtn.addActionListener(ignored -> bookFlight());
        JPanel bookPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bookPanel.setOpaque(false);
        bookPanel.add(bookBtn);
        resultsPanel.add(bookPanel, BorderLayout.SOUTH);

        add(resultsPanel, BorderLayout.CENTER);

        // ── Bottom: My Bookings ────────────────────────────────────────────
        JPanel myBookingsPanel = new JPanel(new BorderLayout(0, 10));
        myBookingsPanel.setOpaque(false);
        myBookingsPanel.setPreferredSize(new Dimension(0, 200));

        JLabel myTitle = UIFactory.createFieldLabel("My Bookings");
        myTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        myBookingsPanel.add(myTitle, BorderLayout.NORTH);

        String[] myCols = {"Booking Ref", "Flight ID", "Route", "Date", "Status"};
        myBookingsTableModel = new DefaultTableModel(myCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        myBookingsTable = new JTable(myBookingsTableModel);
        UIFactory.styleTable(myBookingsTable);

        myBookingsPanel.add(UIFactory.createStyledScrollPane(myBookingsTable), BorderLayout.CENTER);

        JButton cancelBtn = UIFactory.createDangerButton("Cancel Booking");
        cancelBtn.addActionListener(ignored -> cancelBooking());
        JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancelPanel.setOpaque(false);
        cancelPanel.add(cancelBtn);
        myBookingsPanel.add(cancelPanel, BorderLayout.SOUTH);

        add(myBookingsPanel, BorderLayout.SOUTH);

        // Initial loads
        doSearch();
        loadMyBookings();
    }

    private void doSearch() {
        String o = (String) originCb.getSelectedItem();
        String d = (String) destCb.getSelectedItem();

        searchTableModel.setRowCount(0);
        for (Flight f : flightService.getAllFlights()) {
            boolean matchO = "Any".equals(o) || (o != null && o.equals(f.getOrigin()));
            boolean matchD = "Any".equals(d) || (d != null && d.equals(f.getDestination()));
            if (matchO && matchD && "Scheduled".equals(f.getStatus())) {
                searchTableModel.addRow(new Object[]{
                        f.getFlightId(), f.getOrigin(), f.getDestination(),
                        f.getDepartureTime(), f.getArrivalTime(), f.getStatus()
                });
            }
        }
    }

    private void loadMyBookings() {
        myBookingsTableModel.setRowCount(0);
        ArrayList<Booking> myBookings = bookingService.getBookingsForPassenger(currentUser.getId());
        for (Booking b : myBookings) {
            Flight f = flightService.findById(b.getFlightId());
            String route = f != null ? f.getOrigin() + " ➝ " + f.getDestination() : "Unknown";
            
            myBookingsTableModel.addRow(new Object[]{
                    b.getBookingId(), b.getFlightId(), route, b.getBookingDate(), b.getStatus()
            });
        }
    }

    private void bookFlight() {
        int row = searchTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a flight to book.", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String flightId = (String) searchTableModel.getValueAt(row, 0);
        Booking b = bookingService.createBooking(flightId, currentUser.getId());
        
        if (b == null) {
            JOptionPane.showMessageDialog(this, "You have already booked this flight.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Flight booked successfully!\nRef: " + b.getBookingId(), "Success", JOptionPane.INFORMATION_MESSAGE);
            loadMyBookings();
        }
    }

    private void cancelBooking() {
        int row = myBookingsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = (String) myBookingsTableModel.getValueAt(row, 4);
        if (status.equals("Cancelled")) {
            JOptionPane.showMessageDialog(this, "This booking is already cancelled.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String ref = (String) myBookingsTableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel booking " + ref + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            bookingService.cancelBooking(ref);
            loadMyBookings();
        }
    }
}
