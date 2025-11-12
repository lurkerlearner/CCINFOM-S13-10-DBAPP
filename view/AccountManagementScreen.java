package view;

import DAO.*;
import model.*;
import controller.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccountManagementScreen extends JFrame {

    private final Client client;
    private final ClientDAO clientDAO = new ClientDAO();
    private final LocationDAO locationDAO = new LocationDAO();

    private final JTextField nameField;
    private final JTextField contactField;
    private final JTextField unitField;
    private final JComboBox<Location> locationDropdown;

    private JButton editBtn;
    private JButton saveBtn;
    private JButton exitBtn;

    public AccountManagementScreen(Client client) {
        this.client = client;

        setTitle("FloodPanda - Account Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JLabel logo = new JLabel("FloodPanda", SwingConstants.RIGHT);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(new Color(220, 31, 127));
        topPanel.add(logo, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;


        nameField = new JTextField(client.getName());
        contactField = new JTextField(client.getContactNo());
        unitField = new JTextField(client.getUnitDetails());

        nameField.setEnabled(false);
        contactField.setEnabled(false);
        unitField.setEnabled(false);

        List<Location> locations = locationDAO.getAllLocations();
        locationDropdown = new JComboBox<>();
        for (Location l : locations) locationDropdown.addItem(l);
        for (int i = 0; i < locationDropdown.getItemCount(); i++) {
            if (locationDropdown.getItemAt(i).getLocationId() == client.getLocationID()) {
                locationDropdown.setSelectedIndex(i);
                break;
            }
        }
        locationDropdown.setEnabled(false);


        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(nameField, gbc);


        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Contact No:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(contactField, gbc);


        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("Unit No:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(unitField, gbc);


        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(locationDropdown, gbc);

        add(centerPanel, BorderLayout.CENTER);


        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        editBtn = new JButton("Edit Profile");
        saveBtn = new JButton("Save Changes");
        exitBtn = new JButton("Exit");

        Dimension btnSize = new Dimension(150, 40);
        for (JButton btn : new JButton[]{editBtn, saveBtn, exitBtn}) {
            btn.setPreferredSize(btnSize);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(new Color(255, 182, 193));
            btn.setFocusPainted(false);
            southPanel.add(btn);
        }

        add(southPanel, BorderLayout.SOUTH);

        editBtn.addActionListener(e -> toggleEditable(true));
        saveBtn.addActionListener(e -> saveChanges());
        exitBtn.addActionListener(e -> {
            dispose();
            new ClientMainMenu(client, locationDAO.getLocationById(client.getLocationID())).setVisible(true);
        });

        setVisible(true);
    }

    private void toggleEditable(boolean editable) {
        nameField.setEnabled(editable);
        contactField.setEnabled(editable);
        unitField.setEnabled(editable);
        locationDropdown.setEnabled(editable);
    }

    private void saveChanges() {
        String newName = nameField.getText().trim();
        String newContact = contactField.getText().trim();
        String newUnit = unitField.getText().trim();
        Location newLocation = (Location) locationDropdown.getSelectedItem();

        if (newName.isEmpty() || newContact.isEmpty() || newUnit.isEmpty() || newLocation == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        if (!newContact.matches("^09\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Contact number must start with 09 and be 11 digits.");
            return;
        }

        client.setName(newName);
        client.setContactNo(newContact);
        client.setUnitDetails(newUnit);
        client.setLocationID(newLocation.getLocationId());

        boolean updated = clientDAO.updateClient(client);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            toggleEditable(false);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile.");
        }
    }
}
