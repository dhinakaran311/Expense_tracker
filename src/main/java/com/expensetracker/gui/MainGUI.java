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
//import com.expensetracker.dao.DAO;

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

}
class ExpenseGUI extends JFrame{

}





