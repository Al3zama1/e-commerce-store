CREATE TABLE customer_order(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    total FLOAT NOT NULL,
    date_placed TIMESTAMP DEFAULT NOW(),
    date_shipped TIMESTAMP,
    date_delivered TIMESTAMP,
    status_id SMALLINT NOT NULL,
    customer_id BIGINT NOT NULL,
    FOREIGN KEY (status_id) REFERENCES order_status(id),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    PRIMARY KEY (id)
);
