CREATE TABLE users (
	id uuid NOT NULL,
	name text NOT NULL,
	email text NOT NULL,
	cpf text NOT NULL,
	birthday date NOT NULL,
	created_date datetime NOT NULL,
	last_modification_date datetime NOT NULL
);