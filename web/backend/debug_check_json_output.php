<?php
include 'db_connect.php';

$sql = "SELECT cq.*, u.full_name, u.email 
        FROM counselor_qualifications cq 
        JOIN users u ON cq.user_id = u.id"; 

$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    echo "First row data:\n";
    var_dump($row);
} else {
    echo "No records found.\n";
}

$conn->close();
?>
