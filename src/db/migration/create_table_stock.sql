CREATE TYPE movement AS ENUM ('ENTER','EXIT');

CREATE TABLE stock(
    id VARCHAR PRIMARY KEY,
    movement movement,
    quantity FLOAT,
    unity unity,
    date TIMESTAMP,
    id_ingredient VARCHAR,
    FOREIGN KEY (id_ingredient) REFERENCES ingredient(id)
);