package com.example.project2.controller;

import com.example.project2.dto.CreateUserDTO;
import com.example.project2.dto.UserDTO;
import com.example.project2.model.Role;
import com.example.project2.model.User;
import com.example.project2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Для тестирования с Postman
@Tag(name = "User Management", description = "API для управления пользователями")
public class UserRestController {

    @Autowired
    private UserService userService;

    /**
     * Получить всех пользователей
     * GET /api/users
     */
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Получить пользователя по ID
     * GET /api/users/{id}
     */
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(convertToDTO(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создать нового пользователя
     * POST /api/users
     */
    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или пользователь уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<?> createUser(
            @Parameter(description = "Данные нового пользователя", required = true)
            @Valid @RequestBody CreateUserDTO createUserDTO) {
        try {
            // Проверяем, не существует ли уже пользователь с таким username или email
            if (userService.existsByUsername(createUserDTO.getUsername())) {
                return ResponseEntity.badRequest()
                    .body("Пользователь с именем '" + createUserDTO.getUsername() + "' уже существует");
            }
            
            if (userService.existsByEmail(createUserDTO.getEmail())) {
                return ResponseEntity.badRequest()
                    .body("Пользователь с email '" + createUserDTO.getEmail() + "' уже существует");
            }

            User user = new User();
            user.setUsername(createUserDTO.getUsername());
            user.setEmail(createUserDTO.getEmail());
            user.setPassword(createUserDTO.getPassword());
            user.setRole(createUserDTO.getRole());

            User savedUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    /**
     * Обновить пользователя
     * PUT /api/users/{id}
     */
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные существующего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или пользователь уже существует"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id, 
            @Parameter(description = "Новые данные пользователя", required = true)
            @Valid @RequestBody UserDTO userDetails) {
        try {
            Optional<User> userOptional = userService.findById(id);
            if (!userOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOptional.get();
            
            // Проверяем уникальность username (если изменился)
            if (!user.getUsername().equals(userDetails.getUsername()) && 
                userService.existsByUsername(userDetails.getUsername())) {
                return ResponseEntity.badRequest()
                    .body("Пользователь с именем '" + userDetails.getUsername() + "' уже существует");
            }
            
            // Проверяем уникальность email (если изменился)
            if (!user.getEmail().equals(userDetails.getEmail()) && 
                userService.existsByEmail(userDetails.getEmail())) {
                return ResponseEntity.badRequest()
                    .body("Пользователь с email '" + userDetails.getEmail() + "' уже существует");
            }

            // Обновляем поля
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setRole(userDetails.getRole());
            user.setIsActive(userDetails.getIsActive());

            User updatedUser = userService.save(user);
            return ResponseEntity.ok(convertToDTO(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }

    /**
     * Удалить пользователя
     * DELETE /api/users/{id}
     */
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из системы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Optional<User> userOptional = userService.findById(id);
            if (!userOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            userService.deleteById(id);
            return ResponseEntity.ok("Пользователь успешно удален");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    /**
     * Получить пользователя по имени
     * GET /api/users/username/{username}
     */
    @Operation(summary = "Найти пользователя по имени", description = "Возвращает пользователя по имени пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(
            @Parameter(description = "Имя пользователя", required = true, example = "admin")
            @PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(convertToDTO(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Получить пользователя по email
     * GET /api/users/email/{email}
     */
    @Operation(summary = "Найти пользователя по email", description = "Возвращает пользователя по email адресу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(
            @Parameter(description = "Email адрес", required = true, example = "admin@example.com")
            @PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(convertToDTO(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Деактивировать пользователя
     * PATCH /api/users/{id}/deactivate
     */
    @Operation(summary = "Деактивировать пользователя", description = "Деактивирует пользователя (устанавливает isActive = false)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно деактивирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Optional<User> userOptional = userService.findById(id);
            if (!userOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            userService.deactivateUser(id);
            return ResponseEntity.ok("Пользователь деактивирован");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при деактивации пользователя: " + e.getMessage());
        }
    }

    /**
     * Активировать пользователя
     * PATCH /api/users/{id}/activate
     */
    @Operation(summary = "Активировать пользователя", description = "Активирует пользователя (устанавливает isActive = true)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно активирован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Optional<User> userOptional = userService.findById(id);
            if (!userOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            userService.activateUser(id);
            return ResponseEntity.ok("Пользователь активирован");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при активации пользователя: " + e.getMessage());
        }
    }

    /**
     * Конвертирует User в UserDTO (без пароля)
     */
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getIsActive()
        );
    }
}
