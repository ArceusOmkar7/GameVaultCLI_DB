package com.project.gamevault.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrameCode {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Game Vault");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        initComponents();
    }

    private void initComponents() {
        // Main layout
        getContentPane().setLayout(new BorderLayout());

        // Sidebar (Left)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(200, 200, 200));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        JLabel title = new JLabel("Game Vault");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebar.add(title);

        // Menu items with icons
        sidebar.add(createMenuItem("Dashboard", "dashboard.png"));
        sidebar.add(createMenuItem("Cart", "shopping-bag.png"));
        sidebar.add(createMenuItem("Billing", "bill.png"));

        // Top bar (North)
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setBackground(Color.WHITE);

        JLabel helloLabel = new JLabel("Hello, Admin");
        topBar.add(helloLabel);

        JLabel profileIcon = new JLabel();
        ImageIcon icon = loadImage("main_logo.png");
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            profileIcon.setIcon(new ImageIcon(scaled));
        }
        topBar.add(profileIcon);

        // Content panel (Center)
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BorderLayout());

        JLabel pageTitle = new JLabel("Page");
        pageTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        content.add(pageTitle, BorderLayout.NORTH);

        // Add all to frame
        add(sidebar, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        pack();
    }

    private JPanel createMenuItem(String text, String imageName) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel iconLabel = new JLabel();
        ImageIcon icon = loadImage(imageName);
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaled));
        }

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(iconLabel);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(textLabel);

        return panel;
    }

    private ImageIcon loadImage(String fileName) {
        try {
            // Make sure images are inside: src/main/resources/images/
            java.net.URL imageURL = getClass().getResource("/images/" + fileName); // Leading slash refers to the root of the classpath
            if (imageURL != null) {
                return new ImageIcon(imageURL);
            } else {
                System.err.println("Image not found: " + fileName);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
