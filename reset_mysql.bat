@echo off
echo Stopping MySQL Service...
net stop mysql
taskkill /F /IM mysqld.exe

echo Starting MySQL in recovery mode...
start "" "C:\xampp\mysql\bin\mysqld.exe" --skip-grant-tables --skip-networking

echo Waiting for database to start...
timeout /t 5 /nobreak

echo Resetting Root Privileges...
"C:\xampp\mysql\bin\mysql.exe" -u root -e "FLUSH PRIVILEGES; CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY ''; GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION; FLUSH PRIVILEGES;"

echo Stopping recovery mode...
taskkill /F /IM mysqld.exe

echo Restarting MySQL Service...
net start mysql

echo Done! Please check phpMyAdmin now.
