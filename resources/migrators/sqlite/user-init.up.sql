CREATE TABLE user (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  email VARCHAR(40) UNIQUE,
  last_name  VARCHAR(30),
  first_name VARCHAR(25),
  pass VARCHAR(20),
  join_date DATE DEFAULT CURRENT_DATE,
  last_login DATE,
  is_active BOOLEAN DEFAULT FALSE,
  activation_id INTEGER
);

INSERT INTO user (first_name, last_name, email, last_login, pass)
VALUES ('Saeid', 'Akbari', '***REMOVED***', CURRENT_DATE, 'dbus0913');
