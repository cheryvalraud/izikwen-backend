# Izikwen Backend

Secure **Spring Boot REST API** powering **Izikwen**, a platform designed to make buying and managing **USDT and Bitcoin** easier and safer, especially for users in Haiti.

---

## 🚀 Overview
Izikwen Backend provides authentication, wallet management, and secure transaction endpoints.  
The goal of this project is to explore secure backend architecture while building a real-world financial access solution.

---

## 🛠 Tech Stack
- **Java 21 (LTS)**
- **Spring Boot**
- **Spring Security**
- **JWT Authentication**
- **JPA / Hibernate**
- **Maven**
- **H2 / PostgreSQL (configurable)**

---

## 🔐 Core Features
- User Registration & Login
- JWT-based Authentication
- Two-Factor Authentication (2FA)
- Wallet Balance Management
- Transaction Endpoints
- Secure REST APIs
- Role-based Access Control (basic)

---

## 🧩 Architecture
- RESTful API Design
- Layered Architecture  
  `Controller → Service → Repository`
- Token-based Security
- Environment-based Configuration
- Modular and Scalable Structure

---

## ⚙️ Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/cheryvalraud/izikwen-backend.git

2. Navigate to the Project
cd izikwen-backend

3. Run the Application
mvn spring-boot:run


The API will start on:

http://localhost:8080

🔒 Security Notice

Sensitive data such as:

API Keys

JWT Secrets

Database Credentials

are not included in this repository.
Configuration is handled through environment variables and ignored files using .gitignore.

🌍 Purpose

This project was built to demonstrate secure backend engineering practices while addressing accessibility challenges in digital finance.
It focuses on simplicity, security, and usability for users who need reliable crypto access tools.

📌 Planned Improvements

Payment Gateway Integration

Enhanced Transaction History

Admin Dashboard

Notification & Email System

Multi-currency Support

🧪 Testing

Basic endpoint testing was performed using tools such as:

Postman



👤 Author

Valraud Chery

Computer Science Student & Full-Stack Developer
Passionate about secure systems, financial technology, and scalable backend architecture.
