package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.*;

public class SalesReportPanel extends JPanel {
    private DeliveryController controller;

    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel monthlyPanel;
    private JPanel annualPanel;

    // Components for monthly report
    private JTable monthlyReportTable;
    private DefaultTableModel monthlyTableModel;
    private JTextField monthlyInputYear;
    private JTextField monthlyInputMonth;
    private JButton monthlyGenerateButton;

    // Components for annual report
    private JTable annualReportTable;
    private DefaultTableModel annualTableModel;
    private JTextField annualInputYear;
    private JButton annualGenerateButton;

    // Button to go back to main menu
    private JButton mainMenuButton;

    public SalesReportPanel(DeliveryController deliveryController)
    {
        this.controller = deliveryController;
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
        
        tabbedPane.addTab("Monthly Report", monthlyPanel);
        tabbedPane.addTab("Annual Report", annualPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    // Create the panel for monthly sales reports
    private void createMonthlyReportPanel()
    {
        monthlyPanel = new JPanel();
        monthlyPanel.setLayout(new BorderLayout());
        monthlyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input panel for monthly report
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Monthly Report Parameters"));

        monthlyInputYear = new JTextField(10);
        monthlyInputMonth = new JTextField(5);

        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(monthlyInputYear);
        inputPanel.add(Box.createHorizontalStrut(20));
        inputPanel.add(new JLabel("Month (1-12):"));
        inputPanel.add(monthlyInputMonth);

        // Table for monthly report
        String[] monthlyColumns = {
            "Year", "Month", "Sales Made", "Gross Income", "Net Profit"
        };

        monthlyTableModel = new DefaultTableModel(monthlyColumns, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        monthlyReportTable = new JTable(monthlyTableModel);
        monthlyReportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        monthlyReportTable.getTableHeader().setReorderingAllowed(false);

        // Button panel for monthly report
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        monthlyGenerateButton = new JButton("Generate Monthly Report");
        monthlyGenerateButton.addActionListener(e -> generateMonthlyReport());

        buttonPanel.add(mainMenuButton);
        buttonPanel.add(monthlyGenerateButton);
        
        // Add components to monthly panel
        monthlyPanel.add(inputPanel, BorderLayout.NORTH);
        monthlyPanel.add(new JScrollPane(monthlyReportTable), BorderLayout.CENTER);
        monthlyPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Create the panel for annual sales reports
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

        // Table for annual report (no month column)
        String[] annualColumns = {
            "Year", "Sales Made", "Gross Income", "Net Profit"
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

    // Generate monthly sales report
    private void generateMonthlyReport() 
    {
        monthlyTableModel.setRowCount(0); // Clear table

        try 
        {
            int year = Integer.parseInt(monthlyInputYear.getText().trim());
            int month = Integer.parseInt(monthlyInputMonth.getText().trim());

            // Validate year
            if (year < 1900 || year > 2100) 
            {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid year.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate month
            if (month < 1 || month > 12) 
            {
                JOptionPane.showMessageDialog(this, 
                    "Month must be between 1 and 12.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get monthly sales report
            ArrayList<SalesReport> salesRecords = controller.generateSalesReportByMonthYear(year, month);
            
            if (salesRecords.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, 
                    "No sales data found for " + getMonthName(month) + " " + year, 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Populate table with monthly report data
            for (SalesReport s : salesRecords) 
            {
                Object[] row = new Object[]
                {
                    s.getYear(),
                    getMonthName(s.getMonth()),
                    s.getSales_made(),
                    String.format("%.2f", s.getGross_income()),
                    String.format("%.2f", s.getNet_profit())
                };
                monthlyTableModel.addRow(row);
            }

            JOptionPane.showMessageDialog(this, 
                "Monthly sales report generated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);

        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers for year and month.", 
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

    // Generate annual sales report
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

            // Get annual sales report
            ArrayList<SalesReport> salesRecords = controller.generateSalesReportByYear(year);
            
            if (salesRecords.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, 
                    "No sales data found for year " + year, 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Populate table with annual report data
            for (SalesReport s : salesRecords) 
            {
                Object[] row = new Object[]
                {
                    s.getYear(),
                    s.getSales_made(),
                    String.format("%.2f", s.getGross_income()),
                    String.format("%.2f", s.getNet_profit())
                };
                annualTableModel.addRow(row);
            }

            JOptionPane.showMessageDialog(this, 
                "Annual sales report generated successfully!", 
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

    // Helper method to convert month number to month name
    private String getMonthName(int month) 
    {
        String[] monthNames = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        
        if (month >= 1 && month <= 12) 
        {
            return monthNames[month - 1];
        }
        return String.valueOf(month);
    }
}