package com.example.travelagency;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewTripsFrame extends JFrame {

    private JTable tripTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public ViewTripsFrame() {

        setTitle("Travel Agency Management System - View Trips");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        createUI();
        loadTrips();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // =========================
// HEADER
// =========================

        HeaderPanel headerPanel =
                new HeaderPanel("View Trips");

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );
        // =========================
        // SEARCH
        // =========================

        JPanel searchPanel = new JPanel(new FlowLayout());

        searchPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search:");

        searchLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        searchField = new JTextField(25);

        searchField.setPreferredSize(
                new Dimension(250, 35)
        );

        JButton searchButton =
                new JButton("Search");

        JButton showAllButton =
                new JButton("Show All");

        styleButton(searchButton);
        styleButton(showAllButton);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        mainPanel.add(
                searchPanel,
                BorderLayout.PAGE_START
        );

        // =========================
        // TABLE
        // =========================

        String[] columns = {
                "ID",
                "Trip Name",
                "Destination",
                "Start Date",
                "End Date",
                "Price"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        tripTable = new JTable(tableModel);

        tripTable.setRowHeight(30);

        tripTable.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        tripTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane =
                new JScrollPane(tripTable);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 20, 10, 20
                )
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =========================
        // BUTTONS
        // =========================

        JPanel buttonPanel = new JPanel();

        buttonPanel.setBackground(Color.WHITE);

        JButton editButton =
                new JButton("Edit Selected");

        JButton deleteButton =
                new JButton("Delete Selected");

        JButton refreshButton =
                new JButton("Refresh");

        JButton backButton =
                new JButton("Back to Home");

        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(refreshButton);
        styleButton(backButton);

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        // =========================
        // ACTIONS
        // =========================

        searchButton.addActionListener(
                e -> searchTrips()
        );

        showAllButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadTrips();
                }
        );

        refreshButton.addActionListener(
                e -> loadTrips()
        );

        editButton.addActionListener(
                e -> editSelectedTrip()
        );

        deleteButton.addActionListener(
                e -> deleteSelectedTrip()
        );

        backButton.addActionListener(e -> {

            dispose();

            new HomeFrame().setVisible(true);
        });

        searchField.addActionListener(
                e -> searchTrips()
        );
    }

    // =========================
    // LOAD TRIPS
    // =========================

    private void loadTrips() {

        tableModel.setRowCount(0);

        for (Trip trip : TripManager.getTrips()) {

            addTripToTable(trip);
        }
    }

    // =========================
    // ADD TRIP TO TABLE
    // =========================

    private void addTripToTable(Trip trip) {

        tableModel.addRow(
                new Object[]{
                        trip.getId(),
                        trip.getTripName(),
                        trip.getDestination(),
                        trip.getStartDate(),
                        trip.getEndDate(),
                        trip.getPrice()
                }
        );
    }

    // =========================
    // SEARCH
    // =========================

    private void searchTrips() {

        String searchText =
                searchField.getText()
                        .trim()
                        .toLowerCase();

        tableModel.setRowCount(0);

        if (searchText.isEmpty()) {

            loadTrips();

            return;
        }

        for (Trip trip : TripManager.getTrips()) {

            boolean matches =
                    trip.getTripName()
                            .toLowerCase()
                            .contains(searchText)
                            ||
                            trip.getDestination()
                                    .toLowerCase()
                                    .contains(searchText);

            if (matches) {

                addTripToTable(trip);
            }
        }

        if (tableModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No trips found.",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // =========================
    // EDIT TRIP
    // =========================

    private void editSelectedTrip() {

        int selectedRow =
                tripTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a trip first.",
                    "No Trip Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );

        Trip selectedTrip = null;

        for (Trip trip : TripManager.getTrips()) {

            if (trip.getId() == id) {

                selectedTrip = trip;

                break;
            }
        }

        if (selectedTrip != null) {

            new EditTripFrame(
                    selectedTrip,
                    this
            ).setVisible(true);
        }
    }

    // =========================
    // DELETE TRIP
    // =========================

    private void deleteSelectedTrip() {

        int selectedRow =
                tripTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a trip first.",
                    "No Trip Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this trip?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice == JOptionPane.YES_OPTION) {

            TripManager.deleteTrip(id);

            loadTrips();

            JOptionPane.showMessageDialog(
                    this,
                    "Trip deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
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
    public void refreshTable() {
        loadTrips();
    }
}