<?php
include 'db_connect.php';

$counselor_id = $_GET['counselor_id'] ?? null;

if (!$counselor_id) {
    echo json_encode(["status" => "error", "message" => "Counselor ID required"]);
    exit();
}

$sql = "SELECT DISTINCT u.id, u.full_name as name, u.email,
        TIMESTAMPDIFF(YEAR, pp.date_of_birth, CURDATE()) AS age,
        pp.gender,
        (SELECT condition_name FROM medical_history WHERE user_id = u.id LIMIT 1) as condition_name,
        (SELECT appointment_date FROM appointments WHERE patient_id = u.id AND status = 'Completed' ORDER BY appointment_date DESC LIMIT 1) as last_visit
        FROM users u
        JOIN appointments a ON u.id = a.patient_id
        LEFT JOIN patient_profiles pp ON u.id = pp.user_id
        WHERE a.counselor_id = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $counselor_id);
$stmt->execute();
$result = $stmt->get_result();

$patients = [];
while ($row = $result->fetch_assoc()) {
    $row['status'] = 'Active'; // Logic to determine status could be added here
    $row['date'] = $row['last_visit'] ? date("M d, Y", strtotime($row['last_visit'])) : "N/A";
    $patients[] = $row;
}

echo json_encode(["status" => "success", "patients" => $patients]);

$stmt->close();
$conn->close();
?>
