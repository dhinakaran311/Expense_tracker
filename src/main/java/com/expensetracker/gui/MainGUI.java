package com.expensetracker.gui;

import com.expensetracker.dao.DAO;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;
import java.time.LocalDateTime;

public class MainGUI extends JFrame {

    private JButton CategoryButton;
    private JButton ExpenseButton;

    public MainGUI() {
        intitComponents();
        setupLayout();
        // addEventHandlers();
    }

    private void intitComponents() {
        setTitle("Expense Tracker");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        CategoryButton = new JButton("Manage Categories");
        ExpenseButton = new JButton("Manage Expenses");
    }

    // for panel
    private void setupLayout() {
        JPanel btnpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnpanel.add(CategoryButton);
        btnpanel.add(ExpenseButton);
        CategoryButton.setPreferredSize(new Dimension(200, 60));
        CategoryButton.setFont(new Font("Arial", Font.BOLD, 12));
        ExpenseButton.setPreferredSize(new Dimension(200, 60));
        ExpenseButton.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.add(btnpanel);
        add(mainPanel, BorderLayout.CENTER);
    }
}