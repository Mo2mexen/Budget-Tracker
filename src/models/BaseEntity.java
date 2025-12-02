package models;

import java.time.LocalDate;

// Base class for all entities 
public abstract class BaseEntity {
    protected int id;
    protected int userId;
    protected LocalDate createdDate;

    public BaseEntity(int id, int userId) {
        this.id = id;
        this.userId = userId;
        this.createdDate = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    // Abstract method (Polymorphism)
    public abstract String getDisplayInfo();
}
