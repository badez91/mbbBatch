-- SQL script to create the 'records' table

CREATE TABLE records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    trx_amount DECIMAL(15,2) NOT NULL,
    description VARCHAR(255),
    trx_date DATETIME NOT NULL,
    customer_id BIGINT NOT NULL,
    version BIGINT DEFAULT 0,
    INDEX idx_customer_id (customer_id),
    INDEX idx_account_number (account_number),
    INDEX idx_description (description)
);