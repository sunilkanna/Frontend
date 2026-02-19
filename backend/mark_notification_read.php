<?php
include 'db_connect.php';

$data = json_decode(file_get_contents("php://input"), true);
$notification_id = $_POST['notification_id'] ?? $data['notification_id'] ?? null;

if (!$notification_id) {
    echo json_encode(["status" => "error", "message" => "Notification ID required"]);
    exit();
}

$stmt = $conn->prepare("UPDATE notifications SET is_read = TRUE WHERE id = ?");
$stmt->bind_param("i", $notification_id);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Notification marked as read"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
