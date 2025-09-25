package com.expensetracker.dao;

import com.expensetracker.util.DatabaseUtil;
import java.sql.Connection;
import com.expensetracker.model.Category;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private static final String SELECT_ALL_ROW="SELECT * FROM category";
    private static final String INSERT_ROW="INSERT INTO category(name) VALUES(?)";  
    
    // Add a new category
}

