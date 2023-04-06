CREATE TABLE costumer_order(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    date_placed TIMESTAMP NOT NULL,
    order_total DOUBLE PRECISION NOT NULL,
    customer_id INTEGER NOT NULL,
    status_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES customer(id),
    FOREIGN KEY (status_id) REFERENCES order_status(id)
);
