package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.*;

public class FloodImpactReportPanel extends JPanel 
{
    private FloodDataController fdc;

    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel quarterlyPanel;
    private JPanel annualPanel;

    // Components for quarterly report
    private JTable quarterlyReportTable;
    private DefaultTableModel quarterlyTableModel;
    private JTextField qtrInputYear;
    private JTextField qtrInputQuarter;
    private JButton quarterlyGenerateButton;

    // Components for annual report
    private JTable annualReportTable;
    private DefaultTableModel annualTableModel;
    private JTextField annualInputYear;
    private JButton annualGenerateButton;

    // Button to go back to main menu
    private JButton mainMenuButton;

    public FloodImpactReportPanel(FloodDataController controller)
    {
        this.fdc = controller;
        setLayout(new BorderLayout());
        initComponents();
    }

    // Initialize all GUI components
    private void initComponents() 
    {
        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create panels for each report type
        createMonthlyReportPanel();
        createAnnualReportPanel();
        
        tabbedPane.addTab("Quarterly Report", quarterlyPanel);
        tabbedPane.addTab("Annual Report", annualPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    // Create the panel for quarterly flood impact reports
    private void createMonthlyReportPanel()
    {
        quarterlyPanel = new JPanel();
        quarterlyPanel.setLayout(new BorderLayout());
        quarterlyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input panel for quarterly report
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Quarterly Report Parameters"));

        qtrInputYear = new JTextField(10);
        qtrInputQuarter = new JTextField(5);

        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(qtrInputYear);
        inputPanel.add(Box.createHorizontalStrut(20));
        inputPanel.add(new JLabel("Quarter (1-4):"));
        inputPanel.add(qtrInputQuarter);

        // Table for quarterly report
        String[] quarterlyColumns = 
        {
            "Client ID", "Name", "Street", "City", "Flood Severity", 
            "Average Water Level", "Sales"
        };

        quarterlyTableModel = new DefaultTableModel(quarterlyColumns, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        quarterlyReportTable = new JTable(quarterlyTableModel);
        quarterlyReportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        quarterlyReportTable.getTableHeader().setReorderingAllowed(false);

        // Button panel for quarterly report
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> 
        {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        quarterlyGenerateButton = new JButton("Generate Quarterly Report");
        quarterlyGenerateButton.addActionListener(e -> generateQuarterlyReport());

        buttonPanel.add(mainMenuButton);
        buttonPanel.add(quarterlyGenerateButton);
        
        // Add components to monthly panel
        quarterlyPanel.add(inputPanel, BorderLayout.NORTH);
        quarterlyPanel.add(new JScrollPane(quarterlyReportTable), BorderLayout.CENTER);
        quarterlyPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Create the panel for annual flood impact reports
    private void createAnnualReportPanel()
    {
        annualPanel = new JPanel();
        annualPanel.setLayout(new BorderLayout());
        annualPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input panel for annual report
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Annual Report Parameters"));

        annualInputYear = new JTextField(10);

        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(annualInputYear);

        // Table for annual report
        String[] annualColumns = 
        {
            "Client ID", "Name", "Street", "City", "Flood Severity", 
            "Average Water Level", "Sales"
        };

        annualTableModel = new DefaultTableModel(annualColumns, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        annualReportTable = new JTable(annualTableModel);
        annualReportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        annualReportTable.getTableHeader().setReorderingAllowed(false);

        // Button panel for annual report
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        annualGenerateButton = new JButton("Generate Annual Report");
        annualGenerateButton.addActionListener(e -> generateAnnualReport());

        buttonPanel.add(mainMenuButton);
        buttonPanel.add(annualGenerateButton);
        
        // Add components to annual panel
        annualPanel.add(inputPanel, BorderLayout.NORTH);
        annualPanel.add(new JScrollPane(annualReportTable), BorderLayout.CENTER);
        annualPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Generate quarterly flood impact report
    private void generateQuarterlyReport() 
    {
        quarterlyTableModel.setRowCount(0); // Clear table

        try 
        {
            int year = Integer.parseInt(qtrInputYear.getText().trim());
            int quarter = Integer.parseInt(qtrInputQuarter.getText().trim());

            // Validate year
            if (year < 1900 || year > 2100) 
            {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid year.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate quarter
            if (quarter < 1 || quarter > 4) 
            {
                JOptionPane.showMessageDialog(this, 
                    "Quarter must only be a value from 1 to 4.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get quarterly flood impact report
            ArrayList<FloodImpactReport> report = fdc.getQuarterlyReport(year, quarter);
            
            if (report.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, 
                    "No flood data found for " + getQuarter(quarter) + " of " + year, 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Populate table with quarterly report data
            for (FloodImpactReport fir : report) 
            {
                Object[] row = new Object[]
                {
                    fir.getClientID(),
                    fir.getName(),
                    fir.getStreet(),
                    fir.getCity(),
                    fir.getRiskByFactor(),
                    fir.getRiskByAWL(),
                    fir.getSales()
                };
                quarterlyTableModel.addRow(row);
            }

            JOptionPane.showMessageDialog(this, 
                "Quarterly flood impact report generated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);

        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers for year and quarter.", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, 
                "An error occurred while generating the report: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Generate annual flood impact report
    private void generateAnnualReport() 
    {
        annualTableModel.setRowCount(0); // Clear table

        try 
        {
            int year = Integer.parseInt(annualInputYear.getText().trim());

            // Validate year
            if (year < 1900 || year > 2100) 
            {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid year.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get annual flood impact report
            ArrayList<FloodImpactReport> report = fdc.getAnnualReport(year);
            
            if (report.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, 
                    "No flood data found for year " + year, 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Populate table with annual report data
            for (FloodImpactReport fir : report) 
            {
                Object[] row = new Object[]
                {
                    fir.getClientID(),
                    fir.getName(),
                    fir.getStreet(),
                    fir.getCity(),
                    fir.getRiskByFactor(),
                    fir.getRiskByAWL(),
                    fir.getSales()
                };
                annualTableModel.addRow(row);
            }

            JOptionPane.showMessageDialog(this, 
                "Annual flood impact report generated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);

        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number for year.", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, 
                "An error occurred while generating the report: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Helper method to convert quarter number to its included months
    private String getQuarter(int q) 
    {
        String[] quarters = 
        {
            "January to March", "April to June",
            "July to September", "October to December"
        };
        
        return quarters[q - 1];
    }
}