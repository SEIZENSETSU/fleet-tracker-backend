-- Create User table
CREATE TABLE "User"
(
    uid          VARCHAR PRIMARY KEY,
    user_name    VARCHAR,
    fcm_token_id VARCHAR,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP
);
