
# Financial Transfer API

## About the Project

This project is a financial transfer API designed to handle multi-currency transactions with high precision and reliability. It supports various features like currency conversion, balance validation, and concurrent transfer handling.

## Architectural Overview

The API is designed with a clean architecture approach, ensuring clear separation of concerns and making the core business logic isolated from external dependencies. The project is structured into three main packages: `client`, `domain`, and `infra`.

**Client Layer**: 
- Contains controllers and DTOs (Data Transfer Objects).
- Handles incoming HTTP requests, validates the input, and transforms data into a format suitable for the domain layer.
- Communicates with the domain layer exclusively, ensuring a clear boundary from the infrastructure layer.

**Domain Layer**: 
- Core of the application, containing the business logic and rules.
- Includes entities, value objects, services, and interfaces.
- Independent of external layers, making it highly testable and maintainable.

**Infrastructure Layer**: 
- Provides implementations for the interfaces defined in the domain layer.
- Interacts with external systems, ensuring that the domain layer remains decoupled from specific technologies.

### Interactions and Flow

**Client to Domain**: The client layer interacts with the domain layer by sending validated and immutable DTOs. The domain layer processes these DTOs and performs the necessary business logic.

**Domain to Infrastructure**: The domain layer communicates with the infrastructure layer through interfaces. The infrastructure layer provides implementations for these interfaces, ensuring that the domain layer remains decoupled from external systems.

**Infrastructure to Domain**: The infrastructure layer uses the interfaces defined in the domain layer to perform operations. This ensures that any changes in the infrastructure do not impact the domain logic.

## Table of Contents
- [Getting Started](#getting-started)
- [Installation](#Installation)
- [Running the Application](#running-the-application)
- [Testing with Postman](#testing-with-Postman)
- [API Endpoints](#aPI-Endpoints)
- [Technologies Used](#Technologies-Used)
- [Testing](#testing)
- [Database Migration](#database-migration)
- [Additional Information](#additional-information)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/FabioEntringerMoreira/financial-transfer.git
   ```
2. Navigate to the project directory:
   ```sh
   cd financial-transfer-api
   ```
3. Install the necessary dependencies using Maven:
   ```sh
   mvn clean install
   ```

## Running the Application

To run the application, use the following command:
```sh
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## Testing with Postman ##
For manual testing, we provide a Postman collection that can be imported into Postman. This collection contains the main requests for the API. The collection file is available in the source code of the project.

Open Postman.
Import the collection file from the project source.
Use the requests provided in the collection to interact with the API.

## API Endpoints

### Transfer Funds

- **URL:** `/api/v1/transfer`
- **Method:** `POST`
- **Description:** Transfers funds from one account to another applying exchange rate when required.
- **Request Body:**
  ```json
  {
    "debitAccountId": 1,
    "creditAccountId": 2,
    "amount": 100.00
  }
  ```
- **Responses:**
  - **Success (200):**
    ```json
    {
      "transactionId": 12345,
      "status": "Success",
      "message": "Transfer completed successfully"
    }
    ```
  - **Bad Request (400):**
    ```json
    {
      "status": "Failed",
      "message": "The corresponding message for bad request."
    }
    ```
  - **Internal Server Error (500):**
    ```json
    {
      "status": "Failed",
      "message": "The corresponding message for Internal error"
    }
    ```

## Technologies Used

- **Java 17**
- **Spring Boot**
- **H2 Database** for testing and development
- **Flyway** for database migrations
- **RestAssured** for integration testing
- **Maven** for dependency management and build

## Testing

### Unit Tests

Our application ensures high code quality through comprehensive unit tests, maintaining a high standard of reliability and correctness. Key aspects include:

- **High Coverage:** We achieve 100% test coverage across core features, ensuring thorough testing of the application.
- **Concurrency Handling:** Tests validate the application's behavior under concurrent operations, ensuring thread safety and correct transaction handling.
- **Validation Logic:** Extensive tests cover input validation, including null values, invalid parameters, boundary conditions, and business rules.
- **Business Logic:** Core transfer operations are tested, including different currencies, balance checks, and transfer limits.
- **Error Handling:** Tests verify appropriate exceptions are thrown and handled for various error scenarios.
```sh
mvn test
```

### Integration Tests

End to end tests are written using RestAssured to test the API endpoints. These tests ensure that the application components work together as expected.

### Test Cases

- **Successful fund transfer**
- **Transfer with unsupported currency**
- **Transfer with insufficient balance**
- **Transfer with invalid account ID**
- **Transfer with invalid amount**
- **Transfer between the same account**
- **Technical error in currency conversion**

## Database Migration

The project uses Flyway for database migrations. The migration scripts are located in `src/main/resources/db/migration`. For testing purposes, additional migration scripts are in `src/test/resources/db/migration`.

### Adding New Migrations:

- Create a new migration script in the src/main/resources/db/migration directory.
- Name the script with the version number, description, and SQL file extension (e.g., V3__Add_new_table.sql).
- Add your SQL statements to the script to define the changes.
- Flyway will automatically apply the new migration the next time the application is started.

### Visualizing the H2 Database

To visualize the H2 database, navigate to `http://localhost:8080/h2-console` **in runtime** and use the following credentials:
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** `password`


## Additional Information

### Javadoc

The project is thoroughly documented using Javadoc, providing detailed information about the code, its structure, and its functionality. This ensures that developers can easily understand and maintain the codebase.
