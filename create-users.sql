-- Создание тестовых пользователей
-- Пароли зашифрованы с помощью BCrypt

-- Администратор (admin / admin123)
INSERT INTO users (username, email, password, role, created_at, updated_at, is_active) 
VALUES ('admin', 'admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN', NOW(), NOW(), true);

-- Обычный пользователь (user / user123)
INSERT INTO users (username, email, password, role, created_at, updated_at, is_active) 
VALUES ('user', 'user@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'USER', NOW(), NOW(), true);

