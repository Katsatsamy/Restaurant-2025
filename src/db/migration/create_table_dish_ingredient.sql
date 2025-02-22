CREATE TABLE IF NOT EXIST dish_ingredient(
    id_dish VARCHAR,
    id_ingredient VARCHAR,
    required_quantity int,
    unity unity,
    PRIMARY KEY (id_dish, id_ingredient),
    FOREIGN KEY (id_dish) REFERENCES dish (id),
    FOREIGN KEY (id_ingredient) REFERENCES ingredient (id)
);