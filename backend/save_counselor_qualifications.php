<?php
include 'db_connect.php';

// Handle JSON input
$data = json_decode(file_get_contents("php://input"), true);

$user_id = $_POST['user_id'] ?? $data['user_id'] ?? null;
$registration_number = $_POST['registration_number'] ?? $data['registration_number'] ?? null;
$medical_council = $_POST['medical_council'] ?? $data['medical_council'] ?? null;
$registration_year = $_POST['registration_year'] ?? $data['registration_year'] ?? null;
$certificate_url = $_POST['certificate_url'] ?? $data['certificate_url'] ?? null;

if (empty($user_id) || empty($registration_number) || empty($medical_council)) {
    echo json_encode(["status" => "error", "message" => "User ID, registration number, and council are required"]);
    exit();
}

$stmt = $conn->prepare("INSERT INTO counselor_qualifications (user_id, registration_number, medical_council, registration_year, certificate_url) VALUES (?, ?, ?, ?, ?)");
$stmt->bind_param("issss", $user_id, $registration_number, $medical_council, $registration_year, $certificate_url);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Qualification submitted for verification"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
