CREATE TABLE cart(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    customer_id BIGINT NOT NULL,
    total_cost FLOAT DEFAULT 0.0 NOT NULL,
    UNIQUE (customer_id),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    PRIMARY KEY (id)
);
