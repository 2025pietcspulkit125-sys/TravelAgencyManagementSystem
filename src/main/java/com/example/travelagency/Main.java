package com.example.travelagency;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseInitializer.initialize();
        } catch (RuntimeException | java.sql.SQLException exception) {
            System.err.println("Unable to initialize the local database: " + exception.getMessage());
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}