<?php
include 'db_connect.php';

$sql = "SELECT * FROM system_logs ORDER BY timestamp DESC LIMIT 50";
$result = $conn->query($sql);
$logs = [];
while($row = $result->fetch_assoc()) {
    $logs[] = $row;
}

echo json_encode(["status" => "success", "logs" => $logs]);
$conn->close();
?>
