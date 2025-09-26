package com.expensetracker.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Expense {
    private int expense_id;
    private double amount;
    private Date date;
    private int category_id;
    private LocalDateTime time;
    private String description;
    public Expense(){
        this.expense_id = 0;
        this.amount = 0.0;
        this.date = new Date();
        this.category_id = 0;
        this.time = LocalDateTime.now();
        this.description = "";
    }
    public Expense(int expense_id, double amount, Date date, int category_id, LocalDateTime time, String description) {
        this.expense_id = expense_id;
        this.amount = amount;
        this.date = date;
        this.category_id = category_id;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }

}