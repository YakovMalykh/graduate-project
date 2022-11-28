-- liquibase formatted sql

-- changeset sascvotch:1

CREATE TABLE IF NOT EXISTS users
(
    user_id   SERIAL PRIMARY KEY,
    user_first_name VARCHAR (100) NOT NULL,
    user_last_name VARCHAR (100) NOT NULL,
    user_phone_number VARCHAR(20) NOT NULL,
    user_email VARCHAR (30) UNIQUE NOT NULL,
    user_password VARCHAR (255) NOT NULL,
    user_role VARCHAR (10) NOT NULL
    );
CREATE TABLE IF NOT EXISTS ads
(
    ads_id  SERIAL PRIMARY KEY,
    author_id BIGINT REFERENCES users (user_id),
    ads_image VARCHAR (250),
    ads_title VARCHAR (100) NOT NULL,
    ads_description TEXT,
    ads_price INTEGER NOT NULL
    );

CREATE TABLE IF NOT EXISTS comment
(
    comment_id   SERIAL PRIMARY KEY,
    author_id BIGINT REFERENCES users (user_id),
    ads_id BIGINT REFERENCES ads (ads_id),
    comment_created TIMESTAMP NOT NULL,
    comment_text TEXT NOT NULL
    );

-- changeset sascvotch:2

CREATE TABLE IF NOT EXISTS avatars
(
    avatar_id   SERIAL PRIMARY KEY,
    author_id BIGINT REFERENCES users (user_id),
    file_path VARCHAR (250) NOT NULL,
    file_size BIGINT NOT NULL,
    media_type VARCHAR (250) NOT NULL,
    prewiew OID NOT NULL
    );

-- changeset sascvotch:3
ALTER TABLE Avatars DROP COLUMN IF EXISTS file_path;
ALTER TABLE Avatars DROP COLUMN IF EXISTS file_size;

-- changeset sascvotch:4

INSERT INTO users (user_first_name, user_last_name,user_phone_number,user_email,user_password,user_role)
VALUES ('Admin','Admin','+7(999)999-99-99','admin@admin.ru','adminpassword','ADMIN');