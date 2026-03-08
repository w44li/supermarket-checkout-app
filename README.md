# Supermarket Checkout Web Service

This is a simple Spring Boot application that calculates checkout prices for a supermarket. It handles regular prices and weekly bundle offers automatically.

## Task (Coding Kata)

Implement a simplified supermarket checkout system. The cart can contain any number and combination of available items, in any order. Additionally the checkout system should support weekly offers, where an offer defines a number of items that are discounted when bought together. For example: one apple costs 0.30€, but 2 apples are offered at 0.45€. These offers should be applied automatically during checkout.

## What does it do?

Imagine you're shopping at a supermarket. Some items have special offers like "Buy 3 apples for €2.50 instead of €3.00". This app does that calculation for you.

For example:
- Normal price: 1 apple = €1.00
- Weekly offer: 3 apples = €2.50
- You buy 7 apples → the app calculates: 2 bundles (€5.00) + 1 regular apple (€1.00) = **€6.00 total**

The offers have start and end dates, so they only apply during that week. The app figures out which offers are active based on today's date.

You can also add and delete products and offers through the API.

## Tech Stack

- Java 17 and Spring Boot 3.2.3
- Maven for building the project
- H2 database (runs in memory, no installation needed)
- JUnit and Mockito for testing

## How to run it

First, make sure you have Java 17 and Maven installed on your computer. You can check by running `java -version` and `mvn -version`.

Start the app with `mvn spring-boot:run`.

The app will start at `http://localhost:8080`.

If you see a "port already in use" error:
- Option 1: Open src/main/resources/application.properties and change `server.port` to another number.
- Option 2: Stop the process using port 8080, then run again:
  - `lsof -i :8080`
  - `kill -9 <PID>`

If you want to see the database, visit `http://localhost:8080/h2-console` and use:
- JDBC URL: `jdbc:h2:mem:checkoutdb`
- Username: `sa`
- Password: `demo`

## How to test it

I wrote 25 tests to make sure everything works correctly, including edge cases. Run them with `mvn test`.

The tests cover things like:
- Calculating prices with and without offers
- Handling edge cases (like buying fewer items than needed for a bundle)
- Making sure the cart works even if items are listed multiple times
- Checking that expired offers don't get applied
- Validating that overlapping offers for the same product are rejected

## Architecture

The project follows a simple layered architecture:

```
┌─────────────────────────────────┐
│   REST Controllers              │  ← Handle HTTP requests
│   (Product, Offer, Checkout)   │
└────────────┬────────────────────┘
             │
┌────────────▼────────────────────┐
│   Service Layer                 │  ← Business logic lives here
│   (Calculate prices, etc)       │
└────────────┬────────────────────┘
             │
┌────────────▼────────────────────┐
│   Repository Layer              │  ← Talk to the database
└────────────┬────────────────────┘
             │
┌────────────▼────────────────────┐
│   H2 Database                   │  ← Store products and offers
└─────────────────────────────────┘
```

Each layer has a specific job:
- **Controllers** receive requests and send responses
- **Services** contain the business logic (like calculating bundle prices)
- **Repositories** handle saving and loading data from the database
- **Models** are just data objects (Product, Offer, Cart, etc.)

## API Endpoints

Once the app is running, you can use these endpoints:

**Products:**
- `GET /products` - See all products
- `GET /products/{id}` - Get a specific product
- `POST /products` - Add a new product

  - Example:
    ```sh
    curl -X POST http://localhost:8080/products \
      -H "Content-Type: application/json" \
      -d '{
        "name": "Orange",
        "price": 0.80
      }'
    ```
    
- `DELETE /products/{id}` - Remove a product

**Offers:**
- `GET /offers` - See all offers
- `GET /offers/{id}` - Get a specific offer
- `DELETE /offers/{id}` - Remove an offer
- `POST /offers` - Create a new offer

  - Example:
    ```sh
    curl -X POST http://localhost:8080/offers \
      -H "Content-Type: application/json" \
      -d '{
        "productId": 1,
        "requiredQuantity": 3,
        "bundlePrice": 2.50,
        "startDate": "2026-03-01",
        "endDate": "2026-03-31"
      }'
    ```

**Checkout:**
- `POST /cart/checkout` - Calculate the total for a shopping cart

  - Example:
    ```sh
    curl -X POST http://localhost:8080/cart/checkout \
      -H "Content-Type: application/json" \
      -d '{
        "items": [
          {
            "productId": 1,
            "name": "Apple",
            "quantity": 7
          }
        ]
      }'
    ```

  - Response: the endpoint returns the calculated total, for example `6.00`

## Project Structure

Here's how the code is organized:

```
src/main/java/com/supermarket/checkout/
├── web/                    Controllers that handle requests
├── service/                Business logic for checkout, offers, products
├── repository/             Database access
├── model/                  Data classes (Product, Offer, Cart, etc.)
├── config/                 Startup configuration
└── exception/              Error handling

src/test/java/              All the tests
```

## Important Notes

- H2 runs in memory, so the data resets every time the app restarts. You can make it persistent by changing `spring.datasource.url` to a file-based URL in `src/main/resources/application.properties`.
- When you start the app, it automatically loads 10 sample products so you can test right away
- I used `BigDecimal` for all money calculations because floating-point numbers aren't accurate enough for currency
- Offers for the same product are allowed as long as their date ranges do not overlap
- The cart merges duplicate items automatically (if you add apples twice, it combines them and calculates total price)
- All validation and error messages follow REST best practices

## Limitations

- Overlapping offers for the same product are not supported.
- Only bundle-style offers are supported as part of the kata scope.