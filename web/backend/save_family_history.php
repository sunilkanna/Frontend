<?php
include 'db_connect.php';

// Handle JSON input
$data = json_decode(file_get_contents("php://input"), true);

$user_id = $_POST['user_id'] ?? $data['user_id'] ?? null;
$relation = $_POST['relation'] ?? $data['relation'] ?? null;
$condition = $_POST['condition'] ?? $data['condition'] ?? null;
$description = $_POST['description'] ?? $data['description'] ?? null;

if (empty($user_id)) {
    echo json_encode(["status" => "error", "message" => "User ID is required"]);
    exit();
}

$stmt = $conn->prepare("INSERT INTO family_history (user_id, relation, condition_name, description) VALUES (?, ?, ?, ?)");
$stmt->bind_param("isss", $user_id, $relation, $condition, $description);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Family history saved"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
