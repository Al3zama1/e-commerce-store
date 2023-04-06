CREATE TABLE employee(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
     first_name VARCHAR(50) NOT NULL,
     last_name VARCHAR(50) NOT NULL,
     email VARCHAR(50) UNIQUE NOT NULL,
     password VARCHAR(255) NOT NULL,
     phone VARCHAR(20) NOT NULL,
     date_of_birth DATE NOT NULL ,
     start_date DATE NOT NULL,
     leave_date DATE,
     street VARCHAR(50) NOT NULL,
     city VARCHAR(50) NOT NULL,
     region VARCHAR(50),
     postal_code VARCHAR(20),
     country VARCHAR(50),
     PRIMARY KEY (id)
);
