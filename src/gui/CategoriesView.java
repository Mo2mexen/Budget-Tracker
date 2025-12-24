package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.User;
import models.Category;
import models.Category.CategoryType;
import database.CategoryDAO;

public class CategoriesView {
    private User user;
    private CategoryDAO dao = new CategoryDAO();
    private TableView<Category> table = new TableView<>();
    private VBox view;

    public CategoriesView(User user) {
        this.user = user;
        this.view = create();
    }

    private VBox create() {
        VBox v = new VBox(15);
        v.setPadding(new Insets(20));

        Label title = new Label("Categories");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER_LEFT);
        btns.getChildren().addAll(
            createBtn("Add", "#27ae60", e -> addDialog()),
            createBtn("Edit", "#3498db", e -> editDialog()),
            createBtn("Delete", "#e74c3c", e -> deleteDialog()),
            createBtn("Refresh", "#95a5a6", e -> refresh())
        );

        TableColumn<Category, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Category, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        nameCol.setPrefWidth(200);

        TableColumn<Category, CategoryType> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(120);

        TableColumn<Category, String> colorCol = new TableColumn<>("Color");
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorCol.setPrefWidth(100);

        TableColumn<Category, String> iconCol = new TableColumn<>("Icon");
        iconCol.setCellValueFactory(new PropertyValueFactory<>("icon"));
        iconCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, nameCol, typeCol, colorCol, iconCol);
        refresh();

        v.getChildren().addAll(title, btns, table);
        return v;
    }

    private Button createBtn(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        btn.setOnAction(action);
        return btn;
    }

    private void refresh() {
        table.getItems().clear();
        table.getItems().addAll(dao.getUserCategories(user.getId()));
    }

    private void addDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Category");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        ComboBox<CategoryType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(CategoryType.values());
        typeCombo.setValue(CategoryType.EXPENSE);
        TextField colorField = new TextField("#FF5722");
        TextField iconField = new TextField("💰");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeCombo, 1, 1);
        grid.add(new Label("Color:"), 0, 2);
        grid.add(colorField, 1, 2);
        grid.add(new Label("Icon:"), 0, 3);
        grid.add(iconField, 1, 3);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    alert(Alert.AlertType.ERROR, "Error", "Name required");
                    return;
                }
                if (dao.createCategory(user.getId(), name, typeCombo.getValue().name(),
                    colorField.getText().trim(), iconField.getText().trim())) {
                    alert(Alert.AlertType.INFORMATION, "Success", "Category created");
                    refresh();
                } else {
                    alert(Alert.AlertType.ERROR, "Error", "Failed");
                }
            }
        });
    }

    private void editDialog() {
        Category cat = table.getSelectionModel().getSelectedItem();
        if (cat == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select category");
            return;
        }
        if (cat.isDefault()) {
            alert(Alert.AlertType.WARNING, "Warning", "Cannot edit default");
            return;
        }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Edit Category");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(cat.getCategoryName());
        TextField colorField = new TextField(cat.getColor());
        TextField iconField = new TextField(cat.getIcon());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Color:"), 0, 1);
        grid.add(colorField, 1, 1);
        grid.add(new Label("Icon:"), 0, 2);
        grid.add(iconField, 1, 2);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    alert(Alert.AlertType.ERROR, "Error", "Name required");
                    return;
                }
                if (dao.updateCategory(cat.getId(), name, colorField.getText().trim(), iconField.getText().trim())) {
                    alert(Alert.AlertType.INFORMATION, "Success", "Updated");
                    refresh();
                } else {
                    alert(Alert.AlertType.ERROR, "Error", "Failed");
                }
            }
        });
    }

    private void deleteDialog() {
        Category cat = table.getSelectionModel().getSelectedItem();
        if (cat == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select category");
            return;
        }
        if (cat.isDefault()) {
            alert(Alert.AlertType.WARNING, "Warning", "Cannot delete default");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + cat.getCategoryName() + "?",
            ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (dao.deleteCategory(cat.getId())) {
                alert(Alert.AlertType.INFORMATION, "Success", "Deleted");
                refresh();
            } else {
                alert(Alert.AlertType.ERROR, "Error", "Failed");
            }
        }
    }

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}
