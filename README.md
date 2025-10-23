# 🔐 TrustCircle

A social recommendation platform for sharing trusted experiences across Health, Restaurants, Apartments, Travel, and more.

## 🚀 Quick Start

### 📋 Prerequisites
- Java 25
- PostgreSQL 15
- Node.js 18+

### ⚙️ Setup

**1. Database**
```sql
CREATE DATABASE trustcircle;
```

**2. Configuration**

Edit `app/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/trustcircle
spring.datasource.username=your_user
spring.datasource.password=your_password
jwt.secret=your_secret_key
```

**3. Run**
```bash
# Backend (http://localhost:8080)
./gradlew :app:bootRun

# Frontend (http://localhost:4200)
cd frontend && npm install && npm start
```

## 🛠️ Tech Stack

- **Backend:** Spring Boot 3 + Java 25 + PostgreSQL
- **Frontend:** Angular + TypeScript + Tailwind CSS
- **Auth:** JWT (1 hour expiry)

## 📡 API Reference

Base URL: `/api/v1`

**Auth**
- `POST /auth/signup` - Register
- `POST /auth/login` - Login (returns JWT)

**Recommendations**
- `GET /recommendations?category={category}` - List all
- `POST /recommendations` - Create (auth required)
- `PUT /recommendations/{id}` - Update (auth required)
- `DELETE /recommendations/{id}` - Delete (auth required)

**Users**
- `GET /users/{id}` - Get profile
- `GET /users/{id}/recommendations` - Get user's recommendations
