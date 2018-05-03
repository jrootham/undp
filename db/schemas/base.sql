CREATE TABLE languages
(
	language_id INTEGER PRIMARY KEY,
	code TEXT NOT NULL,
	name TEXT NOT NULL
);

INSERT INTO languages (language_id, code, name) VALUES (1, 'en', 'English');

CREATE TABLE roles
(
	role_id INTEGER PRIMARY KEY
);

INSERT INTO roles (role_id) VALUES (1);
INSERT INTO roles (role_id) VALUES (2);
INSERT INTO roles (role_id) VALUES (3);
INSERT INTO roles (role_id) VALUES (4);

CREATE TABLE role_names 
(
	role INTEGER NOT NULL REFERENCES roles (role_id),
	language INTEGER NOT NULL REFERENCES languages (language_id),	
	name TEXT NOT NULL,
	PRIMARY KEY (role, language)
);

INSERT INTO role_names (role, language, name) VALUES (1, 1, 'Administrator');
INSERT INTO role_names (role, language, name) VALUES (2, 1, 'Moderator');
INSERT INTO role_names (role, language, name) VALUES (3, 1, 'Translator');
INSERT INTO role_names (role, language, name) VALUES (4, 1, 'Executive');

CREATE TABLE sections
(
	section_id INTEGER NOT NULL PRIMARY KEY
);

CREATE TABLE section_names
(
	section INTEGER NOT NULL REFERENCES sections (section_id),
	name TEXT NOT NULL,
	language INTEGER NOT NULL REFERENCES languages (language_id),
	PRIMARY KEY (section, language)
);

CREATE TABLE ridings
(
	riding_id INTEGER PRIMARY KEY
);

CREATE TABLE riding_names
(
	riding INTEGER NOT NULL,
	language INTEGER NOT NULL,
	name TEXT NOT NULL,
	PRIMARY KEY (riding,language)
);

CREATE TABLE members 
(
    member_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    email TEXT NOT NULL,
    riding INTEGER NOT NULL REFERENCES ridings (riding_id),
    valid BOOLEAN NOT NULL DEFAULT TRUE,
    banned BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE member_role
(
	member INTEGER NOT NULL REFERENCES members (member_id),
	role INTEGER NOT NULL REFERENCES roles (role_id),
	PRIMARY KEY (member, role)
);

CREATE TABLE resolutions 
(
	resolution_id SERIAL PRIMARY KEY,
	section INTEGER NOT NULL REFERENCES sections (section_id),
	author INTEGER NOT NULL REFERENCES members (member_id),
	public BOOLEAN NOT NULL DEFAULT FALSE,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE versions
(
    version_id SERIAL PRIMARY KEY,
    resolution INTEGER NOT NULL REFERENCES resolutions (resolution_id),
    version INTEGER NOT NULL,
    language INTEGER NOT NULL REFERENCES languages (language_id),
    translation_depth INTEGER NOT NULL,
    title TEXT NOT NULL,
    motivation TEXT NOT NULL,
    resolution_body TEXT NOT NULL,
    author INTEGER NOT NULL REFERENCES members (member_id),
    public BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (resolution, version, language)
);

CREATE TABLE comments
(
	comment_id SERIAL PRIMARY KEY,
	version INTEGER NOT NULL REFERENCES versions (version_id),
	thread INTEGER NOT NULL,
	thread_index INTEGER NOT NULL,
	language INTEGER NOT NULL REFERENCES languages (language_id),
	thread_parent INTEGER REFERENCES comments (comment_id),
	author INTEGER NOT NULL REFERENCES members (member_id),
	translation_depth INTEGER NOT NULL,
	public BOOLEAN NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	became_public TIMESTAMP DEFAULT NULL,
	UNIQUE (version, thread, thread_index, language)
);

CREATE TABLE submitted
(
	version INTEGER NOT NULL REFERENCES versions (version_id),
	submitter INTEGER NOT NULL REFERENCES members (member_id),
	riding INTEGER NOT NULL REFERENCES ridings (riding_id),
	PRIMARY KEY (version, submitter)
);

CREATE TABLE passed
(
	version INTEGER NOT NULL REFERENCES versions (version_id),
	riding INTEGER NOT NULL REFERENCES ridings (riding_id),
	PRIMARY KEY (version, riding)
);

CREATE TABLE collaborators
(
	collaborators_id SERIAL PRIMARY KEY,
	resolution INTEGER NOT NULL REFERENCES resolutions (resolution_id),
	host INTEGER NOT NULL REFERENCES members (member_id),
	invitee INTEGER NOT NULL REFERENCES members (member_id),
	UNIQUE (resolution, host,invitee)
);

CREATE TABLE request_translation_version
(
	version INTEGER NOT NULL REFERENCES versions (version_id),
	requester INTEGER NOT NULL REFERENCES members (member_id),
	PRIMARY KEY (version, requester)
);

CREATE TABLE request_translation_comment
(
	comment INTEGER NOT NULL REFERENCES comments (comment_id),
	requester INTEGER NOT NULL REFERENCES members (member_id),
	PRIMARY KEY (comment, requester)
);

CREATE TABLE request_moderation_version
(
	version INTEGER NOT NULL REFERENCES versions (version_id),
	requester INTEGER NOT NULL REFERENCES members (member_id),
	reason TEXT NOT NULL,
	PRIMARY KEY (version, requester)
);

CREATE TABLE request_moderation_comment
(
	comment INTEGER NOT NULL REFERENCES comments (comment_id),
	requester INTEGER NOT NULL REFERENCES members (member_id),
	reason TEXT NOT NULL,
	PRIMARY KEY (comment, requester)
);
