# Budget Tracker

A personal finance management application built with Java and MySQL.

## Overview

Budget Tracker helps users manage their finances by tracking income, expenses, budgets, and savings goals. The application uses a MySQL database for persistent storage and features both CLI and GUI interfaces.

## Technology Stack

- **Language**: Java
- **Database**: MySQL
- **GUI**: JavaFX
- **JDBC Driver**: MySQL Connector/J 9.1.0

## Project Structure

```
Budget-Tracker/
├── src/
│   ├── Main.java                  # CLI entry point
│   ├── models/                    # Data models (classes)
│   │   ├── BaseEntity.java        # Abstract parent
│   │   ├── Calculable.java        # Interface for financial calculations
│   │   ├── Displayable.java       # Interface for display operations
│   │   ├── User.java              # Standalone root entity
│   │   ├── Account.java
│   │   ├── Category.java
│   │   ├── Transaction.java
│   │   ├── Budget.java
│   │   └── Goal.java
│   ├── database/                  # Database layer (DAO Pattern)
│   │   ├── DatabaseConnection.java
│   │   ├── UserDAO.java
│   │   ├── AccountDAO.java
│   │   ├── CategoryDAO.java
│   │   ├── TransactionDAO.java
│   │   ├── BudgetDAO.java
│   │   ├── GoalDAO.java
│   │   ├── schema.sql
│   │   └── sample_data.sql
│   ├── utils/                     # Utility classes
│   │   └── FileManager.java       # CSV export functionality
│   ├── gui/                       # JavaFX GUI components (MVC Pattern)
│   │   ├── BudgetTrackerApp.java  # GUI entry point
│   │   ├── views/                 # View layer (UI only)
│   │   │   ├── AccountsView.java
│   │   │   ├── BudgetsView.java
│   │   │   ├── CategoriesView.java
│   │   │   ├── GoalsView.java
│   │   │   ├── LoginView.java
│   │   │   ├── MainAppView.java
│   │   │   ├── ProfileView.java
│   │   │   ├── RegisterView.java
│   │   │   ├── ReportsView.java
│   │   │   └── TransactionsView.java
│   │   └── controllers/           # Controller layer (Event handling)
│   │       ├── AccountsController.java
│   │       ├── BudgetsController.java
│   │       ├── CategoriesController.java
│   │       ├── GoalsController.java
│   │       ├── LoginController.java
│   │       ├── MainAppController.java
│   │       ├── ProfileController.java
│   │       ├── RegisterController.java
│   │       ├── ReportsController.java
│   │       └── TransactionsController.java
│   └── lib/                       # External libraries
│       ├── mysql-connector-j-9.1.0.jar
│       └── javafx-sdk-23.0.1/
├── bin/                           # Compiled class files
├── reports/                       # Generated CSV exports
├── run.bat                        # CLI launcher
└── run-gui.bat                    # GUI launcher
```

## Features

- User authentication (login/register)
- Multiple account management (Checking, Savings, Cash, Credit Card)
- Transaction tracking (income/expense/transfer)
- Budget monitoring with status indicators
- Savings goal tracking with progress
- Monthly financial reports
- CSV export functionality
- Multi-currency support
- Password management

## Application Architecture

```
BudgetTrackerApp (Entry Point - launches app)
    ↓
LoginView (First screen)
    ↓
MainAppView (Main Frame - contains everything after login)
    ↓ (switches between)
    ├── ProfileView
    ├── AccountsView
    ├── CategoriesView
    ├── TransactionsView
    ├── BudgetsView
    ├── GoalsView
    └── ReportsView
```

## User Interfaces

### CLI (Command Line Interface)
Console-based interface for terminal users.


### GUI (Graphical User Interface)
Full-featured JavaFX desktop application with:
- Navigation menu for easy access
- CRUD operations for all entities
- Color-coded budget status indicators
- User profile management
- Data export to CSV

## Notes

- Both CLI and GUI share the same database
- Data created in CLI is accessible from GUI and vice versa
- GUI uses pure Java code (no FXML files)
- All business logic is in the DAO layer

# RUN MAIN USING RUN.BAT FILE OR GUI USING RUN-GUI.BAT AFTER SETTING UP THE DATABASE
