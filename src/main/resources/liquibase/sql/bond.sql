--liquibase formatted sql

--changeset aspeeencinaf:22
INSERT INTO bonds(id, user_id, name, code, invested_amount, annual_rate, maturity_date, coupon_type, current_value)
VALUES ('d9a8d170-b1f1-4e6b-b5f2-2dc6a1c12345', '1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec', 'Italian Government Bond 2030',
        'ITB2030', 10000.00, 2.50, '2030-12-31', 'Fixed', 10250.75);


--changeset aspeeencinaf:23
INSERT INTO bonds(id, user_id, name, code, invested_amount, annual_rate, maturity_date, coupon_type, current_value)
VALUES ('b45e690e-56c2-4bb1-9922-6fbe6c7a789a', '1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec', 'US Treasury Bond 2028',
        'UST2028', 15000.00, 3.10, '2028-06-15', 'Floating', 15450.00);


--changeset aspeeencinaf:24
INSERT INTO bonds(id, user_id, name, code, invested_amount, annual_rate, maturity_date, coupon_type, current_value)
VALUES ('cc6df3fc-03c4-4f08-91b9-0b92d1db3344', '1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec', 'Corporate Bond ABC',
        'CBABC2027', 7500.00, 4.75, '2027-03-01', 'Zero-Coupon', 8000.00);


--changeset aspeeencinaf:25
INSERT INTO bonds(id, user_id, name, code, invested_amount, annual_rate, maturity_date, coupon_type, current_value)
VALUES ('f8a0d0e4-4f72-41a3-9f33-d5c8290f5678', '1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec', 'Green Energy Bond 2032',
        'GEB2032', 20000.00, 2.85, '2032-09-30', 'Fixed', 21000.50);
