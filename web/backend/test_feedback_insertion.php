<?php
include 'db_connect.php';

echo "<h2>Finding valid test data...</h2>";

$query = "SELECT id, patient_id FROM appointments LIMIT 1";
$result = $conn->query($query);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $appointment_id = $row['id'];
    $patient_id = $row['patient_id'];
    
    echo "<p>Found Appointment ID: $appointment_id, Patient ID: $patient_id</p>";
    
    $rating = 5;
    $comments = "Test feedback from verification script with valid IDs";

    $stmt = $conn->prepare("INSERT INTO feedback (appointment_id, patient_id, rating, comments) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("iiis", $appointment_id, $patient_id, $rating, $comments);

    if ($stmt->execute()) {
        echo "<p style='color: green;'>Test feedback inserted successfully!</p>";
    } else {
        echo "<p style='color: red;'>Failed to insert test feedback: " . $stmt->error . "</p>";
    }
    $stmt->close();
} else {
    echo "<p style='color: orange;'>No appointments found in database. Cannot run insertion test.</p>";
}

$conn->close();
?>
