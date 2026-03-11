<?php
include 'db_connect.php';

$patient_id = $_GET['patient_id'];

$sql = "SELECT a.id, a.appointment_date, a.time_slot, a.status, u.full_name as counselor_name 
        FROM appointments a 
        JOIN users u ON a.counselor_id = u.id 
        WHERE a.patient_id = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $patient_id);
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
