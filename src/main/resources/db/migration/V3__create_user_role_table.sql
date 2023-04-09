CREATE TABLE user_role(
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES role(id),
    PRIMARY KEY (user_id, role_id)
)
