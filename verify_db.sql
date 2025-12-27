-- Verification script to check database structure
USE budget_tracker;

-- Show categories table structure (should NOT have color or icon columns)
SHOW COLUMNS FROM categories;

-- Count existing categories
SELECT COUNT(*) as total_categories FROM categories;

-- Show sample categories
SELECT * FROM categories LIMIT 5;
