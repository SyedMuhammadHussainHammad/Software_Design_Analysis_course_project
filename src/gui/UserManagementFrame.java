package gui;

import model.User;
import service.AuthService;
import utils.AppColors;
import utils.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * UserManagementFrame — Admin-only panel.
 * Displays all registered users and allows adding new users (via SignupDialog)
 * and deleting existing users.
 */
public class UserManagementFrame extends JPanel {

    @SuppressWarnings("unused")
    private final User currentUser;
    private final AuthService authService = AuthService.getInstance();

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel countLbl;

    public UserManagementFrame(User user) {
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
        topBar.add(UIFactory.createTitleLabel("User Management"), BorderLayout.WEST);

        countLbl = UIFactory.createSubLabel("");
        topBar.add(countLbl, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────────
        String[] cols = {"ID", "Name", "Email", "Role"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UIFactory.styleTable(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(AppColors.BG_DARK);
        tableWrap.setBorder(new EmptyBorder(12, 18, 0, 18));
        tableWrap.add(UIFactory.createStyledScrollPane(table));
        add(tableWrap, BorderLayout.CENTER);

        // ── Action bar ────────────────────────────────────────────────────
        JPanel actBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        actBar.setBackground(AppColors.BG_DARK);
        actBar.setBorder(new EmptyBorder(0, 12, 6, 12));

        JButton addBtn    = UIFactory.createPrimaryButton("Add User");
        JButton deleteBtn = UIFactory.createDangerButton("Delete User");
        JButton refreshBtn = UIFactory.createSecondaryButton("Refresh");

        addBtn.addActionListener(ignored -> showAddUserDialog());
        deleteBtn.addActionListener(ignored -> deleteUser());
        refreshBtn.addActionListener(ignored -> loadTable());

        actBar.add(addBtn);
        actBar.add(deleteBtn);
        actBar.add(refreshBtn);
        add(actBar, BorderLayout.SOUTH);

        loadTable();
    }

    // ── Table Loading ────────────────────────────────────────────────────────
    private void loadTable() {
        tableModel.setRowCount(0);
        ArrayList<User> users = authService.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                    u.getId(), u.getName(), u.getEmail(), u.getRole()
            });
        }
        countLbl.setText(users.size() + " users");
    }

    // ── Add User ─────────────────────────────────────────────────────────────
    private void showAddUserDialog() {
        // Pass null as owner — SignupDialog is modal and works fine without a parent reference
        SignupDialog dlg = new SignupDialog(null);
        dlg.setVisible(true);
        loadTable(); // Refresh after dialog closes
    }

    // ── Delete User ──────────────────────────────────────────────────────────
    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a user to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int userId   = (int) tableModel.getValueAt(row, 0);
        String uName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete user \"" + uName + "\" (ID " + userId + ")?\nThis cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean removed = authService.deleteUser(userId);
            if (removed) {
                loadTable();
                JOptionPane.showMessageDialog(this, "User deleted successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Could not delete user. You cannot delete the only Admin account.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
