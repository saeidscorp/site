CREATE TABLE comment (
  id       INTEGER PRIMARY KEY AUTOINCREMENT,
  writer REFERENCES user(id),
  target REFERENCES post (id),
  date_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  is_reply BOOLEAN             DEFAULT FALSE,
  reply_to REFERENCES comment (id) DEFAULT NULL,
  text     TEXT
);

INSERT INTO comment (writer, target, text)
VALUES (1, 1, 'Nice article! Congratulations to you!');