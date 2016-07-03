CREATE TABLE post (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title VARCHAR(60),
  featured_image REFERENCES media(id),
  short TEXT(200),
  date_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  author REFERENCES author(user_id),
  content TEXT);