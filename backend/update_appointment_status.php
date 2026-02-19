<?php
include 'db_connect.php';

$data = json_decode(file_get_contents("php://input"), true);
$appointment_id = $_POST['appointment_id'] ?? $data['appointment_id'] ?? null;
$status = $_POST['status'] ?? $data['status'] ?? null; // 'Confirmed', 'Completed', 'Cancelled'

if (!$appointment_id || !$status) {
    echo json_encode(["status" => "error", "message" => "Appointment ID and status required"]);
    exit();
}

$stmt = $conn->prepare("UPDATE appointments SET status = ? WHERE id = ?");
$stmt->bind_param("si", $status, $appointment_id);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Appointment updated to $status"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
