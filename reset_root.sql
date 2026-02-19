-- Reset root password to empty string
UPDATE mysql.user SET Password=PASSWORD('') WHERE User='root';
UPDATE mysql.user SET authentication_string=PASSWORD('') WHERE User='root';

-- Ensure root exists and has full privileges on localhost
CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION;

-- Ensure root exists for 127.0.0.1 (IPV4)
CREATE USER IF NOT EXISTS 'root'@'127.0.0.1' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'127.0.0.1' WITH GRANT OPTION;

-- Ensure root exists for ::1 (IPV6)
CREATE USER IF NOT EXISTS 'root'@'::1' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'::1' WITH GRANT OPTION;

FLUSH PRIVILEGES;
