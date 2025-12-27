package gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Category;
import models.Category.CategoryType;
import database.CategoryDAO;
import gui.views.CategoriesView;
import java.util.Optional;

public class CategoriesController {
    private CategoriesView view;
    private CategoryDAO categoryDAO;

    public CategoriesController(CategoriesView view) {
        this.view = view;
        this.categoryDAO = new CategoryDAO();
        attachEventHandlers();
        loadCategories();
    }

    private void attachEventHandlers() {
        view.getAddButton().setOnAction(event -> addCategory());
        view.getEditButton().setOnAction(event -> editCategory());
        view.getDeleteButton().setOnAction(event -> deleteCategory());
        view.getRefreshButton().setOnAction(event -> loadCategories());
    }

    private void loadCategories() {
        view.getCategoriesTable().getItems().clear();
        view.getCategoriesTable().getItems().addAll(categoryDAO.getUserCategories(view.getUser().getId()));
    }

    private void addCategory() {
        Dialog<ButtonType> dialog = createCategoryDialog("Add Category", null);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        VBox content = (VBox) dialog.getDialogPane().getContent();
        TextField nameField = (TextField) content.getChildren().get(1);
        String categoryName = nameField.getText().trim();

        if (categoryName.isEmpty()) {
            showMessage("Error", "Name is required");
            return;
        }

        ComboBox<CategoryType> typeBox = (ComboBox<CategoryType>) content.getChildren().get(3);
        boolean success = categoryDAO.createCategory(view.getUser().getId(), categoryName, typeBox.getValue().name(), "", "");

        if (success) {
            showMessage("Success", "Category created");
            loadCategories();
        } else {
            showMessage("Error", "Failed to create category");
        }
    }

    private void editCategory() {
        Category selectedCategory = view.getCategoriesTable().getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showMessage("Warning", "Please select a category");
            return;
        }

        if (selectedCategory.isDefault()) {
            showMessage("Warning", "Cannot edit default category");
            return;
        }

        Dialog<ButtonType> dialog = createCategoryDialog("Edit Category", selectedCategory);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        VBox content = (VBox) dialog.getDialogPane().getContent();
        TextField nameField = (TextField) content.getChildren().get(1);
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

    private Dialog<ButtonType> createCategoryDialog(String title, Category category) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField nameField = new TextField(category != null ? category.getCategoryName() : "");

        ComboBox<CategoryType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(CategoryType.values());
        typeBox.setValue(category != null ? category.getType() : CategoryType.EXPENSE);
        typeBox.setDisable(category != null);

        dialogBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Type:"), typeBox
        );

        dialog.getDialogPane().setContent(dialogBox);
        return dialog;
    }

    private void deleteCategory() {
        Category selectedCategory = view.getCategoriesTable().getSelectionModel().getSelectedItem();
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

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        boolean success = categoryDAO.deleteCategory(selectedCategory.getId());
        if (success) {
            showMessage("Success", "Category deleted");
            loadCategories();
        } else {
            showMessage("Error", "Failed to delete category");
        }
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
