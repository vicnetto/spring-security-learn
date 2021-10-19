CREATE SCHEMA IF NOT EXISTS general;

CREATE TABLE IF NOT EXISTS general.user (
    id SERIAL PRIMARY KEY,
    username TEXT,
    password TEXT,
    role TEXT,
    is_account_non_expired BOOLEAN,
    is_account_non_locked BOOLEAN,
    is_credentials_non_expired BOOLEAN,
    is_enabled BOOLEAN
)