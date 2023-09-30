SHOW DATABASES;

CREATE DATABASE rest_template_db;

USE rest_template_db;

SHOW TABLES;

DESC table_name;

SHOW CREATE TABLE m_posts;

SELECT * FROM m_posts;
ALTER TABLE m_posts MODIFY COLUMN body LONGTEXT;

DELETE FROM m_posts;

DROP TABLE m_posts;