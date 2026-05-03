package utils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Factory Pattern — creates pre-styled Swing components.
 * Overhauled to provide a modern, professional, "glass-like" aesthetic 
 * with rounded corners and clean typography.
 */
public class UIFactory {

    // ─── Fonts ──────────────────────────────────────────────────────────────
    private static final Font FONT_BOLD_LG = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOLD_MD = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    // ─── Labels ────────────────────────────────────────────────────────────
    public static JLabel createTitleLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BOLD_LG);
        lbl.setForeground(AppColors.TEXT_PRIMARY);
        return lbl;
    }

    public static JLabel createSubLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(AppColors.TEXT_SECONDARY);
        return lbl;
    }

    public static JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BOLD_MD);
        lbl.setForeground(AppColors.TEXT_SECONDARY);
        return lbl;
    }

    // ─── Text Inputs ────────────────────────────────────────────────────────
    public static JTextField createTextField() {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        tf.setOpaque(false);
        styleTextField(tf);
        return tf;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        pf.setOpaque(false);
        styleTextField(pf);
        return pf;
    }

    private static void styleTextField(JTextField tf) {
        tf.setBackground(AppColors.BG_CARD);
        tf.setForeground(AppColors.TEXT_PRIMARY);
        tf.setCaretColor(AppColors.ACCENT_BLUE);
        tf.setFont(FONT_REGULAR);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(AppColors.BORDER, 10),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
    }

    public static JTextArea createTextArea(int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setBackground(AppColors.BG_CARD);
        ta.setForeground(AppColors.TEXT_PRIMARY);
        ta.setCaretColor(AppColors.ACCENT_BLUE);
        ta.setFont(FONT_REGULAR);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(AppColors.BORDER, 10),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        return ta;
    }

    // ─── Combo Box ──────────────────────────────────────────────────────────
    public static JComboBox<String> createComboBox(String[] options) {
        JComboBox<String> cb = new JComboBox<>(options);
        cb.setBackground(AppColors.BG_CARD);
        cb.setForeground(AppColors.TEXT_PRIMARY);
        cb.setFont(FONT_REGULAR);
        // Simple line border for combo boxes due to complex internal UI rendering
        cb.setBorder(BorderFactory.createLineBorder(AppColors.BORDER, 1));
        return cb;
    }

    // ─── Buttons ────────────────────────────────────────────────────────────
    public static JButton createPrimaryButton(String text) {
        JButton btn = createRoundedButton(text, AppColors.ACCENT_BLUE, Color.WHITE);
        addHoverEffect(btn, AppColors.ACCENT_BLUE, AppColors.ACCENT_HOVER);
        return btn;
    }

    public static JButton createDangerButton(String text) {
        JButton btn = createRoundedButton(text, AppColors.STATUS_RED, Color.WHITE);
        addHoverEffect(btn, AppColors.STATUS_RED, new Color(255, 100, 100));
        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = createRoundedButton(text, AppColors.BG_CARD, AppColors.TEXT_PRIMARY);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(AppColors.BORDER, 12),
                BorderFactory.createEmptyBorder(14, 32, 14, 32)));
        addHoverEffect(btn, AppColors.BG_CARD, AppColors.TABLE_SELECT);
        return btn;
    }

    public static JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(AppColors.BG_SIDEBAR);
        btn.setForeground(AppColors.TEXT_PRIMARY);
        btn.setFont(FONT_REGULAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        addHoverEffect(btn, AppColors.BG_SIDEBAR, AppColors.TABLE_ROW2);
        return btn;
    }

    private static JButton createRoundedButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width = Math.max(size.width, 140);
                size.height = 40; // Standardized height of 40px
                return size;
            }
        };
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FONT_BOLD_MD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(14, 32, 14, 32));
        
        return btn;
    }

    private static void addHoverEffect(JButton btn, Color normal, Color hover) {
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(normal); }
        });
    }

    // ─── Panels ─────────────────────────────────────────────────────────────
    @SuppressWarnings("unused")
    public static JPanel createDarkPanel() {
        JPanel p = new JPanel();
        p.setBackground(AppColors.BG_PANEL);
        return p;
    }

    public static JPanel createCardPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.setColor(AppColors.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBackground(AppColors.BG_CARD);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        return p;
    }

    // ─── Table ──────────────────────────────────────────────────────────────
    public static void styleTable(JTable table) {
        table.setBackground(AppColors.TABLE_ROW1);
        table.setForeground(AppColors.TEXT_PRIMARY);
        table.setFont(FONT_REGULAR);
        table.setRowHeight(38); // Increased row height for breathing room
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(AppColors.TABLE_SELECT);
        table.setSelectionForeground(AppColors.TEXT_PRIMARY);
        table.setFillsViewportHeight(true);

        // Clean alternating row colours
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, false, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16)); // More cell padding
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? AppColors.TABLE_ROW1 : AppColors.TABLE_ROW2);
                }
                return this;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setBackground(AppColors.TABLE_HEADER);
        header.setForeground(AppColors.TEXT_SECONDARY);
        header.setFont(FONT_BOLD_MD);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        // Soft bottom border
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppColors.BORDER));
    }

    public static JScrollPane createStyledScrollPane(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setBackground(AppColors.BG_PANEL);
        sp.getViewport().setBackground(AppColors.BG_PANEL);
        sp.setBorder(new RoundedBorder(AppColors.BORDER, 12));
        return sp;
    }

    // ─── Separator ──────────────────────────────────────────────────────────
    public static JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(AppColors.BORDER);
        sep.setBackground(AppColors.BORDER);
        return sep;
    }

    // ─── Utility Class for Rounded Borders ──────────────────────────────────
    private static class RoundedBorder implements Border {
        private final int radius;
        private final Color color;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            // Draw slightly inside the bounds to ensure the border isn't clipped
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);
            g2.dispose();
        }
    }
}
