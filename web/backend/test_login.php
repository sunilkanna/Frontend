<?php
// Simulate a login request
$_POST['email'] = 'admin@genecare.com';
$_POST['password'] = 'admin123';

// Determine the script's directory and include login.php from there
$currentDir = __DIR__;
chdir($currentDir);

// Instead of including login.php directly (which outputs JSON and exits), 
// we'll use a slightly different approach to test the logic or just use curl/http request if possible.
// Because login.php acts as an endpoint, including it directly might cause issues with output buffering if not handled.
// However, since this is a CLI test, capturing the output is fine.

ob_start();
include 'login.php';
$output = ob_get_clean();

echo "Login Response:\n" . $output . "\n";

$data = json_decode($output, true);

if ($data && isset($data['status']) && $data['status'] === 'success') {
    echo "TEST PASSED: Login successful.\n";
    if ($data['user']['user_type'] === 'Admin') {
        echo "TEST PASSED: User type is Admin.\n";
    } else {
        echo "TEST FAILED: User type is " . $data['user']['user_type'] . " (Expected: Admin)\n";
    }
} else {
    echo "TEST FAILED: Login failed.\n";
    echo "Message: " . ($data['message'] ?? 'Unknown error') . "\n";
}
?>
