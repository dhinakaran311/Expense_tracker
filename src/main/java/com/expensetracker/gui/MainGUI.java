package com.expensetracker.gui;
//UI and Listerners
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
//Models
import com.expensetracker.model.Expense;
import com.expensetracker.model.Category;
//DAO
import com.expensetracker.dao.CategoryDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class MainGUI extends JFrame{
    private JButton category;
    private JButton expense;

    public MainGUI(){
        initializeComponents();
        setupLayout();
        setupListeners();
    }
    private void  initializeComponents(){
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,500);
        setLocationRelativeTo(null);
        category = new JButton("Category");
        expense = new JButton("Expense");
    }

    private void setupLayout(){
        JPanel buttonPanel = new JPanel (new FlowLayout(FlowLayout.CENTER,20,20));
        buttonPanel.add(category);
        buttonPanel.add(expense);

        category.setPreferredSize(new Dimension(250,100));
        category.setFont(new Font("Arial",Font.BOLD,25));
        expense.setPreferredSize(new Dimension(250,100));
        expense.setFont(new Font("Arial",Font.BOLD,25));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(buttonPanel);

        add(mainPanel,BorderLayout.CENTER);
    }
    private void setupListeners(){
        category.addActionListener(e->{
            CategoryGUI categoryUI = new CategoryGUI();
            categoryUI.setVisible(true);
        });
        expense.addActionListener(e->{
            ExpenseGUI expenseUI = new ExpenseGUI();
            expenseUI.setVisible(true);
        });

    }

}
class CategoryGUI extends JFrame{
    private JButton addCategory;
    private JTable categoryTable;
    private DefaultTableModel categoryTableModel;
    private JTextField categoryName;
    private CategoryDAO categoryDAO;

    public CategoryGUI(){
        this.categoryDAO = new CategoryDAO();
        initializeComponents();
        setupLayout();
        SetupEventListeners();
        loadCategory();
    }

    private void initializeComponents(){
        setTitle("Category Table");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,400);
        setLocationRelativeTo(null);
        addCategory = new JButton("ADD CATEGORY");

        categoryName = new JTextField(20);
        String[] columns = {"ID","Name"};

        DefaultTableModel categoryTableModel = new DefaultTableModel(columns,0);
        JTable categoryTable = new JTable(categoryTableModel);
        categoryTable.setDefaultEditor(Object.class, null);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }
    private void setupLayout(){
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc =  new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.WEST;
        
        inputPanel.add(new JLabel("Category name"),gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(categoryName,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(addCategory,gbc);
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);

        add(northPanel,BorderLayout.NORTH);
        add(new JScrollPane(categoryTable),BorderLayout.CENTER);


    }
    private void SetupEventListeners(){
        addCategory.addActionListener((e)->{addCategory();});
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
    
    private void addCategory(){
        String name = categoryName.getText().trim();
        if(name.isEmpty()){
            JOptionPane.showMessageDialog(this, "Category name cannot be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try{
            categoryDAO.addCategory(name);
            loadCategory();
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

class ExpenseGUI extends JFrame{
    private CategoryDAO expenseDAO;

}




