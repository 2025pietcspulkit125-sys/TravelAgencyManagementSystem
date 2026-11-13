package com.example.travelagency;

import javax.swing.*;
import java.awt.*;

public class EditTripFrame extends JFrame {

    private final Trip trip;
    private final ViewTripsFrame parentFrame;

    private JTextField tripNameField;
    private JTextField destinationField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField priceField;
    private JTextArea descriptionArea;

    public EditTripFrame(
            Trip trip,
            ViewTripsFrame parentFrame) {

        this.trip = trip;
        this.parentFrame = parentFrame;

        setTitle("Edit Trip");
        setSize(700, 600);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        createUI();
        loadTripData();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(Color.WHITE);

        // =========================
// HEADER
// =========================

        HeaderPanel headerPanel =
                new HeaderPanel("Edit Trip");

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // Form
        JPanel formPanel =
                new JPanel(new GridBagLayout());

        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 10, 8, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Trip Name
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                createLabel("Trip Name"),
                gbc
        );

        gbc.gridx = 1;

        tripNameField = new JTextField(25);

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

        destinationField =
                new JTextField(25);

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

        startDateField =
                new JTextField(25);

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

        endDateField =
                new JTextField(25);

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

        priceField =
                new JTextField(25);

        formPanel.add(
                priceField,
                gbc
        );

        // Description
        gbc.gridx = 0;
        gbc.gridy = 5;

        gbc.anchor =
                GridBagConstraints.NORTH;

        formPanel.add(
                createLabel("Description"),
                gbc
        );

        gbc.gridx = 1;

        descriptionArea =
                new JTextArea(5, 25);

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScroll =
                new JScrollPane(
                        descriptionArea
                );

        formPanel.add(
                descriptionScroll,
                gbc
        );

        // Buttons
        JPanel buttonPanel =
                new JPanel();

        buttonPanel.setBackground(Color.WHITE);

        JButton updateButton =
                new JButton("Update Trip");

        JButton cancelButton =
                new JButton("Cancel");

        styleButton(updateButton);
        styleButton(cancelButton);

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;

        gbc.anchor =
                GridBagConstraints.CENTER;

        formPanel.add(
                buttonPanel,
                gbc
        );

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        updateButton.addActionListener(
                e -> updateTrip()
        );

        cancelButton.addActionListener(
                e -> dispose()
        );
    }

    // =========================
    // LOAD DATA
    // =========================

    private void loadTripData() {

        tripNameField.setText(
                trip.getTripName()
        );

        destinationField.setText(
                trip.getDestination()
        );

        startDateField.setText(
                trip.getStartDate()
        );

        endDateField.setText(
                trip.getEndDate()
        );

        priceField.setText(
                String.valueOf(trip.getPrice())
        );

        descriptionArea.setText(
                trip.getDescription()
        );
    }

    // =========================
    // UPDATE
    // =========================

    private void updateTrip() {

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

            price =
                    Double.parseDouble(priceText);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid price.",
                    "Invalid Price",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        trip.setTripName(tripName);
        trip.setDestination(destination);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        trip.setPrice(price);
        trip.setDescription(description);
        TripManager.updateTrip(trip);

        JOptionPane.showMessageDialog(
                this,
                "Trip updated successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        parentFrame.refreshTable();

        dispose();
    }

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        return label;
    }

    private void styleButton(JButton button) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        button.setBackground(
                new Color(100, 125, 235)
        );

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);
    }
}