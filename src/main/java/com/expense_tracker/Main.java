package com.expense_tracker;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.expense_tracker.gui.MainUI;
import com.expense_tracker.util.DatabaseUtil;

public class Main {
    public static void main(String[] args) {
        // Test database connection first
        try (Connection cn = DatabaseUtil.getDBConnection()) {
            System.out.println("Database connection has been established");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Failed to connect to the database. Please check your database settings and make sure MySQL is running.\n\nError: " + e.getMessage(),
                "Database Connection Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Warning: Failed to set system look and feel: " + e.getMessage());
        }

        // Start the UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainUI mainUI = new MainUI();
                mainUI.setLocationRelativeTo(null); // Center the window
                mainUI.setVisible(true);
            } catch (Exception e) {
                String errorMsg = "Failed to initialize the application: " + e.getMessage();
                System.err.println(errorMsg);
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    errorMsg + "\n\nPlease check the console for more details.",
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}