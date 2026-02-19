<?php
include 'db_connect.php';

$counselor_id = $_GET['counselor_id'] ?? null;

if (!$counselor_id) {
    echo json_encode(["status" => "error", "message" => "Counselor ID required"]);
    exit();
}

$response = [
    "status" => "success",
    "total_sessions" => 0,
    "total_patients" => 0,
    "total_earnings" => 0.0,
    "average_rating" => 0.0,
    "rating_count" => 0
];

// 1. Total Completed Sessions
$sql_sessions = "SELECT COUNT(*) as count FROM appointments WHERE counselor_id = ? AND status = 'Completed'";
$stmt = $conn->prepare($sql_sessions);
$stmt->bind_param("i", $counselor_id);
$stmt->execute();
$result = $stmt->get_result();
if ($row = $result->fetch_assoc()) {
    $response["total_sessions"] = $row['count'];
}
$stmt->close();

// 2. Total Unique Patients
$sql_patients = "SELECT COUNT(DISTINCT patient_id) as count FROM appointments WHERE counselor_id = ?";
$stmt = $conn->prepare($sql_patients);
$stmt->bind_param("i", $counselor_id);
$stmt->execute();
$result = $stmt->get_result();
if ($row = $result->fetch_assoc()) {
    $response["total_patients"] = $row['count'];
}
$stmt->close();

// 3. Total Earnings (from payments table, linked via appointments)
$sql_earnings = "SELECT SUM(p.amount) as total 
                 FROM payments p 
                 JOIN appointments a ON p.appointment_id = a.id 
                 WHERE a.counselor_id = ? AND p.status = 'Completed'";
$stmt = $conn->prepare($sql_earnings);
$stmt->bind_param("i", $counselor_id);
$stmt->execute();
$result = $stmt->get_result();
if ($row = $result->fetch_assoc()) {
    $response["total_earnings"] = (float)$row['total'];
}
$stmt->close();

// 4. Average Rating
$sql_rating = "SELECT AVG(f.rating) as avg_rating, COUNT(*) as count 
               FROM feedback f 
               JOIN appointments a ON f.appointment_id = a.id 
               WHERE a.counselor_id = ?";
$stmt = $conn->prepare($sql_rating);
$stmt->bind_param("i", $counselor_id);
$stmt->execute();
$result = $stmt->get_result();
if ($row = $result->fetch_assoc()) {
    $response["average_rating"] = $row['avg_rating'] ? round((float)$row['avg_rating'], 1) : 0.0;
    $response["rating_count"] = $row['count'];
}
$stmt->close();

echo json_encode($response);
$conn->close();
?>
