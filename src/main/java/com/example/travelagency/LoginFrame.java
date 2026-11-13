package com.example.travelagency;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame() {

        setTitle(
                "Travel Agency Management System - Login"
        );

        setSize(900, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
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
                        "Travel Agency Management System"
                );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // =========================
        // LOGIN AREA
        // =========================

        JPanel centerPanel =
                new JPanel(new GridBagLayout());

        centerPanel.setBackground(
                new Color(102, 92, 190)
        );

        // =========================
        // LOGIN CARD
        // =========================

        JPanel loginPanel =
                new JPanel();

        loginPanel.setPreferredSize(
                new Dimension(400, 400)
        );

        loginPanel.setBackground(Color.WHITE);

        loginPanel.setLayout(
                new BoxLayout(
                        loginPanel,
                        BoxLayout.Y_AXIS
                )
        );

        loginPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        40,
                        25,
                        40
                )
        );

        // Logo
        JLabel logoLabel =
                new JLabel(" \u2708\uFE0F");

        logoLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        40
                )
        );

        logoLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(logoLabel);

        // Title
        JLabel titleLabel =
                new JLabel(
                        "Travel Agency"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        27
                )
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(titleLabel);

        JLabel systemLabel =
                new JLabel(
                        "Management System"
                );

        systemLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        systemLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(systemLabel);

        loginPanel.add(
                Box.createVerticalStrut(20)
        );

        // =========================
        // USERNAME
        // =========================

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
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(usernameLabel);

        usernameField =
                new JTextField();

        usernameField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        loginPanel.add(usernameField);

        loginPanel.add(
                Box.createVerticalStrut(15)
        );

        // =========================
        // PASSWORD
        // =========================

        JLabel passwordLabel =
                new JLabel("Password");

        passwordLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        passwordLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginPanel.add(passwordLabel);

        passwordField =
                new JPasswordField();

        passwordField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        loginPanel.add(passwordField);

        loginPanel.add(
                Box.createVerticalStrut(10)
        );

        // =========================
        // FORGOT PASSWORD
        // =========================

        JButton forgotPasswordButton =
                new JButton(
                        "Change Password"
                );

        forgotPasswordButton.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        forgotPasswordButton.setBorderPainted(false);

        forgotPasswordButton.setContentAreaFilled(false);

        forgotPasswordButton.setForeground(
                new Color(50, 90, 180)
        );

        forgotPasswordButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        loginPanel.add(
                forgotPasswordButton
        );

        JButton registerButton = new JButton("Create an account");
        registerButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setForeground(new Color(50, 90, 180));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginPanel.add(registerButton);

        loginPanel.add(
                Box.createVerticalStrut(15)
        );

        // =========================
        // LOGIN BUTTON
        // =========================

        JButton loginButton =
                new JButton("Sign In");

        loginButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        loginButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginButton.setBackground(
                new Color(100, 125, 235)
        );

        loginButton.setForeground(
                Color.WHITE
        );

        loginButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        loginButton.setFocusPainted(false);

        loginPanel.add(loginButton);

        loginPanel.add(
                Box.createVerticalStrut(12)
        );

        // Message
        messageLabel =
                new JLabel(
                        " "
                );

        messageLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        messageLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        loginPanel.add(messageLabel);

        centerPanel.add(loginPanel);

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // =========================
        // ACTIONS
        // =========================

        loginButton.addActionListener(
                e -> login()
        );

        passwordField.addActionListener(
                e -> login()
        );

        forgotPasswordButton.addActionListener(e -> {

            dispose();

            new ForgotPasswordFrame()
                    .setVisible(true);
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });
    }

    // =========================
    // LOGIN
    // =========================

    private void login() {

        String username =
                usernameField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                );

        if (UserManager.authenticate(
                username,
                password
        )) {

            messageLabel.setForeground(
                    new Color(0, 150, 70)
            );

            messageLabel.setText(
                    "Login successful!"
            );

            dispose();

            new HomeFrame().setVisible(true);

        } else {

            messageLabel.setForeground(
                    Color.RED
            );

            messageLabel.setText(
                    "Invalid username or password."
            );
        }
    }
}
