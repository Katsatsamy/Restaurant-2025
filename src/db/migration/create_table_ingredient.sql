CREATE ROLE new_user WITH LOGIN PASSWORD '1234';

CREATE DATABASE restaurant;

GRANT CONNECT ON DATABASE restaurant TO new_user;
GRANT USAGE, CREATE ON SCHEMA public TO new_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO new_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO new_user;

CREATE TYPE unity AS ENUM ('G','L','U');

CREATE TABLE ingredient(
    id VARCHAR primary key,
    name VARCHAR,
    unity unity
);

CREATE TABLE price(
    id VARCHAR PRIMARY KEY,
    unit_price float,
    date DATE DEFAULT CURRENT_DATE,
    id_ingredient VARCHAR,
    FOREIGN KEY (id_ingredient) REFERENCES ingredient(id)
);

CREATE VIEW ingredient_price AS
SELECT i.id, i.name, i.unity, p.unit_price,p.date FROM ingredient i JOIN price p ON i.id = p.id_ingredient;