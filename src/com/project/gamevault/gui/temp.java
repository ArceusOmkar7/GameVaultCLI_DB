package com.project.gamevault.gui;

import javax.swing.*;
import java.awt.*;

public class temp extends JFrame {

    public temp() {
        setTitle("Game Vault Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(230, 230, 230));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("🟨", JLabel.CENTER);
        logo.setFont(new Font("SansSerif", Font.PLAIN, 32));
        JLabel name = new JLabel("Game Vault", JLabel.CENTER);
        name.setFont(new Font("SansSerif", Font.BOLD, 16));

        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(logo);
        sidebarPanel.add(name);
        sidebarPanel.add(Box.createVerticalStrut(30));
        sidebarPanel.add(createSidebarButton("📊", "Dashboard"));
        sidebarPanel.add(createSidebarButton("🛒", "Cart"));
        sidebarPanel.add(createSidebarButton("📋", "Billing"));

        // Top Bar
        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setPreferredSize(new Dimension(getWidth(), 50));
        topBarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topBarPanel.setBackground(Color.WHITE);

        JLabel pageTitle = new JLabel("Page");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        profilePanel.setOpaque(false);
        JLabel helloLabel = new JLabel("Hello, Admin");
        JLabel profileIcon = new JLabel("👤");  // Avoid using ImageIcon for now

        profilePanel.add(helloLabel);
        profilePanel.add(profileIcon);

        topBarPanel.add(pageTitle, BorderLayout.WEST);
        topBarPanel.add(profilePanel, BorderLayout.EAST);

        // Center Content Area
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);

        // Add to frame
        add(sidebarPanel, BorderLayout.WEST);
        add(topBarPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createSidebarButton(String icon, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setMaximumSize(new Dimension(200, 40));
        panel.setBackground(new Color(245, 245, 245));

        JLabel iconLabel = new JLabel(icon);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(iconLabel);
        panel.add(textLabel);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(temp::new);
    }
}
