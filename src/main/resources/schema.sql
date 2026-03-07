CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

CREATE TABLE offers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    required_quantity INT NOT NULL,
    bundle_price DECIMAL(10,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT chk_offer_dates CHECK (end_date >= start_date),
    CONSTRAINT fk_offers_product
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);