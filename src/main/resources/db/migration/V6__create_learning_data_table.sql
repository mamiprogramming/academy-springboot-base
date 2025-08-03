CREATE TABLE learning_data (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    item VARCHAR(100) NOT NULL,
    learning_month CHAR(7) NOT NULL,
    learning_time INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_learning_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_learning_category FOREIGN KEY (category_id) REFERENCES categories(id)
);