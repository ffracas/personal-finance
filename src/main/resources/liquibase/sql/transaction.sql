--liquibase formatted sql

--changeset aspeeencinaf:13
INSERT INTO transactions (id, user_id, amount, transaction_date, type, category, sub_category)
VALUES ('550e8400-e29b-41d4-a716-446655440000', -- Example UUID for id
        '1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec', -- Example UUID for user_id
        150.00, '2025-04-05', 'income', 'Groceries', 'Supermarket');

--changeset aspeeencinaf:14
INSERT INTO transactions (id, user_id, amount, transaction_date, type, category, sub_category)
VALUES ('660e8400-e29b-41d4-a716-556655440111', -- Example UUID for id
        '1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec', -- Same user_id as above
        200.00, '2025-04-04', 'expense', 'Utilities', 'Electricity');
--changeset aspeeencinaf:15
INSERT INTO transactions (id, user_id, amount, transaction_date, type, category, sub_category)
VALUES ('660e8400-e29b-41d4-a716-556655440112', -- Example UUID for id
        '1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec', -- Same user_id as above
        100.00, '2025-04-04', 'expense', 'Life', 'Medical');
