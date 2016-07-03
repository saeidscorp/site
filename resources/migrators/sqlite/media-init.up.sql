
CREATE TABLE media (
  id   INTEGER PRIMARY KEY AUTOINCREMENT,
  owner REFERENCES author(user_id),
  path VARCHAR(256),
  mime VARCHAR(30)
);