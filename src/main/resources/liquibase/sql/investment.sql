--liquibase formatted sql

--changeset aspeeencinaf:19
INSERT INTO Investments (id, user_id, name, code, shares_owned, unit_price, investment_date, current_value)
VALUES ('a1b2c3d4-e5f6-7890-abcd-1234567890ab', '1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec', 'Apple Inc.',
        'AAPL', 50.0000, 150.2500, '2024-01-15', 7512.50);

--changeset aspeeencinaf:20
INSERT INTO Investments (id, user_id, name, code, shares_owned, unit_price, investment_date, current_value)
VALUES ('b2c3d4e5-f678-9012-abcd-2345678901bc', '1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec', 'Tesla Motors',
        'TSLA', 20.0000, 720.1000, '2023-11-03', 14800.00);

--changeset aspeeencinaf:21
INSERT INTO Investments (id, user_id, name, code, shares_owned, unit_price, investment_date, current_value)
VALUES ('c3d4e5f6-7890-1234-abcd-3456789012cd', '1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec', 'Amazon.com', 'AMZN',
        10.5000, 3300.7500, '2024-02-28', 34657.88);
