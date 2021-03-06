CREATE TABLE user (
  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  email VARCHAR(40) UNIQUE,
  last_name  VARCHAR(30),
  first_name VARCHAR(25),
  pass VARCHAR(20),
  role VARCHAR(30) DEFAULT 'none',
  join_date DATE DEFAULT CURRENT_DATE,
  last_login DATE,
  is_active BOOLEAN DEFAULT 0,
  activation_id VARCHAR(100) UNIQUE,
  uuid VARCHAR(43) NOT NULL,
  profile_picture REFERENCES media(id)
);