CREATE TABLE tag (
  id   INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(20) UNIQUE NOT NULL,
  desc VARCHAR(60)
);

INSERT INTO tag (name, desc) VALUES ('clojure', 'The Clojure programming language.');
INSERT INTO tag (name, desc) VALUES ('java', 'The Java programming language.');
