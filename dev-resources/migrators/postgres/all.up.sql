CREATE TABLE author (
  user_id INTEGER PRIMARY KEY--,
  --   FOREIGN KEY (user_id) REFERENCES "user" (id)
);

CREATE TABLE media (
  id    SERIAL PRIMARY KEY,
  owner INTEGER REFERENCES author (user_id),
  path  VARCHAR(256),
  mime  VARCHAR(30)
);

CREATE TABLE post (
  id             SERIAL PRIMARY KEY,
  url_title      VARCHAR(60) UNIQUE,
  title          VARCHAR(150),
  featured_image INTEGER REFERENCES media (id),
  short          TEXT,
  date_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  author         INTEGER REFERENCES author (user_id),
  content        TEXT
);

CREATE TABLE tag (
  id     SERIAL PRIMARY KEY,
  name   VARCHAR(20) UNIQUE NOT NULL,
  "desc" VARCHAR(60)
);

CREATE TABLE post_tag (
  tag_id  INTEGER REFERENCES tag (id),
  post_id INTEGER REFERENCES post (id)
);

CREATE TABLE "user" (
  id              SERIAL PRIMARY KEY NOT NULL,
  email           VARCHAR(40) UNIQUE,
  last_name       VARCHAR(30),
  first_name      VARCHAR(25),
  pass            VARCHAR(200),
  role            VARCHAR(30) DEFAULT 'none',
  join_date       DATE        DEFAULT CURRENT_DATE,
  last_login      DATE,
  is_active       BOOLEAN     DEFAULT FALSE,
  activation_id   VARCHAR(100) UNIQUE,
  uuid            VARCHAR(43)        NOT NULL,
  profile_picture INTEGER --REFERENCES media (id)
);
ALTER TABLE "user"
  ADD FOREIGN KEY (profile_picture) REFERENCES media (id);

CREATE TABLE comment (
  id          SERIAL PRIMARY KEY,
  writer      INTEGER REFERENCES "user" (id),
  target      INTEGER REFERENCES post (id),
  date_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_reply    BOOLEAN   DEFAULT FALSE,
  reply_to    INTEGER   DEFAULT NULL, -- REFERENCES comment (id),
  is_accepted BOOLEAN   DEFAULT FALSE,
  text        TEXT
);
ALTER TABLE comment
  ADD FOREIGN KEY (reply_to) REFERENCES comment (id);
ALTER TABLE author
  ADD FOREIGN KEY (user_id) REFERENCES "user" (id);


CREATE OR REPLACE FUNCTION truncate_tables(username IN VARCHAR)
  RETURNS VOID AS $$
DECLARE
    statements CURSOR FOR
    SELECT tablename
    FROM pg_tables
    WHERE tableowner = username AND schemaname = 'public';
BEGIN
  FOR stmt IN statements LOOP
    EXECUTE 'TRUNCATE TABLE ' || quote_ident(stmt.tablename) || ' RESTART IDENTITY CASCADE;';
  END LOOP;
END;
$$ LANGUAGE plpgsql;