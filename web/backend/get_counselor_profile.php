<?php
include 'db_connect.php';

$user_id = $_GET['user_id'] ?? null;

if (!$user_id) {
    echo json_encode(["status" => "error", "message" => "User ID required"]);
    exit();
}


$sql = "SELECT cp.*, u.full_name, u.email, cq.status, cq.rejection_reason 
        FROM counselor_profiles cp 
        JOIN users u ON cp.user_id = u.id 
        LEFT JOIN counselor_qualifications cq ON cp.user_id = cq.user_id
        WHERE cp.user_id = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $profile = $result->fetch_assoc();
    echo json_encode(["status" => "success", "profile" => $profile]);
} else {
    echo json_encode(["status" => "error", "message" => "Profile not found"]);
}

$stmt->close();
$conn->close();
?>
