package com.expensetracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.util.DatabaseUtil;

public class CategoryDAO{
    private static final String SELECT_ALL = "SELECT * FROM category";
    private static final String SELECT_EXP = "SELECT * FROM expenses";
    private static final String ADD_CATEGORY = "INSERT INTO category (name) VALUES (?)";

    private Category getCategoryRow(ResultSet rs) throws SQLException{
        Category cat = new Category();
        cat.setCategory_id(rs.getInt("id"));
        cat.setName(rs.getString("name"));
        return cat; 
    }

    public Expense getExpenseRow(ResultSet rs) throws SQLException{
        Expense exp = new Expense();
        exp.setId(rs.getInt("id"));
        exp.setAmount(rs.getDouble("amount"));
        exp.setDate(rs.getDate("date"));
        exp.setCategory_id(rs.getInt("category_id"));
        exp.setDescription(rs.getString("description"));
        return exp;
    }

    public List<Category> getAllCategories() throws SQLException{
        List<Category> categories = new ArrayList<>();
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
            ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    categories.add(getCategoryRow(rs));
                }
            }
        return categories;
    }

    public List<Expense> getAllExpenses() throws SQLException{
        List<Expense> expenses = new ArrayList<>();
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_EXP);
            ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    expenses.add(getExpenseRow(rs));
                }
            }
        return expenses;
    }
    public void addCategory(Category category) throws SQLException{
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(ADD_CATEGORY)){
                ps.setString(1, category.getName());
                ps.executeUpdate();
            }
    }

    public void addExpense(Expense expense) throws SQLException{    
        String ADD_EXPENSE = "INSERT INTO expenses (amount, date, category_id, description) VALUES (?,?,?,?)";
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(ADD_EXPENSE)){
                ps.setDouble(1, expense.getAmount());
                ps.setDate(2, new java.sql.Date(expense.getDate().getTime()));
                ps.setInt(3, expense.getCategory_id());
                ps.setString(4, expense.getDescription());
                ps.executeUpdate();
            }
    }
    
}