CREATE USER 'client'@'localhost' IDENTIFIED BY 'P6au23q7';
GRANT SELECT, UPDATE, DELETE, INSERT ON ease.* TO 'client'@'localhost';
GRANT SELECT, UPDATE, DELETE, INSERT ON easeLogs.* TO 'client'@'localhost';
FLUSH PRIVILEGES;

