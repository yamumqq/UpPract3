-- Создание администратора
-- Пароль: admin123 (зашифрован с помощью BCrypt)
INSERT INTO users (username, email, password, role, created_at, updated_at, is_active) 
VALUES ('admin', 'admin@edustream.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN', NOW(), NOW(), true);

-- Создание обычного пользователя для тестирования
-- Пароль: user123 (зашифрован с помощью BCrypt)
INSERT INTO users (username, email, password, role, created_at, updated_at, is_active) 
VALUES ('user', 'user@edustream.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'USER', NOW(), NOW(), true);
