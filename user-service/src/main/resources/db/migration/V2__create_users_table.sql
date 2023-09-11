CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255),
    phone_number VARCHAR(50),
    preference VARCHAR(10),
    password varchar(255),
    role VARCHAR(10),
    wallet_id bigint unique,
    referred_by VARCHAR(255)
);