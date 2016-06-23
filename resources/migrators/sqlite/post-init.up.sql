CREATE TABLE post (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title VARCHAR(60),
  featured_image REFERENCES media(id),
  short TEXT(200),
  date_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  author_id REFERENCES author(user_id),
  content TEXT);

INSERT INTO post (title, short, author_id, content)
VALUES ('Joining the Linux community!', 'Today Im in the community', 1,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla laoreet aliquet sem, a imperdiet turpis ultricies a. Donec quis pulvinar risus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Ut sem leo, pellentesque sit amet volutpat at, fermentum eget magna. Maecenas rutrum metus vel commodo pellentesque. Ut quis lorem et ipsum tincidunt tristique. Vivamus lobortis suscipit turpis sit amet vestibulum. Quisque sagittis mauris eget ligula varius euismod. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum lorem velit, ultricies ac augue vel, blandit ullamcorper sapien. Fusce venenatis elit nec lacinia varius. Vestibulum ac libero a mi rhoncus ornare.');
