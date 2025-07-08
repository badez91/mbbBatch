
# Spring Boot Batch Upload & RESTful API System

## Overview

This project implements a Spring Boot application that supports:
- Batch uploading of transaction records from a pipe-delimited text file
- RESTful APIs for searching and updating records
- Pagination and filtering
- Optimistic locking for concurrent updates
- Validation and unit testing

## Features

1. **Batch Upload**: Upload `.txt` files via API and parse into MySQL.
2. **Search API**: Filter records by customer ID, account number, and description.
3. **Update API**: Update transaction details with optimistic locking support.
4. **Pagination**: Search results support pagination.
5. **Validation**: Includes basic validation on uploaded file fields.
6. **Testing**: Includes unit tests for batch upload and REST endpoints.

---

## Technologies

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- JUnit & MockMvc

---

## Design Patterns Used

# Design Patterns Used in Spring Boot Batch Upload System

## 1. Builder Pattern
- **Used in**: `Records` entity
- **Why**: Simplifies creation of complex objects. Improves readability when mapping parsed values to entity fields.

## 2. Singleton Pattern
- **Used in**: Spring-managed beans like `RecordsService`, `RecordsController`, `RecordsRepository`
- **Why**: Spring automatically provides singleton scope by default. Ensures only one instance of each service is used.

## 3. Strategy Pattern (for extensibility)
- **Used in**: `FileParser` utility class (conceptual use)
- **Why**: Isolates parsing logic. Easily allows switching to other formats (CSV, JSON) in the future.

## 4. Template Method Pattern
- **Used in**: `RecordsRepository` via `JpaRepository`
- **Why**: Abstracts common data access methods. Encourages reuse and cleaner code.

## 5. Specification Pattern
- **Used in**: `searchRecords(...)` in service layer
- **Why**: Supports dynamic query construction based on optional parameters like customerId, accountNumber, and description.

## 6. Layered Architecture
- **Used in**: Full stack (Controller → Service → Repository)
- **Why**: Enforces separation of concerns, testability, and maintainability.

---

## Summary Table

| Design Pattern        | Purpose                             | Location                     |
|-----------------------|-------------------------------------|------------------------------|
| Builder               | Clean object construction           | `Records` entity             |
| Singleton             | Single instance reuse               | Spring Beans (Service, Repo) |
| Strategy (future)     | Pluggable parsing strategies        | `FileParser`                 |
| Template Method       | CRUD abstraction                    | `JpaRepository`              |
| Specification         | Dynamic search queries              | Search method in Service     |
| Layered Architecture  | Separation of concerns              | Controller → Service → Repo  |

---

## API Endpoints

### 1. Upload Transactions
- **POST** `/api/batch/upload`
- Upload a pipe-delimited text file with transaction data.

### 2. Search Transactions
- **GET** `/api/records/search`
- Supports query params: `customerId`, `accountNumber`, `description`, `page`, `size`.

### 3. Update Transaction
- **PUT** `/api/transactions`
- JSON body with `id`, `trxAmount`, `description`, and `version`.

---

## License

MIT
