CREATE TABLE user (
  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  email VARCHAR(40) UNIQUE,
  last_name  VARCHAR(30),
  first_name VARCHAR(25),
  pass VARCHAR(20),
  join_date DATE DEFAULT CURRENT_DATE,
  last_login DATE,
  is_active BOOLEAN DEFAULT FALSE,
  activation_id VARCHAR(100) UNIQUE,
  uuid VARCHAR(43) NOT NULL
);

/*
INSERT INTO user (first_name, last_name, email, last_login, pass)
VALUES ('Saeid', 'Akbari', '***REMOVED***', CURRENT_DATE, 'dbus0913');
*/

INSERT INTO user (first_name, last_name, email, "is_active", "pass", "uuid") VALUES
  ('Saeid', 'Akbari', '***REMOVED***', 1,
   'bcrypt+sha512$d6d175aaa9c525174d817a74$12$24326124313224314d345444356149457a67516150447967517a67472e717a2e777047565a7071495330625441704f46686a556b5535376849743575',
   'b4f18236-2a14-49f6-837e-5e23def53124')
