<?php
include 'db_connect.php';

$user_id = $_GET['user_id'] ?? null;

if (!$user_id) {
    echo json_encode(["status" => "error", "message" => "User ID required"]);
    exit();
}

$stmt = $conn->prepare("SELECT * FROM medical_history WHERE user_id = ? ORDER BY diagnosis_date DESC");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$history = [];
while ($row = $result->fetch_assoc()) {
    $history[] = $row;
}

echo json_encode(["status" => "success", "medical_history" => $history]);

$stmt->close();
$conn->close();
?>
