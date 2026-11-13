package com.example.travelagency;

import javax.swing.*;
import java.awt.*;

public class EditCustomerFrame extends JFrame {

    private final Customer customer;
    private final ViewCustomersFrame parentFrame;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;

    public EditCustomerFrame(
            Customer customer,
            ViewCustomersFrame parentFrame) {

        this.customer = customer;
        this.parentFrame = parentFrame;

        setTitle("Travel Agency Management System - Edit Customer");
        setSize(700, 550);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        createUI();
        loadCustomerData();
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
                new HeaderPanel("Edit Customer");

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );
        // =========================
        // FORM
        // =========================

        JPanel formPanel =
                new JPanel(new GridBagLayout());

        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                createLabel("Name"),
                gbc
        );

        gbc.gridx = 1;

        nameField =
                new JTextField(25);

        nameField.setPreferredSize(
                new Dimension(350, 35)
        );

        formPanel.add(
                nameField,
                gbc
        );

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
                createLabel("Email"),
                gbc
        );

        gbc.gridx = 1;

        emailField =
                new JTextField(25);

        emailField.setPreferredSize(
                new Dimension(350, 35)
        );

        formPanel.add(
                emailField,
                gbc
        );

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 2;

        formPanel.add(
                createLabel("Phone"),
                gbc
        );

        gbc.gridx = 1;

        phoneField =
                new JTextField(25);

        phoneField.setPreferredSize(
                new Dimension(350, 35)
        );

        formPanel.add(
                phoneField,
                gbc
        );

        // Address
        gbc.gridx = 0;
        gbc.gridy = 3;

        gbc.anchor =
                GridBagConstraints.NORTH;

        formPanel.add(
                createLabel("Address"),
                gbc
        );

        gbc.gridx = 1;

        addressArea =
                new JTextArea(5, 25);

        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);

        JScrollPane addressScrollPane =
                new JScrollPane(addressArea);

        addressScrollPane.setPreferredSize(
                new Dimension(350, 120)
        );

        formPanel.add(
                addressScrollPane,
                gbc
        );

        // =========================
        // BUTTONS
        // =========================

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.setBackground(Color.WHITE);

        JButton updateButton =
                new JButton("Update Customer");

        JButton cancelButton =
                new JButton("Cancel");

        styleButton(updateButton);
        styleButton(cancelButton);

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
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

        // =========================
        // ACTIONS
        // =========================

        updateButton.addActionListener(
                e -> updateCustomer()
        );

        cancelButton.addActionListener(
                e -> dispose()
        );
    }

    // =========================
    // LOAD CUSTOMER DATA
    // =========================

    private void loadCustomerData() {

        nameField.setText(
                customer.getName()
        );

        emailField.setText(
                customer.getEmail()
        );

        phoneField.setText(
                customer.getPhone()
        );

        addressArea.setText(
                customer.getAddress()
        );
    }

    // =========================
    // UPDATE CUSTOMER
    // =========================

    private void updateCustomer() {

        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();

        String phone =
                phoneField.getText().trim();

        String address =
                addressArea.getText().trim();

        if (name.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        CustomerManager.updateCustomer(customer);

        JOptionPane.showMessageDialog(
                this,
                "Customer updated successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        parentFrame.refreshTable();

        dispose();
    }

    // =========================
    // LABEL
    // =========================

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        return label;
    }

    // =========================
    // BUTTON STYLE
    // =========================

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