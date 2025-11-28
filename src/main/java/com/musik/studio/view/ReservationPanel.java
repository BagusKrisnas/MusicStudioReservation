/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.view;

import com.musik.studio.dao.CustomerDAO;
import com.musik.studio.dao.ReservationDAO;
import com.musik.studio.dao.StudioDAO;
import com.musik.studio.model.Customer;
import com.musik.studio.model.Reservation;
import com.musik.studio.model.Studio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReservationPanel extends JPanel {
    private ReservationDAO reservationDAO;
    private StudioDAO studioDAO;
    private CustomerDAO customerDAO;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    private JComboBox<Studio> studioComboBox;
    private JComboBox<Customer> customerComboBox;
    private JTextField dateField;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;
    
    public ReservationPanel() {
        reservationDAO = new ReservationDAO();
        studioDAO = new StudioDAO();
        customerDAO = new CustomerDAO();
        initComponents();
        loadData();
        loadStudiosAndCustomers();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Sistem Reservasi Studio", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = createFormPanel();
        
        // Table
        String[] columnNames = {"ID", "Studio", "Customer", "Tanggal", "Waktu Mulai", "Waktu Selesai", "Total", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable = new JTable(tableModel);
        reservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        JButton addButton = new JButton("Buat Reservasi");
        JButton cancelButton = new JButton("Batalkan Reservasi");
        JButton confirmButton = new JButton("Konfirmasi Reservasi");
        JButton refreshButton = new JButton("Refresh");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);
        
        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Event listeners
        addButton.addActionListener(e -> createReservation());
        confirmButton.addActionListener(e -> updateReservationStatus("CONFIRMED"));
        cancelButton.addActionListener(e -> updateReservationStatus("CANCELLED"));
        refreshButton.addActionListener(e -> loadData());
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Reservasi Baru"));
        
        // Studio selection
        formPanel.add(new JLabel("Studio:"));
        studioComboBox = new JComboBox<>();
        formPanel.add(studioComboBox);
        
        // Customer selection
        formPanel.add(new JLabel("Customer:"));
        customerComboBox = new JComboBox<>();
        formPanel.add(customerComboBox);
        
        // Date selection (using text field with today's date as default)
        formPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        dateField = new JTextField();
        dateField.setText(LocalDate.now().toString()); // Set today's date as default
        formPanel.add(dateField);
        
        // Time selection
        formPanel.add(new JLabel("Waktu Mulai:"));
        startTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
        startTimeSpinner.setEditor(startTimeEditor);
        startTimeSpinner.setValue(new java.util.Date()); // Set current time
        formPanel.add(startTimeSpinner);
        
        formPanel.add(new JLabel("Waktu Selesai:"));
        endTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endTimeEditor);
        
        // Set end time to 1 hour from now by default
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.HOUR, 1);
        endTimeSpinner.setValue(calendar.getTime());
        formPanel.add(endTimeSpinner);
        
        return formPanel;
    }
    
    private void loadStudiosAndCustomers() {
        try {
            System.out.println("🔄 Loading studios and customers...");

            // Load studios
            List<Studio> studios = studioDAO.findActiveStudios();
            studioComboBox.removeAllItems();
            System.out.println("📊 Studios loaded: " + studios.size());

            for (Studio studio : studios) {
                studioComboBox.addItem(studio);
                System.out.println("   ➕ Added to combo: " + studio.getName());
            }

            // Load customers
            List<Customer> customers = customerDAO.findAll();
            customerComboBox.removeAllItems();
            System.out.println("📊 Customers loaded: " + customers.size());

            for (Customer customer : customers) {
                customerComboBox.addItem(customer);
            }

            // Force UI refresh
            studioComboBox.revalidate();
            studioComboBox.repaint();

            System.out.println("✅ ComboBox refresh completed");

        } catch (Exception e) {
            System.out.println("❌ Error loading data: " + e.getMessage());
            e.printStackTrace();
            showError("Error loading data: " + e.getMessage());
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Reservation> reservations = reservationDAO.findAll();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            
            for (Reservation reservation : reservations) {
                Object[] rowData = {
                    reservation.getId(),
                    reservation.getStudio().getName(),
                    reservation.getCustomer().getName(),
                    reservation.getStartTime().format(dateFormatter),
                    reservation.getStartTime().format(timeFormatter),
                    reservation.getEndTime().format(timeFormatter),
                    "Rp " + reservation.getTotalPrice(),
                    getStatusText(reservation.getStatus())
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }
    
    private String getStatusText(String status) {
        switch (status) {
            case "PENDING": return "⏳ Pending";
            case "CONFIRMED": return "✅ Dikonfirmasi";
            case "CANCELLED": return "❌ Dibatalkan";
            case "COMPLETED": return "✓ Selesai";
            default: return status;
        }
    }
    
    private void createReservation() {
        Studio selectedStudio = (Studio) studioComboBox.getSelectedItem();
        Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
        
        if (selectedStudio == null || selectedCustomer == null) {
            showError("Pilih studio dan customer terlebih dahulu!");
            return;
        }
        
        String dateText = dateField.getText().trim();
        java.util.Date startTime = (java.util.Date) startTimeSpinner.getValue();
        java.util.Date endTime = (java.util.Date) endTimeSpinner.getValue();
        
        if (dateText.isEmpty() || startTime == null || endTime == null) {
            showError("Isi semua field tanggal dan waktu!");
            return;
        }
        
        try {
            // Parse date
            LocalDate selectedDate = LocalDate.parse(dateText);
            
            // Convert java.util.Date to LocalTime
            LocalTime startLocalTime = startTime.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalTime();
            LocalTime endLocalTime = endTime.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalTime();
            
            // Combine date and time
            LocalDateTime startDateTime = LocalDateTime.of(selectedDate, startLocalTime);
            LocalDateTime endDateTime = LocalDateTime.of(selectedDate, endLocalTime);
            
            // Validation
            if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {
                showError("Waktu selesai harus setelah waktu mulai!");
                return;
            }
            
            if (startDateTime.isBefore(LocalDateTime.now())) {
                showError("Tidak bisa membuat reservasi untuk waktu yang sudah lewat!");
                return;
            }
            
            // Check availability
            boolean isAvailable = reservationDAO.isStudioAvailable(
                selectedStudio.getId(), startDateTime, endDateTime, null);
            
            if (!isAvailable) {
                showError("Studio tidak tersedia pada waktu yang dipilih!");
                return;
            }
            
            // Create reservation
            Reservation reservation = new Reservation(
                selectedStudio, selectedCustomer, startDateTime, endDateTime);
            
            reservationDAO.save(reservation);
            loadData();
            loadStudiosAndCustomers(); // Refresh comboboxes
            JOptionPane.showMessageDialog(this, 
                "Reservasi berhasil dibuat! Total: Rp " + reservation.getTotalPrice());
            
        } catch (DateTimeParseException e) {
            showError("Format tanggal salah! Gunakan format: YYYY-MM-DD (contoh: 2024-01-15)");
        } catch (Exception e) {
            showError("Error creating reservation: " + e.getMessage());
        }
    }
    
    private void updateReservationStatus(String newStatus) {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Pilih reservasi yang akan diupdate!");
            return;
        }
        
        Long reservationId = (Long) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = getStatusFromText((String) tableModel.getValueAt(selectedRow, 7));
        
        // Validation
        if ("COMPLETED".equals(currentStatus) || "CANCELLED".equals(currentStatus)) {
            showError("Tidak bisa mengubah status reservasi yang sudah selesai atau dibatalkan!");
            return;
        }
        
        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation != null) {
            reservation.setStatus(newStatus);
            
            try {
                reservationDAO.update(reservation);
                loadData();
                JOptionPane.showMessageDialog(this, 
                    "Status reservasi berhasil diubah menjadi: " + getStatusText(newStatus));
            } catch (Exception e) {
                showError("Error updating reservation: " + e.getMessage());
            }
        }
    }
    
    private String getStatusFromText(String statusText) {
        if (statusText.contains("Pending")) return "PENDING";
        if (statusText.contains("Dikonfirmasi")) return "CONFIRMED";
        if (statusText.contains("Dibatalkan")) return "CANCELLED";
        if (statusText.contains("Selesai")) return "COMPLETED";
        return statusText;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
