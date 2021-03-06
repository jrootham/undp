CREATE TABLE language
(
	id SERIAL PRIMARY KEY,
	code TEXT NOT NULL,
	name TEXT NOT NULL
);

CREATE UNIQUE INDEX code ON language (code);

INSERT INTO language (code, name) VALUES ('en', 'English');
INSERT INTO language (code, name) VALUES ('fr', 'Français');

CREATE TABLE fixed_string
(
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	version INTEGER NOT NULL,
	index INTEGER NOT NULL
);

CREATE UNIQUE INDEX name_version ON fixed_string (name,version);
CREATE UNIQUE INDEX name_version_index ON fixed_string (name,version,index);

CREATE TABLE fixed_translation
(
	id SERIAL PRIMARY KEY,
	fixed_string_id INTEGER REFERENCES fixed_string(id),
	language_id INTEGER REFERENCES language(id),
	string TEXT NOT NULL
);

CREATE UNIQUE INDEX fixed_string_language ON fixed_translation (fixed_string_id,language_id);
