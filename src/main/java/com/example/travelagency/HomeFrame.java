package com.example.travelagency;

import javax.swing.*;
import java.awt.*;

public class HomeFrame extends JFrame {

    public HomeFrame() {

        setTitle("Travel Agency Management System - Home");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        createUI();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // =========================
// HEADER
// =========================

        HeaderPanel headerPanel =
                new HeaderPanel("Travel Agency Management System");

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );
        // =========================
        // MENU
        // =========================

        JPanel menuPanel = new JPanel(
                new GridLayout(3, 2, 20, 20)
        );

        menuPanel.setBackground(Color.WHITE);

        menuPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        60, 150, 60, 150
                )
        );

        JButton createTripButton =
                new JButton("Create Trip");

        JButton viewTripsButton =
                new JButton("View Trips");
        JButton addCustomerButton =
                new JButton("Add Customer");

        JButton viewCustomersButton =
                new JButton("View Customers");

        JButton changePasswordButton =
                new JButton("Change Password");

        JButton logoutButton =
                new JButton("Logout");

        styleButton(createTripButton);
        styleButton(viewTripsButton);
        styleButton(changePasswordButton);
        styleButton(logoutButton);
        styleButton(addCustomerButton);
        styleButton(viewCustomersButton);

        menuPanel.add(createTripButton);
        menuPanel.add(viewTripsButton);
        menuPanel.add(changePasswordButton);
        menuPanel.add(logoutButton);
        menuPanel.add(addCustomerButton);
        menuPanel.add(viewCustomersButton);

        mainPanel.add(
                menuPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // =========================
        // BUTTON ACTIONS
        // =========================

        createTripButton.addActionListener(e -> {

            new CreateTripFrame().setVisible(true);

            dispose();
        });

        viewTripsButton.addActionListener(e -> {

            new ViewTripsFrame().setVisible(true);

            dispose();
        });
        addCustomerButton.addActionListener(e -> {

            new AddCustomerFrame().setVisible(true);

            dispose();
        });

        viewCustomersButton.addActionListener(e -> {

            new ViewCustomersFrame().setVisible(true);

            dispose();
        });

        changePasswordButton.addActionListener(e -> {

            dispose();

            new ForgotPasswordFrame().setVisible(true);
        });

        logoutButton.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {

                dispose();

                new LoginFrame().setVisible(true);
            }
        });
    }

    private void styleButton(JButton button) {

        button.setFont(
                new Font("Arial", Font.BOLD, 17)
        );

        button.setBackground(
                new Color(100, 125, 235)
        );

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);
    }
}