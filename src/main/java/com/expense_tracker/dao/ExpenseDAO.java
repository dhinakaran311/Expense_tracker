package com.expense_tracker.dao;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;

import com.expense_tracker.model.Category;
import com.expense_tracker.model.Expense;
import com.expense_tracker.util.DatabaseUtil;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
public class ExpenseDAO {
    private static final String SELECT_ALL = "SELECT * FROM category";
    private static final String SELECT_EXP = "SELECT * FROM expense";
    private static final String SELECT_EXP_BY_ID = "SELECT * FROM expense WHERE category_id = ?";
    private static final String ADD_CATEGORY = "INSERT INTO category (name) VALUES (?)";
    private static final String FILTER_NAMES = "SELECT name FROM category";
    private static final String GET_CATEGORY_ID = "SELECT category_id FROM category WHERE name = ?";
    private static final String ADD_EXPENSE = "INSERT INTO expense (amount, expense_date, category_id, description) VALUES (?, ?, ?, ?)";
    private static final String GET_CATEGORY_NAME = "SELECT name FROM category WHERE category_id = ?";
    private static final String DELETE_EXPENSE = "DELETE FROM expense WHERE expense_id = ?";
    private static final String UPDATE_EXPENSE = "UPDATE expense SET amount = ?, expense_date = ?, category_id = ?, description = ? WHERE expense_id = ?";
    private static final String GET_TOTAL_AMOUNT = "SELECT SUM(amount) FROM expense WHERE category_id = ?";
    private static final String GET_TOTAL_AMOUNT_ALL = "SELECT SUM(amount) FROM expense";
    private static final String UPDATE_CATEGORY = "UPDATE category SET name = ? WHERE category_id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM category WHERE category_id = ?";
    
