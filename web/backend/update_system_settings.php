<?php
include 'db_connect.php';

$data = json_decode(file_get_contents("php://input"), true);
$key = $data['setting_key'] ?? null;
$value = $data['setting_value'] ?? null;

if (!$key || $value === null) {
    echo json_encode(["status" => "error", "message" => "Setting key and value are required"]);
    exit();
}

$stmt = $conn->prepare("UPDATE system_settings SET setting_value = ? WHERE setting_key = ?");
$stmt->bind_param("ss", $value, $key);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Setting updated successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Database error: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>
