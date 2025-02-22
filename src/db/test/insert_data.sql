INSERT INTO ingredient (id, name, unity) VALUES
('1', 'Saucisse', 'G'),
('2', 'Huile', 'L'),
('3', 'Oeuf', 'U'),
('4', 'Pain', 'U');

INSERT INTO price (id, unit_price, date, id_ingredient) VALUES
                                                            ('1', 20, '2025-01-01', '1'),
                                                            ('2', 10000, '2025-01-01', '2'),
                                                            ('3', 1000, '2025-01-01', '3'),
                                                            ('4', 1000, '2025-01-01', '4');

INSERT INTO dish (id, name, unit_price) VALUES
    ('1', 'Hot dog', 15000);

INSERT INTO dish_ingredient (id_dish, id_ingredient, required_quantity, unity) VALUES
                                                                                   ('1', '1', 100, 'G'),
                                                                                   ('1', '2', 0.15, 'L'),
                                                                                   ('1', '3', 1, 'U'),
                                                                                   ('1', '4', 1, 'U');
