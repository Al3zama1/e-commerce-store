CREATE TABLE customer_order(
    order_id INTEGER GENERATED ALWAYS AS IDENTITY,
    date_placed TIMESTAMP NOT NULL,
    order_total DOUBLE PRECISION NOT NULL,
    customer_id INTEGER NOT NULL,
    PRIMARY KEY (order_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);
