package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuPopularityPanel extends JPanel {

    private MealController mealController;
    
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

    public MenuPopularityPanel(MealController mealController)
    {
        this.mealController = mealController;
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

    private void createMonthlyReportPanel(){

        monthlyPanel = new JPanel();
        monthlyPanel.setLayout(new BorderLayout());
        monthlyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Monthly Report Parameters"));
        
        monthlyInputYear = new JTextField(10);
        monthlyInputMonth = new JTextField(5);

        inputPanel.add(new JLabel("Year: "));
        inputPanel.add(monthlyInputYear);
        inputPanel.add(Box.createHorizontalStrut(20));
        inputPanel.add(new JLabel("Month (1-12):"));
        inputPanel.add(monthlyInputMonth);

        String [] monthlyColumns = {"Meal ID", "Meal Name", "Times Ordered", "Total Revenue", "Distinct Locations"};

        monthlyTableModel = new DefaultTableModel(monthlyColumns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // all cells false
            }
        };

        monthlyReportTable = new JTable(monthlyTableModel);
        monthlyReportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        monthlyReportTable.getTableHeader().setReorderingAllowed(false);

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

    private void createAnnualReportPanel(){
        annualPanel = new JPanel(new BorderLayout());
        annualPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Enter Annual Report Parameters: "));   
        
        annualInputYear = new JTextField(10);

        inputPanel.add(new JLabel("Year: "));
        inputPanel.add(annualInputYear);

        String [] annualColumns = {"Meal ID", "Meal Name", "Times Ordered", "Total Revenue", "Distinct Locations"};

        annualTableModel = new DefaultTableModel(annualColumns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // all cells false
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


    private void generateMonthlyReport(){
        monthlyTableModel.setRowCount(0); // clear existing data

        try{
            int year = Integer.parseInt(monthlyInputYear.getText().trim());
            int month = Integer.parseInt(monthlyInputMonth.getText().trim());

            if(month < 1 || month > 12){
                JOptionPane.showMessageDialog(this, "Month must be between 1 and 12", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(year < 1900 || year > 2100){
                        JOptionPane.showMessageDialog(this,"Please enter a valid year.","Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;       
            }

            List<MealPerformance> rows = mealController.getMealPerformanceByMonthYear(year, month);

           if (rows.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No meal popularity data for " + getMonthName(month) + " " + year);
                return;
            }

            for (MealPerformance mp : rows) {
                monthlyTableModel.addRow(new Object[]{

                    mp.getMealId(),
                    mp.getMealName(),
                    mp.getTimesOrdered(),
                    mp.getTotalRevenue(),
                    mp.getDistinctLocations()
                });
            }

            JOptionPane.showMessageDialog(this,
                    "Monthly meal popularity report generated successfully!",
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

    private void generateAnnualReport(){
        annualTableModel.setRowCount(0);
        try{

            int year = Integer.parseInt(annualInputYear.getText().trim());
            if(year < 1900 || year > 2100){
                JOptionPane.showMessageDialog(this, "Enter a valid year");
                return;
            }
        List<MealPerformance> rows = mealController.getMealPerformanceByYear(year);
            
        for (MealPerformance mp : rows) {
                annualTableModel.addRow(new Object[]{

                    mp.getMealId(),
                    mp.getMealName(),
                    mp.getTimesOrdered(),
                    mp.getTotalRevenue(),
                    mp.getDistinctLocations()
                });
            }
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Please enter a valid year.");            
        } catch (Exception e){
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
