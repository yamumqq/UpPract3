# REST API для управления пользователями

## Описание

Создан простой REST API с использованием Spring Boot для управления сущностью "Пользователь" с полным набором CRUD операций.

## Что реализовано

### 1. REST API Контроллер (`UserRestController.java`)
- **Аннотации Spring:**
  - `@RestController` - для создания REST контроллера
  - `@RequestMapping("/api/users")` - базовый путь для всех эндпоинтов
  - `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping` - для различных HTTP методов
  - `@PathVariable` - для получения параметров из URL
  - `@RequestBody` - для получения данных из тела запроса
  - `@Valid` - для валидации входящих данных
  - `@CrossOrigin` - для поддержки CORS

### 2. DTO классы
- **`UserDTO.java`** - для безопасной передачи данных пользователя (без пароля)
- **`CreateUserDTO.java`** - для создания новых пользователей

### 3. CRUD операции
- **CREATE** - `POST /api/users` - создание нового пользователя
- **READ** - `GET /api/users` - получение всех пользователей
- **READ** - `GET /api/users/{id}` - получение пользователя по ID
- **UPDATE** - `PUT /api/users/{id}` - обновление пользователя
- **DELETE** - `DELETE /api/users/{id}` - удаление пользователя

### 4. Дополнительные операции
- `GET /api/users/username/{username}` - поиск по имени пользователя
- `GET /api/users/email/{email}` - поиск по email
- `PATCH /api/users/{id}/deactivate` - деактивация пользователя
- `PATCH /api/users/{id}/activate` - активация пользователя

### 5. Тестирование
- **Unit тесты** (`UserRestControllerTest.java`) с использованием MockMvc
- **HTML интерфейс** (`test-api.html`) для интерактивного тестирования
- **Документация** (`REST_API_EXAMPLES.md`) с примерами запросов для Postman

## Запуск и тестирование

### 1. Запуск приложения
```bash
# Используйте один из batch файлов:
run-app.bat
# или
start.bat
```

### 2. Тестирование через браузер
1. Откройте файл `test-api.html` в браузере
2. Используйте интерактивный интерфейс для тестирования всех операций

### 3. Тестирование через Postman
1. Импортируйте коллекцию из файла `REST_API_EXAMPLES.md`
2. Базовый URL: `http://localhost:8080/api/users`

### 4. Примеры запросов

#### Создание пользователя
```bash
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "role": "USER"
}
```

#### Получение всех пользователей
```bash
GET http://localhost:8080/api/users
```

#### Обновление пользователя
```bash
PUT http://localhost:8080/api/users/1
Content-Type: application/json

{
  "username": "updateduser",
  "email": "updated@example.com",
  "role": "ADMIN",
  "isActive": true
}
```

## Особенности реализации

### 1. Безопасность
- Пароли не возвращаются в ответах API
- Валидация входящих данных с помощью Bean Validation
- Проверка уникальности username и email

### 2. Обработка ошибок
- HTTP статус коды (200, 201, 400, 404, 500)
- Информативные сообщения об ошибках
- Обработка исключений

### 3. Валидация
- `@NotBlank` - обязательные поля
- `@Size` - ограничения по длине
- `@Email` - валидация email
- `@Valid` - активация валидации

### 4. Архитектура
- Разделение на слои (Controller, Service, Repository)
- Использование DTO для передачи данных
- Следование принципам REST

## Структура файлов

```
src/main/java/com/example/project2/
├── controller/
│   └── UserRestController.java          # REST API контроллер
├── dto/
│   ├── UserDTO.java                     # DTO для пользователя
│   └── CreateUserDTO.java               # DTO для создания пользователя
└── model/
    └── User.java                        # Модель пользователя

src/test/java/com/example/project2/controller/
└── UserRestControllerTest.java          # Тесты для REST API

Документация:
├── REST_API_EXAMPLES.md                 # Примеры запросов для Postman
├── test-api.html                        # HTML интерфейс для тестирования
└── README_REST_API.md                   # Данное руководство
```

## Заключение

Реализован полнофункциональный REST API для управления пользователями с:
- ✅ Правильным использованием аннотаций Spring
- ✅ Полным набором CRUD операций
- ✅ Валидацией и обработкой ошибок
- ✅ Тестами и документацией
- ✅ Готовыми примерами для тестирования в Postman
