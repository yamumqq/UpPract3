package com.example.project2.model;

public enum Role {
    ADMIN("Администратор"),
    USER("Пользователь");
    
    private final String displayName;
    
    Role(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

