<?php
// Helper to simulate request
function runScript($scriptName, $postData) {
    global $conn; // Ensure connection is available if needed, though scripts include db_connect.php
    
    // Save original State
    $originalPost = $_POST;
    
    // Set Mock Data
    $_POST = $postData;
    
    // Capture Output
    ob_start();
    include $scriptName;
    $output = ob_get_clean();
    
    // Restore State
    $_POST = $originalPost;
    
    return json_decode($output, true);
}

echo "--- STARTING COUNSELOR VERIFICATION FLOW TEST (DIRECT INCLUDE) ---\n";

include 'db_connect.php';

// 1. Create Test Counselor
$testEmail = "test_counselor_" . time() . "@example.com";
$testPass = "pass123";
$hashedPass = password_hash($testPass, PASSWORD_DEFAULT);
$stmt = $conn->prepare("INSERT INTO users (full_name, email, password_hash, user_type) VALUES ('Test Counselor', ?, ?, 'Counselor')");
$stmt->bind_param("ss", $testEmail, $hashedPass);
$stmt->execute();
$counselorId = $conn->insert_id;
$stmt->close();

echo "1. Created Test Counselor: ID $counselorId ($testEmail)\n";

// 2. Submit Qualifications
$qualData = [
    "user_id" => $counselorId,
    "registration_number" => "REG123",
    "medical_council" => "Test Council",
    "registration_year" => "2020",
    "certificate_url" => "http://example.com/cert.jpg"
];
// We need to simulate the JSON input for some scripts that read php://input
// But since our scripts check $_POST OR json_decode, we can just populate $_POST if the script supports it.
// Looking at save_counselor_qualifications.php: $user_id = $_POST['user_id'] ?? $data['user_id']
// So $_POST should work.

$resp = runScript('save_counselor_qualifications.php', $qualData);
echo "2. Submit Qualifications: " . ($resp['status'] ?? 'Error') . "\n";

// 3. Login - Should have status 'Pending'
// login.php reads php://input first, then falls back to $_POST.
// We need to make sure it picks up our data. Since we can't easily mock php://input without stream wrappers,
// we rely on the script checking $_POST as well.
$loginData = ["email" => $testEmail, "password" => $testPass];
$resp = runScript('login.php', $loginData);
$status = $resp['user']['verificationStatus'] ?? 'NULL';
echo "3. Login Check (Pre-Approval): Status is " . $status . " (Expected: Pending)\n";

// 4. Admin Approve
$adminId = 1; 
$approveData = [
    "admin_id" => $adminId,
    "counselor_id" => $counselorId,
    "status" => "Approved"
];
$resp = runScript('admin_verify_counselor.php', $approveData);
echo "4. Admin Approve: " . ($resp['status'] ?? 'Error') . "\n";

// 5. Login - Should have status 'Approved'
$resp = runScript('login.php', $loginData);
$status = $resp['user']['verificationStatus'] ?? 'NULL';
echo "5. Login Check (Post-Approval): Status is " . $status . " (Expected: Approved)\n";

echo "--- TEST COMPLETE ---\n";
?>
