CREATE TABLE employee_role(
    role_id BIGINT REFERENCES user_role(id),
    employee_id BIGINT REFERENCES employee(id),
    PRIMARY KEY (role_id, employee_id)
);
