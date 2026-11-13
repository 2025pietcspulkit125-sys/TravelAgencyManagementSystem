package com.example.travelagency;

import javax.swing.*;
import java.awt.*;

public class ForgotPasswordFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel messageLabel;

    public ForgotPasswordFrame() {

        setTitle(
                "Travel Agency Management System - Change Password"
        );

        setSize(900, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setResizable(true);

        createUI();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(102, 92, 190)
        );

        // =========================
        // HEADER
        // =========================

        HeaderPanel headerPanel =
                new HeaderPanel(
                        "Change Password"
                );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // =========================
        // CENTER
        // =========================

        JPanel centerPanel =
                new JPanel(new GridBagLayout());

        centerPanel.setBackground(
                new Color(102, 92, 190)
        );

        JPanel card =
                new JPanel();

        card.setPreferredSize(
                new Dimension(430, 440)
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220)
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                35,
                                25,
                                35
                        )
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        // Title
        JLabel titleLabel =
                new JLabel(
                        "Change Password"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(20)
        );

        // Username
        JLabel usernameLabel =
                new JLabel("Username");

        usernameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        usernameLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(usernameLabel);

        usernameField =
                new JTextField();

        usernameField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        card.add(usernameField);

        card.add(
                Box.createVerticalStrut(15)
        );

        // New Password
        JLabel currentPasswordLabel =
                new JLabel("Current Password");

        currentPasswordLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        currentPasswordLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(currentPasswordLabel);

        currentPasswordField =
                new JPasswordField();

        currentPasswordField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 40)
        );

        card.add(currentPasswordField);

        card.add(Box.createVerticalStrut(15));

        // New Password
        JLabel newPasswordLabel =
                new JLabel("New Password");

        newPasswordLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        newPasswordLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(newPasswordLabel);

        newPasswordField =
                new JPasswordField();

        newPasswordField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        card.add(newPasswordField);

        card.add(
                Box.createVerticalStrut(15)
        );

        // Confirm Password
        JLabel confirmPasswordLabel =
                new JLabel("Confirm New Password");

        confirmPasswordLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        confirmPasswordLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(confirmPasswordLabel);

        confirmPasswordField =
                new JPasswordField();

        confirmPasswordField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        card.add(confirmPasswordField);

        card.add(
                Box.createVerticalStrut(18)
        );

        // Buttons
        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        buttonPanel.setOpaque(false);

        JButton resetButton =
                new JButton("Change Password");

        JButton backButton =
                new JButton("Back to Login");

        styleButton(resetButton);
        styleButton(backButton);

        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        card.add(buttonPanel);

        // Message
        messageLabel =
                new JLabel(
                        " ",
                        SwingConstants.CENTER
                );

        messageLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        messageLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        card.add(messageLabel);

        centerPanel.add(card);

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // =========================
        // ACTIONS
        // =========================

        resetButton.addActionListener(
                e -> resetPassword()
        );

        backButton.addActionListener(e -> {

            dispose();

            new LoginFrame().setVisible(true);
        });
    }

    private void resetPassword() {

        String username =
                usernameField
                        .getText()
                        .trim();

        String newPassword =
                new String(
                        newPasswordField
                                .getPassword()
                );

        String currentPassword =
                new String(
                        currentPasswordField
                                .getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField
                                .getPassword()
                );

        // Username validation
        if (username.isEmpty()) {

            showError(
                    "Please enter username."
            );

            return;
        }

        // Password validation
        if (currentPassword.isEmpty()
                || newPassword.isEmpty()
                || confirmPassword.isEmpty()) {

            showError(
                    "Please enter your current password and both new passwords."
            );

            return;
        }

        if (!UserManager.authenticate(username, currentPassword)) {
            showError("Invalid username or current password.");
            return;
        }

        if (newPassword.length() < 8) {

            showError(
                    "Password must contain at least 8 characters."
            );

            return;
        }

        if (!newPassword.equals(
                confirmPassword
        )) {

            showError(
                    "Passwords do not match."
            );

            return;
        }

        UserManager.changePassword(username, newPassword);

        JOptionPane.showMessageDialog(
                this,
                "Password changed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

        new LoginFrame().setVisible(true);
    }

    private void showError(String message) {

        messageLabel.setForeground(
                Color.RED
        );

        messageLabel.setText(message);
    }

    private void styleButton(
            JButton button
    ) {

        button.setBackground(
                new Color(100, 125, 235)
        );

        button.setForeground(
                Color.WHITE
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        button.setFocusPainted(false);
    }
}
