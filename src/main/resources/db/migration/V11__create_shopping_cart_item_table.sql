CREATE TABLE cart_item(
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity SMALLINT NOT NULL,
    UNIQUE (product_id, cart_id),
    FOREIGN KEY (cart_id) REFERENCES cart(id)
);
