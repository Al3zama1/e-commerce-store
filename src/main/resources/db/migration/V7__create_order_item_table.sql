CREATE TABLE order_item(
    order_id BIGINT REFERENCES customer_order(order_id),
    product_id BIGINT REFERENCES product(product_id),
    PRIMARY KEY (order_id, product_id)
);
