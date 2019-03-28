CREATE TABLE members
(
	id SERIAL PRIMARY KEY,
	user_name TEXT NOT NULL,
	email TEXT NOT NULL,
	password TEXT,
	nonce TEXT
);

CREATE UNIQUE INDEX user_name ON members(user_name);