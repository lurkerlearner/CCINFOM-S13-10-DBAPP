package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import controller.RiderController;
import model.Rider;

public class RiderPanel extends JPanel 
{
    private RiderController controller;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    private JPanel editPanel;
    
    // Components for adding riders
    private JTextField riderName;
    private JTextField hireDate;
    private JTextField contactNo;
    private JButton addButton;
    
    // Components for viewing riders
    private JTable riderTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailsButton;
    
    // Components for searching riders
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    // Button to go back to main menu
    private JButton mainMenuButton;

    public RiderPanel(RiderController controller) 
    {
        this.controller = controller;
        
        setLayout(new BorderLayout());
        
        initComponents();
    }
    
    // Initialize all GUI components
    private void initComponents() 
    {
        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create and add panels for each tab
        createAddPanel();
        createViewPanel();
        createSearchPanel();
        createEditPanel();
        
        tabbedPane.addTab("Add Rider", addPanel);
        tabbedPane.addTab("View All Riders", viewPanel);
        tabbedPane.addTab("Search Riders", searchPanel);
        tabbedPane.addTab("Edit Rider", editPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    // Create the panel for adding a rider
    private void createAddPanel() 
    {
        addPanel = new JPanel();
        addPanel.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
            
        // Create fields
        riderName = new JTextField(8);
        hireDate = new JTextField(8);
        contactNo = new JTextField(8);
        
        // Add fields to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Rider Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(riderName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Hire Date:"), gbc);

        gbc.gridx = 1;
        formPanel.add(hireDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Contact No:"), gbc);

        gbc.gridx = 1;
        formPanel.add(contactNo, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        addButton = new JButton("Add Rider");
        addButton.addActionListener(e -> addRider());
        
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addButton);
        
        // Add panels to the main panel
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Create the panel for viewing all riders
    private void createViewPanel() 
    {
        viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {"Rider ID", "Rider Name", "Hire Date", "Contact No."};

        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        riderTable = new JTable(tableModel);
        riderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        riderTable.getTableHeader().setReorderingAllowed(false);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshRiderTable());
        
        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showRiderDetails());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        
        viewPanel.add(new JScrollPane(riderTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate the table
        refreshRiderTable();
    }
    
    // Create the panel for searching riders
    private void createSearchPanel() 
    {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        searchTypeComboBox = new JComboBox<>(new String[]{"By Rider ID"});

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchRider());
        
        searchControls.add(new JLabel("Search:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);
        
        // Create table
        String[] columnNames = {"Rider ID", "Rider Name", "Hire Date", "Contact No."};

        searchTableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        searchResultTable = new JTable(searchTableModel);
        searchResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultTable.getTableHeader().setReorderingAllowed(false);
        
        // Create button for details
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JButton detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showSearchResultDetails());
        
        buttonPanel.add(detailsButton);
        
        // Add components to the panel
        searchPanel.add(searchControls, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResultTable), BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Create the panel for editing riders
    private void createEditPanel() {
        editPanel = new JPanel(new BorderLayout());
        editPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JComboBox<Rider> riderDropdown = new JComboBox<>();
        for (Rider r : controller.getAllRiders()) {
            riderDropdown.addItem(r);
        }
        riderDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Rider rider) {
                    setText("Rider ID " + rider.getRiderID());
                }
                return this;
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Rider:"), gbc);
        gbc.gridx = 1;
        formPanel.add(riderDropdown, gbc);

        SpinnerDateModel timeModel = new SpinnerDateModel();
        JSpinner editDateHiredSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(editDateHiredSpinner, "yyyy-MM-dd");
        editDateHiredSpinner.setEditor(dateEditor);

        JTextField editNameField = new JTextField(5);
        JTextField editContactNoField = new JTextField(5);

        int row = 1;
        addField(formPanel, gbc, row++, "Rider Name:", editNameField);
        addField(formPanel, gbc, row++, "Hire Date:", editDateHiredSpinner);
        addField(formPanel, gbc, row++, "Contact Number:", editContactNoField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Changes");

        buttonPanel.add(saveBtn);

        editPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        editPanel.add(buttonPanel, BorderLayout.SOUTH);

        riderDropdown.addActionListener(e -> 
        {
            Rider selected = (Rider) riderDropdown.getSelectedItem();
            if (selected != null) 
            {
                editNameField.setText("" + selected.getRiderName());
                LocalDate today = LocalDate.now();
                Date dateOnly = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
                editDateHiredSpinner.setValue(dateOnly);
                editContactNoField.setText("" + selected.getContactNo());
            }
        });

        saveBtn.addActionListener(e -> {
            try 
            {
                Rider selected = (Rider) riderDropdown.getSelectedItem();
                if (selected == null) 
                {
                    JOptionPane.showMessageDialog(this, "Please select a rider.");
                    return;
                }

                String newName = editNameField.getText();
                Date newHD = (Date) editDateHiredSpinner.getValue();
                String newContactNo = editContactNoField.getText();

                int key = selected.getRiderID();
                
                if (newName != selected.getRiderName())
                {
                    boolean N_ok = controller.modifyRiderColumn(key, "rider_name", newName);
                    if (N_ok)
                        JOptionPane.showMessageDialog(this, "Rider name updated successfully!");
                    refreshRiderTable();
                }

                if (newHD != selected.getHireDate())
                {
                    java.sql.Date newHireDate = (java.sql.Date) newHD;
                    boolean HD_ok = controller.modifyRiderColumn(key, "hire_date", newHireDate);
                    if (HD_ok)
                        JOptionPane.showMessageDialog(this, "Hire date updated successfully!");
                    refreshRiderTable();
                }

                if (newContactNo != selected.getContactNo())
                {
                    boolean CN_ok = controller.modifyRiderColumn(key, "contact_no", newContactNo);
                    if (CN_ok)
                        JOptionPane.showMessageDialog(this, "Contact number updated successfully!");
                    refreshRiderTable();
                }
            } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });

    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Add new rider using the input fields
    private void addRider() 
    {
        try 
        {
            String name = riderName.getText().trim();
            java.sql.Date hireDateSQL = java.sql.Date.valueOf(hireDate.getText().trim());             
            String contact = contactNo.getText().trim();

            controller.addRider(name, hireDateSQL, contact);

            JOptionPane.showMessageDialog(this, "Rider added successfully!");
            clearAddFields();
            refreshRiderTable();

        } 
        catch (IllegalArgumentException e) 
        {
            JOptionPane.showMessageDialog(this, "Invalid value/s cannot be added.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Clear all input fields in the add panel 
    private void clearAddFields()
    {
        riderName.setText("");
        hireDate.setText("");
        contactNo.setText("");
    }
    
    // Refresh the Rider table with the latest data
    private void refreshRiderTable() 
    {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all riders
        List<Rider> allRiders = controller.getAllRiders();
        
        // Populate the table
        for (Rider r : allRiders) 
        {
            Object[] row = new Object[] 
            {
                r.getRiderID(),
                r.getRiderName(),
                r.getHireDate(),
                r.getContactNo()
            };

            tableModel.addRow(row);
        }
    }
    
    // Show detailed information about the selected rider
    private void showRiderDetails() 
    {
        int selectedRow = riderTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a rider to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int riderID = (int) tableModel.getValueAt(selectedRow, 0);
        Rider rider = controller.getRiderById(riderID);
        
        if (rider != null) {
            JTextArea textArea = new JTextArea(rider.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Rider Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Search for a rider based on the selected criteria
    private void searchRider() 
    {
        // Clear the table
        searchTableModel.setRowCount(0);
        
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String query = searchField.getText().trim();
        
        List<Rider> results = new ArrayList<>();
                
        try
        {
            switch (searchType) 
            {
                case "By Rider ID":
                    int riderID = Integer.parseInt(query);
                    Rider r = controller.getRiderById(riderID);
                    if (r != null) results.add(r);
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Unknown search type.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Populate the table with search results
        for (Rider r : results) 
        {
            Object[] row = new Object[] 
            {
                r.getRiderID(),
                r.getRiderName(),
                r.getHireDate(),
                r.getContactNo()
            };

            searchTableModel.addRow(row);
        }
        
        if (results.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No riders found matching the search criteria.",
                                        "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Show detailed information about the selected search result.
    private void showSearchResultDetails() 
    {
        int selectedRow = searchResultTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a rider to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int riderID = (int) searchTableModel.getValueAt(selectedRow, 0);
        Rider rider = controller.getRiderById(riderID);
        
        if (rider != null) 
        {
            JTextArea textArea = new JTextArea(rider.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Rider Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
