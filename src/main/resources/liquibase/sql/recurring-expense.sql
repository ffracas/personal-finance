--changeset aspeeencinaf:16
INSERT INTO RecurringExpenses (id, user_id, amount, category, frequency, start_date, end_date)
VALUES ('5f3b5b3e-4f87-442b-b0cd-37c58a10548e', '1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec', 120.00, 'Rent', 'Monthly',
        '2023-01-01', '2023-12-31');

--changeset aspeeencinaf:17
INSERT INTO RecurringExpenses (id, user_id, amount, category, frequency, start_date, end_date)
VALUES ('b6d09d88-5689-4f26-9e2f-c597b45c1592', '1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec', 75.50, 'Subscription',
        'Monthly', '2023-02-01', NULL);

--changeset aspeeencinaf:18
INSERT INTO RecurringExpenses (id, user_id, amount, category, frequency, start_date, end_date)
VALUES ('9c0a7723-8d92-471b-a0f3-d6b0c31ea510', '1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec', 150.00, 'Utilities',
        'Quarterly', '2023-03-15', '2024-03-15');
