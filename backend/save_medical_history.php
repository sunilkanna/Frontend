<?php
include 'db_connect.php';

// Handle JSON input
$data = json_decode(file_get_contents("php://input"), true);

$user_id = $_POST['user_id'] ?? $data['user_id'] ?? null;
$condition = $_POST['condition'] ?? $data['condition'] ?? null;
$medications = $_POST['medications'] ?? $data['medications'] ?? null;
$allergies = $_POST['allergies'] ?? $data['allergies'] ?? null;
$surgeries = $_POST['surgeries'] ?? $data['surgeries'] ?? null;
$diagnosis_date = $_POST['diagnosis_date'] ?? $data['diagnosis_date'] ?? date('Y-m-d');

if (empty($user_id)) {
    echo json_encode(["status" => "error", "message" => "User ID is required"]);
    exit();
}

$stmt = $conn->prepare("INSERT INTO medical_history (user_id, condition_name, diagnosis_date, medications, allergies, surgeries) VALUES (?, ?, ?, ?, ?, ?)");
$stmt->bind_param("isssss", $user_id, $condition, $diagnosis_date, $medications, $allergies, $surgeries);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Medical history saved"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
