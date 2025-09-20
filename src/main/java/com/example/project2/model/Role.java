package com.example.project2.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Роли пользователей в системе")
public enum Role {
    @Schema(description = "Администратор системы")
    ADMIN("Administrator"),
    @Schema(description = "Обычный пользователь")
    USER("User");
    
    private final String displayName;
    
    Role(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

