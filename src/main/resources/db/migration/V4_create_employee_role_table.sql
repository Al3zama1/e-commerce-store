CREATE TABLE employee_role(
    role_id BIGINT REFERENCES user_role(role_id),
    employee_id BIGINT REFERENCES employee(employee_id),
    PRIMARY KEY (role_id, employee_id)
);
