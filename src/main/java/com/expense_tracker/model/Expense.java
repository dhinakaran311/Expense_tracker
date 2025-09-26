package com.expense_tracker.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Expense {
    private int expense_id;
    private String name;
    private double amount;
    private Date date;
    private int category_id;
    private String category_name;  // Add category name field
    private LocalDateTime time;
    private String description;
    public Expense(){
        this.expense_id = 0;
        this.name = "";
        this.amount = 0.0;
        this.date = new Date();
        this.category_id = 0;
        this.category_name = "";
        this.time = LocalDateTime.now();
        this.description = "";
    }
    public Expense(int expense_id, String name, double amount, Date date, int category_id, String category_name, LocalDateTime time, String description) {
        this.expense_id = expense_id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.category_id = category_id;
        this.category_name = category_name;
        this.time = time;           
        this.description = description;
    }
    public int getExpense_id() {
        return expense_id;
    }
    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getCategory_id() {
        return category_id;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setId(int int1) {
        this.expense_id = int1;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}