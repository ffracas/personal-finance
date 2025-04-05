--liquibase formatted sql

--changeset ffracas:22
INSERT INTO DepositAccounts ( id, user_id, invested_amount, annual_rate, start_date, end_date )
VALUES ('d7f9a010-b12e-4f91-9c1a-aaaaffff1111','11111111-2222-3333-4444-555555555555',
        5000.00,2.75,'2024-05-01','2025-05-01');

--changeset ffracas:23
INSERT INTO DepositAccounts ( id, user_id, invested_amount, annual_rate, start_date, end_date)
VALUES ('c4be5e01-9d2a-48d4-b913-bbbbffff2222','99999999-8888-7777-6666-555555555555',
        10000.00,3.25,'2024-06-15','2025-06-15');