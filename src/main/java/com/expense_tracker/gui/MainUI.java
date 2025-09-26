package com.expense_tracker.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.time.LocalDateTime;
import com.expense_tracker.dao.ExpenseDAO;
import com.expense_tracker.model.Category;
import com.expense_tracker.model.Expense;
public class MainUI extends JFrame {
    private JButton categoryBtn;
    private JButton expenseBtn;

    public MainUI() {
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize buttons
        categoryBtn = new JButton("Manage Categories");
        expenseBtn = new JButton("Manage Expenses");

        // Set button styles
        Font buttonFont = new Font("Arial", Font.BOLD, 24);
        categoryBtn.setFont(buttonFont);
        expenseBtn.setFont(buttonFont);

        categoryBtn.setPreferredSize(new Dimension(300, 100));
        expenseBtn.setPreferredSize(new Dimension(300, 100));

        categoryBtn.setBackground(new Color(144, 238, 144)); // Light green
        expenseBtn.setBackground(new Color(255, 182, 193));   // Light pink
    }

    private void setupLayout() {
        // Create button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add category button
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(categoryBtn, gbc);
        
        // Add expense button
        gbc.gridy = 1;
        buttonPanel.add(expenseBtn, gbc);

        // Add main panel to frame
        add(buttonPanel, BorderLayout.CENTER);
        
        // Add some padding
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }

