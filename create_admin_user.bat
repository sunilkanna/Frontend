@echo off
echo Stopping MySQL...
net stop mysql
taskkill /F /IM mysqld.exe

echo Starting MySQL in Recovery Mode...
start "" "C:\xampp\mysql\bin\mysqld.exe" --skip-grant-tables --skip-networking --shared-memory --console

echo Waiting for startup...
timeout /t 10 /nobreak

echo Creating 'admin' User...
"C:\xampp\mysql\bin\mysql.exe" -u root -e "FLUSH PRIVILEGES; CREATE USER 'admin'@'localhost' IDENTIFIED BY 'password'; GRANT ALL PRIVILEGES ON *.* TO 'admin'@'localhost' WITH GRANT OPTION; CREATE USER 'admin'@'127.0.0.1' IDENTIFIED BY 'password'; GRANT ALL PRIVILEGES ON *.* TO 'admin'@'127.0.0.1' WITH GRANT OPTION; CREATE USER 'admin'@'%%' IDENTIFIED BY 'password'; GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%%' WITH GRANT OPTION; FLUSH PRIVILEGES;"

echo Restarting MySQL...
taskkill /F /IM mysqld.exe
net start mysql

echo Done! New user 'admin' created.
