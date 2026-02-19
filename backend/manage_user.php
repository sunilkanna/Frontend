<?php
include 'db_connect.php';
include 'add_system_log.php';

$data = json_decode(file_get_contents("php://input"), true);
$user_id = $data['user_id'] ?? null;
$action = $data['action'] ?? null; // delete, suspend, activate

if (!$user_id || !$action) {
    echo json_encode(["status" => "error", "message" => "User ID and action required"]);
    exit();
}

if ($action == 'delete') {
    $stmt = $conn->prepare("DELETE FROM users WHERE id = ?");
    $stmt->bind_param("i", $user_id);
    if ($stmt->execute()) {
        add_system_log($conn, "User ID $user_id was deleted", 'WARNING', 'Admin');
        echo json_encode(["status" => "success", "message" => "User deleted"]);
    } else {
        echo json_encode(["status" => "error", "message" => $stmt->error]);
    }
} else {
    // Handle suspend/activate (would need a status column in users)
    echo json_encode(["status" => "error", "message" => "Action not implemented"]);
}

$conn->close();
?>
