# Budget Tracker

A personal finance management application built with Java and MySQL.

## Overview

Budget Tracker helps users manage their finances by tracking income, expenses, budgets, and savings goals. The application uses a MySQL database for persistent storage and will feature a GUI interface.

## Technology Stack

- **Language**: Java
- **Database**: MySQL
- **GUI**: JavaFX

## Project Structure

```
Budget-Tracker/
├── src/
│   ├── Main.java                  # Application entry point
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
│   ├── gui/                       # GUI components
│   └── reports/                   # Generated CSV exports
│   └── lib/                       # External libraries (MySQL connector)
└── bin/                           # Compiled class files
```

## Features
- User authentication (login/register)
- Multiple account management
- Transaction tracking (income/expense)
- Budget monitoring
- Savings goal tracking
- Monthly financial reports
- CSV export functionality
