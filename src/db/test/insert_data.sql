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
INSERT INTO stock (id, movement, quantity, unity, date, id_ingredient) VALUES
                                                                           ('1', 'ENTER', 100, 'U', '2025-02-01 08:00:00.0', '3'),  -- Oeuf (100 unités)
                                                                           ('2', 'ENTER', 50, 'U', '2025-02-01 08:00:00.0', '4'),   -- Pain (50 unités)
                                                                           ('3', 'ENTER', 10000, 'G', '2025-02-01 08:00:00.0', '1'), -- Saucisse (10 kg = 10 000 g)
                                                                           ('4', 'ENTER', 20, 'L', '2025-02-01 08:00:00.0', '2');   -- Huile (20 L)
