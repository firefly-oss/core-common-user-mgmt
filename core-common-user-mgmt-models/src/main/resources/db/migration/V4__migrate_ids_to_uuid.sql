-- Migration script to convert all ID fields from BIGINT to UUID
-- This script handles data migration and maintains referential integrity

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Step 1: Add temporary UUID columns to all tables
ALTER TABLE user_account ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE role ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE permission ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE user_role ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE role_permission ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE user_external_identity ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE audit_log ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();

-- Step 2: Add temporary UUID foreign key columns
ALTER TABLE user_external_identity ADD COLUMN user_account_id_uuid UUID;
ALTER TABLE user_role ADD COLUMN user_account_id_uuid UUID;
ALTER TABLE user_role ADD COLUMN role_id_uuid UUID;
ALTER TABLE role_permission ADD COLUMN role_id_uuid UUID;
ALTER TABLE role_permission ADD COLUMN permission_id_uuid UUID;
ALTER TABLE audit_log ADD COLUMN user_account_id_uuid UUID;

-- Add temporary UUID columns for created_by and updated_by references
ALTER TABLE user_account ADD COLUMN created_by_uuid UUID;
ALTER TABLE user_account ADD COLUMN updated_by_uuid UUID;
ALTER TABLE role ADD COLUMN created_by_uuid UUID;
ALTER TABLE role ADD COLUMN updated_by_uuid UUID;
ALTER TABLE permission ADD COLUMN created_by_uuid UUID;
ALTER TABLE permission ADD COLUMN updated_by_uuid UUID;
ALTER TABLE user_role ADD COLUMN created_by_uuid UUID;
ALTER TABLE user_role ADD COLUMN updated_by_uuid UUID;
ALTER TABLE user_role ADD COLUMN assigned_by_uuid UUID;
ALTER TABLE role_permission ADD COLUMN created_by_uuid UUID;
ALTER TABLE role_permission ADD COLUMN updated_by_uuid UUID;
ALTER TABLE user_external_identity ADD COLUMN created_by_uuid UUID;
ALTER TABLE user_external_identity ADD COLUMN updated_by_uuid UUID;

-- Step 3: Populate UUID foreign key columns by mapping from existing BIGINT values
-- Map user_account references
UPDATE user_external_identity 
SET user_account_id_uuid = ua.id_uuid 
FROM user_account ua 
WHERE user_external_identity.user_account_id = ua.id;

UPDATE user_role 
SET user_account_id_uuid = ua.id_uuid 
FROM user_account ua 
WHERE user_role.user_account_id = ua.id;

UPDATE audit_log 
SET user_account_id_uuid = ua.id_uuid 
FROM user_account ua 
WHERE audit_log.user_account_id = ua.id;

-- Map role references
UPDATE user_role 
SET role_id_uuid = r.id_uuid 
FROM role r 
WHERE user_role.role_id = r.id;

UPDATE role_permission 
SET role_id_uuid = r.id_uuid 
FROM role r 
WHERE role_permission.role_id = r.id;

-- Map permission references
UPDATE role_permission 
SET permission_id_uuid = p.id_uuid 
FROM permission p 
WHERE role_permission.permission_id = p.id;

-- Map created_by and updated_by references
UPDATE user_account 
SET created_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE user_account.created_by = ua.id;

UPDATE user_account 
SET updated_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE user_account.updated_by = ua.id;

UPDATE role 
SET created_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE role.created_by = ua.id;

UPDATE role 
SET updated_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE role.updated_by = ua.id;

UPDATE permission 
SET created_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE permission.created_by = ua.id;

UPDATE permission 
SET updated_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE permission.updated_by = ua.id;

UPDATE user_role 
SET created_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE user_role.created_by = ua.id;

UPDATE user_role 
SET updated_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE user_role.updated_by = ua.id;

UPDATE user_role 
SET assigned_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE user_role.assigned_by = ua.id;

UPDATE role_permission 
SET created_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE role_permission.created_by = ua.id;

UPDATE role_permission 
SET updated_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE role_permission.updated_by = ua.id;

UPDATE user_external_identity 
SET created_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE user_external_identity.created_by = ua.id;

UPDATE user_external_identity 
SET updated_by_uuid = ua.id_uuid 
FROM user_account ua 
WHERE user_external_identity.updated_by = ua.id;

