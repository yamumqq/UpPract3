package com.example.project2.controller;

import com.example.project2.dto.CreateUserDTO;
import com.example.project2.dto.UserDTO;
import com.example.project2.model.Role;
import com.example.project2.model.User;
import com.example.project2.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllUsers() throws Exception {
        // Подготовка данных
        User user1 = createTestUser(1L, "user1", "user1@test.com", Role.USER);
        User user2 = createTestUser(2L, "user2", "user2@test.com", Role.ADMIN);
        List<User> users = Arrays.asList(user1, user2);

        when(userService.findAll()).thenReturn(users);

        // Выполнение и проверка
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].email").value("user2@test.com"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"));
    }

    @Test
    public void testGetUserById() throws Exception {
        // Подготовка данных
        User user = createTestUser(1L, "testuser", "test@test.com", Role.USER);
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // Выполнение и проверка
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist()); // Пароль не должен возвращаться
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        // Подготовка данных
        when(userService.findById(999L)).thenReturn(Optional.empty());

        // Выполнение и проверка
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        // Подготовка данных
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("newuser");
        createUserDTO.setEmail("newuser@test.com");
        createUserDTO.setPassword("password123");
        createUserDTO.setRole(Role.USER);

        User savedUser = createTestUser(1L, "newuser", "newuser@test.com", Role.USER);
        
        when(userService.existsByUsername("newuser")).thenReturn(false);
        when(userService.existsByEmail("newuser@test.com")).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(savedUser);

        // Выполнение и проверка
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@test.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void testCreateUserWithExistingUsername() throws Exception {
        // Подготовка данных
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("existinguser");
        createUserDTO.setEmail("newuser@test.com");
        createUserDTO.setPassword("password123");
        createUserDTO.setRole(Role.USER);

        when(userService.existsByUsername("existinguser")).thenReturn(true);

        // Выполнение и проверка
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пользователь с именем 'existinguser' уже существует"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Подготовка данных
        User existingUser = createTestUser(1L, "olduser", "old@test.com", Role.USER);
        UserDTO updateDTO = new UserDTO();
        updateDTO.setUsername("newuser");
        updateDTO.setEmail("new@test.com");
        updateDTO.setRole(Role.ADMIN);
        updateDTO.setIsActive(true);

        User updatedUser = createTestUser(1L, "newuser", "new@test.com", Role.ADMIN);
        
        when(userService.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userService.existsByUsername("newuser")).thenReturn(false);
        when(userService.existsByEmail("new@test.com")).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(updatedUser);

        // Выполнение и проверка
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("new@test.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Подготовка данных
        User user = createTestUser(1L, "testuser", "test@test.com", Role.USER);
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // Выполнение и проверка
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь успешно удален"));
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        // Подготовка данных
        when(userService.findById(999L)).thenReturn(Optional.empty());

        // Выполнение и проверка
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        // Подготовка данных
        User user = createTestUser(1L, "testuser", "test@test.com", Role.USER);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Выполнение и проверка
        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    public void testDeactivateUser() throws Exception {
        // Подготовка данных
        User user = createTestUser(1L, "testuser", "test@test.com", Role.USER);
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // Выполнение и проверка
        mockMvc.perform(patch("/api/users/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь деактивирован"));
    }

    @Test
    public void testActivateUser() throws Exception {
        // Подготовка данных
        User user = createTestUser(1L, "testuser", "test@test.com", Role.USER);
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // Выполнение и проверка
        mockMvc.perform(patch("/api/users/1/activate"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь активирован"));
    }

    // Вспомогательный метод для создания тестового пользователя
    private User createTestUser(Long id, String username, String email, Role role) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password123");
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsActive(true);
        return user;
    }
}
