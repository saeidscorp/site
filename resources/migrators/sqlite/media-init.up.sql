
CREATE TABLE media (
  id   INTEGER PRIMARY KEY AUTOINCREMENT,
  path VARCHAR(256),
  mime VARCHAR(30)
);

INSERT INTO media (path, MIME) VALUES ('/srv/uploads/data/profile.png', 'image/png');
