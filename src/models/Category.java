package models;

public class Category extends BaseEntity implements Displayable {
    private String categoryName;
    private CategoryType type;
    private boolean isDefault;

    public enum CategoryType {
        INCOME, EXPENSE
    }

    public Category(int id, int userId, String categoryName, CategoryType type, boolean isDefault) {
        super(id, userId);
        this.categoryName = categoryName;
        this.type = type;
        this.isDefault = isDefault;
    }

    public boolean isIncome() {
        return type == CategoryType.INCOME;
    }

    public boolean isExpense() {
        return type == CategoryType.EXPENSE;
    }

    @Override
    public String getDisplayInfo() {
        return categoryName + " (" + type + ")";
    }

    @Override
    public String getFormattedDisplay() {
        return String.format("%s [%s] - %s",
                categoryName, type, isDefault ? "Default" : "Custom");
    }

    @Override
    public void printDetails() {
        System.out.println("Category Details:");
        System.out.println("Name: " + categoryName);
        System.out.println("Type: " + type);
        System.out.println("Default: " + (isDefault ? "Yes" : "No"));
        System.out.println("Created: " + getCreatedDate());
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return categoryName + " (" + type + ")";
    }
}