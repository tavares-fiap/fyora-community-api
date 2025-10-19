CREATE TABLE user_accounts (
                               id BIGSERIAL PRIMARY KEY,
                               username VARCHAR(120) UNIQUE NOT NULL,
                               password_hash VARCHAR(120) NOT NULL,
                               role VARCHAR(30) NOT NULL DEFAULT 'USER',
                               created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
