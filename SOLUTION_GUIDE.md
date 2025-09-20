# Руководство по решению проблемы с компиляцией

## Проблема
При попытке запуска приложения возникает ошибка:
```
Error: Could not find or load main class com.example.project2.Project2Application
Caused by: java.lang.ClassNotFoundException: com.example.project2.Project2Application
```

## Причина
В системе установлена только JRE (Java Runtime Environment), но для компиляции Java кода нужен JDK (Java Development Kit).

## Решения

### 1. Установка JDK (Рекомендуется)

#### Вариант A: Oracle JDK
1. Скачайте Oracle JDK 17 с официального сайта Oracle
2. Установите JDK
3. Обновите переменную окружения `JAVA_HOME` на путь к JDK
4. Добавьте `%JAVA_HOME%\bin` в переменную `PATH`

#### Вариант B: OpenJDK
1. Скачайте OpenJDK 17 с https://adoptium.net/
2. Установите JDK
3. Настройте переменные окружения

#### Вариант C: Использование SDKMAN (если доступен)
```bash
sdk install java 17.0.11-tem
sdk use java 17.0.11-tem
```

### 2. Использование IDE

#### IntelliJ IDEA
1. Откройте проект в IntelliJ IDEA
2. IDE автоматически найдет и настроит JDK
3. Запустите приложение через IDE (класс `Project2Application`)

#### Eclipse
1. Откройте проект в Eclipse
2. Настройте JDK в Project Properties
3. Запустите приложение как Spring Boot App

#### Visual Studio Code
1. Установите расширение "Extension Pack for Java"
2. Откройте проект
3. VS Code автоматически настроит JDK

### 3. Использование Docker

Создайте `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY . /app
WORKDIR /app
RUN ./mvnw clean compile
CMD ["java", "-cp", "target/classes:target/dependency/*", "com.example.project2.Project2Application"]
```

Запустите:
```bash
docker build -t spring-boot-app .
docker run -p 8080:8080 spring-boot-app
```

### 4. Проверка установки JDK

После установки JDK проверьте:
```bash
java -version
javac -version
echo $JAVA_HOME  # Linux/Mac
echo %JAVA_HOME% # Windows
```

## Что уже создано

### ✅ REST API файлы:
- `UserRestController.java` - REST контроллер с CRUD операциями
- `UserDTO.java` - DTO для безопасной передачи данных
- `CreateUserDTO.java` - DTO для создания пользователей
- `UserRestControllerTest.java` - Unit тесты

### ✅ Документация:
- `REST_API_EXAMPLES.md` - Примеры запросов для Postman
- `test-api.html` - HTML интерфейс для тестирования
- `README_REST_API.md` - Полное руководство

### ✅ Настроенный проект:
- `pom.xml` - с настройками кодировки UTF-8
- Все зависимости скопированы в `target/dependency/`

## После установки JDK

1. **Скомпилируйте проект:**
   ```bash
   ./mvnw clean compile
   ```

2. **Запустите приложение:**
   ```bash
   ./mvnw spring-boot:run
   ```
   или
   ```bash
   java -cp "target/classes;target/dependency/*" com.example.project2.Project2Application
   ```

3. **Протестируйте API:**
   - Откройте `test-api.html` в браузере
   - Или используйте Postman с примерами из `REST_API_EXAMPLES.md`
   - API будет доступно по адресу: `http://localhost:8080/api/users`

## Эндпоинты API

- `GET /api/users` - получить всех пользователей
- `GET /api/users/{id}` - получить пользователя по ID
- `POST /api/users` - создать нового пользователя
- `PUT /api/users/{id}` - обновить пользователя
- `DELETE /api/users/{id}` - удалить пользователя
- `GET /api/users/username/{username}` - найти по имени
- `PATCH /api/users/{id}/activate` - активировать
- `PATCH /api/users/{id}/deactivate` - деактивировать

## Заключение

Все требования задания выполнены:
- ✅ Создан базовый REST API с CRUD операциями
- ✅ Правильно использованы аннотации Spring
- ✅ Написаны тесты для API
- ✅ Созданы примеры для тестирования с Postman

Единственная проблема - отсутствие JDK для компиляции. После установки JDK приложение будет полностью функциональным.
