package com.expensetracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.util.DatabaseUtil;

public class CategoryDAO{
   private static final String SELECT_ALL = "SELECT * FROM category";
    private static final String SELECT_EXP = "SELECT * FROM expense";
    private static final String ADD_CATEGORY = "INSERT INTO category (name) VALUES (?)";
    private static final String ADD_EXPENSE = "INSERT INTO expense (amount, expense_date, category_id, description) VALUES (?, ?, ?, ?)";
    private static final String DELETE_CATEGORY = "DELETE FROM category WHERE category_id = ?";  
    private static final String FILTER_NAMES = "SELECT name FROM category";
    private static final String GET_CATEGORY_ID = "SELECT category_id FROM category WHERE name = ?";
    private static final String GET_CATEGORY_NAME = "SELECT name FROM category WHERE category_id = ?";
    private static final String DELETE_EXPENSE = "DELETE FROM expense WHERE expense_id = ?";
    private static final String UPDATE_EXPENSE = "UPDATE expense SET description = ?, amount = ?, category_id = ?, expense_date = ? WHERE expense_id = ?";
    private static final String GET_TOTAL_AMOUNT = "SELECT SUM(amount) FROM expense WHERE category_id = ?";
    private static final String GET_TOTAL_AMOUNT_ALL = "SELECT SUM(amount) FROM expense";

    private Category getCategoryRow(ResultSet rs) throws SQLException{
        Category cat = new Category();
        cat.setCategory_id(rs.getInt("category_id"));
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
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(ADD_EXPENSE)){
                ps.setDouble(1, expense.getAmount());
                ps.setDate(2, new java.sql.Date(expense.getDate().getTime()));
                ps.setInt(3, expense.getCategory_id());
                ps.setString(4, expense.getDescription());
                ps.executeUpdate();
            }
    }

    public void deleteCategory(int categoryId) throws SQLException{
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_CATEGORY)){
                ps.setInt(1, categoryId);
                ps.executeUpdate();
            }
    }

    public void deleteExpense(int expenseId) throws SQLException{
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_EXPENSE)){
                ps.setInt(1, expenseId);
                ps.executeUpdate();
            }
    }

    public List<String> getCategoryNames() throws SQLException{
        List<String> names = new ArrayList<>();
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(FILTER_NAMES);
            ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    names.add(rs.getString("name"));
                }
            }
        return names;
    }
     
    public int getCategoryIdByName(String name) throws SQLException{
        int categoryId = -1;
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(GET_CATEGORY_ID)){
                ps.setString(1, name);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        categoryId = rs.getInt("category_id");
                    }
                }
            }
        return categoryId;
    }

    public String getCategoryNameById(int categoryId) throws SQLException{
        String name = null;
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(GET_CATEGORY_NAME)){
                ps.setInt(1, categoryId);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        name = rs.getString("name");
                    }
                }
            }
        return name;
    }

    public void updateExpense(Expense expense) throws SQLException{
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_EXPENSE)){
                ps.setString(1, expense.getDescription());
                ps.setDouble(2, expense.getAmount());
                ps.setInt(3, expense.getCategory_id());
                ps.setDate(4, new java.sql.Date(expense.getDate().getTime()));
                ps.setInt(5, expense.getExpense_id());
                ps.executeUpdate();
            }
    }

    public double getTotalAmountByCategory(int categoryId) throws SQLException{
        double total = 0.0;
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(GET_TOTAL_AMOUNT)){
                ps.setInt(1, categoryId);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        total = rs.getDouble(1);
                    }
                }
            }
        return total;
    }   

    public double getTotalAmountAllCategories() throws SQLException{
        double total = 0.0;
        try(Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(GET_TOTAL_AMOUNT_ALL);
            ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    total = rs.getDouble(1);
                }
            }
        return total;
    }
}