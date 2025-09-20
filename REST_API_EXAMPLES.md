# REST API для управления пользователями

## Базовый URL
```
http://localhost:8080/api/users
```

## Эндпоинты API

### 1. Получить всех пользователей
**GET** `/api/users`

**Пример запроса:**
```
GET http://localhost:8080/api/users
```

**Пример ответа:**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "role": "ADMIN",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00",
    "isActive": true
  },
  {
    "id": 2,
    "username": "user1",
    "email": "user1@example.com",
    "role": "USER",
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": "2024-01-15T11:00:00",
    "isActive": true
  }
]
```

### 2. Получить пользователя по ID
**GET** `/api/users/{id}`

**Пример запроса:**
```
GET http://localhost:8080/api/users/1
```

**Пример ответа:**
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "role": "ADMIN",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "isActive": true
}
```

### 3. Создать нового пользователя
**POST** `/api/users`

**Заголовки:**
```
Content-Type: application/json
```

**Тело запроса:**
```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "password123",
  "role": "USER"
}
```

**Пример ответа (201 Created):**
```json
{
  "id": 3,
  "username": "newuser",
  "email": "newuser@example.com",
  "role": "USER",
  "createdAt": "2024-01-15T12:00:00",
  "updatedAt": "2024-01-15T12:00:00",
  "isActive": true
}
```

### 4. Обновить пользователя
**PUT** `/api/users/{id}`

**Заголовки:**
```
Content-Type: application/json
```

**Тело запроса:**
```json
{
  "username": "updateduser",
  "email": "updated@example.com",
  "role": "ADMIN",
  "isActive": true
}
```

**Пример ответа:**
```json
{
  "id": 1,
  "username": "updateduser",
  "email": "updated@example.com",
  "role": "ADMIN",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T12:30:00",
  "isActive": true
}
```

### 5. Удалить пользователя
**DELETE** `/api/users/{id}`

**Пример запроса:**
```
DELETE http://localhost:8080/api/users/3
```

**Пример ответа:**
```
Пользователь успешно удален
```

### 6. Получить пользователя по имени
**GET** `/api/users/username/{username}`

**Пример запроса:**
```
GET http://localhost:8080/api/users/username/admin
```

### 7. Получить пользователя по email
**GET** `/api/users/email/{email}`

**Пример запроса:**
```
GET http://localhost:8080/api/users/email/admin@example.com
```

### 8. Деактивировать пользователя
**PATCH** `/api/users/{id}/deactivate`

**Пример запроса:**
```
PATCH http://localhost:8080/api/users/1/deactivate
```

**Пример ответа:**
```
Пользователь деактивирован
```

### 9. Активировать пользователя
**PATCH** `/api/users/{id}/activate`

**Пример запроса:**
```
PATCH http://localhost:8080/api/users/1/activate
```

**Пример ответа:**
```
Пользователь активирован
```

## Коды ответов

- **200 OK** - Успешный запрос
- **201 Created** - Ресурс успешно создан
- **400 Bad Request** - Некорректный запрос (валидация не прошла)
- **404 Not Found** - Ресурс не найден
- **500 Internal Server Error** - Внутренняя ошибка сервера

## Примеры ошибок

### Пользователь с таким именем уже существует
```json
"Пользователь с именем 'admin' уже существует"
```

### Пользователь с таким email уже существует
```json
"Пользователь с email 'admin@example.com' уже существует"
```

### Пользователь не найден
```
HTTP 404 Not Found
```

## Тестирование в Postman

1. **Создайте коллекцию** "User Management API"
2. **Добавьте переменную окружения** `baseUrl` = `http://localhost:8080`
3. **Создайте запросы** для каждого эндпоинта
4. **Используйте переменную** `{{baseUrl}}/api/users` в URL

### Примеры тестовых сценариев:

1. **Создание пользователя** → **Получение по ID** → **Обновление** → **Удаление**
2. **Создание пользователя с дублирующимся именем** (должна быть ошибка)
3. **Получение несуществующего пользователя** (должен быть 404)
4. **Активация/деактивация пользователя**

## Запуск приложения

1. Убедитесь, что приложение запущено:
   ```bash
   ./mvnw spring-boot:run
   ```

2. Приложение будет доступно по адресу: `http://localhost:8080`

3. Для тестирования API используйте базовый URL: `http://localhost:8080/api/users`
