<?php
include 'db_connect.php';

$patient_id = $_GET['patient_id'] ?? null;

if (!$patient_id) {
    echo json_encode(["status" => "error", "message" => "Patient ID required"]);
    exit();
}

// 1. Fetch Basic Profile Info
$profile_sql = "SELECT u.id, u.full_name as name, u.email,
                pp.date_of_birth, pp.gender, pp.phone, pp.address,
                pp.height, pp.weight, pp.blood_type,
                TIMESTAMPDIFF(YEAR, pp.date_of_birth, CURDATE()) AS age
                FROM users u
                LEFT JOIN patient_profiles pp ON u.id = pp.user_id
                WHERE u.id = ? AND LOWER(u.user_type) = 'patient'";

$stmt = $conn->prepare($profile_sql);
$stmt->bind_param("i", $patient_id);
$stmt->execute();
$profile_res = $stmt->get_result();
$profile = $profile_res->fetch_assoc();
$stmt->close();

if (!$profile) {
    echo json_encode(["status" => "error", "message" => "Patient not found"]);
    exit();
}

// 2. Fetch Medical History
$history_sql = "SELECT id, condition_name, diagnosis_date, medications, allergies, surgeries 
                FROM medical_history 
                WHERE user_id = ? 
                ORDER BY diagnosis_date DESC";
$stmt = $conn->prepare($history_sql);
$stmt->bind_param("i", $patient_id);
$stmt->execute();
$history_res = $stmt->get_result();
$medical_history = [];
while ($row = $history_res->fetch_assoc()) {
    $medical_history[] = [
        "id" => $row['id'],
        "condition_name" => $row['condition_name'] ?? 'Unknown',
        "diagnosis_date" => $row['diagnosis_date'] ?? '',
        "medications" => $row['medications'] ?? '',
        "allergies" => $row['allergies'] ?? '',
        "surgeries" => $row['surgeries'] ?? ''
    ];
}
$stmt->close();

// 3. Fetch Genetic Risks
$risk_sql = "SELECT risk_category, details, assessed_at 
             FROM risk_assessments 
             WHERE patient_id = ? 
             ORDER BY assessed_at DESC LIMIT 5";
$stmt = $conn->prepare($risk_sql);
$stmt->bind_param("i", $patient_id);
$stmt->execute();
$risk_res = $stmt->get_result();
$genetic_risks = [];
while ($row = $risk_res->fetch_assoc()) {
    $genetic_risks[] = [
        "category" => $row['risk_category'] ?? 'General',
        "assessed_at" => $row['assessed_at'] ?? ''
    ];
}
$stmt->close();

echo json_encode([
    "status" => "success",
    "patient" => [
        "id" => $profile['id'],
        "name" => $profile['name'],
        "email" => $profile['email'],
        "age" => $profile['age'] ?? 0,
        "gender" => $profile['gender'] ?? 'Unknown',
        "phone" => $profile['phone'] ?? '',
        "address" => $profile['address'] ?? '',
        "date_of_birth" => $profile['date_of_birth'] ?? '',
        "height" => $profile['height'] ?? 'N/A',
        "weight" => $profile['weight'] ?? 'N/A',
        "blood_type" => $profile['blood_type'] ?? 'N/A',
        "medical_history" => $medical_history,
        "genetic_risks" => $genetic_risks
    ]
]);

$conn->close();
?>
