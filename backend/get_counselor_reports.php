<?php
include 'db_connect.php';

$counselor_id = $_GET['counselor_id'] ?? null;

if (!$counselor_id) {
    echo json_encode(["status" => "error", "message" => "Counselor ID required"]);
    exit();
}

// Fetch reports from two sources:
// 1. Central patient_reports (reports uploaded by patients who have an appointment with this counselor)
// 2. counselor_reports (reports manually created/assigned by the counselor)
$query = "
    (SELECT 
        pr.id, 
        pr.file_name as title, 
        'Patient Upload' as category, 
        DATE(pr.uploaded_at) as date, 
        COALESCE(u.full_name, 'Unknown Patient') as patientName, 
        pr.patient_id as patientId, 
        pr.file_url as fileUrl
    FROM patient_reports pr
    LEFT JOIN users u ON pr.patient_id = u.id
    WHERE pr.patient_id IN (
        SELECT DISTINCT patient_id 
        FROM appointments 
        WHERE counselor_id = ?
    ))
    UNION ALL
    (SELECT 
        cr.id, 
        cr.title, 
        cr.category, 
        cr.report_date as date, 
        'System' as patientName, 
        0 as patientId, 
        cr.file_url as fileUrl
    FROM counselor_reports cr
    WHERE cr.counselor_id = ?)
    ORDER BY date DESC";

$stmt = $conn->prepare($query);
$stmt->bind_param("ii", $counselor_id, $counselor_id);
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
