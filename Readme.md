# Budget Tracker
A desktop application for managing personal finances built with Java, JavaFX, and MySQL.

## Features
- Track income and expenses across multiple accounts
- Set monthly budgets and monitor spending
- Create and track savings goals
- **Export data to CSV/JSON files using Gson and Apache Commons CSV libraries**
- Visual reports with charts

## Technology Stack
- **Backend**: Java (OOP)
- **GUI**: JavaFX
- **Database**: MySQL
- **Libraries**: 
  - **Gson 2.10.1** - Converts Java objects to JSON for data backup
  - **Apache Commons CSV 1.10.0** - Creates CSV files that open in Excel

## Project Structure
```
BudgetTracker/
├── models/
│   ├── User.java
│   ├── Account.java
│   ├── Category.java
│   ├── Transaction.java
│   ├── Budget.java
│   └── Goal.java
├── utils/
│   └── FileManager.java
├── lib/
│   ├── gson-2.10.1.jar
│   └── commons-csv-1.10.0.jar
├── reports/                    ← Auto-generated exports
│   ├── transactions_*.csv
│   ├── budgets_*.csv
│   ├── goals_*.csv
│   └── backup_*.json
└── TestFileManager.java
```

## Database Schema
- **users**: User accounts and profiles
- **accounts**: Bank accounts (checking, savings, cash, credit)
- **categories**: Income/expense categories
- **transactions**: All financial transactions
- **budgets**: Monthly spending limits
- **goals**: Savings goals with progress tracking

## Database Setup
1. Install MySQL
2. Create database: `CREATE DATABASE budget_tracker;`
3. Run `database/schema.sql`

## File Export Feature
FileManager uses two external libraries to export data:
- **Gson** - Automatically converts all user data to JSON format for complete backup
- **Apache Commons CSV** - Generates CSV files that can be opened directly in Excel

All exports are saved in the `reports/` folder with timestamps.

## Usage Flow
1. Login with username/password
2. Add accounts (checking, savings, etc.)
3. Create categories (Food, Transport, Salary)
4. Record transactions
5. Set monthly budgets
6. Track savings goals
7. Export to CSV/JSON from reports folder

