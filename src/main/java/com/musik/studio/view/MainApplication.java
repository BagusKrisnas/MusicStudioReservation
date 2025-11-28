/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.view;

import javax.swing.*;

public class MainApplication extends JFrame {
    
    public MainApplication() {
        initComponents();
        setupUI();
    }
    
    private void initComponents() {
        setTitle("Sistem Reservasi Studio Musik - com.musik.studio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }
    
    private void setupUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create panels
        StudioManagementPanel studioPanel = new StudioManagementPanel();
        ReservationPanel reservationPanel = new ReservationPanel();
        CustomerManagementPanel customerPanel = new CustomerManagementPanel();

        // Placeholder panel untuk laporan
        JPanel reportPanel = new JPanel();
        reportPanel.add(new JLabel("Laporan - Dalam Pengembangan"));

        // Add tabs
        tabbedPane.addTab("🎵 Studio", studioPanel);
        tabbedPane.addTab("📅 Reservasi", reservationPanel);
        tabbedPane.addTab("👥 Customer", customerPanel);
        tabbedPane.addTab("📊 Laporan", reportPanel);

        add(tabbedPane);
    }
}
