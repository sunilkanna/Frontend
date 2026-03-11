<?php
include 'db_connect.php';

$counselor_id = $_GET['counselor_id'] ?? null;
$date = $_GET['date'] ?? date('Y-m-d');

if (!$counselor_id) {
    echo json_encode(["status" => "error", "message" => "Counselor ID required"]);
    exit();
}

// Fetch appointments including patient details
$sql = "SELECT a.*, u.full_name as patient_name, u.email 
        FROM appointments a 
        JOIN users u ON a.patient_id = u.id 
        WHERE a.counselor_id = ? AND a.appointment_date = ? AND a.status != 'Cancelled'
        ORDER BY a.time_slot ASC";

$stmt = $conn->prepare($sql);
$stmt->bind_param("is", $counselor_id, $date);
$stmt->execute();
$result = $stmt->get_result();

$appointments = [];
while ($row = $result->fetch_assoc()) {
    $appointments[] = $row;
}

echo json_encode(["status" => "success", "appointments" => $appointments]);

$stmt->close();
$conn->close();
?>
