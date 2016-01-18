CREATE TABLE post_tag (
  tag_id REFERENCES tag (id),
  post_id REFERENCES post (id)
);


INSERT INTO post_tag (post_id, tag_id) VALUES (1, 1);
INSERT INTO post_tag (post_id, tag_id) VALUES (1, 2);
