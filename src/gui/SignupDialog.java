package gui;

import model.User;
import service.AuthService;
import utils.AppColors;
import utils.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * SignupDialog — Modal dialog for creating a new user account.
 * Demonstrates ASSOCIATION with AuthService (Singleton).
 */
public class SignupDialog extends JDialog {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JLabel statusLabel;

    public SignupDialog(JFrame parent) {
        super(parent, "SkyStream — Create Account", true);
        setSize(440, 520);
        setResizable(false);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        // ── Root panel ──────────────────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.BG_DARK);
        setContentPane(root);

        // ── Header ──────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppColors.BG_SIDEBAR);
        header.setBorder(new EmptyBorder(22, 30, 22, 30));

        JLabel title = UIFactory.createTitleLabel("Create Account");
        title.setForeground(AppColors.ACCENT_TEAL);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(title, BorderLayout.CENTER);

        JLabel subtitle = UIFactory.createSubLabel("Join the SkyStream operations team");
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(subtitle, BorderLayout.SOUTH);

        root.add(header, BorderLayout.NORTH);

        // ── Form card ───────────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setBackground(AppColors.BG_PANEL);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Name
        JLabel nameLbl = UIFactory.createFieldLabel("Full Name");
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField = UIFactory.createTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Email
        JLabel emailLbl = UIFactory.createFieldLabel("Email Address");
        emailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField = UIFactory.createTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password
        JLabel passLbl = UIFactory.createFieldLabel("Password");
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField = UIFactory.createPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Role
        JLabel roleLbl = UIFactory.createFieldLabel("Role");
        roleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleCombo = UIFactory.createComboBox(new String[]{"Passenger", "Pilot", "Dispatcher", "Admin"});
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        roleCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(AppColors.STATUS_RED);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Register button
        JButton registerBtn = UIFactory.createPrimaryButton("  REGISTER  ");
        registerBtn.setBackground(AppColors.ACCENT_TEAL);
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        registerBtn.addActionListener(ignored -> attemptRegister());

        // Cancel button
        JButton cancelBtn = UIFactory.createSecondaryButton("Cancel");
        cancelBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        cancelBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cancelBtn.addActionListener(ignored -> dispose());

        // ── Assemble ────────────────────────────────────────────────────────
        card.add(nameLbl);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(nameField);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(emailLbl);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(emailField);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(passLbl);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(roleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(roleCombo);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(statusLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(registerBtn);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(cancelBtn);

        root.add(card, BorderLayout.CENTER);
    }

    private void attemptRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        // ── Validation ──────────────────────────────────────────────────────
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("⚠  Please fill in all fields.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            statusLabel.setText("⚠  Please enter a valid email address.");
            return;
        }
        if (pass.length() < 4) {
            statusLabel.setText("⚠  Password must be at least 4 characters.");
            return;
        }

        // ── Register via AuthService ────────────────────────────────────────
        User created = AuthService.getInstance().registerUser(name, email, pass, role);
        if (created == null) {
            statusLabel.setText("✗  This email is already registered.");
            return;
        }

        // Success
        JOptionPane.showMessageDialog(this,
                "Account created successfully!\nYou can now log in as: " + email,
                "Registration Complete",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
