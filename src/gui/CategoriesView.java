package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.User;
import models.Category;
import models.Category.CategoryType;
import database.CategoryDAO;

public class CategoriesView {
    private User user;
    private CategoryDAO categoryDAO;
    private TableView<Category> categoriesTable;
    private VBox mainContainer;

    public CategoriesView(User user) {
        this.user = user;
        this.categoryDAO = new CategoryDAO();
        this.categoriesTable = new TableView<>();
        this.mainContainer = createView();
    }

    private VBox createView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        // Title
        Label titleLabel = new Label("Categories");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Create buttons
        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");

        addButton.setOnAction(event -> addCategory());
        editButton.setOnAction(event -> editCategory());
        deleteButton.setOnAction(event -> deleteCategory());
        refreshButton.setOnAction(event -> loadCategories());

        HBox buttonBox = new HBox(5);
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton, refreshButton);

        // Create table columns
        TableColumn<Category, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Category, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        TableColumn<Category, CategoryType> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        categoriesTable.getColumns().addAll(idColumn, nameColumn, typeColumn);
        loadCategories();

        container.getChildren().addAll(titleLabel, buttonBox, categoriesTable);
        return container;
    }

    private void loadCategories() {
        categoriesTable.getItems().clear();
        categoriesTable.getItems().addAll(categoryDAO.getUserCategories(user.getId()));
    }

    private void addCategory() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Category");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField nameField = new TextField();

        ComboBox<CategoryType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(CategoryType.values());
        typeBox.setValue(CategoryType.EXPENSE);

        dialogBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Type:"), typeBox
        );

        dialog.getDialogPane().setContent(dialogBox);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String categoryName = nameField.getText().trim();

                if (categoryName.isEmpty()) {
                    showMessage("Error", "Name is required");
                    return;
                }

                String type = typeBox.getValue().name();
                boolean success = categoryDAO.createCategory(user.getId(), categoryName, type, "", "");

                if (success) {
                    showMessage("Success", "Category created");
                    loadCategories();
                } else {
                    showMessage("Error", "Failed to create category");
                }
            }
        });
    }

    private void editCategory() {
        Category selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) {
            showMessage("Warning", "Please select a category");
            return;
        }

        if (selectedCategory.isDefault()) {
            showMessage("Warning", "Cannot edit default category");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Category");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField nameField = new TextField(selectedCategory.getCategoryName());

        dialogBox.getChildren().addAll(
            new Label("Name:"), nameField
        );

        dialog.getDialogPane().setContent(dialogBox);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String categoryName = nameField.getText().trim();

                if (categoryName.isEmpty()) {
                    showMessage("Error", "Name is required");
                    return;
                }

                boolean success = categoryDAO.updateCategory(selectedCategory.getId(), categoryName, "", "");

                if (success) {
                    showMessage("Success", "Category updated");
                    loadCategories();
                } else {
                    showMessage("Error", "Failed to update category");
                }
            }
        });
    }

    private void deleteCategory() {
        Category selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) {
            showMessage("Warning", "Please select a category");
            return;
        }

        if (selectedCategory.isDefault()) {
            showMessage("Warning", "Cannot delete default category");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Delete category: " + selectedCategory.getCategoryName() + "?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = categoryDAO.deleteCategory(selectedCategory.getId());

                if (success) {
                    showMessage("Success", "Category deleted");
                    loadCategories();
                } else {
                    showMessage("Error", "Failed to delete category");
                }
            }
        });
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return mainContainer;
    }
}
