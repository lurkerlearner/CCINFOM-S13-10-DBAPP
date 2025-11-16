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
    private JPanel addPanel, viewPanel, searchPanel;

    private JTextField streetField, cityField, zipField;
    private JButton addButton;

    private JTable locationTable;
    private DefaultTableModel tableModel;

    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    // Button to go back to main menu
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

        tabbedPane.addTab("Add Location", addPanel);
        tabbedPane.addTab("View All", viewPanel);
        tabbedPane.addTab("Search", searchPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void createAddPanel() {
        addPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5,5,5,5);

        streetField = new JTextField(20);
        cityField = new JTextField(15);
        zipField = new JTextField(10);

        int y = 0;
        gbc.gridx=0; gbc.gridy=y; addPanel.add(new JLabel("Street:"), gbc);
        gbc.gridx=1; addPanel.add(streetField, gbc); y++;

        gbc.gridx=0; gbc.gridy=y; addPanel.add(new JLabel("City:"), gbc);
        gbc.gridx=1; addPanel.add(cityField, gbc); y++;

        gbc.gridx=0; gbc.gridy=y; addPanel.add(new JLabel("ZIP:"), gbc);
        gbc.gridx=1; addPanel.add(zipField, gbc); y++;

        addButton = new JButton("Add Location");
        addButton.addActionListener(e -> addLocation());
        gbc.gridx=1; gbc.gridy=y+1; addPanel.add(addButton, gbc);

        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        gbc.gridx=0; gbc.gridy=y+1; addPanel.add(mainMenuButton, gbc);

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

    private void createViewPanel() {
        viewPanel = new JPanel(new BorderLayout());
        String[] columns = {"ID","Street","City","ZIP"};
        tableModel = new DefaultTableModel(columns,0) {
            @Override public boolean isCellEditable(int row, int col){return false;}
        };
        locationTable = new JTable(tableModel);
        viewPanel.add(new JScrollPane(locationTable), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshLocationTable());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshLocationTable();
    }

    private void refreshLocationTable() {
        tableModel.setRowCount(0);
        List<Location> locations = controller.getAllLocations();
        for(Location loc : locations) {
            tableModel.addRow(new Object[]{loc.getLocationId(), loc.getStreet(), loc.getCity(), loc.getZip()});
        }
    }

    private void createSearchPanel() {
        searchPanel = new JPanel(new BorderLayout());
        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchTypeComboBox = new JComboBox<>(new String[]{"ID","Street","City","ZIP"});
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchLocations());

        searchControls.add(new JLabel("Search by:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);

        searchPanel.add(searchControls, BorderLayout.NORTH);

        String[] columns = {"ID","Street","City","ZIP"};
        searchTableModel = new DefaultTableModel(columns,0){
            @Override public boolean isCellEditable(int row,int col){return false;}
        };
        searchResultTable = new JTable(searchTableModel);
        searchPanel.add(new JScrollPane(searchResultTable), BorderLayout.CENTER);
    }

    private void searchLocations() {
        String type = ((String)searchTypeComboBox.getSelectedItem()).toLowerCase();
        String value = searchField.getText().trim();

        List<Location> results = controller.searchLocations(type, value);
        searchTableModel.setRowCount(0);
        for(Location loc : results) {
            searchTableModel.addRow(new Object[]{loc.getLocationId(), loc.getStreet(), loc.getCity(), loc.getZip()});
        }

        if(results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No locations found.", "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

