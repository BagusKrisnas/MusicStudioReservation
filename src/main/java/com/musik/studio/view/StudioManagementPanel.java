/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.view;

import com.musik.studio.dao.StudioDAO;
import com.musik.studio.model.Studio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class StudioManagementPanel extends JPanel {
    private StudioDAO studioDAO;
    private JTable studioTable;
    private DefaultTableModel tableModel;
    
    public StudioManagementPanel() {
        studioDAO = new StudioDAO();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Manajemen Studio", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"ID", "Nama Studio", "Fasilitas", "Harga/Jam", "Kapasitas", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        studioTable = new JTable(tableModel);
        studioTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        JButton addButton = new JButton("Tambah Studio");
        JButton editButton = new JButton("Edit Studio");
        JButton deleteButton = new JButton("Hapus Studio");
        JButton refreshButton = new JButton("Refresh");
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Main panel with table and buttons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(studioTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Event listeners
        addButton.addActionListener(e -> showStudioForm());
        editButton.addActionListener(e -> editStudio());
        deleteButton.addActionListener(e -> deleteStudio());
        refreshButton.addActionListener(e -> loadData());
    }
    
    private void loadData() {
        tableModel.setRowCount(0); // Clear existing data
        try {
            List<Studio> studios = studioDAO.findAll();
            for (Studio studio : studios) {
                Object[] rowData = {
                    studio.getId(),
                    studio.getName(),
                    studio.getFacilities(),
                    "Rp " + studio.getPricePerHour(),
                    studio.getCapacity() + " orang",
                    studio.isActive() ? "Aktif" : "Non-Aktif"
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showStudioForm() {
        // Simple form untuk demo
        JTextField nameField = new JTextField();
        JTextField facilitiesField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField capacityField = new JTextField();
        
        Object[] message = {
            "Nama Studio:", nameField,
            "Fasilitas:", facilitiesField,
            "Harga per Jam:", priceField,
            "Kapasitas:", capacityField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Tambah Studio", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                Studio studio = new Studio();
                studio.setName(nameField.getText());
                studio.setFacilities(facilitiesField.getText());
                studio.setPricePerHour(new BigDecimal(priceField.getText()));
                studio.setCapacity(Integer.parseInt(capacityField.getText()));
                
                studioDAO.save(studio);
                loadData();
                JOptionPane.showMessageDialog(this, "Studio berhasil ditambahkan!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editStudio() {
        int selectedRow = studioTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih studio yang akan diedit!");
            return;
        }
        
        Long studioId = (Long) tableModel.getValueAt(selectedRow, 0);
        Studio studio = studioDAO.findById(studioId);
        
        if (studio != null) {
            JTextField nameField = new JTextField(studio.getName());
            JTextField facilitiesField = new JTextField(studio.getFacilities());
            JTextField priceField = new JTextField(studio.getPricePerHour().toString());
            JTextField capacityField = new JTextField(studio.getCapacity().toString());
            
            Object[] message = {
                "Nama Studio:", nameField,
                "Fasilitas:", facilitiesField,
                "Harga per Jam:", priceField,
                "Kapasitas:", capacityField
            };
            
            int option = JOptionPane.showConfirmDialog(this, message, 
                "Edit Studio", JOptionPane.OK_CANCEL_OPTION);
            
            if (option == JOptionPane.OK_OPTION) {
                try {
                    studio.setName(nameField.getText());
                    studio.setFacilities(facilitiesField.getText());
                    studio.setPricePerHour(new BigDecimal(priceField.getText()));
                    studio.setCapacity(Integer.parseInt(capacityField.getText()));
                    
                    studioDAO.update(studio);
                    loadData();
                    JOptionPane.showMessageDialog(this, "Studio berhasil diupdate!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void deleteStudio() {
        int selectedRow = studioTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih studio yang akan dihapus!");
            return;
        }
        
        Long studioId = (Long) tableModel.getValueAt(selectedRow, 0);
        String studioName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus studio: " + studioName + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studioDAO.delete(studioId);
                loadData();
                JOptionPane.showMessageDialog(this, "Studio berhasil dihapus!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