    public int getCategoryID(String categoryName) {
        try (Connection conn = DatabaseUtil.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_CATEGORY_ID)) {
            
            stmt.setString(1, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("category_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if category not found
    }
    
    public List<Expense> filterExpenseByCategory(int categoryId) {
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_EXP_BY_ID)) {
            
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense();
                    expense.setExpense_id(rs.getInt("expense_id"));
                    expense.setAmount(rs.getDouble("amount"));
                    expense.setDate(new java.util.Date(rs.getTimestamp("expense_date").getTime()));
                    expense.setTime(rs.getTimestamp("expense_date").toLocalDateTime());
                    expense.setCategory_id(rs.getInt("category_id"));
                    
                    // Get category name using the category_id
                    String categoryName = getCategoryName(rs.getInt("category_id"));
                    expense.setCategory_name(categoryName);
                    
                    expense.setDescription(rs.getString("description"));
                    expenses.add(expense);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }
    


    private Category getCategoryRow(ResultSet rs) throws SQLException{
        int id = rs.getInt("category_id");
        String catname = rs.getString("name");
        return new Category(id, catname);
    }

    private Expense getExpenseRow(ResultSet rs) throws SQLException
    {
        int id = rs.getInt("expense_id");
        String description = rs.getString("description");
        double amount = rs.getDouble("amount");
        int categoryId = rs.getInt("category_id");
        Date date = rs.getDate("expense_date");
        LocalDateTime time = rs.getTimestamp("expense_date") != null ? 
                           rs.getTimestamp("expense_date").toLocalDateTime() : 
                           LocalDateTime.now();
        
        // Get category name
        String categoryName = "";
        try (Connection con = DatabaseUtil.getDBConnection();
             PreparedStatement stmt = con.prepareStatement(GET_CATEGORY_NAME)) {
            stmt.setInt(1, categoryId);
            ResultSet categoryRs = stmt.executeQuery();
            if (categoryRs.next()) {
                categoryName = categoryRs.getString("name");
            }
        }
        
        // Using description as the name since expense_name doesn't exist in the schema
        return new Expense(id, description, amount, date, categoryId, categoryName, time, description);
    }

    public List<Category> getAllcat() throws SQLException{
        List<Category> cats = new ArrayList<>();
        try(
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_ALL)) {
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    cats.add(getCategoryRow(rs));
                }
            }
        return cats;
    }


    public List<Expense> getAllExpenses() throws SQLException
    {
        List<Expense> exps = new ArrayList<>();
        try(
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_EXP)) {
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    exps.add(getExpenseRow(rs));
                }
            }
        return exps;
    }
    public List<Expense> getExpensesByCategory(int id) throws SQLException
    {
        List<Expense> exps = new ArrayList<>();
        try(
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(SELECT_EXP_BY_ID)) {
                stmt.setInt(1,id);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    exps.add(getExpenseRow(rs));
                }
            }
        return exps;
    }
    public void addCategory( String name) throws SQLException{
        try(
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(ADD_CATEGORY)
        ){
            stmt.setString(1,name);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Creating category failed, no rows affected.");
            }
        }
    }

    /**
     * Gets all category names as a List of Strings
     * @return List of category names
     * @throws SQLException if a database access error occurs
     */
    public List<String> getAllcatnames() throws SQLException {
        List<String> names = new ArrayList<>();
        try (
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(FILTER_NAMES);
            ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        }
        return names;
    }
    public int getCategoryId(String name) throws SQLException {
        int id = -1;
        try (
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(GET_CATEGORY_ID)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("category_id");
                }
            }
        }
        return id;
    }
    public String getCategoryName(int id) throws SQLException {
        String name = null;
        try (
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(GET_CATEGORY_NAME)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                }
            }
        }
        return name;
    }
    
    /**
     * Gets all category names as a String array
     * @return Array of category names
     * @throws SQLException if a database access error occurs
     */
    public String[] getAllCategoryNames() throws SQLException {
        List<String> names = getAllcatnames();
        return names.toArray(new String[0]);
    }
    public void addExpense(Expense expense) throws SQLException{
        try(
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(ADD_EXPENSE, Statement.RETURN_GENERATED_KEYS)
        ){
            stmt.setDouble(1, expense.getAmount());
            stmt.setDate(2, new java.sql.Date(expense.getDate().getTime()));
            stmt.setInt(3, expense.getCategory_id());
            stmt.setString(4, expense.getDescription());
            
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Creating expense failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    expense.setExpense_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating expense failed, no ID obtained.");
                }
            }
        }
    }
    public void deleteExpense(int id) throws SQLException{
        try(
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_EXPENSE)
        ){
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new SQLException("Deleting expense failed, no rows affected.");
            }
        }
    }

    public boolean updateExpense(Expense expense) throws SQLException{
        try(
            Connection conn = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_EXPENSE);
        ){
            stmt.setDouble(1, expense.getAmount());
            stmt.setDate(2, new java.sql.Date(expense.getDate().getTime()));
            stmt.setInt(3, expense.getCategory_id());
            stmt.setString(4, expense.getDescription());
            stmt.setInt(5, expense.getExpense_id());
            int rows = stmt.executeUpdate();
            return rows>0;
        }

    }
    public double getTotalAmount(int catID) throws SQLException{
        double total = 0.0;
        try(
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(GET_TOTAL_AMOUNT)) {
                stmt.setInt(1, catID);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    total = rs.getDouble(1);
                }
            }

        return total;
    }
    public double getTotalAmountAll() throws SQLException{
        double total = 0.0;
        try(
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(GET_TOTAL_AMOUNT_ALL)) {
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    total = rs.getDouble(1);
                }
            }

        return total;
    }
    
    public boolean updateCategory(Category category) throws SQLException {
        try (
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_CATEGORY)
        ) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getCategory_id());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean deleteCategory(int categoryId) throws SQLException {
        try (
            Connection con = DatabaseUtil.getDBConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_CATEGORY)
        ) {
            stmt.setInt(1, categoryId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}