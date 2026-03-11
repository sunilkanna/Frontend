<?php
include 'db_connect.php';

$patient_id = $_GET['patient_id'] ?? null;

if (!$patient_id) {
    echo json_encode(["status" => "error", "message" => "Patient ID required"]);
    exit();
}

$stmt = $conn->prepare("SELECT * FROM patient_reports WHERE patient_id = ? ORDER BY uploaded_at DESC");
$stmt->bind_param("i", $patient_id);
$stmt->execute();
$result = $stmt->get_result();

$reports = [];
while ($row = $result->fetch_assoc()) {
    $reports[] = $row;
}

echo json_encode(["status" => "success", "reports" => $reports]);

$stmt->close();
$conn->close();
?>
