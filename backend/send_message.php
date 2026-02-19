<?php
include 'db_connect.php';

// Handle JSON input
$data = json_decode(file_get_contents("php://input"), true);

$sender_id = $_POST['sender_id'] ?? $data['sender_id'] ?? null;
$receiver_id = $_POST['receiver_id'] ?? $data['receiver_id'] ?? null;
$message_text = $_POST['message_text'] ?? $data['message_text'] ?? null;

if (empty($sender_id) || empty($receiver_id) || empty($message_text)) {
    echo json_encode(["status" => "error", "message" => "All fields are required"]);
    exit();
}

$stmt = $conn->prepare("INSERT INTO messages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)");
$stmt->bind_param("iis", $sender_id, $receiver_id, $message_text);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message_id" => $conn->insert_id]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
