
package view;

import controller.DietPreferenceController;
import model.DietPreference;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DietPreferencePanel extends JPanel 
{
    private DietPreferenceController controller;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    
    // Components for adding Diet Preferences
    private JTextField dietID; 
    private JTextField dietNameField;
    private JTextField descriptionField;
    private JButton addButton;
    
    // Components for viewing Diet Preferences
    private JTable dietTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailsButton;
    private JButton deleteButton; 
    
    // Components for searching Diet Preferences
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    public DietPreferencePanel(DietPreferenceController controller) 
    {
        this.controller = controller;
        
        setLayout(new BorderLayout());
        
        initComponents();
    }
    
    // Initialize all GUI components
    private void initComponents() 
    {
        tabbedPane = new JTabbedPane();
        
        createAddPanel();
        createViewPanel();
        createSearchPanel();
        
        tabbedPane.addTab("Add Diet Preference", addPanel);
        tabbedPane.addTab("View All / Modify", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
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
        
        // Fields based on DietPreference POJO
        dietID = new JTextField(5); 
        dietID.setEditable(false); 
        dietNameField = new JTextField(25); 
        descriptionField = new JTextField(35); 
        
        int row = 0;
        

        gbc.gridx = 0; gbc.gridy = row++;
        formPanel.add(new JLabel("Diet ID (Auto-Generated):"), gbc);
        gbc.gridx = 1;
        formPanel.add(dietID, gbc);

 
        gbc.gridx = 0; gbc.gridy = row++;
        formPanel.add(new JLabel("Diet Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dietNameField, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Add Diet Preference");
        addButton.addActionListener(e -> addDietPreference());
        
        buttonPanel.add(addButton);
        
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createViewPanel() 
    {
        viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        

        String[] columnNames = {"ID", "Diet Name", "Description"};

        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {

                return column > 0; 
            }
        };

        dietTable = new JTable(tableModel);
        dietTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dietTable.getTableHeader().setReorderingAllowed(false);
        

        tableModel.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                updateDietPreferenceFromTable(row);
            }
        });
        

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshDietTable());
        
        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showDietDetails(dietTable, tableModel));
        
        deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteDietPreference());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        buttonPanel.add(deleteButton);
        
        viewPanel.add(new JScrollPane(dietTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshDietTable();
    }
    

    private void createSearchPanel() 
    {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        

        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        searchTypeComboBox = new JComboBox<>(new String[]{"By ID", "By Name"});
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchDietPreference());
        
        searchControls.add(new JLabel("Search By:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);
        
        // Create table
        String[] columnNames = {"ID", "Diet Name", "Description"};

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
        
 
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JButton detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showDietDetails(searchResultTable, searchTableModel));
        
        buttonPanel.add(detailsButton);

        searchPanel.add(searchControls, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResultTable), BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    

    
    private void addDietPreference() 
    {
        try 
        {
            String name = dietNameField.getText().trim();
            String description = descriptionField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Diet Name cannot be empty.",
                                             "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DietPreference newPref = new DietPreference(0, name, description);

            if (controller.createNewDietPreference(newPref)) {
                JOptionPane.showMessageDialog(this, "Diet Preference added successfully!");
                clearAddFields();
                refreshDietTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add diet preference. It might already exist.",
                                             "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(),
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDietPreferenceFromTable(int row) 
    {
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            String newName = tableModel.getValueAt(row, 1).toString().trim();
            String newDescription = tableModel.getValueAt(row, 2).toString().trim();
            
            DietPreference updatedPref = new DietPreference(id, newName, newDescription);
            
            if (controller.updateDietPreference(updatedPref)) {
               
            } else {
                
                JOptionPane.showMessageDialog(this, "Update failed. Check if Diet Name is unique and not empty.",
                                             "Update Error", JOptionPane.ERROR_MESSAGE);
              
                refreshDietTable();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid data or update error: " + e.getMessage(),
                                         "Error", JOptionPane.ERROR_MESSAGE);
            refreshDietTable();
        }
    }

    private void deleteDietPreference() {
        int selectedRow = dietTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a diet preference to delete.",
                                         "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the selected diet preference?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idToDelete = (int) tableModel.getValueAt(selectedRow, 0);
                
                if (controller.deleteDietPreference(idToDelete)) {
                    JOptionPane.showMessageDialog(this, "Diet Preference deleted successfully.");
                    refreshDietTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete diet preference. It might be referenced by a meal.",
                                                 "Deletion Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error occurred during deletion: " + e.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearAddFields()
    {
        dietNameField.setText("");
        descriptionField.setText("");
    }
    
    private void refreshDietTable() 
    {
        tableModel.setRowCount(0);
        
      
        List<DietPreference> allDiets = controller.getAvailableDietPreferences();
        
        for (DietPreference dp : allDiets) 
        {
            Object[] row = new Object[] 
            {
                dp.getDiet_preference_id(), 
                dp.getDiet_name(), 
                dp.getDescription()
            };

            tableModel.addRow(row);
        }
    }
    
    private void showDietDetails(JTable table, DefaultTableModel model) 
    {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a diet preference to view details.",
                                         "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int dietID = (int) model.getValueAt(selectedRow, 0);
        
     
        DietPreference diet = controller.getPreferenceDetails(dietID); 
        
        if (diet != null) {
            String details = String.format("ID: %d\nName: %s\nDescription: %s",
                diet.getDiet_preference_id(),
                diet.getDiet_name(),
                diet.getDescription());

            JTextArea textArea = new JTextArea(details);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Diet Preference Details", 
                                         JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void searchDietPreference() 
    {
        searchTableModel.setRowCount(0);
        
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String query = searchField.getText().trim();
        
        if (query.isEmpty() && !searchType.equals("By ID")) {
            JOptionPane.showMessageDialog(this, "Please enter a search query.", 
                                         "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<DietPreference> results = new ArrayList<>();
        DietPreference singleResult = null;
        
        try
        {
            switch (searchType) 
            {
                case "By ID":
                    if (!query.isEmpty()) {
                        int id = Integer.parseInt(query);
                        singleResult = controller.getPreferenceDetails(id);
                    }
                    break;

                case "By Name":
                    singleResult = controller.getPreferenceByName(query);
                    break;

                default:
                    return; 
            }
            

            if (singleResult != null) {
                 results.add(singleResult);
            }
        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", 
                                         "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        catch (Exception e) 
        {
             JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        for (DietPreference dp : results) 
        {
            Object[] row = new Object[] 
            {
                dp.getDiet_preference_id(), 
                dp.getDiet_name(), 
                dp.getDescription()
            };

            searchTableModel.addRow(row);
        }
        
        if (results.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No diet preferences found matching the search criteria.",
                                         "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}