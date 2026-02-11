-- Create casts for enum types to allow implicit conversion from VARCHAR

-- Cast for user_type_enum
CREATE CAST (VARCHAR AS user_type_enum)
    WITH INOUT AS IMPLICIT;

-- Cast for theme_preference_enum
CREATE CAST (VARCHAR AS theme_preference_enum)
    WITH INOUT AS IMPLICIT;

-- Cast for scope_type_enum
CREATE CAST (VARCHAR AS scope_type_enum)
    WITH INOUT AS IMPLICIT;