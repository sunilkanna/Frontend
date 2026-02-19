<?php
include 'db_connect.php';

$sql = "SELECT u.id, u.full_name, cp.profile_image_url, cp.specialization, 
        COALESCE(cp.consultation_fee, 0) as consultation_fee,
        5.0 as rating -- Placeholder for rating as it's not in the schema yet
        FROM users u 
        LEFT JOIN counselor_profiles cp ON u.id = cp.user_id 
        WHERE u.user_type = 'Counselor'";

$result = $conn->query($sql);

$counselors = [];
if ($result && $result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        // Ensure numeric types are correct for JSON
        $row['id'] = (int)$row['id'];
        $row['rating'] = (float)$row['rating'];
        $counselors[] = $row;
    }
}

echo json_encode(["status" => "success", "counselors" => $counselors]);

$conn->close();
?>
