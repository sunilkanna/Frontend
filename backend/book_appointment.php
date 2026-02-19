<?php
include 'db_connect.php';

// Handle JSON input
$data = json_decode(file_get_contents("php://input"), true);

$patient_id = $_POST['patient_id'] ?? $data['patient_id'] ?? null;
$counselor_id = $_POST['counselor_id'] ?? $data['counselor_id'] ?? null;
$date = $_POST['date'] ?? $data['date'] ?? null; // Format: YYYY-MM-DD
$time = $_POST['time'] ?? $data['time'] ?? null;

if (empty($patient_id) || empty($counselor_id) || empty($date) || empty($time)) {
    echo json_encode(["status" => "error", "message" => "All fields are required"]);
    exit();
}

// Check availability
$check = $conn->prepare("SELECT id FROM appointments WHERE counselor_id = ? AND appointment_date = ? AND time_slot = ? AND status != 'Cancelled'");
$check->bind_param("iss", $counselor_id, $date, $time);
$check->execute();
$check->store_result();

if ($check->num_rows > 0) {
    echo json_encode(["status" => "error", "message" => "Slot already booked"]);
    exit();
}
$check->close();

$stmt = $conn->prepare("INSERT INTO appointments (patient_id, counselor_id, appointment_date, time_slot) VALUES (?, ?, ?, ?)");
$stmt->bind_param("iiss", $patient_id, $counselor_id, $date, $time);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "appointment_id" => $stmt->insert_id, "message" => "Appointment booked successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
