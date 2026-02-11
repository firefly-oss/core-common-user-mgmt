-- Create enum types for the user management system

-- User type enum (EMPLOYEE or DISTRIBUTOR)
CREATE TYPE user_type_enum AS ENUM ('EMPLOYEE', 'DISTRIBUTOR');

-- Theme preference enum (LIGHT, DARK, SYSTEM)
CREATE TYPE theme_preference_enum AS ENUM ('LIGHT', 'DARK', 'SYSTEM');

-- Role scope type enum (GLOBAL, BRANCH, DISTRIBUTOR)
CREATE TYPE scope_type_enum AS ENUM ('GLOBAL', 'BRANCH', 'DISTRIBUTOR');