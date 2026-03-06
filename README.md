# 🛒 Supermarket Checkout App

A Spring Boot application demonstrating fundamental concepts including REST APIs, JPA/Database, and testing.

## 🚀 What You've Built

This project includes:

- **Spring Boot Application**: Auto-configured web application
- **REST API**: Complete CRUD operations for products 
- **Database Integration**: H2 in-memory database with JPA
- **Layered Architecture**: Controller → Service → Repository pattern
- **Sample Data**: 10 products loaded automatically
- **Testing**: Integration tests with JUnit

## 🏗️ Project Structure

```
src/
├── main/java/com/supermarket/checkout/
│   ├── CheckoutApplication.java          # Main application class
│   ├── controller/
│   │   ├── CheckoutController.java       # Basic welcome endpoints
│   │   └── ProductController.java        # Product CRUD API
│   ├── service/
│   │   └── ProductService.java           # Business logic layer
│   ├── repository/
│   │   └── ProductRepository.java        # Database operations
│   ├── model/
│   │   └── Product.java                  # Product entity/model
│   └── config/
│       └── DataInitializer.java          # Sample data setup
├── main/resources/
│   └── application.properties            # App configuration
└── test/java/
    └── CheckoutApplicationTests.java     # Integration tests
```

## 🔧 Requirements

- **Java 17** or later
- **Maven** (or use the Maven wrapper: `./mvnw`)

## 🎯 How to Run

### Option 1: Using Maven
```bash
# Navigate to project directory
cd /Users/waleedulhaq/Desktop/supermarket-checkout-app

# Run the application
mvn spring-boot:run
```

### Option 2: Using Maven Wrapper (if available)
```bash
./mvnw spring-boot:run
```

### Option 3: Build and Run JAR
```bash
# Build the project
mvn clean package

# Run the JAR file
java -jar target/checkout-app-1.0.0.jar
```

## 🌐 Testing Your Application

Once running, Spring Boot will automatically choose an available port (like 50429, 51234, etc.) and display it in the console output.

**Look for this message in the console:**
```
🛒 Supermarket Checkout App is running!
🌐 Visit: http://localhost:[ACTUAL_PORT]
🗄️  Database Console: http://localhost:[ACTUAL_PORT]/h2-console
```

### API Endpoints to Try:

#### Basic Endpoints (replace [PORT] with actual port from console):
- `GET http://localhost:[PORT]/api/checkout/welcome` - Welcome message
- `GET http://localhost:[PORT]/api/checkout/hello/YourName` - Personal greeting
- `GET http://localhost:[PORT]/api/checkout/health` - Health check

#### Product API (CRUD Operations):
- `GET http://localhost:[PORT]/api/products` - Get all products
- `GET http://localhost:[PORT]/api/products/1` - Get product by ID
- `GET http://localhost:[PORT]/api/products/search?name=apple` - Search products
- `POST http://localhost:[PORT]/api/products` - Create new product
- `PUT http://localhost:[PORT]/api/products/1` - Update product
- `DELETE http://localhost:[PORT]/api/products/1` - Delete product

### Database Console:
- Visit: **http://localhost:[PORT]/h2-console** (use actual port from console)
- JDBC URL: `jdbc:h2:mem:checkoutdb`
- Username: `sa`
- Password: (leave empty)

## 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run tests with detailed output
mvn test -Dtest=CheckoutApplicationTests
```

## 📝 Sample API Calls

### Get all products (replace 50429 with your actual port):
```bash
curl http://localhost:50429/api/products
```

### Create a new product:
```bash
curl -X POST http://localhost:50429/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Green Apple",
    "price": 1.25
  }'
```

### Search for products:
```bash
curl "http://localhost:50429/api/products/search?name=milk"
```

## 🎓 Key Spring Boot Concepts Demonstrated

### 1. **Auto-Configuration**
- Automatic web server setup (Tomcat)
- Database configuration (H2)
- JPA setup with Hibernate

### 2. **Dependency Injection** 
- Constructor injection in services
- `@Autowired` alternative approaches

### 3. **Layered Architecture**
- **Controller Layer**: HTTP request handling
- **Service Layer**: Business logic
- **Repository Layer**: Data access

### 4. **Annotations Explained**
- `@SpringBootApplication`: Main app configuration
- `@RestController`: REST API endpoints  
- `@Service`: Business logic components
- `@Repository`: Data access components
- `@Entity`: Database table mapping
- `@PostConstruct`: Initialization method

### 5. **Spring Data JPA**
- Automatic query generation
- Custom query methods
- Database relationship mapping

## 🔄 Next Steps for Learning

1. **Add more entities**: Create `Customer`, `Order`, `OrderItem` classes
2. **Add validation**: Use `@Valid` and Bean Validation annotations
3. **Error handling**: Create `@ControllerAdvice` for global exception handling
4. **Security**: Add Spring Security for authentication
5. **Documentation**: Add Swagger/OpenAPI documentation
6. **Profiles**: Learn about different application profiles (dev, prod)
7. **Actuator**: Add monitoring and health check endpoints

## 🐛 Common Issues

### Port Already in Use
The app uses automatic port selection (`server.port=0`), so it will find an available port automatically. If you want to use a specific port, change it in `application.properties`:
```properties
# Use a specific port
server.port=8080

# Or use automatic selection (current setting)
server.port=0
```

### Java Version Issues
Ensure you're using Java 17+:
```bash
java -version
```

### Maven Issues
If Maven commands don't work, try using the wrapper:
```bash
./mvnw clean install
```

## 📚 Learning Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA Guide](https://spring.io/guides/gs/accessing-data-jpa/)
- [Building REST Services](https://spring.io/guides/gs/rest-service/)

---

**Congratulations! 🎉** You've built your first Spring Boot application!