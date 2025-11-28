/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.view;

import com.musik.studio.dao.CustomerDAO;
import com.musik.studio.model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerManagementPanel extends JPanel {
    private CustomerDAO customerDAO;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public CustomerManagementPanel() {
        customerDAO = new CustomerDAO();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Manajemen Customer", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        
        JButton searchButton = new JButton("Cari");
        JButton clearSearchButton = new JButton("Clear");
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);
        
        // Table
        String[] columnNames = {"ID", "Nama", "Email", "Telepon", "Tanggal Daftar", "Member"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Boolean.class : Object.class;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        JButton addButton = new JButton("Tambah Customer");
        JButton editButton = new JButton("Edit Customer");
        JButton deleteButton = new JButton("Hapus Customer");
        JButton refreshButton = new JButton("Refresh");
        JButton toggleMemberButton = new JButton("Toggle Member");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(toggleMemberButton);
        buttonPanel.add(refreshButton);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(customerTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Event listeners
        addButton.addActionListener(e -> showCustomerForm());
        editButton.addActionListener(e -> editCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        toggleMemberButton.addActionListener(e -> toggleMemberStatus());
        refreshButton.addActionListener(e -> loadData());
        searchButton.addActionListener(e -> searchCustomers());
        clearSearchButton.addActionListener(e -> {
            searchField.setText("");
            loadData();
        });
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Customer> customers = customerDAO.findAll();
            for (Customer customer : customers) {
                Object[] rowData = {
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getRegistrationDate(),
                    customer.isMember()
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }
    
    private void searchCustomers() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        try {
            List<Customer> customers = customerDAO.findByName(searchText);
            for (Customer customer : customers) {
                Object[] rowData = {
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getRegistrationDate(),
                    customer.isMember()
                };
                tableModel.addRow(rowData);
            }
            
            if (customers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tidak ada customer ditemukan", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            showError("Error searching: " + e.getMessage());
        }
    }
    
    private void showCustomerForm() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JCheckBox memberCheckbox = new JCheckBox("Customer Member");
        
        Object[] message = {
            "Nama Customer:", nameField,
            "Email:", emailField,
            "Telepon:", phoneField,
            memberCheckbox
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Tambah Customer", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Validation
                if (nameField.getText().trim().isEmpty()) {
                    showError("Nama customer harus diisi!");
                    return;
                }
                
                Customer customer = new Customer();
                customer.setName(nameField.getText().trim());
                customer.setEmail(emailField.getText().trim());
                customer.setPhone(phoneField.getText().trim());
                customer.setMember(memberCheckbox.isSelected());
                
                customerDAO.save(customer);
                loadData();
                JOptionPane.showMessageDialog(this, "Customer berhasil ditambahkan!");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        }
    }
    
    private void editCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Pilih customer yang akan diedit!");
            return;
        }
        
        Long customerId = (Long) tableModel.getValueAt(selectedRow, 0);
        Customer customer = customerDAO.findById(customerId);
        
        if (customer != null) {
            JTextField nameField = new JTextField(customer.getName());
            JTextField emailField = new JTextField(customer.getEmail());
            JTextField phoneField = new JTextField(customer.getPhone());
            JCheckBox memberCheckbox = new JCheckBox("Customer Member", customer.isMember());
            
            Object[] message = {
                "Nama Customer:", nameField,
                "Email:", emailField,
                "Telepon:", phoneField,
                memberCheckbox
            };
            
            int option = JOptionPane.showConfirmDialog(this, message, 
                "Edit Customer", JOptionPane.OK_CANCEL_OPTION);
            
            if (option == JOptionPane.OK_OPTION) {
                try {
                    customer.setName(nameField.getText().trim());
                    customer.setEmail(emailField.getText().trim());
                    customer.setPhone(phoneField.getText().trim());
                    customer.setMember(memberCheckbox.isSelected());
                    
                    customerDAO.update(customer);
                    loadData();
                    JOptionPane.showMessageDialog(this, "Customer berhasil diupdate!");
                } catch (Exception e) {
                    showError("Error: " + e.getMessage());
                }
            }
        }
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Pilih customer yang akan dihapus!");
            return;
        }
        
        Long customerId = (Long) tableModel.getValueAt(selectedRow, 0);
        String customerName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus customer: " + customerName + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                customerDAO.delete(customerId);
                loadData();
                JOptionPane.showMessageDialog(this, "Customer berhasil dihapus!");
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        }
    }
    
    private void toggleMemberStatus() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Pilih customer yang akan diubah status membernya!");
            return;
        }
        
        Long customerId = (Long) tableModel.getValueAt(selectedRow, 0);
        Customer customer = customerDAO.findById(customerId);
        
        if (customer != null) {
            boolean newStatus = !customer.isMember();
            customer.setMember(newStatus);
            
            try {
                customerDAO.update(customer);
                loadData();
                JOptionPane.showMessageDialog(this, 
                    "Status member berhasil diubah menjadi: " + (newStatus ? "Member" : "Non-Member"));
            } catch (Exception e) {
                showError("Error: " + e.getMessage());
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
