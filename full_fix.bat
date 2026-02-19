@echo off
echo Stopping MySQL Service...
net stop mysql
taskkill /F /IM mysqld.exe

echo Copying reset script...
copy /Y "c:\Users\kanna sunil\AndroidStudioProjects\Genecare\reset_root.sql" "C:\xampp\mysql\reset_root.sql"

echo Starting MySQL with Init File to Reset Privileges...
start "" "C:\xampp\mysql\bin\mysqld.exe" --defaults-file="C:\xampp\mysql\bin\my.ini" --init-file="C:\xampp\mysql\reset_root.sql"

echo Waiting for 15 seconds for privileges to flush...
timeout /t 15 /nobreak

echo Killing MySQL...
taskkill /F /IM mysqld.exe

echo Deleting temporary reset script...
del "C:\xampp\mysql\reset_root.sql"

echo Restarting MySQL Service Normally...
net start mysql

echo Done! Please try phpMyAdmin now.
