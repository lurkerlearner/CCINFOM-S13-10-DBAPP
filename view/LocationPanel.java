package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LocationPanel extends JPanel {

    private LocationController controller;

    private JTabbedPane tabbedPane;
    private JPanel addPanel, viewPanel, searchPanel, editPanel;

    private JTextField streetField, cityField, zipField;
    private JButton addLocationBtn;
    private JButton refreshBtn;

    private JTable locationTable;
    private DefaultTableModel locationTableModel;

    private JComboBox<String> searchTypeDropdown;
    private JTextField searchField;
    private JTable searchTable;
    private DefaultTableModel searchTableModel;
    private JButton searchBtn;
    private JButton searchDetailsBtn;

    private JButton mainMenuButton;

    public LocationPanel(LocationController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        createAddPanel();
        createViewPanel();
        createSearchPanel();
        createEditPanel();

        tabbedPane.addTab("Add Location", addPanel);
        tabbedPane.addTab("View Locations", viewPanel);
        tabbedPane.addTab("Search Locations", searchPanel);
        tabbedPane.addTab("Edit Locations", editPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void createAddPanel() {
        addPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        streetField = new JTextField(20);
        cityField = new JTextField(20);
        zipField = new JTextField(20);

        int row = 0;
        addField(formPanel, gbc, row++, "Street:", streetField);
        addField(formPanel, gbc, row++, "City:", cityField);
        addField(formPanel, gbc, row++, "ZIP Code:", zipField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });

        addLocationBtn = new JButton("Add Location");
        addLocationBtn.addActionListener(e -> addLocation());

        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addLocationBtn);

        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void createViewPanel() {
        viewPanel = new JPanel(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        locationTableModel = new DefaultTableModel(new String[]{
                "Location ID", "Street", "City", "ZIP"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        locationTable = new JTable(locationTableModel);
        locationTable.getTableHeader().setReorderingAllowed(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshLocationTable());
        buttonPanel.add(refreshBtn);

        viewPanel.add(new JScrollPane(locationTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshLocationTable();
    }

    private void createSearchPanel() {
        searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchTypeDropdown = new JComboBox<>(new String[]{"By ID", "By Street", "By City", "By ZIP"});
        searchField = new JTextField(20);
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchLocations());

        top.add(new JLabel("Search:"));
        top.add(searchTypeDropdown);
        top.add(searchField);
        top.add(searchBtn);

        searchTableModel = new DefaultTableModel(new String[]{
                "Location ID", "Street", "City", "ZIP"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        searchTable = new JTable(searchTableModel);
        searchTable.getTableHeader().setReorderingAllowed(false);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchDetailsBtn = new JButton("View Details");
        searchDetailsBtn.addActionListener(e -> showSearchDetails());
        bottom.add(searchDetailsBtn);

        searchPanel.add(top, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchTable), BorderLayout.CENTER);
        searchPanel.add(bottom, BorderLayout.SOUTH);
    }

    private void createEditPanel(){
        editPanel = new JPanel(new BorderLayout());
        editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //todo:IMPLEMENT EDIT
    }

    private void addLocation() {
        String street = streetField.getText().trim();
        String city = cityField.getText().trim();
        String zip = zipField.getText().trim();

        if(controller.addLocation(street, city, zip)) {
            JOptionPane.showMessageDialog(this, "Location added successfully!");
            clearAddFields();
            refreshLocationTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add location.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearAddFields() {
        streetField.setText("");
        cityField.setText("");
        zipField.setText("");
    }

    private void refreshLocationTable() {
        locationTableModel.setRowCount(0);
        List<Location> locations = controller.getAllLocations();
        for(Location loc : locations) {
            locationTableModel.addRow(new Object[]{loc.getLocationId(), loc.getStreet(), loc.getCity(), loc.getZip()});
        }
    }

    private void searchLocations() {
        String type = searchTypeDropdown.getSelectedItem().toString().toLowerCase();
        String query = searchField.getText().trim();

        List<Location> results;
        searchTableModel.setRowCount(0);

        switch (searchTypeDropdown.getSelectedIndex()) {
            case 0 -> results = controller.searchLocationById(query);
            case 1 -> results = controller.searchLocationByStreet(query);
            case 2 -> results = controller.searchLocationByCity(query);
            case 3 -> results = controller.searchLocationByZip(query);
            default -> {return;}
        }

        for(Location loc : results) {
            searchTableModel.addRow(new Object[]{
                    loc.getLocationId(),
                    loc.getStreet(),
                    loc.getCity(),
                    loc.getZip()});
        }

    }

    private void showSearchDetails() {
        int row = searchTable.getSelectedRow();
        if(row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a location.");
            return;
        }

        String details =
                "Location ID: " + searchTableModel.getValueAt(row, 0) + "\n" +
                        "Street: " + searchTableModel.getValueAt(row, 1) + "\n" +
                        "City: " + searchTableModel.getValueAt(row, 2) + "\n" +
                        "ZIP: " + searchTableModel.getValueAt(row, 3);

        JTextArea area = new JTextArea(details);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scroll, "Location Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
