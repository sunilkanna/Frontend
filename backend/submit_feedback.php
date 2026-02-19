<?php
include 'db_connect.php';

$appointment_id = $_POST['appointment_id'];
$patient_id = $_POST['patient_id'];
$rating = $_POST['rating'];
$comments = $_POST['comments'];

$stmt = $conn->prepare("INSERT INTO feedback (appointment_id, patient_id, rating, comments) VALUES (?, ?, ?, ?)");
$stmt->bind_param("iiis", $appointment_id, $patient_id, $rating, $comments);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Feedback submitted"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
