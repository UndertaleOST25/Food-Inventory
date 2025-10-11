import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
            String.format("₱%.2f", price)
        };
    }
}

public class FoodInventorySortGUI extends JFrame {
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
    private JComboBox<String> algorithmComboBox;
    
    public FoodInventorySortGUI() {
        ingredients = new ArrayList<>();
        initializeGUI();
        loadSampleData();
    }
    
    private void initializeGUI() {
        setTitle("Restaurant Food Inventory Management - Sorting Algorithms Demo");
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
        setSize(900, 700);
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
        panel.add(new JLabel("Price (₱):"), gbc);
        gbc.gridx = 3;
        SpinnerNumberModel priceModel = new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.5);
        priceSpinner = new JSpinner(priceModel);
        JSpinner.NumberEditor priceEditor = new JSpinner.NumberEditor(priceSpinner, "₱#0.00");
        priceSpinner.setEditor(priceEditor);
        panel.add(priceSpinner, gbc);
        
        // Sort combo box
        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(new JLabel("Sort By:"), gbc);
        gbc.gridx = 3;
        String[] sortOptions = {"Name", "Category", "Quantity", "Expiration Date", "Price"};
        sortComboBox = new JComboBox<>(sortOptions);
        panel.add(sortComboBox, gbc);
        
        // Algorithm combo box
        gbc.gridx = 4; gbc.gridy = 2;
        panel.add(new JLabel("Algorithm:"), gbc);
        gbc.gridx = 5;
        String[] algorithms = {"Merge Sort", "Quick Sort", "Bubble Sort"};
        algorithmComboBox = new JComboBox<>(algorithms);
        panel.add(algorithmComboBox, gbc);
        
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
        JButton clearButton = new JButton("Clear Form");
        JButton deleteButton = new JButton("Delete Selected");
        JButton lowStockButton = new JButton("Show Low Stock");
        JButton demoButton = new JButton("Demo All Algorithms");
        
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
        
        // Demo button action
        demoButton.addActionListener(e -> demonstrateAllAlgorithms());
        
        // Enter key support for adding ingredients
        nameField.addActionListener(e -> addIngredient());
        
        panel.add(addButton);
        panel.add(sortButton);
        panel.add(clearButton);
        panel.add(deleteButton);
        panel.add(lowStockButton);
        panel.add(demoButton);
        
