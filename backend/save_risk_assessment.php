<?php
include 'db_connect.php';

// Handle JSON input
$data = json_decode(file_get_contents("php://input"), true);

$patient_id = $_POST['patient_id'] ?? $data['patient_id'] ?? null;
$risk_score = $_POST['risk_score'] ?? $data['risk_score'] ?? 0;
$risk_category = $_POST['risk_category'] ?? $data['risk_category'] ?? 'Low';
$details = $_POST['details'] ?? $data['details'] ?? '{}'; // JSON string

if (empty($patient_id)) {
    echo json_encode(["status" => "error", "message" => "Patient ID is required"]);
    exit();
}

// If details is an array, convert to string
if (is_array($details)) {
    $details = json_encode($details);
}

$stmt = $conn->prepare("INSERT INTO risk_assessments (patient_id, risk_score, risk_category, details) VALUES (?, ?, ?, ?)");
$stmt->bind_param("iiss", $patient_id, $risk_score, $risk_category, $details);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Assessment saved"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
