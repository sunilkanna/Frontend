<?php
include_once 'db_connect.php';

$counselor_id = $_GET['counselor_id'] ?? null;

if (!$counselor_id) {
    echo json_encode(["status" => "error", "message" => "Counselor ID required"]);
    exit();
}

// Fetch appointments for this counselor, joining with patient details
$sql = "SELECT a.id, a.appointment_date, a.time_slot, a.status, a.meeting_link,
               u.full_name as patient_name, u.profile_image_url as patient_image,
               'Initial Consultation' as type, -- Placeholder logic for type
               'Routine Checkup' as reason,    -- Placeholder logic for reason
               (SELECT COUNT(*) FROM patient_reports pr WHERE pr.patient_id = a.patient_id) as report_count
        FROM appointments a 
        JOIN users u ON a.patient_id = u.id 
        WHERE a.counselor_id = ?
        ORDER BY a.appointment_date DESC, a.time_slot ASC";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $counselor_id);
$stmt->execute();
$result = $stmt->get_result();

$appointments = [];
while ($row = $result->fetch_assoc()) {
    // Add some mocked data for fields not yet in DB but needed by UI
    $row['image_initial'] = substr($row['patient_name'], 0, 1);
    // Random color for demo (in production, this should be consistent or client-side)
    $row['image_color_hex'] = '#FFCC80'; 
    $row['has_report'] = ($row['report_count'] > 0);
    
    unset($row['report_count']); // Remove internal counter from output
    $appointments[] = $row;
}

echo json_encode(["status" => "success", "appointments" => $appointments]);

$stmt->close();
$conn->close();
?>
