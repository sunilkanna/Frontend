<?php
// Try connecting via localhost (socket/pipe) as skip-grant-tables disables networking
$conn = new mysqli("localhost", "root", "");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

echo "Connected successfully via 127.0.0.1\n";

// Restore localhost privileges
$sql = "GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION";
if ($conn->query($sql) === TRUE) {
    echo "Privileges granted successfully to 'root'@'localhost'\n";
} else {
    echo "Error granting privileges: " . $conn->error . "\n";
    // Try creating the user if it doesn't exist
    $sql_create = "CREATE USER 'root'@'localhost' IDENTIFIED BY ''";
    if ($conn->query($sql_create) === TRUE) {
        echo "User 'root'@'localhost' created successfully\n";
        $conn->query($sql); // Try grant again
    } else {
        echo "Error creating user: " . $conn->error . "\n";
    }
}

$conn->query("FLUSH PRIVILEGES");
echo "Privileges flushed.\n";

$conn->close();
?>
