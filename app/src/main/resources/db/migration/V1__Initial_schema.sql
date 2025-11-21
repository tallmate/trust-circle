-- =============================================================================
-- TrustCircle Initial Schema
-- Version: 1
-- Description: Creates core tables for users, categories, and recommendations
-- =============================================================================

-- Users table
-- Stores user account information for authentication and profile management
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    avatar_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Indexes for efficient user lookups
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);

-- Categories table
-- Defines the types of recommendations (Health, Restaurants, Apartments, Travel, etc.)
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP NOT NULL
);

-- Recommendations table
-- Core table storing user recommendations
CREATE TABLE recommendations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES categories(id),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(500),
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Indexes for efficient querying
CREATE INDEX idx_recommendations_user_id ON recommendations(user_id);
CREATE INDEX idx_recommendations_category_id ON recommendations(category_id);
CREATE INDEX idx_recommendations_created_at ON recommendations(created_at DESC);

-- =============================================================================
-- Initial Data
-- =============================================================================

-- Insert default categories
INSERT INTO categories (name, description, created_at) VALUES
    ('Health', 'Medical services, doctors, clinics, and wellness', CURRENT_TIMESTAMP),
    ('Restaurants', 'Food and dining experiences', CURRENT_TIMESTAMP),
    ('Apartments', 'Housing and accommodation', CURRENT_TIMESTAMP),
    ('Travel', 'Destinations, hotels, and travel tips', CURRENT_TIMESTAMP),
    ('Other', 'Miscellaneous recommendations', CURRENT_TIMESTAMP);
