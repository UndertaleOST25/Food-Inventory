import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

public class FoodInventoryGUI extends JFrame {
    private List<Ingredient> ingredients;
    private DefaultTableModel tableModel;
    private JTable inventoryTable;
    private JTextField nameField;
    private JComboBox<String> categoryComboBox;
    private JSpinner quantitySpinner;
    private JSpinner dateSpinner;
    private JSpinner priceSpinner;
    private JComboBox<String> algorithmComboBox;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public FoodInventoryGUI() {
        ingredients = new ArrayList<>();
        initializeGUI();
        loadSampleData();
    }

    private void initializeGUI() {
        setTitle("Restaurant Food Inventory Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 255, 240));

        JPanel headerPanel = createHeaderPanel();
        JPanel inputPanel = createInputPanel();
        JPanel tablePanel = createTablePanel();
        JPanel buttonPanel = createButtonPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(950, 600);
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 128, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Restaurant Food Inventory Management", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(new Color(0, 128, 0));
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchField = new JTextField(20);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        panel.add(title, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 255, 245));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 128, 0)), "Add Ingredient"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(12);
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        String[] categories = {"Produce", "Dairy", "Meat", "Seafood", "Dry Goods", "Spices", "Beverages", "Frozen"};
        categoryComboBox = new JComboBox<>(categories);
        panel.add(categoryComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        panel.add(quantitySpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Expiration Date:"), gbc);
        gbc.gridx = 1;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy"));
        panel.add(dateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Price (₱):"), gbc);
        gbc.gridx = 1;
        priceSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.5));
        priceSpinner.setEditor(new JSpinner.NumberEditor(priceSpinner, "₱#0.00"));
        panel.add(priceSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Sort Algorithm:"), gbc);
        gbc.gridx = 1;
        String[] algorithms = {"Built-in (Click Headers)", "Bubble Sort", "Quick Sort", "Merge Sort"};
        algorithmComboBox = new JComboBox<>(algorithms);
        panel.add(algorithmComboBox, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 255, 245));
        String[] columns = {"Name", "Category", "Quantity", "Expiration Date", "Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        inventoryTable = new JTable(tableModel);
        inventoryTable.setFillsViewportHeight(true);
        inventoryTable.setSelectionBackground(new Color(144, 238, 144));
        inventoryTable.setSelectionForeground(Color.BLACK);
        inventoryTable.setRowHeight(25);
        inventoryTable.getTableHeader().setBackground(new Color(0, 128, 0));
        inventoryTable.getTableHeader().setForeground(Color.WHITE);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        inventoryTable.setAutoCreateRowSorter(true);
        
        rowSorter = new TableRowSorter<>(tableModel);
        
        rowSorter.setComparator(2, Comparator.comparingInt(o -> Integer.parseInt(o.toString()))); // Quantity
        rowSorter.setComparator(3, Comparator.comparing(o -> {
            // Date parsing for expiration date
            try {
                String dateStr = o.toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                return LocalDate.parse(dateStr, formatter);
            } catch (Exception e) {
                return LocalDate.MIN;
            }
        }));
        rowSorter.setComparator(4, Comparator.comparing(o -> {
            try {
                String priceStr = o.toString().replace("₱", "").trim();
                return Double.parseDouble(priceStr);
            } catch (Exception e) {
                return 0.0;
            }
        }));
        
        inventoryTable.setRowSorter(rowSorter);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(230, 250, 230));

        JButton addButton = makeButton("Add Ingredient");
        JButton editButton = makeButton("Edit Selected");
        JButton clearButton = makeButton("Clear Form");
        JButton deleteButton = makeButton("Delete Selected");
        JButton lowStockButton = makeButton("Show Low Stock");
        JButton demoSortButton = makeButton("Demo Sort Algorithm"); 

        addButton.addActionListener(e -> addIngredient());
        editButton.addActionListener(e -> editSelectedIngredient());
        clearButton.addActionListener(e -> clearForm());
        deleteButton.addActionListener(e -> deleteSelectedIngredient());
        lowStockButton.addActionListener(e -> showLowStock());
        demoSortButton.addActionListener(e -> demoSortAlgorithm());

        panel.add(addButton);
        panel.add(editButton);
        panel.add(clearButton);
        panel.add(deleteButton);
        panel.add(lowStockButton);
        panel.add(demoSortButton);

        return panel;
    }

    private JButton makeButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 150, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void filterTable() {
        String text = searchField.getText().trim();
        if (text.length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void addIngredient() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ingredient name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String category = (String) categoryComboBox.getSelectedItem();
            int quantity = (Integer) quantitySpinner.getValue();
            java.util.Date date = (java.util.Date) dateSpinner.getValue();
            LocalDate expirationDate = LocalDate.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault());
            double price = (Double) priceSpinner.getValue();

            Ingredient ingredient = new Ingredient(name, category, quantity, expirationDate, price);
            ingredients.add(ingredient);
            tableModel.addRow(ingredient.toTableRow());
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Ingredient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding ingredient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void demoSortAlgorithm() {
        String algorithm = (String) algorithmComboBox.getSelectedItem();
        
        if ("Built-in (Click Headers)".equals(algorithm)) {
            JOptionPane.showMessageDialog(this, 
                "Use the column headers to sort!\n\n" +
                "Click any column header to sort by that column.\n" +
                "Click again to reverse the order.", 
                "Column Header Sorting", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Comparator<Ingredient> comparator = Comparator.comparing(Ingredient::getName, String.CASE_INSENSITIVE_ORDER);
        
        long startTime = System.nanoTime();
        
        switch (algorithm) {
            case "Bubble Sort" -> bubbleSort(comparator);
            case "Quick Sort" -> quickSort(comparator);
            case "Merge Sort" -> mergeSort(comparator);
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        
        refreshTable();
        
        String message = String.format(
            "Algorithm: %s\nTime: %d ms\nItems sorted: %d\n\n" +
            "Note: For regular use, click column headers to sort.",
            algorithm, duration, ingredients.size()
        );
        
        JOptionPane.showMessageDialog(this, message, "Algorithm Demo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void bubbleSort(Comparator<Ingredient> comparator) {
        int n = ingredients.size();
        boolean swapped;
        
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(ingredients.get(j), ingredients.get(j + 1)) > 0) {
                    swapIngredients(j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    private void quickSort(Comparator<Ingredient> comparator) {
        if (ingredients.isEmpty()) return;
        quickSort(0, ingredients.size() - 1, comparator);
    }

    private void quickSort(int low, int high, Comparator<Ingredient> comparator) {
        if (low < high) {
            int pivotIndex = partition(low, high, comparator);
            quickSort(low, pivotIndex - 1, comparator);
            quickSort(pivotIndex + 1, high, comparator);
        }
    }

    private int partition(int low, int high, Comparator<Ingredient> comparator) {
        Ingredient pivot = ingredients.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (comparator.compare(ingredients.get(j), pivot) <= 0) {
                i++;
                swapIngredients(i, j);
            }
        }
        swapIngredients(i + 1, high);
        return i + 1;
    }

    private void mergeSort(Comparator<Ingredient> comparator) {
        if (ingredients.size() <= 1) return;
        
        List<Ingredient> sorted = mergeSortHelper(new ArrayList<>(ingredients), comparator);
        ingredients.clear();
        ingredients.addAll(sorted);
    }

    private List<Ingredient> mergeSortHelper(List<Ingredient> list, Comparator<Ingredient> comparator) {
        if (list.size() <= 1) return list;
        
        int mid = list.size() / 2;
        List<Ingredient> left = mergeSortHelper(new ArrayList<>(list.subList(0, mid)), comparator);
        List<Ingredient> right = mergeSortHelper(new ArrayList<>(list.subList(mid, list.size())), comparator);
        
        return merge(left, right, comparator);
    }

    private List<Ingredient> merge(List<Ingredient> left, List<Ingredient> right, Comparator<Ingredient> comparator) {
        List<Ingredient> merged = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
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

    private void swapIngredients(int i, int j) {
        Ingredient temp = ingredients.get(i);
        ingredients.set(i, ingredients.get(j));
        ingredients.set(j, temp);
    }

    private void deleteSelectedIngredient() {
        int row = inventoryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an ingredient to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected ingredient?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int modelRow = inventoryTable.convertRowIndexToModel(row);
            ingredients.remove(modelRow);
            tableModel.removeRow(modelRow);
            JOptionPane.showMessageDialog(this, "Ingredient deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editSelectedIngredient() {
        int viewRow = inventoryTable.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ingredient to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int modelRow = inventoryTable.convertRowIndexToModel(viewRow);
        Ingredient selectedIngredient = ingredients.get(modelRow);
        
        nameField.setText(selectedIngredient.getName());
        categoryComboBox.setSelectedItem(selectedIngredient.getCategory());
        quantitySpinner.setValue(selectedIngredient.getQuantity());
        
        java.util.Date date = java.util.Date.from(selectedIngredient.getExpirationDate()
                .atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        dateSpinner.setValue(date);
        
        priceSpinner.setValue(selectedIngredient.getPrice());
        
        ingredients.remove(modelRow);
        tableModel.removeRow(modelRow);
        
        nameField.requestFocus();
        
        JOptionPane.showMessageDialog(this, 
            "Ingredient loaded for editing. Modify values and click 'Add Ingredient' to save.", 
            "Edit Mode", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showLowStock() {
        StringBuilder msg = new StringBuilder();
        for (Ingredient i : ingredients) {
            if (i.getQuantity() <= 10)
                msg.append("- ").append(i.getName()).append(": ").append(i.getQuantity()).append(" units\n");
        }
        if (msg.length() == 0)
            msg.append("No low stock items!");
        JOptionPane.showMessageDialog(this, msg.toString(), "Low Stock", JOptionPane.INFORMATION_MESSAGE);
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
        for (Ingredient i : ingredients)
            tableModel.addRow(i.toTableRow());
    }

    private void loadSampleData() {

        // Pizza bases
        ingredients.add(new Ingredient("Pizza Sauce", "Produce", 25, LocalDate.now().plusMonths(2), 120.75));
        ingredients.add(new Ingredient("Tomato Paste", "Produce", 18, LocalDate.now().plusMonths(6), 95.50));
        ingredients.add(new Ingredient("Olive Oil", "Produce", 15, LocalDate.now().plusMonths(12), 350.00));
        ingredients.add(new Ingredient("Garlic Puree", "Produce", 12, LocalDate.now().plusMonths(3), 85.25));
        ingredients.add(new Ingredient("Basil Pesto", "Produce", 8, LocalDate.now().plusMonths(4), 280.50));

        // Cheeses
        ingredients.add(new Ingredient("Mozzarella", "Dairy", 45, LocalDate.now().plusWeeks(2), 320.75));
        ingredients.add(new Ingredient("Parmesan", "Dairy", 22, LocalDate.now().plusMonths(3), 450.00));
        ingredients.add(new Ingredient("Provolone", "Dairy", 18, LocalDate.now().plusWeeks(3), 380.25));
        ingredients.add(new Ingredient("Ricotta", "Dairy", 15, LocalDate.now().plusDays(10), 275.50));
        ingredients.add(new Ingredient("Cheddar", "Dairy", 20, LocalDate.now().plusWeeks(4), 295.80));

        // Meats
        ingredients.add(new Ingredient("Pepperoni", "Meat", 35, LocalDate.now().plusWeeks(3), 520.75));
        ingredients.add(new Ingredient("Italian Sausage", "Meat", 28, LocalDate.now().plusWeeks(2), 480.50));
        ingredients.add(new Ingredient("Ham", "Meat", 25, LocalDate.now().plusWeeks(2), 420.25));
        ingredients.add(new Ingredient("Bacon", "Meat", 30, LocalDate.now().plusWeeks(3), 380.00));
        ingredients.add(new Ingredient("Chicken Breast", "Meat", 22, LocalDate.now().plusDays(7), 350.75));
        ingredients.add(new Ingredient("Ground Beef", "Meat", 20, LocalDate.now().plusDays(5), 450.25));
        ingredients.add(new Ingredient("Salami", "Meat", 18, LocalDate.now().plusWeeks(4), 510.50));

        // Vegetables
        ingredients.add(new Ingredient("Mushrooms", "Produce", 32, LocalDate.now().plusDays(10), 120.75));
        ingredients.add(new Ingredient("Green Peppers", "Produce", 28, LocalDate.now().plusDays(14), 95.50));
        ingredients.add(new Ingredient("Onions", "Produce", 40, LocalDate.now().plusDays(21), 65.25));
        ingredients.add(new Ingredient("Black Olives", "Produce", 25, LocalDate.now().plusMonths(6), 145.80));
        ingredients.add(new Ingredient("Tomatoes", "Produce", 35, LocalDate.now().plusDays(7), 88.90));
        ingredients.add(new Ingredient("Spinach", "Produce", 20, LocalDate.now().plusDays(5), 75.25));
        ingredients.add(new Ingredient("Jalapeños", "Produce", 15, LocalDate.now().plusDays(12), 110.50));
        ingredients.add(new Ingredient("Pineapple", "Produce", 18, LocalDate.now().plusDays(8), 165.75));

        // Dough & Flour
        ingredients.add(new Ingredient("Pizza Flour", "Dry Goods", 50, LocalDate.now().plusMonths(8), 280.00));
        ingredients.add(new Ingredient("Bread Flour", "Dry Goods", 35, LocalDate.now().plusMonths(9), 245.50));
        ingredients.add(new Ingredient("Yeast", "Dry Goods", 25, LocalDate.now().plusMonths(6), 185.75));
        ingredients.add(new Ingredient("Sugar", "Dry Goods", 40, LocalDate.now().plusMonths(12), 65.25));
        ingredients.add(new Ingredient("Salt", "Dry Goods", 60, LocalDate.now().plusMonths(24), 45.80));

        // Herbs & Spices
        ingredients.add(new Ingredient("Oregano", "Spices", 45, LocalDate.now().plusMonths(18), 95.50));
        ingredients.add(new Ingredient("Basil", "Spices", 38, LocalDate.now().plusMonths(12), 110.25));
        ingredients.add(new Ingredient("Garlic Powder", "Spices", 42, LocalDate.now().plusMonths(15), 85.75));
        ingredients.add(new Ingredient("Red Pepper Flakes", "Spices", 35, LocalDate.now().plusMonths(20), 75.50));
        ingredients.add(new Ingredient("Black Pepper", "Spices", 48, LocalDate.now().plusMonths(24), 120.00));

        // Seafood (for specialty pizzas)
        ingredients.add(new Ingredient("Anchovies", "Seafood", 12, LocalDate.now().plusMonths(3), 320.75));
        ingredients.add(new Ingredient("Shrimp", "Seafood", 15, LocalDate.now().plusDays(5), 580.50));
        ingredients.add(new Ingredient("Clams", "Seafood", 10, LocalDate.now().plusDays(4), 450.25));

        // Specialty Items
        ingredients.add(new Ingredient("Artichoke Hearts", "Produce", 18, LocalDate.now().plusMonths(6), 220.50));
        ingredients.add(new Ingredient("Sun-Dried Tomatoes", "Produce", 22, LocalDate.now().plusMonths(8), 285.75));
        ingredients.add(new Ingredient("Goat Cheese", "Dairy", 16, LocalDate.now().plusWeeks(3), 380.25));
        ingredients.add(new Ingredient("Feta Cheese", "Dairy", 20, LocalDate.now().plusWeeks(4), 320.50));

        // Beverages
        ingredients.add(new Ingredient("Cola", "Beverages", 72, LocalDate.now().plusMonths(9), 45.00));
        ingredients.add(new Ingredient("Lemonade", "Beverages", 65, LocalDate.now().plusMonths(8), 52.50));
        ingredients.add(new Ingredient("Iced Tea", "Beverages", 58, LocalDate.now().plusMonths(7), 48.75));
        ingredients.add(new Ingredient("Orange Soda", "Beverages", 45, LocalDate.now().plusMonths(10), 47.25));

        // Frozen Items
        ingredients.add(new Ingredient("French Fries", "Frozen", 35, LocalDate.now().plusMonths(6), 185.50));
        ingredients.add(new Ingredient("Garlic Bread", "Frozen", 28, LocalDate.now().plusMonths(5), 220.75));
        ingredients.add(new Ingredient("Mozzarella Sticks", "Frozen", 32, LocalDate.now().plusMonths(4), 280.25));

        refreshTable();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FoodInventoryGUI().setVisible(true));
    }
}
