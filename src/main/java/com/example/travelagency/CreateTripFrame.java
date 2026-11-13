package com.example.travelagency;

import javax.swing.*;
import java.awt.*;

public class CreateTripFrame extends JFrame {

    private JTextField tripNameField;
    private JTextField destinationField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField priceField;
    private JTextArea descriptionArea;

    public CreateTripFrame() {

        setTitle("Travel Agency Management System - Create Trip");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        createUI();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(102, 92, 190));

        // =========================
// HEADER
// =========================

        HeaderPanel headerPanel =
                new HeaderPanel("Create Trip");

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );
        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel = new JPanel(
                new GridBagLayout()
        );

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 50, 25, 50
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Trip Name
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                createLabel("Trip Name"),
                gbc
        );

        gbc.gridx = 1;

        tripNameField = new JTextField();
        tripNameField.setPreferredSize(
                new Dimension(400, 35)
        );

        formPanel.add(
                tripNameField,
                gbc
        );

        // Destination
        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
                createLabel("Destination"),
                gbc
        );

        gbc.gridx = 1;

        destinationField = new JTextField();
        destinationField.setPreferredSize(
                new Dimension(400, 35)
        );

        formPanel.add(
                destinationField,
                gbc
        );

        // Start Date
        gbc.gridx = 0;
        gbc.gridy = 2;

        formPanel.add(
                createLabel("Start Date"),
                gbc
        );

        gbc.gridx = 1;

        startDateField = new JTextField();
        startDateField.setPreferredSize(
                new Dimension(400, 35)
        );

        startDateField.setToolTipText(
                "Example: 2026-10-15"
        );

        formPanel.add(
                startDateField,
                gbc
        );

        // End Date
        gbc.gridx = 0;
        gbc.gridy = 3;

        formPanel.add(
                createLabel("End Date"),
                gbc
        );

        gbc.gridx = 1;

        endDateField = new JTextField();
        endDateField.setPreferredSize(
                new Dimension(400, 35)
        );

        endDateField.setToolTipText(
                "Example: 2026-10-20"
        );

        formPanel.add(
                endDateField,
                gbc
        );

        // Price
        gbc.gridx = 0;
        gbc.gridy = 4;

        formPanel.add(
                createLabel("Price"),
                gbc
        );

        gbc.gridx = 1;

        priceField = new JTextField();
        priceField.setPreferredSize(
                new Dimension(400, 35)
        );

        formPanel.add(
                priceField,
                gbc
        );

        // Description
        gbc.gridx = 0;
        gbc.gridy = 5;

        gbc.anchor = GridBagConstraints.NORTH;

        formPanel.add(
                createLabel("Description"),
                gbc
        );

        gbc.gridx = 1;

        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScrollPane =
                new JScrollPane(descriptionArea);

        descriptionScrollPane.setPreferredSize(
                new Dimension(400, 120)
        );

        formPanel.add(
                descriptionScrollPane,
                gbc
        );

        // =========================
        // BUTTONS
        // =========================

        JPanel buttonPanel = new JPanel();

        buttonPanel.setBackground(Color.WHITE);

        JButton createButton =
                new JButton("Create Trip");

        JButton clearButton =
                new JButton("Clear");

        JButton backButton =
                new JButton("Back to Home");

        styleButton(createButton);
        styleButton(clearButton);
        styleButton(backButton);

        buttonPanel.add(createButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;

        gbc.anchor = GridBagConstraints.CENTER;

        formPanel.add(
                buttonPanel,
                gbc
        );

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // =========================
        // BUTTON ACTIONS
        // =========================

        createButton.addActionListener(
                e -> createTrip()
        );

        clearButton.addActionListener(
                e -> clearForm()
        );

        backButton.addActionListener(e -> {

            dispose();

            new HomeFrame().setVisible(true);

        });
    }

    // =========================
    // CREATE TRIP
    // =========================

    private void createTrip() {

        String tripName =
                tripNameField.getText().trim();

        String destination =
                destinationField.getText().trim();

        String startDate =
                startDateField.getText().trim();

        String endDate =
                endDateField.getText().trim();

        String priceText =
                priceField.getText().trim();

        String description =
                descriptionArea.getText().trim();

        // Validation

        if (tripName.isEmpty()
                || destination.isEmpty()
                || startDate.isEmpty()
                || endDate.isEmpty()
                || priceText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double price;

        try {

            price = Double.parseDouble(priceText);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid price.",
                    "Invalid Price",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Trip trip = new Trip(
                0,
                tripName,
                destination,
                startDate,
                endDate,
                price,
                description
        );

        TripManager.addTrip(trip);

        JOptionPane.showMessageDialog(
                this,
                "Trip created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        clearForm();
    }

    // =========================
    // CLEAR FORM
    // =========================

    private void clearForm() {

        tripNameField.setText("");
        destinationField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        priceField.setText("");
        descriptionArea.setText("");
    }

    // =========================
    // LABEL
    // =========================

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        return label;
    }

    // =========================
    // BUTTON STYLE
    // =========================

    private void styleButton(JButton button) {

        button.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        button.setBackground(
                new Color(100, 125, 235)
        );

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);
    }
}
