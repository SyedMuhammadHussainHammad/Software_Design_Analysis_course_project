package gui;

import model.*;
import service.AuthService;
import service.CrewService;
import service.FlightService;
import utils.AppColors;
import utils.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * CrewAssignmentFrame — assign crew (pilot + dispatcher) to flights.
 * Only Dispatchers and Admins can assign; Pilots are read-only.
 */
public class CrewAssignmentFrame extends JPanel {

    private final User currentUser;
    private final CrewService crewService = new CrewService();
    private final FlightService flightService = new FlightService();

    private JTable table;
    private DefaultTableModel tableModel;

    public CrewAssignmentFrame(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(AppColors.BG_DARK);
        initComponents();
    }

    private void initComponents() {
        // ── Title ─────────────────────────────────────────────────────────
        boolean isPilot = currentUser.getRole().equals("Pilot");
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppColors.BG_PANEL);
        topBar.setBorder(new EmptyBorder(18, 24, 18, 24));
        String frameTitle = isPilot ? "Crew List" : "Crew Assignments";
        topBar.add(UIFactory.createTitleLabel(frameTitle), BorderLayout.WEST);
        if (isPilot) {
            JLabel badge = UIFactory.createSubLabel("  Read-Only View");
            badge.setForeground(utils.AppColors.STATUS_YELLOW);
            topBar.add(badge, BorderLayout.EAST);
        }
        add(topBar, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────────
        String[] cols = { "ID", "Flight ID", "Pilot", "Dispatcher", "Notes" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UIFactory.styleTable(table);

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(AppColors.BG_DARK);
        tableWrap.setBorder(new EmptyBorder(12, 18, 0, 18));
        tableWrap.add(UIFactory.createStyledScrollPane(table));
        add(tableWrap, BorderLayout.CENTER);

        // ── Action bar ───────────────────────────────────────────────────
        boolean canEdit = currentUser.getRole().equals("Admin")
                || currentUser.getRole().equals("Dispatcher");

        JPanel actBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        actBar.setBackground(AppColors.BG_DARK);
        actBar.setBorder(new EmptyBorder(0, 12, 6, 12));

        JButton assignBtn = UIFactory.createPrimaryButton("Assign Crew");
        JButton removeBtn = UIFactory.createDangerButton("Remove");
        JButton refreshBtn = UIFactory.createSecondaryButton("Refresh");

        assignBtn.setEnabled(canEdit);
        removeBtn.setEnabled(canEdit);

        assignBtn.addActionListener(ignored -> showAssignDialog());
        removeBtn.addActionListener(ignored -> removeAssignment());
        refreshBtn.addActionListener(ignored -> loadTable());

        actBar.add(assignBtn);
        actBar.add(removeBtn);
        actBar.add(refreshBtn);

        if (!canEdit)
            actBar.add(UIFactory.createSubLabel("  (Read-only — Pilot view)"));
        add(actBar, BorderLayout.SOUTH);
        loadTable();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (CrewAssignment ca : crewService.getAllAssignments()) {
            tableModel.addRow(new Object[] {
                    ca.getAssignmentId(), ca.getFlightId(),
                    ca.getPilotName(), ca.getDispatcherName(), ca.getNotes()
            });
        }
    }

    private void showAssignDialog() {
        // Flight dropdown
        ArrayList<Flight> flights = flightService.getAllFlights();
        if (flights.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No flights available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] flightOpts = flights.stream().map(Flight::getFlightId).toArray(String[]::new);

        // Pilot dropdown
        ArrayList<Pilot> pilots = AuthService.getInstance().getPilots();
        String[] pilotOpts = pilots.stream().map(User::getName).toArray(String[]::new);

        // Dispatcher dropdown
        ArrayList<Dispatcher> dispatchers = AuthService.getInstance().getDispatchers();
        String[] dispOpts = dispatchers.stream().map(User::getName).toArray(String[]::new);

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Assign Crew", true);
        dlg.setSize(450, 380);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppColors.BG_PANEL);
        form.setBorder(new EmptyBorder(24, 30, 24, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.insets = new Insets(6, 0, 6, 0);

        JComboBox<String> flightCb = UIFactory.createComboBox(flightOpts);
        JComboBox<String> pilotCb = UIFactory.createComboBox(pilotOpts);
        JComboBox<String> dispCb = UIFactory.createComboBox(dispOpts.length > 0 ? dispOpts : new String[] { "N/A" });
        JTextField notesFld = UIFactory.createTextField();

        addRow(form, gc, 0, "Flight:", flightCb);
        addRow(form, gc, 1, "Pilot:", pilotCb);
        addRow(form, gc, 2, "Dispatcher:", dispCb);
        addRow(form, gc, 3, "Notes:", notesFld);

        JButton saveBtn = UIFactory.createPrimaryButton("Assign");
        gc.gridy = 4;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.insets = new Insets(16, 0, 0, 0);
        form.add(saveBtn, gc);

        saveBtn.addActionListener(ignored -> {
            String fId = (String) flightCb.getSelectedItem();
            String pilot = (String) pilotCb.getSelectedItem();
            String disp = (String) dispCb.getSelectedItem();
            String notes = notesFld.getText().trim();

            CrewAssignment ca = new CrewAssignment(crewService.generateId(), fId, pilot, disp, notes);
            crewService.addAssignment(ca);
            loadTable();
            dlg.dispose();
        });

        dlg.add(form);
        dlg.setVisible(true);
    }

    private void addRow(JPanel form, GridBagConstraints gc, int row, String label, JComponent comp) {
        gc.gridy = row;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.insets = new Insets(6, 0, 2, 10);
        form.add(UIFactory.createFieldLabel(label), gc);
        gc.gridx = 1;
        gc.insets = new Insets(6, 0, 2, 0);
        form.add(comp, gc);
    }

    private void removeAssignment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to remove.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove assignment #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            crewService.deleteAssignment(id);
            loadTable();
        }
    }
}
