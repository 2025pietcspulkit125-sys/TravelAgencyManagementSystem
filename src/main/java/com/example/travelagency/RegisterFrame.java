package com.example.travelagency;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();
    private final JLabel messageLabel = new JLabel(" ");

    public RegisterFrame() {
        setTitle("Travel Agency Management System - Create Account");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(102, 92, 190));
        mainPanel.add(new HeaderPanel("Create Account"), BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(420, 420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(35, 45, 35, 45));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Register");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(24));

        addField(card, "Username", usernameField);
        card.add(Box.createVerticalStrut(12));
        addField(card, "Password (8+ characters)", passwordField);
        card.add(Box.createVerticalStrut(12));
        addField(card, "Confirm Password", confirmPasswordField);
        card.add(Box.createVerticalStrut(18));

        JButton registerButton = new JButton("Create Account");
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleButton(registerButton);
        card.add(registerButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(new Color(50, 90, 180));
        card.add(Box.createVerticalStrut(8));
        card.add(backButton);

        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 13));
        card.add(Box.createVerticalStrut(8));
        card.add(messageLabel);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(102, 92, 190));
        center.add(card);
        mainPanel.add(center, BorderLayout.CENTER);
        add(mainPanel);

        registerButton.addActionListener(e -> register());
        confirmPasswordField.addActionListener(e -> register());
        backButton.addActionListener(e -> returnToLogin());
    }

    private void addField(JPanel panel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(field);
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmation = new String(confirmPasswordField.getPassword());
        if (username.isEmpty() || password.length() < 8) {
            showError("Use a username and a password of at least 8 characters.");
            return;
        }
        if (!password.equals(confirmation)) {
            showError("The passwords do not match.");
            return;
        }
        try {
            UserManager.registerUser(username, password, "user");
            JOptionPane.showMessageDialog(this, "Account created. You can now sign in.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            returnToLogin();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (IllegalStateException exception) {
            showError("Unable to create account. Check the database connection.");
        }
    }

    private void showError(String message) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(message);
    }

    private void returnToLogin() {
        dispose();
        new LoginFrame().setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(100, 125, 235));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }
}
