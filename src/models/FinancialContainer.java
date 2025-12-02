package models;

import java.util.ArrayList;
import java.util.List;

// Generic class for storing financial items 
public class FinancialContainer<T extends BaseEntity> {
    private List<T> items;
    private String containerName;

    public FinancialContainer(String containerName) {
        this.items = new ArrayList<>();
        this.containerName = containerName;
    }

    public void addItem(T item) {
        items.add(item);
    }

    public void removeItem(T item) {
        items.remove(item);
    }

    public T getItemById(int id) {
        for (T item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public List<T> getAllItems() {
        return new ArrayList<>(items);
    }

    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    
    public void displayAll() {
        System.out.println("=== " + containerName + " ===");
        for (T item : items) {
            System.out.println(item.getDisplayInfo());
        }
        System.out.println("Total items: " + getCount());
    }

    public String getContainerName() {
        return containerName;
    }
}