    private void setupListeners() {
        categoryBtn.addActionListener(e -> {
            try {
                CategoryUI categoryUI = new CategoryUI();
                categoryUI.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error opening Category UI: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        expenseBtn.addActionListener(e -> {
            try {
                ExpenseUI expenseUI = new ExpenseUI();
                expenseUI.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error opening Expense UI: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
}
class CategoryUI extends JFrame{

    private JButton addCategory;
    private JButton updateCategory;
    private JButton deleteCategory;
    private JTable categoryTable;
    private DefaultTableModel categoryTableModel;
    private JTextField categoryName;
    private ExpenseDAO expenseDAO;




    public CategoryUI(){
        this.expenseDAO = new ExpenseDAO();
        initializeComponents();
        setupLayout();
        loadCategory();
        setUpListeners();
    }

    private void initializeComponents(){
        setTitle("Category");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 1000);
        setLocationRelativeTo(null);
        addCategory = new JButton("Add Category");
        updateCategory = new JButton("Update Category");
        deleteCategory = new JButton("Delete Category");
        
        // categoryTableModel = new DefaultTableModel();
        categoryName = new JTextField(20);
        String[] columns = {"ID","Name"};
        categoryTableModel = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        categoryTable = new JTable(categoryTableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if(!e.getValueIsAdjusting()){
                loadSelectedRow();
            }
        });
        
    }
    private void setupLayout(){
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx  = 0;
        gbc.gridy = 0;

        gbc.anchor = GridBagConstraints.WEST;

        inputPanel.add(new JLabel("category name"),gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(categoryName,gbc);


        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(addCategory);
        buttonPanel.add(updateCategory);
        buttonPanel.add(deleteCategory);
        northPanel.add(buttonPanel,BorderLayout.SOUTH);

        add(northPanel,BorderLayout.NORTH);
        add(new JScrollPane(categoryTable),BorderLayout.CENTER);
        
    }

    private void loadCategory(){
        try{
            List<Category> categories =  expenseDAO.getAllcat();
            updateCategoryTable(categories);
        }
        catch(Exception e){
            e.printStackTrace(); // This will print the detailed SQL error to the console
            JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
            
    private void updateCategoryTable(List<Category> categories) {
        categoryTableModel.setRowCount(0);
        for (Category c : categories) {
            categoryTableModel.addRow(new Object[]{c.getCategory_id(), c.getName()});
        }
    }
    private void setUpListeners(){
        addCategory.addActionListener(e->{
            addCategorySql();
        });
        updateCategory.addActionListener(e->{
            updateCategorySql();
        });

        deleteCategory.addActionListener(e->{
            deleteCategorySql();
        });
    }
    private void addCategorySql()
    {
        String name = categoryName.getText().trim();
        if(name.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try
        {
            expenseDAO.addCategory(name);
            loadCategory();
            categoryName.setText("");
            JOptionPane.showMessageDialog(this, "Category added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadSelectedRow()
    {
        int selectedRow = categoryTable.getSelectedRow();
        if(selectedRow == -1)
        {
            return;
        }
        String name = (String)categoryTableModel.getValueAt(selectedRow, 1);
        categoryName.setText(name);
    }

    // update category

    private void updateCategorySql(){
        int row = categoryTable.getSelectedRow();
        if(row>=0)
        {
            try{
                int id = (int)categoryTable.getValueAt(row, 0);
                String catName = (String) categoryName.getText().trim();
                Category category = new Category(id, catName);
                if(expenseDAO.updateCategory(category))
                {
                    JOptionPane.showMessageDialog(this,"Update Sucessfull","Info",JOptionPane.INFORMATION_MESSAGE);
                    loadCategory();
                    categoryName.setText("");

                }
            }
            catch(SQLException e)
            {
                JOptionPane.showMessageDialog(this, "SQL Error","Error",JOptionPane.ERROR_MESSAGE);
            }
        }

        else{
            JOptionPane.showConfirmDialog(this, "Select row to update","Error",JOptionPane.ERROR_MESSAGE);
        }


    }
    // Delete Category

    private void deleteCategorySql()
    {
        int row = categoryTable.getSelectedRow();
        try {
            if (row >= 0) {
                int id = (int) categoryTable.getValueAt(row, 0);
                boolean deleted = expenseDAO.deleteCategory(id);
                if (deleted) {
                    loadCategory(); // Refresh the category list
                    JOptionPane.showMessageDialog(this, "Category deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete category", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a category to delete", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }
}

class ExpenseUI extends JFrame {
    private ExpenseDAO expenseDAO;
    private JTable expenseTable;
    private DefaultTableModel expenseTableModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> filterComboBox;
    private JTextField amountField;
    private JComboBox<String> categoryComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    public ExpenseUI() {
        this.expenseDAO = new ExpenseDAO();
        // Initialize combo boxes after expenseDAO is set
        this.filterComboBox = new JComboBox<>();
        this.categoryComboBox = new JComboBox<>();
        initializeComponents();
        setupLayout();
        loadExpense();
        setUpListeners();
        // Load combo box data after initialization
        updateComboBoxes();
    }
    
    private void updateComboBoxes() {
        String[] allCategories = filteroption(expenseDAO, 0);
        String[] filterOptions = filteroption(expenseDAO, 1);
        DefaultComboBoxModel<String> categoryModel = new DefaultComboBoxModel<>(allCategories);
        DefaultComboBoxModel<String> filterModel = new DefaultComboBoxModel<>(filterOptions);
        categoryComboBox.setModel(categoryModel);
        filterComboBox.setModel(filterModel);
    }

    private void initializeComponents()
    {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 1000);
        setLocationRelativeTo(null);

        titleField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        
        addButton = new JButton("Add Expense");
        updateButton = new JButton("Update Expense");
        deleteButton = new JButton("Delete Expense");
        amountField = new JTextField(20);
        
        String[] columns = {"ID","Name","Amount","Category","Description","Date"};
        expenseTableModel = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        expenseTable = new JTable(expenseTableModel);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if(!e.getValueIsAdjusting()){
                loadSelectedRow();
            }
                
        });
    }
    //setuplayout
    private void setupLayout() {
        
        setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel ("Expense Name"),gbc);
        gbc.gridx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        inputPanel.add(titleField,gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        inputPanel.add(new JLabel("Description"),gbc);
        gbc.gridx=1;
        inputPanel.add(new JScrollPane(descriptionArea),gbc);
        
        gbc.gridx=1;
        gbc.gridy=2;
        JPanel filterPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Select Category:"));
        filterPanel.add(filterComboBox);

        gbc.gridx=0;
        gbc.gridy=3;
        inputPanel.add(new JLabel("Amount:"),gbc);
        gbc.gridx=1;
        inputPanel.add(amountField,gbc);

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryComboBox);

        
        JPanel buttonPanel=new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        
        
        JPanel northPanel=new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);
        northPanel.add(buttonPanel,BorderLayout.SOUTH);
        northPanel.add(filterPanel,BorderLayout.NORTH);
        
        
        add(northPanel,BorderLayout.NORTH);
        
        add(new JScrollPane(expenseTable),BorderLayout.CENTER);
    }
    
    private void loadExpense() {
        try{
            List<Expense> expenses = expenseDAO.getAllExpenses();
            updateExpenseTable(expenses);
            setDefault();
            filterComboBox.setSelectedIndex(0);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    private void loadSelectedRow(){
        int row = expenseTable.getSelectedRow();
        if(row >=0){
            titleField.setText(expenseTableModel.getValueAt(row, 1).toString()!=""?expenseTableModel.getValueAt(row, 1).toString():"NULL");
            amountField.setText(expenseTableModel.getValueAt(row, 2).toString()!=""?expenseTableModel.getValueAt(row, 2).toString():"NULL");
            categoryComboBox.setSelectedItem(expenseTableModel.getValueAt(row, 3).toString());
            descriptionArea.setText(expenseTableModel.getValueAt(row, 4).toString());       
            
        }
    }
    private void updateExpenseTable(List<Expense> expenses) {
        expenseTableModel.setRowCount(0);
        for (Expense expense : expenses) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            expenseTableModel.addRow(new Object[]{
                expense.getExpense_id(),
                expense.getName(),
                expense.getAmount(),
                expense.getCategory_name(),
                expense.getDescription(),
                expense.getDate() != null ? sdf.format(expense.getDate()) : ""
            });
        }
    }
    /**
     * Gets category names as a String array
     * @param type If 0, returns all category names; otherwise, returns a specific category name in an array
     * @return Array of category names
     */
    private String[] filteroption(ExpenseDAO expenseDAO, int type) {
        try {
            if (type == 0) {
                // Return all category names
                return expenseDAO != null ? expenseDAO.getAllCategoryNames() : new String[0];
            } else {
                // Return a specific category name in an array
                String name = expenseDAO != null ? expenseDAO.getCategoryName(type) : null;
                return name != null ? new String[]{name} : new String[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }
    private void setUpListeners(){
        addButton.addActionListener(e->{
            addExpenseSql();
        });
        updateButton.addActionListener(e->{
            updateExpenseSql();
        });
        deleteButton.addActionListener(e->{
            deleteExpenseSql();
        });
        filterComboBox.addActionListener(e->{
            filterExpense();
        });
    
    }
    private void addExpenseSql(){
        try{
        String name = titleField.getText().trim();
        double amount = Double.parseDouble(amountField.getText().trim());
        int categoryID = expenseDAO.getCategoryID((String)categoryComboBox.getSelectedItem());
        System.out.println(categoryID);
        String description = descriptionArea.getText().trim();
        Expense expense = new Expense(0, name, amount, new Date(), categoryID, (String)categoryComboBox.getSelectedItem(), LocalDateTime.now(), description);
        expenseDAO.addExpense(expense);
            loadExpense();
            JOptionPane.showMessageDialog(this, "Expense added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateExpenseSql(){
        try{
            int row = expenseTable.getSelectedRow();
            if(row != -1)
            {
                int id =(int) expenseTable.getValueAt(row, 0);
                String name = titleField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                int categoryID = expenseDAO.getCategoryID((String)categoryComboBox.getSelectedItem());
                String description = descriptionArea.getText().trim();
                Expense expense = new Expense(
                    id,                                   // expense_id
                    name,                                 // name
                    amount,                               // amount
                    new Date(),                           // date
                    categoryID,                           // category_id
                    (String)categoryComboBox.getSelectedItem(), // category_name
                    LocalDateTime.now(),                  // time
                    description                           // description
                );
                expenseDAO.updateExpense(expense);
                JOptionPane.showMessageDialog(this,"Update Successfull","Sucess",JOptionPane.INFORMATION_MESSAGE);
                loadExpense();
            }
            else{
                JOptionPane.showMessageDialog(this,"Select row to update","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void deleteExpenseSql(){
        try {
            int row = expenseTable.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this expense?", 
                    "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) expenseTable.getValueAt(row, 0);
                    expenseDAO.deleteExpense(id);
                    JOptionPane.showMessageDialog(this, 
                        "Expense deleted successfully", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadExpense();
                    if (filterComboBox.getItemCount() > 0) {
                        filterComboBox.setSelectedIndex(0);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please select a row to delete", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Database error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "An unexpected error occurred: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }

    }
    private void filterExpense(){
        try {
            Object selected = filterComboBox.getSelectedItem();
            if (selected == null) {
                return;
            }
            
            String selectedCategory = selected.toString();
            if ("All".equals(selectedCategory)) {
                loadExpense();
            } else {
                int categoryID = expenseDAO.getCategoryID(selectedCategory);
                if (categoryID != -1) {
                    List<Expense> expenses = expenseDAO.filterExpenseByCategory(categoryID);
                    updateExpenseTable(expenses);
                    setDefault();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid category selected", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Failed to filter expenses: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void setDefault()
    {
        titleField.setText("");
        amountField.setText("");
        
        descriptionArea.setText("");
    }
    
}