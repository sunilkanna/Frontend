<?php
include 'db_connect.php';

$data = json_decode(file_get_contents("php://input"), true);
$appointment_id = $data['appointment_id'] ?? null;

if (!$appointment_id) {
    echo json_encode(["status" => "error", "message" => "Appointment ID required"]);
    exit();
}

$stmt = $conn->prepare("UPDATE appointments SET status = 'Completed' WHERE id = ?");
$stmt->bind_param("i", $appointment_id);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Appointment completed"]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to update status"]);
}

$stmt->close();
$conn->close();
?>