        return panel;
    }

    // ========== SORTING ALGORITHM IMPLEMENTATIONS ==========
    
    // 1. MERGE SORT
    private void mergeSort(String sortBy) {
        List<Ingredient> sorted = mergeSort(new ArrayList<>(ingredients), sortBy);
        ingredients = sorted;
        JOptionPane.showMessageDialog(this, 
            "Applied Merge Sort (O(n log n))\nStable algorithm, good for large datasets", 
            "Merge Sort Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private List<Ingredient> mergeSort(List<Ingredient> list, String sortBy) {
        if (list.size() <= 1) {
            return list;
        }
        
        int mid = list.size() / 2;
        List<Ingredient> left = mergeSort(new ArrayList<>(list.subList(0, mid)), sortBy);
        List<Ingredient> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())), sortBy);
        
        return merge(left, right, sortBy);
    }
    
    private List<Ingredient> merge(List<Ingredient> left, List<Ingredient> right, String sortBy) {
        List<Ingredient> merged = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < left.size() && j < right.size()) {
            if (compareIngredients(left.get(i), right.get(j), sortBy) <= 0) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }
        
        while (i < left.size()) {
            merged.add(left.get(i++));
        }
        
        while (j < right.size()) {
            merged.add(right.get(j++));
        }
        
        return merged;
    }
    
    // 2. QUICK SORT
    private void quickSort(String sortBy) {
        List<Ingredient> sorted = new ArrayList<>(ingredients);
        quickSort(sorted, 0, sorted.size() - 1, sortBy);
        ingredients = sorted;
        JOptionPane.showMessageDialog(this, 
            "Applied Quick Sort (O(n log n) average)\nFastest in practice, in-place sorting", 
            "Quick Sort Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void quickSort(List<Ingredient> list, int low, int high, String sortBy) {
        if (low < high) {
            int pi = partition(list, low, high, sortBy);
            quickSort(list, low, pi - 1, sortBy);
            quickSort(list, pi + 1, high, sortBy);
        }
    }
    
    private int partition(List<Ingredient> list, int low, int high, String sortBy) {
        Ingredient pivot = list.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (compareIngredients(list.get(j), pivot, sortBy) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
    
    // 3. BUBBLE SORT
    private void bubbleSort(String sortBy) {
        List<Ingredient> sorted = new ArrayList<>(ingredients);
        bubbleSort(sorted, sortBy);
        ingredients = sorted;
        JOptionPane.showMessageDialog(this, 
            "Applied Bubble Sort (O(n²))\nSimple algorithm, good for small datasets", 
            "Bubble Sort Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void bubbleSort(List<Ingredient> list, String sortBy) {
        int n = list.size();
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (compareIngredients(list.get(j), list.get(j + 1), sortBy) > 0) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
    
    // Helper method for comparing ingredients
    private int compareIngredients(Ingredient i1, Ingredient i2, String sortBy) {
        switch (sortBy) {
            case "Name":
                return i1.getName().compareToIgnoreCase(i2.getName());
            case "Category":
                int catCompare = i1.getCategory().compareToIgnoreCase(i2.getCategory());
                return catCompare != 0 ? catCompare : i1.getName().compareToIgnoreCase(i2.getName());
            case "Quantity":
                return Integer.compare(i1.getQuantity(), i2.getQuantity());
            case "Expiration Date":
                return i1.getExpirationDate().compareTo(i2.getExpirationDate());
            case "Price":
                return Double.compare(i1.getPrice(), i2.getPrice());
            default:
                return i1.getName().compareToIgnoreCase(i2.getName());
        }
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
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        
        long startTime = System.nanoTime();
        
        switch (algorithm) {
            case "Merge Sort":
                mergeSort(sortBy);
                break;
            case "Quick Sort":
                quickSort(sortBy);
                break;
            case "Bubble Sort":
                bubbleSort(sortBy);
                break;
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000; // microseconds
        
        refreshTable();
        
        JOptionPane.showMessageDialog(this, 
            String.format("Sorted by %s using %s\nTime: %d microseconds", sortBy, algorithm, duration),
            "Sorting Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void demonstrateAllAlgorithms() {
        if (ingredients.size() < 3) {
            JOptionPane.showMessageDialog(this, "Need at least 3 ingredients to demonstrate sorting", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sortBy = (String) sortComboBox.getSelectedItem();
        StringBuilder results = new StringBuilder();
        results.append("SORTING ALGORITHMS DEMONSTRATION\n");
        results.append("Sorting by: ").append(sortBy).append("\n\n");
        
        // Test each algorithm and measure time
        String[] algorithms = {"Merge Sort", "Quick Sort", "Bubble Sort"};
        
        for (String algorithm : algorithms) {
            // Create a copy for testing
            List<Ingredient> testList = new ArrayList<>(ingredients);
            long startTime = System.nanoTime();
            
            switch (algorithm) {
                case "Merge Sort":
                    ingredients = mergeSort(testList, sortBy);
                    break;
                case "Quick Sort":
                    quickSort(testList, 0, testList.size() - 1, sortBy);
                    ingredients = testList;
                    break;
                case "Bubble Sort":
                    bubbleSort(testList, sortBy);
                    ingredients = testList;
                    break;
            }
            
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000; // microseconds
            
            results.append(algorithm).append(": ").append(duration).append(" microseconds\n");
        }
        
        refreshTable();
        
        JOptionPane.showMessageDialog(this, results.toString(), 
            "Algorithm Comparison", JOptionPane.INFORMATION_MESSAGE);
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
        // Add some sample data in unsorted order
        ingredients.add(new Ingredient("Olive Oil", "Produce", 80, LocalDate.now().plusMonths(3), 52.99));
        ingredients.add(new Ingredient("Tomato Sauce", "Produce", 30, LocalDate.now().plusDays(6), 82.50));
        ingredients.add(new Ingredient("Yeast", "Dry Goods", 67, LocalDate.now().plusDays(20), 55.5));
        ingredients.add(new Ingredient("Mozzarella", "Dairy", 25, LocalDate.now().plusDays(5), 50.0));
        ingredients.add(new Ingredient("Milk", "Dairy", 38, LocalDate.now().plusDays(2), 60.50));
        ingredients.add(new Ingredient("Chicken Breast", "Meat", 42, LocalDate.now().plusDays(3), 262.30));
        ingredients.add(new Ingredient("Basil", "Produce", 30, LocalDate.now().plusDays(6), 40.20));
        ingredients.add(new Ingredient("Ground Beef", "Meat", 29, LocalDate.now().plusDays(3), 304.60));
        ingredients.add(new Ingredient("Eggs", "Dairy", 12, LocalDate.now().plusDays(15), 20.10));
        ingredients.add(new Ingredient("Flour", "Dry Goods", 30, LocalDate.now().plusMonths(6), 100.0));
        ingredients.add(new Ingredient("Cola", "Beverages", 30, LocalDate.now().plusMonths(6), 50.0));
        ingredients.add(new Ingredient("Iced Tea", "Beverages", 50, LocalDate.now().plusMonths(1), 30.0));

        
        refreshTable();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | 
                     InstantiationException | IllegalAccessException e) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    System.err.println("Could not set any look and feel");
                }
            }
            
            new FoodInventorySortGUI().setVisible(true);
        });
    }
}