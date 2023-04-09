CREATE TABLE cart(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    quantity SMALLINT NOT NULL,
    product_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    UNIQUE (product_id, customer_id),
    PRIMARY KEY (id)
);
