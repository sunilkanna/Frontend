<?php
include 'db_connect.php';

echo "--- DEBUG: COUNSELOR REQUESTS ---\n";

// 1. List Counselors
echo "\n1. LIST OF COUNSELORS:\n";
$sql = "SELECT id, full_name, email FROM users WHERE user_type = 'Counselor'";
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo "ID: " . $row["id"] . " - Name: " . $row["full_name"] . " - Email: " . $row["email"] . "\n";
    }
} else {
    echo "No counselors found.\n";
}

// 2. List Pending Appointments
echo "\n2. PENDING APPOINTMENTS:\n";
$sql = "SELECT id, counselor_id, patient_id, appointment_date, time_slot, status FROM appointments WHERE status = 'Pending'";
$result = $conn->query($sql);
if ($result && $result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo "Appt ID: " . $row["id"] . " - Counselor ID: " . $row["counselor_id"] . " - Patient ID: " . $row["patient_id"] . " - Date: " . $row["appointment_date"] . " - Status: " . $row["status"] . "\n";
    }
} else {
    echo "No pending appointments found.\n";
}

// 3. Check for appointments for a specific counselor (e.g., ID 1 or any found above)
// We'll iterate through all counselors found
$result = $conn->query("SELECT id FROM users WHERE user_type = 'Counselor'");
while ($counselor = $result->fetch_assoc()) {
    $cid = $counselor['id'];
    echo "\n3. Checking for Counselor ID $cid:\n";
    
    $stmt = $conn->prepare("SELECT * FROM appointments WHERE counselor_id = ? AND status = 'Pending'");
    $stmt->bind_param("i", $cid);
    $stmt->execute();
    $res = $stmt->get_result();
    
    if ($res->num_rows > 0) {
        echo "  -> Found " . $res->num_rows . " pending request(s).\n";
    } else {
        echo "  -> No pending requests.\n";
    }
}

$conn->close();
?>
