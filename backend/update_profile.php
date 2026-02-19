<?php
include 'db_connect.php';

// Handle JSON input
$data = json_decode(file_get_contents("php://input"), true);

$user_id = $_POST['user_id'] ?? $data['user_id'] ?? null;
$full_name = $_POST['full_name'] ?? $data['full_name'] ?? null;
$dob = $_POST['date_of_birth'] ?? $data['date_of_birth'] ?? null;
$gender = $_POST['gender'] ?? $data['gender'] ?? null;
$phone = $_POST['phone'] ?? $data['phone'] ?? null;
$address = $_POST['address'] ?? $data['address'] ?? null;

if (empty($user_id)) {
    echo json_encode(["status" => "error", "message" => "User ID is required"]);
    exit();
}

// Start transaction to update both tables
$conn->begin_transaction();

try {
    // 1. Update Full Name in 'users' table if provided
    if ($full_name) {
        $stmt1 = $conn->prepare("UPDATE users SET full_name = ? WHERE id = ?");
        $stmt1->bind_param("si", $full_name, $user_id);
        $stmt1->execute();
        $stmt1->close();
    }

    // 2. Insert/Update fields in 'patient_profiles' table
    $stmt2 = $conn->prepare("INSERT INTO patient_profiles (user_id, date_of_birth, gender, phone, address) 
                            VALUES (?, ?, ?, ?, ?)
                            ON DUPLICATE KEY UPDATE date_of_birth=?, gender=?, phone=?, address=?");
    $stmt2->bind_param("issssssss", $user_id, $dob, $gender, $phone, $address, $dob, $gender, $phone, $address);
    $stmt2->execute();
    $stmt2->close();

    $conn->commit();
    echo json_encode(["status" => "success", "message" => "Profile updated successfully"]);
} catch (Exception $e) {
    $conn->rollback();
    echo json_encode(["status" => "error", "message" => "Error updating profile: " . $e->getMessage()]);
}

$conn->close();
?>
