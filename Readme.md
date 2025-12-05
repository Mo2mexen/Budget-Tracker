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
│   ├── models/                    # Data models (OOP Architecture)
│   │   ├── BaseEntity.java        # Abstract parent (Inheritance & Polymorphism)
│   │   ├── Calculable.java        # Interface for financial calculations (Abstraction)
│   │   ├── Displayable.java       # Interface for display operations (Abstraction)
│   │   ├── User.java              # Standalone entity (Encapsulation)
│   │   ├── Account.java           # extends BaseEntity, implements Displayable
│   │   ├── Category.java          # extends BaseEntity, implements Displayable
│   │   ├── Transaction.java       # extends BaseEntity, implements Displayable
│   │   ├── Budget.java            # extends BaseEntity, implements Calculable & Displayable
│   │   └── Goal.java              # extends BaseEntity, implements Calculable & Displayable
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

## OOP Architecture

This project demonstrates the four pillars of Object-Oriented Programming:

### 1. Encapsulation
- All entity classes use private fields with public getters/setters
- Data hiding and controlled access to class members
- Example: User, Account, Transaction, Category, Budget, Goal

### 2. Inheritance
- **BaseEntity** (abstract parent class) provides common functionality
- Child classes: Account, Category, Transaction, Budget, Goal
- Inherits: id, userId, createdDate fields and their accessors
- Eliminates code duplication across entities

### 3. Polymorphism
- **getDisplayInfo()** abstract method defined in BaseEntity
- Each child class overrides with its own implementation
- Runtime polymorphism through method overriding
- Example: Budget displays progress, Transaction displays amount formatting

### 4. Abstraction
- **Calculable** interface: Defines financial calculation contracts
  - calculateTotal(), calculateRemaining(), calculatePercentage()
  - Implemented by: Budget, Goal
- **Displayable** interface: Defines display operation contracts
  - getFormattedDisplay(), printDetails()
  - Implemented by: Account, Category, Transaction, Budget, Goal

## Database Schema
Six main tables:
- **users** - User accounts and authentication
- **accounts** - Bank accounts (checking, savings, cash, credit card)
- **categories** - Income and expense categories
- **transactions** - Financial transactions
- **budgets** - Monthly spending limits per category
- **goals** - Savings goals with progress tracking

## Features
- User authentication (login/register)
- Multiple account management
- Transaction tracking (income/expense)
- Budget monitoring
- Savings goal tracking
- Monthly financial reports
- CSV export functionality
