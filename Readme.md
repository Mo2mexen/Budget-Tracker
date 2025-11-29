# Budget Tracker
A desktop application for managing personal finances built with Java, JavaFX, and MySQL.

## Features
- Track income and expenses across multiple accounts
- Set monthly budgets and monitor spending
- Create and track savings goals
- Export data to CSV/JSON
- Visual reports with charts

## Technology Stack
- **Backend**: Java (OOP)
- **GUI**: JavaFX
- **Database**: MySQL
- **File Export**: CSV & JSON

## Project Structure
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
│   ├── database/
│   │   └── DatabaseManager.java
│   ├── controllers/
│   │   └── DashboardController.java
│   └── Main.java
├── lib/
│   └── mysql-connector-java-8.0.33.jar
└── database/
    └── schema.sql


## Database Schema
- **users**: User accounts and profiles
- **accounts**: Bank accounts (checking, savings, cash, credit)
- **categories**: Income/expense categories
- **transactions**: All financial transactions
- **budgets**: Monthly spending limits
- **goals**: Savings goals with progress tracking

### Database Setup
1. Install MySQL
2. Create database: `CREATE DATABASE budget_tracker;`
3. Run `database/schema.sql`

## Usage Flow
1. Login with username/password
2. Add accounts (checking, savings, etc.)
3. Create categories (Food, Transport, Salary)
4. Record transactions
5. Set monthly budgets
6. Track savings goals
7. View reports and export data

## Development Phases
- **Phase 1**: Core classes (User, Account, Category, Transaction, Budget, Goal)
- **Phase 2**: FileManager (CSV/JSON export/import) 
- **Phase 3**: DatabaseManager (MySQL CRUD operations)
- **Phase 4**: JavaFX GUI (login, dashboard, charts)