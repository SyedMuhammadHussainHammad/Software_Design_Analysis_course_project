package gui;

import model.Aircraft;
import model.Flight;
import model.User;
import service.FlightService;
import utils.AppColors;
import utils.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * FlightManagementFrame — CRUD for flights.
 * Dispatchers and Admins can add/edit/delete.
 * Pilots are read-only.
 */
public class FlightManagementFrame extends JPanel {

    private final User currentUser;
    private final FlightService flightService = new FlightService();

    private JTable flightTable;
    private DefaultTableModel tableModel;

    private static final String[] STATUSES = { "Scheduled", "Departed", "Arrived", "Cancelled" };

    public FlightManagementFrame(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(AppColors.BG_DARK);
        initComponents();
    }

    private void initComponents() {
        // ── Title bar ────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppColors.BG_PANEL);
        topBar.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel title = UIFactory.createTitleLabel("Flight Management");
        topBar.add(title, BorderLayout.WEST);

        JLabel countLbl = UIFactory.createSubLabel("");
        topBar.add(countLbl, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────────
        String[] cols = { "Flight ID", "Origin", "Destination", "Departure", "Arrival", "Status", "Aircraft" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        flightTable = new JTable(tableModel);
        UIFactory.styleTable(flightTable);
        flightTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        flightTable.getColumnModel().getColumn(5).setPreferredWidth(90);

        JScrollPane scroll = UIFactory.createStyledScrollPane(flightTable);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(AppColors.BG_DARK);
        tableWrap.setBorder(new EmptyBorder(12, 18, 0, 18));
        tableWrap.add(scroll);
        add(tableWrap, BorderLayout.CENTER);

        // ── Action bar ────────────────────────────────────────────────────
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        actionBar.setBackground(AppColors.BG_DARK);
        actionBar.setBorder(new EmptyBorder(0, 12, 6, 12));

        boolean canEdit = currentUser.getRole().equals("Admin") || currentUser.getRole().equals("Dispatcher");

        JButton addBtn = UIFactory.createPrimaryButton("Add Flight");
        JButton editBtn = UIFactory.createSecondaryButton("Edit");
        JButton deleteBtn = UIFactory.createDangerButton("Delete");
        JButton refreshBtn = UIFactory.createSecondaryButton("Refresh");

        addBtn.setEnabled(canEdit);
        editBtn.setEnabled(canEdit);
        deleteBtn.setEnabled(canEdit);

        addBtn.addActionListener(e -> showAddDialog());
        editBtn.addActionListener(e -> showEditDialog());
        deleteBtn.addActionListener(e -> deleteFlight());
        refreshBtn.addActionListener(e -> loadTable(countLbl));

        actionBar.add(addBtn);
        actionBar.add(editBtn);
        actionBar.add(deleteBtn);
        actionBar.add(refreshBtn);

        if (!canEdit) {
            JLabel note = UIFactory.createSubLabel("  (Read-only — Pilot view)");
            actionBar.add(note);
        }

        add(actionBar, BorderLayout.SOUTH);
        loadTable(countLbl);
    }

    // ── Table loading ────────────────────────────────────────────────────────
    private void loadTable(JLabel countLbl) {
        tableModel.setRowCount(0);
        ArrayList<Flight> flights = flightService.getAllFlights();
        for (Flight f : flights) {
            tableModel.addRow(new Object[] {
                    f.getFlightId(), f.getOrigin(), f.getDestination(),
                    f.getDepartureTime(), f.getArrivalTime(), f.getStatus(),
                    f.getAircraft() != null ? f.getAircraft().getAircraftId() : "N/A"
            });
        }
        if (countLbl != null)
            countLbl.setText(flights.size() + " flights");
    }

    // ── Add Flight dialog ────────────────────────────────────────────────────
    private void showAddDialog() {
        JDialog dlg = createDialog("Add New Flight");
        JPanel form = buildFlightForm(null, dlg);
        dlg.add(form);
        dlg.setVisible(true);
    }

    // ── Edit dialog ──────────────────────────────────────────────────────────
    private void showEditDialog() {
        int row = flightTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a flight to edit.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String flightId = (String) tableModel.getValueAt(row, 0);
        Flight existing = flightService.findById(flightId);
        if (existing == null)
            return;

        JDialog dlg = createDialog("Edit Flight — " + flightId);
        JPanel form = buildFlightForm(existing, dlg);
        dlg.add(form);
        dlg.setVisible(true);
    }

    private JDialog createDialog(String title) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dlg.setSize(500, 520);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(AppColors.BG_PANEL);
        return dlg;
    }

    private JPanel buildFlightForm(Flight existing, JDialog dlg) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppColors.BG_PANEL);
        form.setBorder(new EmptyBorder(24, 30, 24, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 0, 6, 0);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        JTextField originFld = UIFactory.createTextField();
        JTextField destFld = UIFactory.createTextField();
        JTextField depFld = UIFactory.createTextField();
        JTextField arrFld = UIFactory.createTextField();
        JComboBox<String> statusCb = UIFactory.createComboBox(STATUSES);

        // Aircraft dropdown
        ArrayList<Aircraft> aircraftList = flightService.getAllAircraft();
        String[] acOptions = aircraftList.stream()
                .map(Aircraft::getAircraftId).toArray(String[]::new);
        JComboBox<String> acCb = UIFactory.createComboBox(acOptions);

        if (existing != null) {
            originFld.setText(existing.getOrigin());
            destFld.setText(existing.getDestination());
            depFld.setText(existing.getDepartureTime());
            arrFld.setText(existing.getArrivalTime());
            statusCb.setSelectedItem(existing.getStatus());
            if (existing.getAircraft() != null)
                acCb.setSelectedItem(existing.getAircraft().getAircraftId());
        } else {
            depFld.setText("HH:MM");
            arrFld.setText("HH:MM");
        }

        int row = 0;
        addFormRow(form, gc, row++, "Origin:", originFld);
        addFormRow(form, gc, row++, "Destination:", destFld);
        addFormRow(form, gc, row++, "Departure:", depFld);
        addFormRow(form, gc, row++, "Arrival:", arrFld);
        addFormRow(form, gc, row++, "Status:", statusCb);
        addFormRow(form, gc, row++, "Aircraft:", acCb);

        JButton saveBtn = UIFactory.createPrimaryButton(existing == null ? "Add Flight" : "Save Changes");
        gc.gridy = row;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.insets = new Insets(16, 0, 0, 0);
        form.add(saveBtn, gc);

        saveBtn.addActionListener(e -> {
            String origin = originFld.getText().trim();
            String dest = destFld.getText().trim();
            String dep = depFld.getText().trim();
            String arr = arrFld.getText().trim();
            String status = (String) statusCb.getSelectedItem();
            String acId = (String) acCb.getSelectedItem();

            if (origin.isEmpty() || dest.isEmpty() || dep.isEmpty() || arr.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All fields are required.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Aircraft selectedAc = aircraftList.stream()
                    .filter(a -> a.getAircraftId().equals(acId))
                    .findFirst().orElse(null);

            if (existing == null) {
                String newId = flightService.generateFlightId();
                flightService.addFlight(new Flight(newId, origin, dest, dep, arr, status, selectedAc));
            } else {
                flightService.updateFlight(existing.getFlightId(), origin, dest, dep, arr, status, selectedAc);
            }

            loadTable(null);
            dlg.dispose();
        });

        return form;
    }

    private void addFormRow(JPanel form, GridBagConstraints gc, int row, String labelText, JComponent field) {
        gc.gridy = row;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.insets = new Insets(6, 0, 2, 10);
        JLabel lbl = UIFactory.createFieldLabel(labelText);
        form.add(lbl, gc);
        gc.gridx = 1;
        gc.insets = new Insets(6, 0, 2, 0);
        form.add(field, gc);
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    private void deleteFlight() {
        int row = flightTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a flight to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String flightId = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete flight " + flightId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            flightService.deleteFlight(flightId);
            loadTable(null);
        }
    }
}
