-- test data for Budget Tracker
USE budget_tracker;

-- Clear all data
DELETE FROM transactions;
DELETE FROM budgets;
DELETE FROM goals;
DELETE FROM accounts;
DELETE FROM categories WHERE user_id != 0;
DELETE FROM users;

-- Create a test user
INSERT INTO users (username, password, email, full_name, currency, created_date)
VALUES ('john_doe', 'password123', 'john@example.com', 'John Doe', 'USD', '2024-01-01');

-- Create default categories for this user
INSERT INTO categories (user_id, category_name, type, is_default) VALUES
(1, 'Salary', 'INCOME', TRUE),
(1, 'Business', 'INCOME', TRUE),
(1, 'Food', 'EXPENSE', TRUE),
(1, 'Transport', 'EXPENSE', TRUE),
(1, 'Shopping', 'EXPENSE', TRUE),
(1, 'Entertainment', 'EXPENSE', TRUE),
(1, 'Bills', 'EXPENSE', TRUE);

-- Create accounts
INSERT INTO accounts (user_id, account_name, account_type, balance, currency, created_date) VALUES
(1, 'Main Checking', 'CHECKING', 5000.00, 'USD', '2024-01-01'),
(1, 'Savings', 'SAVINGS', 15000.00, 'USD', '2024-01-01'),
(1, 'Cash', 'CASH', 500.00, 'USD', '2024-01-15');

-- Create budgets for November 2025
INSERT INTO budgets (user_id, category_id, monthly_limit, month, year, current_spent) VALUES
(1, 3, 500.00, 11, 2025, 320.00),  -- Food
(1, 4, 200.00, 11, 2025, 150.00),  -- Transport
(1, 5, 300.00, 11, 2025, 200.00);  -- Shopping

-- Create goals
INSERT INTO goals (user_id, goal_name, target_amount, current_amount, deadline, status, created_date) VALUES
(1, 'Emergency Fund', 10000.00, 6500.00, '2026-12-31', 'ACTIVE', '2025-01-01'),
(1, 'Vacation', 5000.00, 2000.00, '2026-06-30', 'ACTIVE', '2025-03-01');

-- Add transactions
INSERT INTO transactions (user_id, account_id, category_id, amount, description, transaction_date, type, notes) VALUES
(1, 1, 1, 5000.00, 'Monthly Salary', '2025-11-01', 'INCOME', 'Salary payment'),
(1, 1, 3, 120.00, 'Grocery Shopping', '2025-11-03', 'EXPENSE', 'Weekly groceries'),
(1, 1, 3, 85.00, 'Restaurant', '2025-11-05', 'EXPENSE', 'Dinner'),
(1, 1, 4, 60.00, 'Gas', '2025-11-04', 'EXPENSE', 'Full tank'),
(1, 1, 5, 150.00, 'Clothes', '2025-11-07', 'EXPENSE', 'Winter shopping');

SELECT 'Sample data loaded successfully!' AS message;