-- Step 4: Drop all foreign key constraints
ALTER TABLE user_external_identity DROP CONSTRAINT IF EXISTS fk_user_external_identity_user_account;
ALTER TABLE user_external_identity DROP CONSTRAINT IF EXISTS fk_user_external_identity_created_by;
ALTER TABLE user_external_identity DROP CONSTRAINT IF EXISTS fk_user_external_identity_updated_by;
ALTER TABLE user_role DROP CONSTRAINT IF EXISTS fk_user_role_user_account;
ALTER TABLE user_role DROP CONSTRAINT IF EXISTS fk_user_role_role;
ALTER TABLE user_role DROP CONSTRAINT IF EXISTS fk_user_role_created_by;
ALTER TABLE user_role DROP CONSTRAINT IF EXISTS fk_user_role_updated_by;
ALTER TABLE user_role DROP CONSTRAINT IF EXISTS fk_user_role_assigned_by;
ALTER TABLE role_permission DROP CONSTRAINT IF EXISTS fk_role_permission_role;
ALTER TABLE role_permission DROP CONSTRAINT IF EXISTS fk_role_permission_permission;
ALTER TABLE role_permission DROP CONSTRAINT IF EXISTS fk_role_permission_created_by;
ALTER TABLE role_permission DROP CONSTRAINT IF EXISTS fk_role_permission_updated_by;
ALTER TABLE audit_log DROP CONSTRAINT IF EXISTS fk_audit_log_user_account;
ALTER TABLE role DROP CONSTRAINT IF EXISTS fk_role_created_by;
ALTER TABLE role DROP CONSTRAINT IF EXISTS fk_role_updated_by;
ALTER TABLE permission DROP CONSTRAINT IF EXISTS fk_permission_created_by;
ALTER TABLE permission DROP CONSTRAINT IF EXISTS fk_permission_updated_by;
-- Drop self-referencing foreign keys for user_account table
ALTER TABLE user_account DROP CONSTRAINT IF EXISTS fk_user_account_created_by;
ALTER TABLE user_account DROP CONSTRAINT IF EXISTS fk_user_account_updated_by;

-- Step 5: Drop old BIGINT columns and rename UUID columns
-- Drop old primary key constraints and columns
ALTER TABLE user_account DROP CONSTRAINT user_account_pkey;
ALTER TABLE user_account DROP COLUMN id;
ALTER TABLE user_account RENAME COLUMN id_uuid TO id;
ALTER TABLE user_account ADD PRIMARY KEY (id);

ALTER TABLE role DROP CONSTRAINT role_pkey;
ALTER TABLE role DROP COLUMN id;
ALTER TABLE role RENAME COLUMN id_uuid TO id;
ALTER TABLE role ADD PRIMARY KEY (id);

ALTER TABLE permission DROP CONSTRAINT permission_pkey;
ALTER TABLE permission DROP COLUMN id;
ALTER TABLE permission RENAME COLUMN id_uuid TO id;
ALTER TABLE permission ADD PRIMARY KEY (id);

ALTER TABLE user_role DROP CONSTRAINT user_role_pkey;
ALTER TABLE user_role DROP COLUMN id;
ALTER TABLE user_role RENAME COLUMN id_uuid TO id;
ALTER TABLE user_role ADD PRIMARY KEY (id);

ALTER TABLE role_permission DROP CONSTRAINT role_permission_pkey;
ALTER TABLE role_permission DROP COLUMN id;
ALTER TABLE role_permission RENAME COLUMN id_uuid TO id;
ALTER TABLE role_permission ADD PRIMARY KEY (id);

ALTER TABLE user_external_identity DROP CONSTRAINT user_external_identity_pkey;
ALTER TABLE user_external_identity DROP COLUMN id;
ALTER TABLE user_external_identity RENAME COLUMN id_uuid TO id;
ALTER TABLE user_external_identity ADD PRIMARY KEY (id);

ALTER TABLE audit_log DROP CONSTRAINT audit_log_pkey;
ALTER TABLE audit_log DROP COLUMN id;
ALTER TABLE audit_log RENAME COLUMN id_uuid TO id;
ALTER TABLE audit_log ADD PRIMARY KEY (id);

-- Drop old foreign key columns and rename UUID foreign key columns
ALTER TABLE user_external_identity DROP COLUMN user_account_id;
ALTER TABLE user_external_identity RENAME COLUMN user_account_id_uuid TO user_account_id;

ALTER TABLE user_role DROP COLUMN user_account_id;
ALTER TABLE user_role RENAME COLUMN user_account_id_uuid TO user_account_id;
ALTER TABLE user_role DROP COLUMN role_id;
ALTER TABLE user_role RENAME COLUMN role_id_uuid TO role_id;

ALTER TABLE role_permission DROP COLUMN role_id;
ALTER TABLE role_permission RENAME COLUMN role_id_uuid TO role_id;
ALTER TABLE role_permission DROP COLUMN permission_id;
ALTER TABLE role_permission RENAME COLUMN permission_id_uuid TO permission_id;

ALTER TABLE audit_log DROP COLUMN user_account_id;
ALTER TABLE audit_log RENAME COLUMN user_account_id_uuid TO user_account_id;

-- Drop old created_by/updated_by columns and rename UUID columns
ALTER TABLE user_account DROP COLUMN created_by;
ALTER TABLE user_account RENAME COLUMN created_by_uuid TO created_by;
ALTER TABLE user_account DROP COLUMN updated_by;
ALTER TABLE user_account RENAME COLUMN updated_by_uuid TO updated_by;

ALTER TABLE role DROP COLUMN created_by;
ALTER TABLE role RENAME COLUMN created_by_uuid TO created_by;
ALTER TABLE role DROP COLUMN updated_by;
ALTER TABLE role RENAME COLUMN updated_by_uuid TO updated_by;

