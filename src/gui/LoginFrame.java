package gui;

import model.User;
import service.AuthService;
import utils.AppColors;
import utils.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame — the application entry point for users.
 * Validates credentials via AuthService and launches DashboardFrame.
 */
public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private JLabel statusLabel;

    public LoginFrame() {
        setTitle("SkyStream — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 640);
        setResizable(false);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // ── Root panel with dark background ─────────────────────────────────
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.BG_DARK);
        setContentPane(root);

        // ── Top banner ────────────────────────────────────────────────────
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(AppColors.BG_SIDEBAR);
        banner.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel logo = UIFactory.createTitleLabel("SkyStream");
        logo.setForeground(AppColors.ACCENT_BLUE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        banner.add(logo, BorderLayout.CENTER);

        JLabel tagline = UIFactory.createSubLabel("Integrated Airline Operations System");
        tagline.setHorizontalAlignment(SwingConstants.CENTER);
        banner.add(tagline, BorderLayout.SOUTH);

        root.add(banner, BorderLayout.NORTH);

        // ── Form card ─────────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setBackground(AppColors.BG_PANEL);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 50, 40, 50));

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

        // Login button
        loginBtn = UIFactory.createPrimaryButton("  LOGIN  ");
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.addActionListener(e -> attemptLogin());

        // Enter key triggers login
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    attemptLogin();
            }
        });

        // Status/error label
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(AppColors.STATUS_RED);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Credentials hint
        JLabel hint = UIFactory.createSubLabel(
                "Demo — admin@sky.com | pilot@sky.com | passenger@sky.com");
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        // Assemble
        card.add(emailLbl);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(emailField);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(passLbl);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(statusLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        // Create Account button
        JButton createAccBtn = UIFactory.createSecondaryButton("  CREATE ACCOUNT  ");
        createAccBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        createAccBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        createAccBtn.setForeground(AppColors.ACCENT_TEAL);
        createAccBtn.addActionListener(e -> new SignupDialog(this).setVisible(true));
        card.add(createAccBtn);

        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(UIFactory.createSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(hint);

        root.add(card, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────────────────────
        JLabel footer = new JLabel("© 2026 SkyStream Aviation Systems", SwingConstants.CENTER);
        footer.setForeground(AppColors.TEXT_MUTED);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setBorder(new EmptyBorder(12, 0, 12, 0));
        footer.setBackground(AppColors.BG_DARK);
        footer.setOpaque(true);
        root.add(footer, BorderLayout.SOUTH);
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (email.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Please enter both email and password.");
            return;
        }

        User user = AuthService.getInstance().login(email, pass);
        if (user == null) {
            statusLabel.setText("Invalid email or password.");
            passwordField.setText("");
        } else {
            dispose();
            SwingUtilities.invokeLater(() -> new DashboardFrame(user).setVisible(true));
        }
    }
}
