CREATE TABLE shopping_cart(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    product_id BIGINT NOT NULL REFERENCES product(id),
    customer_id BIGINT NOT NULL REFERENCES customer(id),
    quantity SMALLINT NOT NULL,
    UNIQUE (product_id, customer_id),
    PRIMARY KEY(id)
);
