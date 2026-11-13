package com.example.travelagency;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewCustomersFrame extends JFrame {

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public ViewCustomersFrame() {

        setTitle("Travel Agency Management System - View Customers");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        createUI();
        loadCustomers();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // =========================
// HEADER
// =========================

        HeaderPanel headerPanel =
                new HeaderPanel("View Customers");

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );
        // Search
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search:");

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

        // Table
        String[] columns = {
                "ID",
                "Name",
                "Email",
                "Phone",
                "Address"
        };

        tableModel = new DefaultTableModel(
                columns,
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {
                return false;
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(30);

        JScrollPane scrollPane =
                new JScrollPane(customerTable);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // Buttons
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

        // Actions
        searchButton.addActionListener(
                e -> searchCustomers()
        );

        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadCustomers();
        });

        refreshButton.addActionListener(
                e -> loadCustomers()
        );

        deleteButton.addActionListener(
                e -> deleteSelectedCustomer()
        );

        editButton.addActionListener(
                e -> editSelectedCustomer()
        );

        backButton.addActionListener(e -> {
            dispose();
            new HomeFrame().setVisible(true);
        });

        searchField.addActionListener(
                e -> searchCustomers()
        );
    }

    private void loadCustomers() {

        tableModel.setRowCount(0);

        for (Customer customer :
                CustomerManager.getCustomers()) {

            addCustomerToTable(customer);
        }
    }

    private void addCustomerToTable(
            Customer customer) {

        tableModel.addRow(
                new Object[]{
                        customer.getId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getPhone(),
                        customer.getAddress()
                }
        );
    }

    private void searchCustomers() {

        String searchText =
                searchField.getText()
                        .trim()
                        .toLowerCase();

        tableModel.setRowCount(0);

        if (searchText.isEmpty()) {
            loadCustomers();
            return;
        }

        for (Customer customer :
                CustomerManager.getCustomers()) {

            boolean matches =
                    customer.getName()
                            .toLowerCase()
                            .contains(searchText)
                            ||
                            customer.getEmail()
                                    .toLowerCase()
                                    .contains(searchText)
                            ||
                            customer.getPhone()
                                    .toLowerCase()
                                    .contains(searchText);

            if (matches) {
                addCustomerToTable(customer);
            }
        }

        if (tableModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No customers found.",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void deleteSelectedCustomer() {

        int selectedRow =
                customerTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a customer first.",
                    "No Customer Selected",
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
                        "Are you sure you want to delete this customer?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice == JOptionPane.YES_OPTION) {

            CustomerManager.deleteCustomer(id);

            loadCustomers();

            JOptionPane.showMessageDialog(
                    this,
                    "Customer deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void editSelectedCustomer() {

        int selectedRow =
                customerTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a customer first.",
                    "No Customer Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );

        Customer selectedCustomer = null;

        for (Customer customer :
                CustomerManager.getCustomers()) {

            if (customer.getId() == id) {

                selectedCustomer = customer;
                break;
            }
        }

        if (selectedCustomer != null) {

            new EditCustomerFrame(
                    selectedCustomer,
                    this
            ).setVisible(true);
        }
    }

    public void refreshTable() {
        loadCustomers();
    }

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