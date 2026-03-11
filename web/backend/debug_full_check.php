<?php
include 'db_connect.php';

echo "--- DEBUG: DATA INTEGRITY CHECK ---\n";

// 1. Check Counselors
echo "\n1. COUNSELORS:\n";
$res = $conn->query("SELECT id, full_name, email FROM users WHERE user_type = 'Counselor'");
while ($row = $res->fetch_assoc()) {
    echo "ID: " . $row['id'] . " - " . $row['full_name'] . "\n";
}

// 2. Check Patients
echo "\n2. PATIENTS:\n";
$res = $conn->query("SELECT id, full_name, email FROM users WHERE user_type = 'Patient'");
while ($row = $res->fetch_assoc()) {
    echo "ID: " . $row['id'] . " - " . $row['full_name'] . "\n";
}

// 3. Check Appointments and JOIN validity
echo "\n3. APPOINTMENTS (Raw & Joined):\n";
$sql = "SELECT a.id, a.patient_id, a.counselor_id, a.status, 
               u.full_name as patient_name_joined
        FROM appointments a
        LEFT JOIN users u ON a.patient_id = u.id";
$res = $conn->query($sql);
while ($row = $res->fetch_assoc()) {
    $joinStatus = $row['patient_name_joined'] ? "OK ({$row['patient_name_joined']})" : "BROKEN JOIN (Patient ID {$row['patient_id']} not found)";
    echo "Appt ID: " . $row['id'] . 
         " | CID: " . $row['counselor_id'] . 
         " | PID: " . $row['patient_id'] . 
         " | Status: " . $row['status'] . 
         " | Join: " . $joinStatus . "\n";
}

$conn->close();
?>
