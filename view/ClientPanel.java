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


    private JTextField nameField, contactField, unitField, locationIdField, planIdField, dietIdField;
    private JPasswordField passwordField;
    private JButton addClientBtn;
    private JButton refreshBtn;


    private JTable clientTable;
    private DefaultTableModel clientTableModel;


    private JComboBox<String> searchTypeDropdown;
    private JTextField searchField;
    private JTable searchTable;
    private DefaultTableModel searchTableModel;
    private JButton searchBtn;
    private JButton searchDetailsBtn;


    private JTabbedPane tabbedPane;

    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;

    // button to return to main menu
    private JButton mainMenuButton;

   public ClientPanel(ClientController controller){
       this.controller = controller;
       setLayout(new BorderLayout());
       initComponents();
   }

   private void initComponents(){
       tabbedPane = new JTabbedPane();

       createAddPanel();
       createViewPanel();
       createSearchPanel();

       tabbedPane.addTab("Add Client", addPanel);
       tabbedPane.addTab("View Clients", viewPanel);
       tabbedPane.addTab("Search Clients", searchPanel);

       add(tabbedPane, BorderLayout.CENTER);

   }

    private void createAddPanel() {
        addPanel = new JPanel();
        addPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        nameField = new JTextField(20);
        contactField = new JTextField(20);
        passwordField = new JPasswordField(20);
        unitField = new JTextField(20);
        locationIdField = new JTextField(5);
        planIdField = new JTextField(5);
        dietIdField = new JTextField(5);

        int row = 0;
        addField(formPanel, gbc, row++, "Name:", nameField);
        addField(formPanel, gbc, row++, "Contact No:", contactField);
        addField(formPanel, gbc, row++, "Password:", passwordField);
        addField(formPanel, gbc, row++, "Unit Details:", unitField);
        addField(formPanel, gbc, row++, "Location ID:", locationIdField);
        addField(formPanel, gbc, row++, "Plan ID:", planIdField);
        addField(formPanel, gbc, row++, "Diet Pref ID:", dietIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });

        addClientBtn = new JButton("Add Client");
        addClientBtn.addActionListener(e -> addClient());

        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addClientBtn);

        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createViewPanel() {
        viewPanel = new JPanel(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        clientTableModel = new DefaultTableModel(new String[]{
                "Client ID", "Name", "Contact", "Password", "Unit", "Date Created",
                "Location ID", "Plan ID", "Diet Pref ID"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        clientTable = new JTable(clientTableModel);
        clientTable.getTableHeader().setReorderingAllowed(false);
        refreshClientTable();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshClientTable());
        buttonPanel.add(refreshBtn);

        viewPanel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createSearchPanel() {
        searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchTypeDropdown = new JComboBox<>(new String[]{"By Name", "By ID", "By Contact No"});
        searchField = new JTextField(20);
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchClient());

        top.add(new JLabel("Search:"));
        top.add(searchTypeDropdown);
        top.add(searchField);
        top.add(searchBtn);


        searchTableModel = new DefaultTableModel(new String[]{
                "Client ID", "Name", "Contact", "Password", "Unit", "Date",
                "Location ID", "Plan ID", "Diet Pref ID"
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
                    c.getPassword(),
                    c.getUnitDetails(),
                    c.getDateCreated().format(formatter),
                    c.getLocationID(),
                    c.getPlanID(),
                    c.getDietPreferenceID()
            });
        }
    }

    private void showSearchDetails() {
        int row = searchTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client.");
            return;
        }

        String details =
                "Client ID: " + searchTableModel.getValueAt(row, 0) + "\n" +
                        "Name: "      + searchTableModel.getValueAt(row, 1) + "\n" +
                        "Contact: "   + searchTableModel.getValueAt(row, 2) + "\n" +
                        "Password: "  + searchTableModel.getValueAt(row, 3) + "\n" +
                        "Unit: "      + searchTableModel.getValueAt(row, 4) + "\n" +
                        "Date: "      + searchTableModel.getValueAt(row, 5) + "\n" +
                        "Location ID: " + searchTableModel.getValueAt(row, 6) + "\n" +
                        "Plan ID: "     + searchTableModel.getValueAt(row, 7) + "\n" +
                        "Diet Pref ID: "+ searchTableModel.getValueAt(row, 8) + "\n";

        JTextArea area = new JTextArea(details);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scroll, "Client Details",
                JOptionPane.INFORMATION_MESSAGE);
    }



    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
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

            boolean success = controller.addClient(name, contact, password, unit, planID, dietID, locationID);

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

    private void refreshClientTable() {
        clientTableModel.setRowCount(0);

        List<Client> clients = controller.getAllClients();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Client c : clients) {
            clientTableModel.addRow(new Object[]{
                    c.getClientID(),
                    c.getName(),
                    c.getContactNo(),
                    c.getPassword(),
                    c.getUnitDetails(),
                    c.getDateCreated().format(formatter),
                    c.getLocationID(),
                    c.getPlanID(),
                    c.getDietPreferenceID()
            });
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

}

