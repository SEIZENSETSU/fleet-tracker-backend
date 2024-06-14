-- Create Comment table
CREATE TABLE "Comment"
(
    comment_id   SERIAL PRIMARY KEY,
    uid          VARCHAR REFERENCES "User" (uid),
    warehouse_id INT REFERENCES "Warehouse" (warehouse_id),
    contens      VARCHAR,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP
);
