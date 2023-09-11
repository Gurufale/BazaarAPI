Create table wallets(
 id bigint PRIMARY KEY AUTO_INCREMENT,
 balance int(20),
 note VARCHAR(50)
);

ALTER TABLE users ADD FOREIGN KEY (wallet_id) REFERENCES wallets(id);