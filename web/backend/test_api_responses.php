<?php
// Helper to capture script output
function test_endpoint($script, $getParams = [], $postParams = []) {
    echo "\n--- TESTING: $script ---\n";
    
    // Reset global arrays
    $_GET = $getParams;
    $_POST = $postParams;
    
    // Capture output
    ob_start();
    include $script;
    $output = ob_get_clean();
    
    echo "Output: " . substr($output, 0, 500) . (strlen($output) > 500 ? "..." : "") . "\n";
    
    $json = json_decode($output, true);
    if (json_last_error() === JSON_ERROR_NONE) {
        echo "STATUS: Valid JSON\n";
        if (isset($json['status']) && $json['status'] == 'success') {
            echo "RESULT: SUCCESS\n";
            if (isset($json['appointments'])) {
                echo "COUNT: " . count($json['appointments']) . " appointments found.\n";
            }
            if (isset($json['counselor_stats'])) {
                echo "STATS: Found counselor stats.\n";
            }
        } else {
            echo "RESULT: FAILED/ERROR (Message: " . ($json['message'] ?? 'None') . ")\n";
        }
    } else {
        echo "STATUS: Invalid JSON\n";
    }
}

// 1. Test get_counselor_appointments.php
// We saw Counselor ID 52 had appointments in debug_full_check.php
test_endpoint('get_counselor_appointments.php', ['counselor_id' => 52]);

// 2. Test get_dashboard_stats.php
// Need to mock input stream or just use POST if script supports it
// The script uses json_decode(file_get_contents("php://input")), so we might need a workaround 
// or ensure the script checks $_POST too.
// Let's modify get_dashboard_stats.php to check $_POST if input is empty, if we haven't already. 
// (Checking file content in next step if this fails)

// Start simple with POST, many of my scripts support both.
test_endpoint('get_dashboard_stats.php', [], ['user_id' => 52, 'user_type' => 'Counselor']);

?>
