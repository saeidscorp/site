CREATE TABLE post_tag (
  tag_id REFERENCES tag (id),
  post_id REFERENCES post (id)
);