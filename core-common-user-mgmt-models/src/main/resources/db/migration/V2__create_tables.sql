-- Create tables for the user management system

-- USER ACCOUNT table
CREATE TABLE IF NOT EXISTS user_account (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    email VARCHAR(255) NOT NULL UNIQUE,
    user_type user_type_enum NOT NULL,
    branch_id BIGINT,
    distributor_id BIGINT,
    department_id BIGINT,
    position_id BIGINT,
    job_title VARCHAR(255),
    avatar_url VARCHAR(1024),
    theme_preference theme_preference_enum DEFAULT 'SYSTEM',
    language_preference VARCHAR(10) DEFAULT 'en',
    locale VARCHAR(10) DEFAULT 'en_US',
    timezone VARCHAR(50) DEFAULT 'UTC',
    contact_phone VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT fk_user_account_created_by FOREIGN KEY (created_by) REFERENCES user_account(id),
    CONSTRAINT fk_user_account_updated_by FOREIGN KEY (updated_by) REFERENCES user_account(id)
);

-- EXTERNAL IAM LINK table
CREATE TABLE IF NOT EXISTS user_external_identity (
    id BIGSERIAL PRIMARY KEY,
    user_account_id BIGINT NOT NULL,
    provider VARCHAR(50) NOT NULL,
    subject_id VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    linked_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT fk_user_external_identity_user_account FOREIGN KEY (user_account_id) REFERENCES user_account(id),
    CONSTRAINT fk_user_external_identity_created_by FOREIGN KEY (created_by) REFERENCES user_account(id),
    CONSTRAINT fk_user_external_identity_updated_by FOREIGN KEY (updated_by) REFERENCES user_account(id),
    CONSTRAINT uk_provider_subject_id UNIQUE (provider, subject_id)
);

-- ROLE table
CREATE TABLE IF NOT EXISTS role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    is_assignable BOOLEAN DEFAULT TRUE,
    scope_type scope_type_enum DEFAULT 'GLOBAL',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT fk_role_created_by FOREIGN KEY (created_by) REFERENCES user_account(id),
    CONSTRAINT fk_role_updated_by FOREIGN KEY (updated_by) REFERENCES user_account(id)
);

-- PERMISSION table
CREATE TABLE IF NOT EXISTS permission (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    domain VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT fk_permission_created_by FOREIGN KEY (created_by) REFERENCES user_account(id),
    CONSTRAINT fk_permission_updated_by FOREIGN KEY (updated_by) REFERENCES user_account(id)
);

-- USER ROLE ASSIGNMENT table
CREATE TABLE IF NOT EXISTS user_role (
    id BIGSERIAL PRIMARY KEY,
    user_account_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    branch_id BIGINT,
    distributor_id BIGINT,
    assigned_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT fk_user_role_user_account FOREIGN KEY (user_account_id) REFERENCES user_account(id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id),
    CONSTRAINT fk_user_role_assigned_by FOREIGN KEY (assigned_by) REFERENCES user_account(id),
    CONSTRAINT fk_user_role_created_by FOREIGN KEY (created_by) REFERENCES user_account(id),
    CONSTRAINT fk_user_role_updated_by FOREIGN KEY (updated_by) REFERENCES user_account(id),
    CONSTRAINT uk_user_role_scope UNIQUE (user_account_id, role_id, branch_id, distributor_id)
);

-- ROLE-PERMISSION MAPPING table
CREATE TABLE IF NOT EXISTS role_permission (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES role(id),
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permission(id),
    CONSTRAINT fk_role_permission_created_by FOREIGN KEY (created_by) REFERENCES user_account(id),
    CONSTRAINT fk_role_permission_updated_by FOREIGN KEY (updated_by) REFERENCES user_account(id),
    CONSTRAINT uk_role_permission UNIQUE (role_id, permission_id)
);

-- AUDIT LOGGING table
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_account_id BIGINT,
    action VARCHAR(100) NOT NULL,
    resource VARCHAR(100) NOT NULL,
    resource_id VARCHAR(255),
    metadata JSONB,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_log_user_account FOREIGN KEY (user_account_id) REFERENCES user_account(id)
);

-- Create indexes for better performance
CREATE INDEX idx_user_account_email ON user_account(email);
CREATE INDEX idx_user_account_is_active ON user_account(is_active);
CREATE INDEX idx_user_external_identity_user_account_id ON user_external_identity(user_account_id);
CREATE INDEX idx_user_role_user_account_id ON user_role(user_account_id);
CREATE INDEX idx_user_role_role_id ON user_role(role_id);
CREATE INDEX idx_role_permission_role_id ON role_permission(role_id);
CREATE INDEX idx_role_permission_permission_id ON role_permission(permission_id);
CREATE INDEX idx_audit_log_user_account_id ON audit_log(user_account_id);
CREATE INDEX idx_audit_log_action ON audit_log(action);
CREATE INDEX idx_audit_log_resource ON audit_log(resource);
CREATE INDEX idx_audit_log_timestamp ON audit_log(timestamp);