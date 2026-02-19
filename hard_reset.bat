@echo off
echo Stopping MySQL Service...
net stop mysql
taskkill /F /IM mysqld.exe

echo Starting MySQL with skip-grant-tables (No Network)...
start "" "C:\xampp\mysql\bin\mysqld.exe" --skip-grant-tables --skip-networking --shared-memory --console

echo Waiting 10 seconds for startup...
timeout /t 10 /nobreak

echo Resetting Root Privileges...
"C:\xampp\mysql\bin\mysql.exe" -u root -e "FLUSH PRIVILEGES; DROP USER IF EXISTS 'root'@'localhost'; CREATE USER 'root'@'localhost' IDENTIFIED BY ''; GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION; CREATE USER IF NOT EXISTS 'root'@'127.0.0.1' IDENTIFIED BY ''; GRANT ALL PRIVILEGES ON *.* TO 'root'@'127.0.0.1' WITH GRANT OPTION; CREATE USER IF NOT EXISTS 'root'@'::1' IDENTIFIED BY ''; GRANT ALL PRIVILEGES ON *.* TO 'root'@'::1' WITH GRANT OPTION; FLUSH PRIVILEGES;"

echo Killing Recovery Mode...
taskkill /F /IM mysqld.exe

echo Restarting MySQL Service Normally...
net start mysql

echo Done!
