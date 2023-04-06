CREATE TABLE order_item(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    order_id BIGINT NOT NULL REFERENCES customer_order(id),
    product_id BIGINT NOT NULL REFERENCES product(id),
    product_price DOUBLE PRECISION NOT NULL,
    quantity SMALLINT NOT NULL,
    UNIQUE (order_id, product_id),
    PRIMARY KEY (id)
);
