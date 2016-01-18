CREATE TABLE author (
  user_id INTEGER PRIMARY KEY,
  FOREIGN KEY (user_id) REFERENCES user (id)
);


INSERT INTO author (user_id) VALUES (1);