CREATE TABLE sk_example_table (id SERIAL, obj JSONB NOT NULL, PRIMARY KEY(id));
INSERT INTO sk_example_table (obj) VALUES ('{"current":0}'::JSONB);