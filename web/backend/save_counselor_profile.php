<?php
include 'db_connect.php';

// Handle JSON input
$data = json_decode(file_get_contents("php://input"), true);

$user_id = $_POST['user_id'] ?? $data['user_id'] ?? null;
$specialization = $_POST['specialization'] ?? $data['specialization'] ?? null;
$bio = $_POST['bio'] ?? $data['bio'] ?? null;
$experience_years = $_POST['experience_years'] ?? $data['experience_years'] ?? null;
$consultation_fee = $_POST['consultation_fee'] ?? $data['consultation_fee'] ?? null;
$profile_image_url = $_POST['profile_image_url'] ?? $data['profile_image_url'] ?? null;

if (empty($user_id)) {
    echo json_encode(["status" => "error", "message" => "User ID is required"]);
    exit();
}

// Use REPLACE or ON DUPLICATE KEY UPDATE for profiles
$stmt = $conn->prepare("INSERT INTO counselor_profiles (user_id, specialization, bio, experience_years, consultation_fee, profile_image_url) 
                        VALUES (?, ?, ?, ?, ?, ?) 
                        ON DUPLICATE KEY UPDATE 
                        specialization = VALUES(specialization), 
                        bio = VALUES(bio), 
                        experience_years = VALUES(experience_years), 
                        consultation_fee = VALUES(consultation_fee), 
                        profile_image_url = VALUES(profile_image_url)");

$stmt->bind_param("issids", $user_id, $specialization, $bio, $experience_years, $consultation_fee, $profile_image_url);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Counselor profile saved successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

$stmt->close();
$conn->close();
?>
