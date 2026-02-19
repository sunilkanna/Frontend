<?php
include 'db_connect.php';

$result = $conn->query("SELECT id, full_name, email, user_type FROM users");

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo "ID: " . $row["id"]. " - Name: " . $row["full_name"]. " - Email: [" . $row["email"]. "] - Type: " . $row["user_type"]. "\n";
    }
} else {
    echo "0 results";
}
$conn->close();
?>