ALTER TABLE permission DROP COLUMN created_by;
ALTER TABLE permission RENAME COLUMN created_by_uuid TO created_by;
ALTER TABLE permission DROP COLUMN updated_by;
ALTER TABLE permission RENAME COLUMN updated_by_uuid TO updated_by;

ALTER TABLE user_role DROP COLUMN created_by;
ALTER TABLE user_role RENAME COLUMN created_by_uuid TO created_by;
ALTER TABLE user_role DROP COLUMN updated_by;
ALTER TABLE user_role RENAME COLUMN updated_by_uuid TO updated_by;
ALTER TABLE user_role DROP COLUMN assigned_by;
ALTER TABLE user_role RENAME COLUMN assigned_by_uuid TO assigned_by;

ALTER TABLE role_permission DROP COLUMN created_by;
ALTER TABLE role_permission RENAME COLUMN created_by_uuid TO created_by;
ALTER TABLE role_permission DROP COLUMN updated_by;
ALTER TABLE role_permission RENAME COLUMN updated_by_uuid TO updated_by;

ALTER TABLE user_external_identity DROP COLUMN created_by;
ALTER TABLE user_external_identity RENAME COLUMN created_by_uuid TO created_by;
ALTER TABLE user_external_identity DROP COLUMN updated_by;
ALTER TABLE user_external_identity RENAME COLUMN updated_by_uuid TO updated_by;

-- Step 6: Recreate foreign key constraints with UUID references
ALTER TABLE user_external_identity 
ADD CONSTRAINT fk_user_external_identity_user_account 
FOREIGN KEY (user_account_id) REFERENCES user_account(id);

ALTER TABLE user_external_identity 
ADD CONSTRAINT fk_user_external_identity_created_by 
FOREIGN KEY (created_by) REFERENCES user_account(id);

ALTER TABLE user_external_identity 
ADD CONSTRAINT fk_user_external_identity_updated_by 
FOREIGN KEY (updated_by) REFERENCES user_account(id);

ALTER TABLE user_role 
ADD CONSTRAINT fk_user_role_user_account 
FOREIGN KEY (user_account_id) REFERENCES user_account(id);

ALTER TABLE user_role 
ADD CONSTRAINT fk_user_role_role 
FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE user_role 
ADD CONSTRAINT fk_user_role_created_by 
FOREIGN KEY (created_by) REFERENCES user_account(id);

ALTER TABLE user_role 
ADD CONSTRAINT fk_user_role_updated_by 
FOREIGN KEY (updated_by) REFERENCES user_account(id);

ALTER TABLE user_role 
ADD CONSTRAINT fk_user_role_assigned_by 
FOREIGN KEY (assigned_by) REFERENCES user_account(id);

ALTER TABLE role_permission 
ADD CONSTRAINT fk_role_permission_role 
FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE role_permission 
ADD CONSTRAINT fk_role_permission_permission 
FOREIGN KEY (permission_id) REFERENCES permission(id);

ALTER TABLE role_permission 
ADD CONSTRAINT fk_role_permission_created_by 
FOREIGN KEY (created_by) REFERENCES user_account(id);

ALTER TABLE role_permission 
ADD CONSTRAINT fk_role_permission_updated_by 
FOREIGN KEY (updated_by) REFERENCES user_account(id);

ALTER TABLE audit_log 
ADD CONSTRAINT fk_audit_log_user_account 
FOREIGN KEY (user_account_id) REFERENCES user_account(id);

ALTER TABLE role 
ADD CONSTRAINT fk_role_created_by 
FOREIGN KEY (created_by) REFERENCES user_account(id);

ALTER TABLE role 
ADD CONSTRAINT fk_role_updated_by 
FOREIGN KEY (updated_by) REFERENCES user_account(id);

ALTER TABLE permission 
ADD CONSTRAINT fk_permission_created_by 
FOREIGN KEY (created_by) REFERENCES user_account(id);

ALTER TABLE permission
ADD CONSTRAINT fk_permission_updated_by
FOREIGN KEY (updated_by) REFERENCES user_account(id);

-- Add self-referencing foreign keys for user_account table
ALTER TABLE user_account
ADD CONSTRAINT fk_user_account_created_by
FOREIGN KEY (created_by) REFERENCES user_account(id);

ALTER TABLE user_account
ADD CONSTRAINT fk_user_account_updated_by
FOREIGN KEY (updated_by) REFERENCES user_account(id);

-- Step 7: Recreate indexes with UUID columns
DROP INDEX IF EXISTS idx_user_account_email;
DROP INDEX IF EXISTS idx_user_account_is_active;
DROP INDEX IF EXISTS idx_user_external_identity_user_account_id;
DROP INDEX IF EXISTS idx_user_role_user_account_id;
DROP INDEX IF EXISTS idx_user_role_role_id;
DROP INDEX IF EXISTS idx_role_permission_role_id;
DROP INDEX IF EXISTS idx_role_permission_permission_id;
DROP INDEX IF EXISTS idx_audit_log_user_account_id;
DROP INDEX IF EXISTS idx_audit_log_action;
DROP INDEX IF EXISTS idx_audit_log_resource;

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
