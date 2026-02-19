@echo off
cd /d c:\xampp
start "MySQL Server" cmd /k c:\xampp\mysql\bin\mysqld --defaults-file=c:\xampp\mysql\bin\my.ini --console
