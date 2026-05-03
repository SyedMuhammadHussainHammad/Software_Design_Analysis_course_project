import gui.LoginFrame;
import javax.swing.*;

/**
 * SkyStream — Integrated Airline Operations System
 * Entry point: launches the Login screen on the Swing EDT.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Main {
    static void main(String[] args) {
        // Set a modern look-and-feel (falls back to system LAF)
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}