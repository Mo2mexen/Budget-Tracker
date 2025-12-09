package models;

public class Category extends BaseEntity implements Displayable {
    private String categoryName;
    private CategoryType type;
    private String color;
    private String icon;
    private boolean isDefault;

    public enum CategoryType {
        INCOME, EXPENSE
    }

    public Category(int id, int userId, String categoryName, CategoryType type, String color, String icon, boolean isDefault) {
        super(id, userId);
        this.categoryName = categoryName;
        this.type = type;
        this.color = color;
        this.icon = icon;
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
        return icon + " " + categoryName + " (" + type + ")";
    }

    @Override
    public String getFormattedDisplay() {
        return String.format("%s %s [%s] - %s",
                icon, categoryName, type, isDefault ? "Default" : "Custom");
    }

    @Override
    public void printDetails() {
        System.out.println("Category Details:");
        System.out.println("Name: " + categoryName);
        System.out.println("Type: " + type);
        System.out.println("Icon: " + icon);
        System.out.println("Color: " + color);
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return icon + " " + categoryName + " (" + type + ")";
    }
}