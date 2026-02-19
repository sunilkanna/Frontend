<?php
include 'db_connect.php';

// Total Users
$total_users = $conn->query("SELECT COUNT(*) as count FROM users")->fetch_assoc()['count'];

// Active Sessions (Dummy or count distinct users in appointments/logs recently)
$active_sessions = 120; // Simulated for now

// User Growth (Dummy data for sparkline)
$growth_data = [10, 25, 18, 32, 45, 40, 55, 70];

// Session Distribution (Dummy)
$distribution_data = [45, 60, 55, 70, 85, 50, 65];
$distribution_labels = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];

// Demographics (Patient, Counselor, Admin)
$demo_sql = "SELECT user_type, COUNT(*) as count FROM users GROUP BY user_type";
$res = $conn->query($demo_sql);
$demo_data = [];
while($row = $res->fetch_assoc()) {
    $demo_data[$row['user_type']] = (int)$row['count'];
}

echo json_encode([
    "status" => "success",
    "total_users" => (string)$total_users,
    "active_sessions" => (string)$active_sessions,
    "user_growth_data" => $growth_data,
    "session_distribution_data" => $distribution_data,
    "session_distribution_labels" => $distribution_labels,
    "user_demographics_data" => array_values($demo_data)
]);

$conn->close();
?>
