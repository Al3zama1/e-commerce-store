CREATE TABLE order_item(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    quantity SMALLINT NOT NULL,
    price FLOAT NOT NULL,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);
