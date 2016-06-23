
CREATE TABLE media (
  id   INTEGER PRIMARY KEY AUTOINCREMENT,
  path VARCHAR(256),
  mime VARCHAR(30)
);

INSERT INTO media (id, path, MIME) VALUES (1, '/media/uploads/screenshot.png', 'image/png');
