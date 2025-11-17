package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientEngagementPanel extends JPanel {

    private ClientEngagementController controller;

    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel monthlyPanel;
    private JPanel annualPanel;
    private JPanel weeklyPanel;

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

    public ClientEngagementPanel(ClientEngagementController controller)
    {
        this.controller = controller;
        setLayout(new BorderLayout());
        initComponents();
    }

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

        String[] monthlyColumns = {
                "Client ID", "Client Name",
                "Week 1 Orders", "Week 2 Orders", "Week 3 Orders", "Week 4 Orders", "Week 5 Orders",
                "Most Ordered Meal"
        };

        monthlyTableModel = new DefaultTableModel(monthlyColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
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


        monthlyPanel.add(inputPanel, BorderLayout.NORTH);
        monthlyPanel.add(new JScrollPane(monthlyReportTable), BorderLayout.CENTER);
        monthlyPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createAnnualReportPanel() {

        annualPanel = new JPanel(new BorderLayout());
        annualPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Annual Report Parameters"));

        annualInputYear = new JTextField(10);

        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(annualInputYear);


        String[] annualColumns = {
                "Client ID", "Client Name",
                "Total Annual Orders",
                "Most Ordered Meal"
        };

        annualTableModel = new DefaultTableModel(annualColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        annualReportTable = new JTable(annualTableModel);
        annualReportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        annualReportTable.getTableHeader().setReorderingAllowed(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });

        annualGenerateButton = new JButton("Generate Annual Report");
        annualGenerateButton.addActionListener(e -> generateAnnualReport());

        buttonPanel.add(mainMenuButton);
        buttonPanel.add(annualGenerateButton);

        annualPanel.add(inputPanel, BorderLayout.NORTH);
        annualPanel.add(new JScrollPane(annualReportTable), BorderLayout.CENTER);
        annualPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void generateMonthlyReport() {
        monthlyTableModel.setRowCount(0); // Clear table

        try {
            int year = Integer.parseInt(monthlyInputYear.getText().trim());
            int month = Integer.parseInt(monthlyInputMonth.getText().trim());

            // Validate year
            if (year < 1900 || year > 2100) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid year.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate month
            if (month < 1 || month > 12) {
                JOptionPane.showMessageDialog(this,
                        "Month must be between 1 and 12.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Object[]> rows = controller.getMonthlyReport(month, year);

            if (rows.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No engagement data for " + getMonthName(month) + " " + year);
                return;
            }

            for (Object[] r : rows) {
                monthlyTableModel.addRow(r);
            }

            JOptionPane.showMessageDialog(this,
                    "Monthly client engagement report generated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for year and month.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "An error occurred while generating the report: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void generateAnnualReport() {
        annualTableModel.setRowCount(0); // Clear previous data
        try {
            int year = Integer.parseInt(annualInputYear.getText().trim());
            if (year < 1900 || year > 2100) {
                JOptionPane.showMessageDialog(this, "Enter a valid year.");
                return;
            }

            List<Object[]> rows = controller.getAnnualReport(year);
            for (Object[] r : rows) {
                annualTableModel.addRow(r);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid year.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return (month >= 1 && month <= 12) ? monthNames[month - 1] : String.valueOf(month);
    }



}
