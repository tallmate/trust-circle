# Technical Design Document - TrustCircle MVP

## Overview

**TrustCircle** is a social recommendation platform for sharing trusted experiences across categories (Health, Restaurants, Apartments, Travel, Other).

**MVP Features:**
- User authentication (signup/login)
- CRUD operations for recommendations
- Public feed with category filtering
- Image uploads (optional)
- Shareable links

**Out of scope:**
- Social features (follow/likes/comments)
- Private sharing, advanced search, mobile apps

---

## Architecture

```
Frontend (Angular + Tailwind)
    ↓ REST API
Backend (Spring Boot 3 + Java 25)
    ↓ JDBC
PostgreSQL 15
    ↓ Optional
AWS S3 (images)
```

**Stack:**
- Backend: Spring Boot 3, Java 25, Gradle Kotlin DSL, JWT
- Frontend: Angular, TypeScript, Tailwind CSS
- Database: PostgreSQL 15
- Storage: AWS S3 (optional)

---

## Database Schema

**Relationship:** User 1:N Recommendation

**users**
```sql
id              UUID PRIMARY KEY
name            VARCHAR(100) NOT NULL
email           VARCHAR(255) UNIQUE NOT NULL
password_hash   VARCHAR(255) NOT NULL
profile_image   TEXT
created_at      TIMESTAMP DEFAULT NOW()
```

**recommendations**
```sql
id              UUID PRIMARY KEY
user_id         UUID NOT NULL REFERENCES users(id)
title           VARCHAR(255) NOT NULL
category        VARCHAR(50) NOT NULL CHECK (category IN ('Health','Restaurant','Apartment','Travel','Other'))
description     TEXT
image_url       TEXT
rating          INT CHECK (rating BETWEEN 1 AND 5)
created_at      TIMESTAMP DEFAULT NOW()
```

---

## API Endpoints

**Base:** `/api/v1`

**Auth**
- `POST /auth/signup` - Register user
- `POST /auth/login` - Login (returns JWT)

**Users**
- `GET /users/{id}` - Get user profile
- `GET /users/{id}/recommendations` - Get user's recommendations

**Recommendations**
- `GET /recommendations?category={category}` - List all (filter by category)
- `GET /recommendations/{id}` - Get one
- `POST /recommendations` - Create (auth required)
- `PUT /recommendations/{id}` - Update (auth required)
- `DELETE /recommendations/{id}` - Delete (auth required)

**Uploads**
- `POST /uploads/image` - Upload image (returns S3 URL)

---

## Backend Structure

**Layers:** Controller → Service → Repository → Database

**Package Structure:**
```
com.rbohush.trustcircle
 ├─ config/          # Security, CORS, etc.
 ├─ controller/      # REST endpoints
 ├─ service/         # Business logic
 ├─ repository/      # JPA repositories
 ├─ model/
 │   ├─ entity/      # User, Recommendation
 │   └─ dto/         # Request/Response DTOs
 └─ security/        # JWT filter, auth
```

**Key Dependencies:**
- `spring-boot-starter-web`
- `spring-boot-starter-security`
- `spring-boot-starter-data-jpa`
- `jjwt-api` (JWT)
- `postgresql`
- `aws-sdk-s3` (optional)

**Environment Variables:**
```
DB_URL, DB_USER, DB_PASSWORD
JWT_SECRET
AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_BUCKET_NAME
```

---

## Security

**Authentication Flow:**
1. User signs up/login → JWT returned
2. Frontend stores token (HttpOnly cookie recommended)
3. Protected requests: `Authorization: Bearer <token>`
4. Backend validates JWT signature
5. Token expiry: 1 hour (configurable)

**Password Security:**
- BCrypt hashing (never store plain text)
- Min 8 characters

**JWT Claims:** `sub`, `userId`, `email`

---

## Future Enhancements

- Follow/unfollow users
- Likes, comments, reactions
- Private sharing
- Advanced search & tags
- Mobile app (React Native)
- AI recommendations
- Location-based features (maps)