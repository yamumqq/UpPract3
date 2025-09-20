package com.example.project2.dto;

import com.example.project2.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "DTO для передачи данных пользователя (без пароля)")
public class UserDTO {
    
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;
    
    @Schema(description = "Имя пользователя", example = "john_doe", required = true)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Schema(description = "Email адрес пользователя", example = "john@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;
    
    @Schema(description = "Дата создания пользователя", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Дата последнего обновления", example = "2024-01-15T12:00:00")
    private LocalDateTime updatedAt;
    
    @Schema(description = "Статус активности пользователя", example = "true")
    private Boolean isActive;
    
    // Конструкторы
    public UserDTO() {}
    
    public UserDTO(Long id, String username, String email, Role role, 
                   LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
