package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientPanel extends JPanel {

    private ClientController controller;

    // Fields for Add Client
    private JTextField nameField, contactField, unitField, locationIdField, planIdField, dietIdField;
    private JPasswordField passwordField;
    private JButton addClientBtn;

    // Table and models
    private JTable clientTable;
    private DefaultTableModel clientTableModel;

    // Search
    private JComboBox<String> searchTypeDropdown;
    private JTextField searchField;
    private JTable searchTable;
    private DefaultTableModel searchTableModel;
    private JButton searchBtn;

    private JTabbedPane tabbedPane;

    // button to return to main menu
    private JButton mainMenuButton;

    public ClientPanel(ClientController clientController) {
        this.controller = clientController; // correctly assign passed controller

        setLayout(new BorderLayout());
        initComponents();
        createTabs();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void createTabs() {
        tabbedPane.addTab("Add Client", createAddPanel());
        tabbedPane.addTab("View Clients", createViewPanel());
        tabbedPane.addTab("Search Clients", createSearchPanel());
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField();
        contactField = new JTextField();
        passwordField = new JPasswordField();
        unitField = new JTextField();
        locationIdField = new JTextField();
        planIdField = new JTextField();
        dietIdField = new JTextField();
        addClientBtn = new JButton("Add Client");
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });

        int row = 0;
        addLabelAndField(panel, gbc, row++, "Name:", nameField);
        addLabelAndField(panel, gbc, row++, "Contact No:", contactField);
        addLabelAndField(panel, gbc, row++, "Password:", passwordField);
        addLabelAndField(panel, gbc, row++, "Unit Details:", unitField);
        addLabelAndField(panel, gbc, row++, "Location ID:", locationIdField);
        addLabelAndField(panel, gbc, row++, "Plan ID:", planIdField);
        addLabelAndField(panel, gbc, row++, "Diet Pref ID:", dietIdField);

        gbc.gridx = 1;
        gbc.gridy = row;
        panel.add(addClientBtn, gbc);
        gbc.gridx = 0;
        panel.add(mainMenuButton, gbc);

        addClientBtn.addActionListener(e -> addClient());

        return panel;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addClient() {
        try {
            String name = nameField.getText();
            String contact = contactField.getText();
            String password = new String(passwordField.getPassword());
            String unit = unitField.getText();
            int locationID = Integer.parseInt(locationIdField.getText());
            int planID = Integer.parseInt(planIdField.getText());
            int dietID = Integer.parseInt(dietIdField.getText());

            boolean success = controller.addClient(name, contact, password, planID, dietID, locationID);

            if (success) {
                JOptionPane.showMessageDialog(this, "Client added successfully!");
                clearAddFields();
                refreshClientTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add client.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
        }
    }

    private void clearAddFields() {
        nameField.setText("");
        contactField.setText("");
        passwordField.setText("");
        unitField.setText("");
        locationIdField.setText("");
        planIdField.setText("");
        dietIdField.setText("");
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        clientTableModel = new DefaultTableModel(new String[]{
                "Client ID", "Name", "Contact", "Unit", "Date Created",
                "Location ID", "Plan ID", "Diet Pref ID"
        }, 0);

        clientTable = new JTable(clientTableModel);
        refreshClientTable();

        panel.add(new JScrollPane(clientTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshClientTable());
        bottom.add(refreshBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshClientTable() {
        clientTableModel.setRowCount(0);

        List<Client> clients = controller.getAllClients();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Client c : clients) {
            clientTableModel.addRow(new Object[]{
                    c.getClientID(),
                    c.getName(),
                    c.getContactNo(),
                    c.getUnitDetails(),
                    c.getDateCreated().format(formatter),
                    c.getLocationID(),
                    c.getPlanID(),
                    c.getDietPreferenceID()
            });
        }
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel();
        searchTypeDropdown = new JComboBox<>(new String[]{"By Name", "By ID", "By Contact No"});
        searchField = new JTextField(20);
        searchBtn = new JButton("Search");

        top.add(searchTypeDropdown);
        top.add(searchField);
        top.add(searchBtn);

        panel.add(top, BorderLayout.NORTH);

        searchTableModel = new DefaultTableModel(new String[]{
                "Client ID", "Name", "Contact", "Unit", "Date",
                "Location ID", "Plan ID", "Diet Pref ID"
        }, 0);

        searchTable = new JTable(searchTableModel);
        panel.add(new JScrollPane(searchTable), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> searchClient());

        return panel;
    }

    private void searchClient() {
        searchTableModel.setRowCount(0);

        String query = searchField.getText().trim();
        List<Client> results;

        switch (searchTypeDropdown.getSelectedIndex()) {
            case 0 -> results = controller.searchClientsByName(query);
            case 1 -> results = controller.searchClientsById(query);
            case 2 -> results = controller.searchClientsByContact(query);
            default -> { return; }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Client c : results) {
            searchTableModel.addRow(new Object[]{
                    c.getClientID(),
                    c.getName(),
                    c.getContactNo(),
                    c.getUnitDetails(),
                    c.getDateCreated().format(formatter),
                    c.getLocationID(),
                    c.getPlanID(),
                    c.getDietPreferenceID()
            });
        }
    }
}

