CREATE TYPE order_status AS ENUM ('CREE', 'CONFIRME', 'EN_PREPARATION', 'TERMINE', 'SERVI');


CREATE TABLE orders (
    id VARCHAR PRIMARY KEY,
    status order_status DEFAULT 'CREE',
    date TIMESTAMP DEFAULT NOW()
);

CREATE TABLE dish_order (
    id VARCHAR PRIMARY KEY,
    id_order VARCHAR,
    id_dish VARCHAR,
    quantity FLOAT CHECK (quantity > 0),
    FOREIGN KEY (id_order) REFERENCES orders(id),
    FOREIGN KEY (id_dish) REFERENCES dish(id)
);

CREATE TABLE dish_order_status_history (
    id SERIAL PRIMARY KEY,
    id_dish_order VARCHAR,
    status order_status,
    date TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (id_dish_order) REFERENCES dish_order(id)
);
