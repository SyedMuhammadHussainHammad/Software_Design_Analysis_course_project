package gui;

import model.User;
import utils.AppColors;
import utils.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * DashboardFrame — main shell after login.
 * Shows sidebar navigation to all sub-modules.
 */
public class DashboardFrame extends JFrame {

    private final User currentUser;
    private JPanel contentArea;

    private FlightManagementFrame flightFrame;
    private CrewAssignmentFrame crewFrame;
    private IncidentFrame incidentFrame;

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("SkyStream — Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.BG_DARK);
        setContentPane(root);

        // ── Header bar ────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppColors.BG_SIDEBAR);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel brandLbl = new JLabel("SkyStream");
        brandLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandLbl.setForeground(AppColors.ACCENT_BLUE);
        header.add(brandLbl, BorderLayout.WEST);

        // User info + logout on the right
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);

        JLabel userLbl = new JLabel(currentUser.getName() + "  |  " + currentUser.getRole());
        userLbl.setForeground(AppColors.TEXT_SECONDARY);
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        headerRight.add(userLbl);

        JButton logoutBtn = UIFactory.createSecondaryButton("Logout");
        logoutBtn.addActionListener(ignored -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });
        headerRight.add(logoutBtn);
        header.add(headerRight, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // ── Sidebar ───────────────────────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setBackground(AppColors.BG_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppColors.BORDER));

        sidebar.add(Box.createRigidArea(new Dimension(0, 18)));
        sidebar.add(makeSidebarSection("MENU"));
        sidebar.add(makeSidebarBtn("Dashboard", this::showWelcome));

        if (currentUser.getRole().equals("Passenger")) {
            sidebar.add(makeSidebarBtn("Book Flights", () -> showFrame(getBookingFrame())));
        } else {
            sidebar.add(makeSidebarBtn("Flights", () -> showFrame(getFlightFrame())));
            sidebar.add(makeSidebarBtn("Crew Assign", () -> showFrame(getCrewFrame())));
            sidebar.add(makeSidebarBtn("Incidents", () -> showFrame(getIncidentFrame())));
        }
        
        sidebar.add(Box.createVerticalGlue());

        // Role badge at bottom of sidebar
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.CENTER));
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(0, 10, 16, 10));
        JLabel roleBadge = new JLabel("Role: " + currentUser.getRole());
        roleBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        roleBadge.setForeground(AppColors.ACCENT_TEAL);
        badge.add(roleBadge);
        sidebar.add(badge);

        root.add(sidebar, BorderLayout.WEST);

        // ── Content area ──────────────────────────────────────────────────
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(AppColors.BG_DARK);
        root.add(contentArea, BorderLayout.CENTER);

        showWelcome();
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    @SuppressWarnings("SameParameterValue")
    private JLabel makeSidebarSection(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(AppColors.TEXT_MUTED);
        lbl.setBorder(new EmptyBorder(10, 14, 4, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton makeSidebarBtn(String text, Runnable action) {
        JButton btn = UIFactory.createSidebarButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(ignored -> action.run());
        return btn;
    }

    private void showFrame(JPanel panel) {
        contentArea.removeAll();
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void showWelcome() {
        JPanel welcome = new JPanel();
        welcome.setBackground(AppColors.BG_DARK);
        welcome.setLayout(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setBackground(AppColors.BG_PANEL);
        inner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.BORDER),
                new EmptyBorder(40, 60, 40, 60)));
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel greet = UIFactory.createTitleLabel("Welcome back, " + currentUser.getName() + "!");
        greet.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = UIFactory.createSubLabel("Role: " + currentUser.getRole()
                + "  |  Use the sidebar to navigate.");
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel icon = new JLabel("SKYSTREAM", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 32));
        icon.setForeground(AppColors.ACCENT_BLUE);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(icon);
        inner.add(Box.createRigidArea(new Dimension(0, 24)));
        inner.add(greet);
        inner.add(Box.createRigidArea(new Dimension(0, 8)));
        inner.add(sub);

        welcome.add(inner);
        showFrame(welcome);
    }

    // Lazy-init sub-frames so they share state
    private FlightManagementFrame getFlightFrame() {
        if (flightFrame == null)
            flightFrame = new FlightManagementFrame(currentUser);
        return flightFrame;
    }

    private CrewAssignmentFrame getCrewFrame() {
        if (crewFrame == null)
            crewFrame = new CrewAssignmentFrame(currentUser);
        return crewFrame;
    }

    private IncidentFrame getIncidentFrame() {
        if (incidentFrame == null)
            incidentFrame = new IncidentFrame(currentUser);
        return incidentFrame;
    }

    private BookingFrame bookingFrame;
    private BookingFrame getBookingFrame() {
        if (bookingFrame == null && currentUser instanceof model.Passenger) {
            bookingFrame = new BookingFrame((model.Passenger) currentUser);
        }
        return bookingFrame;
    }
}
