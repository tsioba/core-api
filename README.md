<div align="center">

# 🚀 Core API 
**A state-of-the-art, production-ready starter template for building secure RESTful APIs.**

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Security](https://img.shields.io/badge/Security-JWT-blue.svg)]()
[![Database](https://img.shields.io/badge/Database-PostgreSQL%20%7C%20H2-blue.svg)]()

</div>

---

## ✨ Key Features

- **🔒 Strict Security:** Stateless authentication using **Spring Security** and **JWT**.
- **📦 Modern Architecture:** Built on the latest **Java 25** and **Spring Boot 4** ecosystem.
- **🗄️ Dual Database Setup:** **PostgreSQL** for production and **H2 In-Memory DB** for lightning-fast testing.
- **🧪 Automated Testing:** Comprehensive E2E integration tests (13+ test cases) using `MockMvc`.
- **📊 Code Coverage:** Integrated **JaCoCo** plugin for detailed test coverage reporting.
- **📄 Auto-generated API Docs:** Integrated **Swagger UI** for instant endpoint testing.
- **⚙️ Environment Variables:** Secure configuration via `.env` files using `spring-dotenv`.

---

## 🛠️ Tech Stack

| Category | Technology |
| :--- | :--- |
| **Core** | Java 25, Spring Boot 4.0.3 |
| **Security** | Spring Security, JJWT (0.12.5) |
| **Data Layer** | Spring Data JPA, Hibernate, PostgreSQL, H2 |
| **Docs & Tooling** | Springdoc OpenAPI (Swagger), Lombok, dotenv |
| **Testing** | JUnit 5, MockMvc, JaCoCo |

---

## 🚀 Getting Started

### 1. Prerequisites
Ensure you have the following installed on your local machine:
- **Java Development Kit (JDK) 25**
- **Maven**

### 2. Environment Configuration

> **⚠️ IMPORTANT:** This project uses environment variables. Do not commit sensitive data!

Create a `.env` file in the root directory and add your own values:
```env
# Database Configuration
DB_URL=jdbc:h2:mem:core_db
DB_USERNAME=db_username
DB_PASSWORD=db_password

# JWT Configuration
JWT_SECRET=YourSuperSecretAndLongJwtKeyThatNeedsToBeReplaced123!
JWT_EXPIRATION=86400000

# Server Configuration
PORT=8080