package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ClientPanel extends JPanel {

    private ClientController clientController;
    private MealPlanController mealPlanController = new MealPlanController();
    private DietPreferenceController dietController = new DietPreferenceController();


    private JTextField nameField, contactField, unitField, locationIdField;
    private JPasswordField passwordField;
    private JComboBox<MealPlan> mealPlanDropdown;
    private JList<DietPreference> dietList;
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
    private JButton deleteBtn;

    private JTabbedPane tabbedPane;

    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    private JPanel editPanel;

    // button to return to main menu
    private JButton mainMenuButton;

   public ClientPanel(ClientController controller){
       this.clientController = controller;
       setLayout(new BorderLayout());
       initComponents();
   }

   private void initComponents(){
       tabbedPane = new JTabbedPane();

       createAddPanel();
       createViewPanel();
       createSearchPanel();
       createEditPanel();

       tabbedPane.addTab("Add Client", addPanel);
       tabbedPane.addTab("View Clients", viewPanel);
       tabbedPane.addTab("Search Clients", searchPanel);
       tabbedPane.addTab("Edit Clients", editPanel);

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

        mealPlanDropdown = new JComboBox<>();
        for (MealPlan mp : mealPlanController.getAllMealPlans()) {
            mealPlanDropdown.addItem(mp);
        }

        dietList = new JList<>(
                dietController.getAvailableDietPreferences().toArray(new DietPreference[0])
        );
        dietList.setVisibleRowCount(5);
        dietList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);


        int row = 0;
        addField(formPanel, gbc, row++, "Name:", nameField);
        addField(formPanel, gbc, row++, "Contact No:", contactField);
        addField(formPanel, gbc, row++, "Password:", passwordField);
        addField(formPanel, gbc, row++, "Unit Details:", unitField);
        addField(formPanel, gbc, row++, "Location ID:", locationIdField);
        addField(formPanel, gbc, row++, "Meal Plan:", mealPlanDropdown);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Diet Preferences:"), gbc);

        gbc.gridx = 1;
        JScrollPane dietScroll = new JScrollPane(dietList);
        dietScroll.setPreferredSize(new Dimension(200, 100));
        formPanel.add(dietScroll, gbc);
        row++;


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
                "Location ID", "Plan ID"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        clientTable = new JTable(clientTableModel);
        clientTable.getTableHeader().setReorderingAllowed(false);
        refreshClientTable();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshClientTable());
        deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteClient());
        buttonPanel.add(refreshBtn);
        buttonPanel.add(deleteBtn);



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
                "Location ID", "Plan ID"
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

    private void searchClient() {
        searchTableModel.setRowCount(0);

        String query = searchField.getText().trim();
        List<Client> results;

        switch (searchTypeDropdown.getSelectedIndex()) {
            case 0 -> results = clientController.searchClientsByName(query);
            case 1 -> results = clientController.searchClientsById(query);
            case 2 -> results = clientController.searchClientsByContact(query);
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
                        "Plan ID: "     + searchTableModel.getValueAt(row, 7) + "\n";

        JTextArea area = new JTextArea(details);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scroll, "Client Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteClient() {
        int selectedRow = clientTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client to delete.",
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected client? This may affect related records (e.g., deliveries, diet preferences).",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int clientIdToDelete = (int) clientTableModel.getValueAt(selectedRow, 0);

                if (clientController.deleteClient(clientIdToDelete)) {
                    JOptionPane.showMessageDialog(this, "Client deleted successfully.");
                    refreshClientTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to delete client. Check database constraints.",
                            "Deletion Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "An error occurred during deletion: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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

            MealPlan selectedPlan = (MealPlan) mealPlanDropdown.getSelectedItem();
            int planID = selectedPlan.getPlan_id();

            List<Integer> dietIDs = dietList.getSelectedValuesList()
                    .stream()
                    .map(DietPreference::getDiet_preference_id)
                    .collect(Collectors.toList());

            boolean success = clientController.addClient(name, contact, password, unit, planID, dietIDs, locationID);

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

        List<Client> clients = clientController.getAllClients();
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
            });
        }

    }

    private void clearAddFields() {
        nameField.setText("");
        contactField.setText("");
        passwordField.setText("");
        unitField.setText("");
        locationIdField.setText("");
        dietList.clearSelection();
        mealPlanDropdown.setSelectedIndex(0);
    }

}

