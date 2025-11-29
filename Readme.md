# Budget Tracker
A desktop application for managing personal finances built with pure Java and MySQL.

## Features
- Track income and expenses across multiple accounts
- Set monthly budgets and monitor spending
- Create and track savings goals
- Export data to Excel-compatible CSV files


## Technology Stack
- **Backend**: Java (OOP)
- **GUI**: JavaFX
- **Database**: MySQL


## Project Structure
```
BudgetTracker/
├── src/
│   ├── models/
│   │   ├── User.java
│   │   ├── Account.java
│   │   ├── Category.java
│   │   ├── Transaction.java
│   │   ├── Budget.java
│   │   └── Goal.java
│   ├── utils/
│   │   └── FileManager.java
│   └── database/
│       └── DatabaseManager.java
├── bin/                    (compiled classes)
├── reports/                (auto-created folder)
│   ├── transactions_*.csv
│   ├── budgets_*.csv
│   ├── goals_*.csv
│   └── complete_report_*.csv
└── database/
    └── schema.sql
```

## Database Schema
- **users**: User accounts and profiles
- **accounts**: Bank accounts (checking, savings, cash, credit)
- **categories**: Income/expense categories
- **transactions**: All financial transactions
- **budgets**: Monthly spending limits
- **goals**: Savings goals with progress tracking

## File Export Feature
Through the GUI, users can export their data to CSV files that open directly in Excel:
- **Transactions Export** → `transactions_29_11_2025.csv`
- **Budgets Export** → `budgets_29_11_2025.csv`
- **Goals Export** → `goals_29_11_2025.csv`
- **Complete Report** → `complete_report_29_11_2025.csv` (all data combined)
All files are automatically saved in the `reports/` folder with the current date.
Files contain raw data only - charts are displayed in the GUI application.

## Usage Flow
1. Login with username/password
2. Add accounts (checking, savings, etc.)
3. Create categories (Food, Transport, Salary)
4. Record transactions
5. Set monthly budgets
6. Track savings goals
7. View visual charts in GUI
8. Export selected data to Excel via GUI buttons

