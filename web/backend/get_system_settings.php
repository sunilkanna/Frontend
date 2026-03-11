<?php
include 'db_connect.php';

$result = $conn->query("SELECT setting_key, setting_value FROM system_settings");
$settings = [];

while ($row = $result->fetch_assoc()) {
    $settings[$row['setting_key']] = $row['setting_value'];
}

echo json_encode(["status" => "success", "settings" => $settings]);

$conn->close();
?>
