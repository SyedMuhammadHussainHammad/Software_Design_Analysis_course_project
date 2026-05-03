package gui;

import model.IncidentReport;
import model.User;
import service.FlightService;
import service.IncidentService;
import utils.AppColors;
import utils.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * IncidentFrame — Pilots submit incident reports; all roles can view them.
 */
public class IncidentFrame extends JPanel {

    private final User currentUser;
    private final IncidentService incidentService = new IncidentService();
    private final FlightService flightService = new FlightService();

    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] SEVERITIES = { "Low", "Medium", "High" };

    public IncidentFrame(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(AppColors.BG_DARK);
        initComponents();
    }

    private void initComponents() {
        // ── Title ─────────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppColors.BG_PANEL);
        topBar.setBorder(new EmptyBorder(18, 24, 18, 24));
        topBar.add(UIFactory.createTitleLabel("Incident Reports"), BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────────
        String[] cols = { "Report ID", "Flight ID", "Reported By", "Severity", "Date", "Description" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UIFactory.styleTable(table);
        table.getColumnModel().getColumn(5).setPreferredWidth(280);

        // Colour-code severity column
        table.getColumnModel().getColumn(3).setCellRenderer((ignoredT, value, isSelected, ignoredHasFocus, row, ignoredCol) -> {
            JLabel lbl = new JLabel(value != null ? value.toString() : "");
            lbl.setOpaque(true);
            lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            if (!isSelected) {
                String sev = value != null ? value.toString() : "";
                switch (sev) {
                    case "High":
                        lbl.setForeground(AppColors.STATUS_RED);
                        break;
                    case "Medium":
                        lbl.setForeground(AppColors.STATUS_YELLOW);
                        break;
                    case "Resolved":
                        lbl.setForeground(AppColors.TEXT_MUTED);
                        break;
                    default:
                        lbl.setForeground(AppColors.STATUS_GREEN);
                        break;
                }
                lbl.setBackground(row % 2 == 0 ? AppColors.TABLE_ROW1 : AppColors.TABLE_ROW2);
            } else {
                lbl.setBackground(AppColors.TABLE_SELECT);
                lbl.setForeground(Color.WHITE);
            }
            return lbl;
        });

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(AppColors.BG_DARK);
        tableWrap.setBorder(new EmptyBorder(12, 18, 0, 18));
        tableWrap.add(UIFactory.createStyledScrollPane(table));
        add(tableWrap, BorderLayout.CENTER);

        // ── Action bar ───────────────────────────────────────────────────
        JPanel actBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        actBar.setBackground(AppColors.BG_DARK);
        actBar.setBorder(new EmptyBorder(0, 12, 6, 12));

        boolean isPilot = currentUser.getRole().equals("Pilot");
        boolean isAdmin = currentUser.getRole().equals("Admin");

        JButton submitBtn = UIFactory.createPrimaryButton("Submit Report");
        JButton resolveBtn = UIFactory.createSecondaryButton("Resolve");
        JButton deleteBtn = UIFactory.createDangerButton("Delete");
        JButton refreshBtn = UIFactory.createSecondaryButton("Refresh");

        // Only pilots (and admins) can submit; only admins can delete and resolve
        submitBtn.setEnabled(isPilot || isAdmin);
        resolveBtn.setEnabled(isAdmin);
        deleteBtn.setEnabled(isAdmin);

        submitBtn.addActionListener(ignored -> showSubmitDialog());
        resolveBtn.addActionListener(ignored -> resolveReport());
        deleteBtn.addActionListener(ignored -> deleteReport());
        refreshBtn.addActionListener(ignored -> loadTable());

        actBar.add(submitBtn);
        actBar.add(resolveBtn);
        actBar.add(deleteBtn);
        actBar.add(refreshBtn);

        if (!isPilot && !isAdmin) {
            actBar.add(UIFactory.createSubLabel("  (View-only for Dispatchers)"));
        }
        add(actBar, BorderLayout.SOUTH);
        loadTable();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (IncidentReport r : incidentService.getAllReports()) {
            tableModel.addRow(new Object[] {
                    r.getReportId(), r.getFlightId(), r.getReportedBy(),
                    r.getSeverity(), r.getDate(), r.getDescription()
            });
        }
    }

    private void showSubmitDialog() {
        ArrayList<model.Flight> flights = flightService.getAllFlights();
        String[] flightOpts = flights.isEmpty()
                ? new String[] { "No flights" }
                : flights.stream().map(model.Flight::getFlightId).toArray(String[]::new);

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Submit Incident Report", true);
        dlg.setSize(500, 440);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppColors.BG_PANEL);
        form.setBorder(new EmptyBorder(24, 30, 24, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.insets = new Insets(6, 0, 6, 0);

        JComboBox<String> flightCb = UIFactory.createComboBox(flightOpts);
        JComboBox<String> severityCb = UIFactory.createComboBox(SEVERITIES);
        JTextArea descArea = UIFactory.createTextArea(4, 20);
        JScrollPane descScroll = UIFactory.createStyledScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(0, 90));

        addFormRow(form, gc, 0, "Flight:", flightCb);
        addFormRow(form, gc, 1, "Severity:", severityCb);

        // Description row (spans two columns, uses scroll pane)
        gc.gridy = 2;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.insets = new Insets(6, 0, 2, 10);
        form.add(UIFactory.createFieldLabel("Description:"), gc);
        gc.gridx = 1;
        gc.insets = new Insets(6, 0, 2, 0);
        form.add(descScroll, gc);

        JButton saveBtn = UIFactory.createPrimaryButton("Submit Report");
        gc.gridy = 3;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.insets = new Insets(18, 0, 0, 0);
        form.add(saveBtn, gc);

        saveBtn.addActionListener(ignored -> {
            String fId = (String) flightCb.getSelectedItem();
            String sev = (String) severityCb.getSelectedItem();
            String desc = descArea.getText().trim();

            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Description cannot be empty.", "Validation",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            IncidentReport ir = new IncidentReport(
                    incidentService.generateId(), fId,
                    currentUser.getName(), desc, sev,
                    LocalDate.now().toString());
            incidentService.addReport(ir);
            loadTable();
            dlg.dispose();
        });

        dlg.add(form);
        dlg.setVisible(true);
    }

    private void addFormRow(JPanel form, GridBagConstraints gc, int row, String label, JComponent comp) {
        gc.gridy = row;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.insets = new Insets(6, 0, 2, 10);
        form.add(UIFactory.createFieldLabel(label), gc);
        gc.gridx = 1;
        gc.insets = new Insets(6, 0, 2, 0);
        form.add(comp, gc);
    }

    private void resolveReport() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a report to resolve.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Resolve report #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            incidentService.resolveReport(id);
            loadTable();
        }
    }

    private void deleteReport() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a report to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete report #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            incidentService.deleteReport(id);
            loadTable();
        }
    }
}
