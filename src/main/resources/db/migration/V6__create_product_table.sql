CREATE TABLE product(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(30) NOT NULL,
    description VARCHAR,
    price FLOAT DEFAULT 0.0,
    stock_quantity  INTEGER DEFAULT 0
)
