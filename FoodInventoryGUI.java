import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Ingredient {
    private String name;
    private String category;
    private int quantity;
    private LocalDate expirationDate;
    private double price;
    
    public Ingredient(String name, String category, int quantity, LocalDate expirationDate, double price) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.price = price;
    }
    
    // Getters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public double getPrice() { return price; }
    
    public Object[] toTableRow() {
        return new Object[]{
            name, 
            category, 
            quantity, 
            expirationDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
            String.format("$%.2f", price)
        };
    }
}

public class FoodInventoryGUI extends JFrame {
    private List<Ingredient> ingredients;
    private DefaultTableModel tableModel;
    private JTable inventoryTable;
    
    // Form components
    private JTextField nameField;
    private JComboBox<String> categoryComboBox;
    private JSpinner quantitySpinner;
    private JSpinner dateSpinner;
    private JSpinner priceSpinner;
    private JComboBox<String> sortComboBox;
    
    public FoodInventoryGUI() {
        ingredients = new ArrayList<>();
        initializeGUI();
        loadSampleData();
    }
    
    private void initializeGUI() {
        setTitle("Restaurant Food Inventory Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Create main panels
        JPanel inputPanel = createInputPanel();
        JPanel tablePanel = createTablePanel();
        JPanel buttonPanel = createButtonPanel();
        
        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Ingredient"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        panel.add(nameField, gbc);
        
        // Category combo box
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        String[] categories = {"Produce", "Dairy", "Meat", "Seafood", "Dry Goods", "Spices", "Beverages", "Frozen"};
        categoryComboBox = new JComboBox<>(categories);
        panel.add(categoryComboBox, gbc);
        
        // Quantity spinner
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        SpinnerNumberModel quantityModel = new SpinnerNumberModel(1, 1, 1000, 1);
        quantitySpinner = new JSpinner(quantityModel);
        panel.add(quantitySpinner, gbc);
        
        // Date spinner
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Expiration Date:"), gbc);
        gbc.gridx = 3;
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy"));
        panel.add(dateSpinner, gbc);
        
        // Price spinner
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Price ($):"), gbc);
        gbc.gridx = 3;
        SpinnerNumberModel priceModel = new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.5);
        priceSpinner = new JSpinner(priceModel);
        JSpinner.NumberEditor priceEditor = new JSpinner.NumberEditor(priceSpinner, "$#0.00");
        priceSpinner.setEditor(priceEditor);
        panel.add(priceSpinner, gbc);
        
        // Sort combo box
        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(new JLabel("Sort By:"), gbc);
        gbc.gridx = 3;
        String[] sortOptions = {"Name", "Category", "Quantity", "Expiration Date", "Price"};
        sortComboBox = new JComboBox<>(sortOptions);
        panel.add(sortComboBox, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table model
        String[] columnNames = {"Name", "Category", "Quantity", "Expiration Date", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        inventoryTable = new JTable(tableModel);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton addButton = new JButton("Add Ingredient");
        JButton sortButton = new JButton("Sort Inventory");
        JButton clearButton = new JButton("Clear Form");  //clear form
        JButton deleteButton = new JButton("Delete Selected");
        JButton lowStockButton = new JButton("Show Low Stock");
        
        // Add button action
        addButton.addActionListener(e -> addIngredient());
        
        // Sort button action
        sortButton.addActionListener(e -> sortInventory());
        
        // Clear form button
        clearButton.addActionListener(e -> clearForm());
        
        // Delete button action
        deleteButton.addActionListener(e -> deleteSelectedIngredient());
        
        // Low stock button action
        lowStockButton.addActionListener(e -> showLowStock());
        
        // Enter key support for adding ingredients
        nameField.addActionListener(e -> addIngredient());
        
        panel.add(addButton);
        panel.add(sortButton);
        panel.add(clearButton);
        panel.add(deleteButton);
        panel.add(lowStockButton);
        
        return panel;
    }
    
    private void addIngredient() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ingredient name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String category = (String) categoryComboBox.getSelectedItem();
            int quantity = (Integer) quantitySpinner.getValue();
            java.util.Date date = (java.util.Date) dateSpinner.getValue();
            LocalDate expirationDate = LocalDate.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault());
            double price = (Double) priceSpinner.getValue();
            
            Ingredient ingredient = new Ingredient(name, category, quantity, expirationDate, price);
            ingredients.add(ingredient);
            
            // Add to table
            tableModel.addRow(ingredient.toTableRow());
            
            // Clear form
            clearForm();
            
            // Show success message
            JOptionPane.showMessageDialog(this, "Ingredient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding ingredient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void sortInventory() {
        String sortBy = (String) sortComboBox.getSelectedItem();
        
        switch (sortBy) {
            case "Name":
                ingredients.sort((i1, i2) -> i1.getName().compareToIgnoreCase(i2.getName()));
                break;
            case "Category":
                ingredients.sort((i1, i2) -> {
                    int catCompare = i1.getCategory().compareToIgnoreCase(i2.getCategory());
                    return catCompare != 0 ? catCompare : i1.getName().compareToIgnoreCase(i2.getName());
                });
                break;
            case "Quantity":
                ingredients.sort((i1, i2) -> Integer.compare(i1.getQuantity(), i2.getQuantity()));
                break;
            case "Expiration Date":
                ingredients.sort((i1, i2) -> i1.getExpirationDate().compareTo(i2.getExpirationDate()));
                break;
            case "Price":
                ingredients.sort((i1, i2) -> Double.compare(i1.getPrice(), i2.getPrice()));
                break;
        }
        
        refreshTable();
        JOptionPane.showMessageDialog(this, "Inventory sorted by " + sortBy, "Sorted", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteSelectedIngredient() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ingredient to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this ingredient?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            ingredients.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        }
    }
    
    private void showLowStock() {
        List<Ingredient> lowStock = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getQuantity() <= 10) {
                lowStock.add(ingredient);
            }
        }
        
        if (lowStock.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No low stock items! All ingredients have sufficient quantity.", "Low Stock", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder("Low Stock Items (≤ 10 units):\n\n");
            for (Ingredient ingredient : lowStock) {
                message.append(String.format("- %s: %d units\n", ingredient.getName(), ingredient.getQuantity()));
            }
            JOptionPane.showMessageDialog(this, message.toString(), "Low Stock Alert", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void clearForm() {
        nameField.setText("");
        categoryComboBox.setSelectedIndex(0);
        quantitySpinner.setValue(1);
        priceSpinner.setValue(0.0);
        nameField.requestFocus();
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Ingredient ingredient : ingredients) {
            tableModel.addRow(ingredient.toTableRow());
        }
    }
    
    private void loadSampleData() {
        // Add some sample data
        ingredients.add(new Ingredient("Tomatoes", "Produce", 15, LocalDate.now().plusDays(5), 2.50));
        ingredients.add(new Ingredient("Chicken Breast", "Meat", 8, LocalDate.now().plusDays(3), 8.99));
        ingredients.add(new Ingredient("Milk", "Dairy", 5, LocalDate.now().plusDays(2), 3.49));
        ingredients.add(new Ingredient("Flour", "Dry Goods", 25, LocalDate.now().plusMonths(6), 4.25));
        ingredients.add(new Ingredient("Eggs", "Dairy", 12, LocalDate.now().plusDays(10), 5.99));
        
        refreshTable();
    }
    
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            // Try to set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | 
                 InstantiationException | IllegalAccessException e) {
            // If system LAF fails, try cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Could not set any look and feel");
            }
        }
        
        new FoodInventoryGUI().setVisible(true);
    });
}
}