CREATE TABLE employee(
    employee_id BIGINT GENERATED ALWAYS AS IDENTITY,
     first_name VARCHAR(50) NOT NULL,
     last_name VARCHAR(50) NOT NULL,
     email VARCHAR(50) UNIQUE NOT NULL,
     password VARCHAR(255) NOT NULL,
     address VARCHAR(50) NOT NULL,
     phone VARCHAR(20) NOT NULL,
     date_of_birth DATE NOT NULL ,
     start_date DATE NOT NULL,
     leave_date DATE,
     PRIMARY KEY (employee_id)
);
