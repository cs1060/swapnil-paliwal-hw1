# Joke Service

A Spring Boot application that serves as a joke management system, demonstrating advanced software engineering principles and practices.

## Unique Technical Approach

### Generic Repository Pattern
The application implements a sophisticated generic repository pattern using Java Generics:
- `RepositoryBase<T>` interface defines the contract for repository operations, where T is a type parameter that can be any model class
- `JokeRepository` implements `RepositoryBase<Joke>`, allowing for type-safe operations with Joke objects
- This approach enables easy extension to support different types of content beyond jokes

#### Advanced Generic Features
1. **Bounded Type Parameters**
   - The repository system is designed to work with any type T that extends our base model classes
   - This ensures type safety while maintaining flexibility

2. **Wildcard Usage (`? super Joke`)**
   - API responses use `ApiResponse<? super Joke>` to allow for polymorphic responses
   - This enables the API to return not just Joke objects, but also any supertype of Joke
   - Particularly useful in the `JokeController` where we handle different response types

3. **Generic Method Implementation**
   ```java
   public interface RepositoryBase<T> {
       T getJoke();
       void addJoke(T joke);
       List<T> getSomeJoke(int count);
   }
   ```
   - All repository operations are generically typed
   - This ensures compile-time type safety
   - Prevents type casting and potential runtime errors

4. **BiPredicate Usage**
   ```java
   private final BiPredicate<Integer, Integer> shouldCallLocal =
       (firstNumber, secondNumber) -> firstNumber > secondNumber;
   ```
   - Uses Java's functional interfaces with generics
   - Enables type-safe decision making for API vs local storage calls

5. **Type Erasure Considerations**
   - The implementation carefully considers Java's type erasure
   - Repository methods are designed to maintain type safety at runtime
   - Generic type information is preserved where needed using reflection

This advanced use of generics provides:
- Compile-time type safety
- Code reusability across different content types
- Flexible polymorphic responses
- Clean and maintainable codebase

### Software Engineering Excellence
1. **Clean Architecture**
   - Clear separation of concerns with Controller, Repository, and Model layers
   - Dependency injection for loose coupling
   - Interface-based design promoting extensibility

2. **Smart Caching Strategy**
   - Implements an in-memory cache to reduce external API calls
   - Uses BiPredicate for intelligent decision-making between local and API calls

3. **Configuration Management**
   - Environment-specific configurations (dev/prod)
   - Externalized configuration using YAML
   - Profile-based settings management

## Running the Application

1. **Prerequisites**
   - Java 17 or higher
   - Maven 3.6+

2. **Launch the Application**
   ```bash
   mvn spring-boot:run
   ```
   The application will start on port 8080 by default.

## API Endpoints

### 1. Get Random Joke
```
GET /api/joke
```
Returns a random joke either from the local cache or the external API.

**Sample Response:**
```json
{
    "joke_id": 1,
    "value": "Chuck Norris can divide by zero.",
    "url": "https://api.chucknorris.io/jokes/xyz"
}
```

### 2. Add Custom Joke
```
POST /api/joke
```
**Request Body:**
```json
{
    "value": "Your custom joke text",
    "url": "optional-url"
}
```

### 3. Web Interface
- Access the web interface at: `http://localhost:8080`
- Features include:
  - View random jokes
  - Add custom jokes
  - See joke count

## Testing with Postman

1. **Get Random Joke**
   - Method: GET
   - URL: `http://localhost:8080/api/joke`
   - Headers: `Accept: application/json`

2. **Add Custom Joke**
   - Method: POST
   - URL: `http://localhost:8080/api/joke`
   - Headers: 
     - `Content-Type: application/json`
     - `Accept: application/json`
   - Body:
     ```json
     {
         "value": "Your joke text here",
         "url": "optional-source-url"
     }
     ```

## Architecture Highlights

The application showcases several advanced software engineering practices:

1. **Type Safety**: Extensive use of generics ensures compile-time type safety
2. **Extensibility**: The generic repository pattern allows for easy addition of new content types
3. **Maintainability**: Clear separation of concerns and well-documented code
4. **Reliability**: Smart caching reduces external dependencies
5. **User Experience**: Both API and web interface available
