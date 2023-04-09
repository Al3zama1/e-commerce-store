INSERT INTO order_status(status)
VALUES ('PROCESSING');

INSERT INTO users(email, password)
VALUES ('john.last@gmail.com', '13535453');

INSERT INTO customer(first_name, last_name, phone, date_of_birth, street, city, region, postal_code, country, user_id)
VALUES ('john', 'last', '23434', '11-02-1998', 'sfsef', 'sfsdf', 'sfsfsd', 'f233', 'fsfdsf', 1);

INSERT INTO customer_order(total, status_id, customer_id)
VALUES (100, 1, 1);
