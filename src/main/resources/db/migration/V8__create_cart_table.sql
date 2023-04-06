CREATE TABLE cart(
    product_id BIGINT REFERENCES product(product_id),
    customer_id BIGINT REFERENCES customer(customer_id),
    quantity INTEGER NOT NULL,
    PRIMARY KEY(product_id, customer_id)
);
