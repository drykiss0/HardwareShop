CREATE TABLE HARDWARE_CATEGORY (
  id	INTEGER IDENTITY,
  name 	VARCHAR(30) UNIQUE,
  item_count  INTEGER DEFAULT 0
);