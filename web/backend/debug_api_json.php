<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Set GET param
$_GET['counselor_id'] = 52; // Dr.Suneel

// Capture output
ob_start();
require 'get_counselor_appointments.php';
$output = ob_get_clean();

echo "--- RAW JSON OUTPUT ---\n";
echo $output;
echo "\n\n--- DECODED ANALYSIS ---\n";
$json = json_decode($output, true);
if ($json) {
    echo "Status: " . ($json['status'] ?? 'None') . "\n";
    if (isset($json['appointments'])) {
        foreach ($json['appointments'] as $appt) {
            echo "Appt ID: " . $appt['id'] . "\n";
            echo "  patient_name key exists? " . (array_key_exists('patient_name', $appt) ? "YES" : "NO") . "\n";
            echo "  patient_name value: " . var_export($appt['patient_name'], true) . "\n";
        }
    }
} else {
    echo "Failed to decode JSON.\n";
}
?>
