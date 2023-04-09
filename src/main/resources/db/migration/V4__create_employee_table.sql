CREATE TABLE employee(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    start_date DATE DEFAULT now(),
    leave_date DATE,
    date_of_birth DATE NOT NULL,
    street VARCHAR(20) NOT NULL,
    city VARCHAR(20) NOT NULL,
    region VARCHAR(20) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    PRIMARY KEY (id)
);
