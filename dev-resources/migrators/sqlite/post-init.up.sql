CREATE TABLE post (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  url_title VARCHAR(60) UNIQUE,
  title VARCHAR(150),
  featured_image REFERENCES media(id),
  short TEXT(250),
  date_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  author REFERENCES author(user_id),
  content TEXT);