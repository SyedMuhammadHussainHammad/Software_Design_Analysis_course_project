package utils;

import java.awt.Color;

/**
 * Centralized color palette for the dark-themed SkyStream UI.
 * Inspired by the sky-stream-airplane-management website aesthetic.
 */
public class AppColors {
    // Background shades (Deep Navy / Black)
    public static final Color BG_DARK = new Color(5, 11, 26);
    public static final Color BG_PANEL = new Color(10, 18, 38);
    public static final Color BG_CARD = new Color(16, 28, 56);
    public static final Color BG_SIDEBAR = new Color(8, 15, 33);

    // Accent / brand (Electric Blue & Gold)
    public static final Color ACCENT_BLUE = new Color(0, 163, 255);
    public static final Color ACCENT_TEAL = new Color(245, 158, 11); // Kept name for compatibility, but it's now Gold/Orange
    public static final Color ACCENT_HOVER = new Color(51, 182, 255);

    // Status colours
    public static final Color STATUS_GREEN = new Color(16, 185, 129); // Modern emerald
    public static final Color STATUS_YELLOW = new Color(245, 158, 11); // Modern amber
    public static final Color STATUS_RED = new Color(239, 68, 68); // Modern red

    // Text
    public static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    public static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);

    // Table
    public static final Color TABLE_HEADER = new Color(15, 23, 42);
    public static final Color TABLE_ROW1 = new Color(10, 18, 38);
    public static final Color TABLE_ROW2 = new Color(12, 21, 45);
    public static final Color TABLE_SELECT = new Color(0, 163, 255, 60); // Semi-transparent blue

    // Border
    public static final Color BORDER = new Color(30, 41, 59);
}
