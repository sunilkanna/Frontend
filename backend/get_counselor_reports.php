<?php
include 'db_connect.php';

$counselor_id = $_GET['counselor_id'] ?? null;

if (!$counselor_id) {
    echo json_encode(["status" => "error", "message" => "Counselor ID required"]);
    exit();
}

$stmt = $conn->prepare("SELECT * FROM counselor_reports WHERE counselor_id = ? ORDER BY report_date DESC");
$stmt->bind_param("i", $counselor_id);
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
