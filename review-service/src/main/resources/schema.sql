DROP TABLE IF EXISTS reviews;
CREATE TABLE reviews (
                         id SERIAL PRIMARY KEY,
                         product_id INT NOT NULL,
                         author VARCHAR(255),
                         subject VARCHAR(255),
                         content TEXT
);