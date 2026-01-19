# Expense Tracker

A desktop application for managing personal expenses with category organization, built using Java Swing and MySQL.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Building the Application](#building-the-application)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Expense Tracker is a Java-based desktop application that helps users manage their personal finances by tracking expenses across different categories. The application provides an intuitive graphical interface for adding, updating, deleting, and viewing expenses with detailed categorization.

## Features

### Category Management
- Create custom expense categories
- Update existing category names
- Delete unused categories
- View all categories in a table format

### Expense Tracking
- Add new expenses with detailed information
- Update existing expense records
- Delete expenses with confirmation
- Filter expenses by category
- View all expenses in a sortable table

### Data Management
- Track expense amount, date, category, and description
- Automatic date/time recording
- Category-based filtering
- Persistent storage in MySQL database

### User Interface
- Clean, intuitive Swing-based GUI
- System look-and-feel integration
- Single-selection table views
- Confirmation dialogs for destructive actions
- Error handling with user-friendly messages

## Technology Stack

- **Language**: Java 17
- **GUI Framework**: Java Swing
- **Database**: MySQL 8.0.33
- **Build Tool**: Apache Maven
- **Database Driver**: MySQL Connector/J 8.0.33
- **Architecture**: MVC Pattern with DAO Layer

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 17 or higher
  - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
  - Verify installation: `java -version`
  
- **Apache Maven**: Version 3.6 or higher
  - Download from [Maven Official Site](https://maven.apache.org/download.cgi)
  - Verify installation: `mvn -version`
  
- **MySQL Server**: Version 8.0 or higher
  - Download from [MySQL Official Site](https://dev.mysql.com/downloads/mysql/)
  - Ensure MySQL service is running

## Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Expense_tracker
```

### 2. Install Dependencies

Maven will automatically download required dependencies when you build the project:

```bash
mvn clean install
```

## Database Setup

### 1. Create Database

Open MySQL command line or MySQL Workbench and execute:

```sql
CREATE DATABASE expense_tracker;
USE expense_tracker;
```

### 2. Create Tables

Execute the following SQL to create required tables:

```sql
-- Create Category Table
CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Create Expense Table
CREATE TABLE expense (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    amount DOUBLE NOT NULL,
    expense_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    category_id INT NOT NULL,
    description TEXT,
    FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE CASCADE
);
```

### 3. Insert Sample Data (Optional)

```sql
-- Sample Categories
INSERT INTO category (name) VALUES 
    ('Food'),
    ('Transport'),
    ('Entertainment'),
    ('Utilities'),
    ('Healthcare');

-- Sample Expenses
INSERT INTO expense (amount, category_id, description) VALUES 
    (50.00, 1, 'Grocery shopping'),
    (20.00, 2, 'Bus fare'),
    (100.00, 3, 'Movie tickets');
```

## Configuration

### Database Credentials

Update the database connection settings in `src/main/java/com/expense_tracker/util/DatabaseUtil.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker?useSSL=true&serverTimezone=UTC";
private static final String USER = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

**Important Security Note**: For production use, move credentials to environment variables or a `.env` file instead of hardcoding them.

### Recommended: Using Environment Variables

1. Create a `.env` file in the project root:

```env
DB_URL=jdbc:mysql://localhost:3306/expense_tracker?useSSL=true&serverTimezone=UTC
DB_USER=your_mysql_username
DB_PASSWORD=your_mysql_password
```

2. Add `.env` to `.gitignore` to prevent committing credentials

3. Modify `DatabaseUtil.java` to read from environment variables (requires dotenv-java dependency)

## Usage

### Running the Application

#### Option 1: Using Maven

```bash
mvn exec:java
```

#### Option 2: Using Compiled JAR

```bash
# Build the JAR file
mvn package

# Run the JAR
java -jar target/expense-tracker-1.0.0.jar
```

### Application Workflow

#### 1. Main Window

When you launch the application, you'll see two main options:
- **Manage Categories**: Opens the category management interface
- **Manage Expenses**: Opens the expense tracking interface

#### 2. Managing Categories

**To Add a Category:**
1. Click "Manage Categories"
2. Enter the category name in the text field
3. Click "Add Category"

**To Update a Category:**
1. Select a category from the table
2. Modify the name in the text field
3. Click "Update Category"

**To Delete a Category:**
1. Select a category from the table
2. Click "Delete Category"
3. Confirm the deletion

#### 3. Managing Expenses

**To Add an Expense:**
1. Click "Manage Expenses"
2. Fill in the following fields:
   - Expense Name
   - Description
   - Amount
   - Category (select from dropdown)
3. Click "Add Expense"

**To Update an Expense:**
1. Select an expense from the table
2. Modify the fields as needed
3. Click "Update Expense"

**To Delete an Expense:**
1. Select an expense from the table
2. Click "Delete Expense"
3. Confirm the deletion

**To Filter Expenses:**
1. Use the category dropdown at the top
2. Select a category to view only its expenses
3. Select "All" to view all expenses

## Project Structure

```
Expense_tracker/
├── pom.xml                                      # Maven configuration
├── README.md                                    # This file
├── .gitignore                                   # Git ignore rules
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── expense_tracker/
│                   ├── Main.java                # Application entry point
│                   ├── dao/
│                   │   └── ExpenseDAO.java      # Data Access Object
│                   ├── gui/
│                   │   └── MainUI.java          # Swing UI components
│                   ├── model/
│                   │   ├── Category.java        # Category entity
│                   │   └── Expense.java         # Expense entity
│                   └── util/
│                       └── DatabaseUtil.java    # Database connection utility
└── target/                                      # Compiled output (generated)
```

## Building the Application

### Development Build

```bash
# Compile the project
mvn compile

# Run tests (if available)
mvn test

# Package as JAR
mvn package
```

### Creating Executable JAR

The project uses `maven-shade-plugin` to create an executable JAR with all dependencies:

```bash
mvn clean package
```

The executable JAR will be created at: `target/expense-tracker-1.0.0.jar`

### Running the JAR

```bash
java -jar target/expense-tracker-1.0.0.jar
```

## Troubleshooting

### Common Issues

**Database Connection Failed**
- Ensure MySQL server is running
- Verify database credentials in `DatabaseUtil.java`
- Check if `expense_tracker` database exists
- Confirm port 3306 is accessible

**JDBC Driver Not Found**
- Run `mvn clean install` to download dependencies
- Verify MySQL Connector is in `pom.xml`

**Application Won't Start**
- Check Java version: `java -version` (must be 17+)
- Verify Maven build completed successfully
- Check console for error messages

**Table Not Found Errors**
- Ensure all database tables are created
- Run the SQL schema provided in Database Setup

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Make your changes
4. Test thoroughly
5. Commit your changes: `git commit -m 'Add some feature'`
6. Push to the branch: `git push origin feature/your-feature`
7. Create a Pull Request

### Code Style Guidelines

- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Keep methods focused and concise
- Use proper exception handling

## Future Enhancements

- Add date range filtering for expenses
- Implement expense reports with charts
- Add budget tracking and alerts
- Export data to CSV/PDF
- Implement search functionality
- Add dark mode theme
- Create dashboard with expense analytics
- Implement user authentication
- Add recurring expense support

## License

This project is open source and available under the [MIT License](LICENSE).

## Support

For issues, questions, or suggestions:
- Open an issue on the GitHub repository
- Contact the maintainer

## Acknowledgements

- Built with [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/)
- Database powered by [MySQL](https://www.mysql.com/)
- Build automation with [Apache Maven](https://maven.apache.org/)

---

**Last Updated**: January 19, 2026
