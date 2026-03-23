<?php
include 'db_connect.php';
require_once 'mailer_utils.php';

$data = json_decode(file_get_contents("php://input"), true);
$email = $_POST['email'] ?? $data['email'] ?? null;

if (empty($email)) {
    echo json_encode(["status" => "error", "message" => "Email is required"]);
    exit();
}

// Check if user exists
$stmt = $conn->prepare("SELECT id, full_name FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    echo json_encode(["status" => "error", "message" => "No account found with this email"]);
    $stmt->close();
    $conn->close();
    exit();
}

$user = $result->fetch_assoc();
$user_name = $user['full_name'];
$stmt->close();

// Generate 5-digit OTP
$otp = str_pad(rand(0, 99999), 5, '0', STR_PAD_LEFT);

// Store new OTP
$conn->query("CREATE TABLE IF NOT EXISTS password_resets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    otp VARCHAR(10) NOT NULL,
    expires_at DATETIME NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)");

$del_stmt = $conn->prepare("DELETE FROM password_resets WHERE email = ?");
$del_stmt->bind_param("s", $email);
$del_stmt->execute();
$del_stmt->close();

$ins_stmt = $conn->prepare("INSERT INTO password_resets (email, otp, expires_at) VALUES (?, ?, NOW() + INTERVAL 10 MINUTE)");
$ins_stmt->bind_param("ss", $email, $otp);

if (!$ins_stmt->execute()) {
    echo json_encode(["status" => "error", "message" => "Failed to generate OTP"]);
    $ins_stmt->close();
    $conn->close();
    exit();
}
$ins_stmt->close();

// Send OTP via email using our utility
if (sendOtpEmail($email, $user_name, $otp)) {
    echo json_encode([
        "status" => "success",
        "message" => "Verification code sent to your email"
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Failed to send email. Please check SMTP configuration."
    ]);
}

$conn->close();
?>
