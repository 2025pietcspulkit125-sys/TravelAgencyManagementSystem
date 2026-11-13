package com.example.travelagency;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private final JLabel titleLabel;

    public HeaderPanel(String title) {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Load image from src/main/resources/travel.jpg
        java.net.URL imageURL =
                getClass().getResource("/travel.jpg");

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        if (imageURL != null) {

            ImageIcon originalIcon =
                    new ImageIcon(imageURL);

            Image originalImage =
                    originalIcon.getImage();

            Image scaledImage =
                    originalImage.getScaledInstance(
                            1000,
                            180,
                            Image.SCALE_SMOOTH
                    );

            imageLabel.setIcon(
                    new ImageIcon(scaledImage)
            );

        } else {

            imageLabel.setText(
                    "Header Image Not Found"
            );

            imageLabel.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            20
                    )
            );

            imageLabel.setForeground(Color.RED);
        }

        titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        titleLabel.setForeground(Color.WHITE);

        // Put title over the image
        JPanel imagePanel =
                new JPanel(new BorderLayout());

        imagePanel.setOpaque(false);

        imagePanel.add(
                imageLabel,
                BorderLayout.CENTER
        );

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 10, 5, 10
                )
        );

        imagePanel.add(
                titleLabel,
                BorderLayout.SOUTH
        );

        add(
                imagePanel,
                BorderLayout.CENTER
        );
    }
}